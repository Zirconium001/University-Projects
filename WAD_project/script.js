// ====== STATE ======
const state = {
  text: "",
  duration: 60,
  timeLeft: 60,
  timer: null,
  started: false,
  correctChars: 0,
  mistakes: 0,
  countdown: null,
  countingDown: false,
  currentTheme: "red"
};

const paragraphs = [
  "Programming is less about memorizing syntax and more about solving small problems quickly and cleanly.",
  "Typing speed improves when your hands stay relaxed, your eyes stay ahead, and your rhythm stays steady.",
  "Frontend development works best when strong visuals, clear feedback, and simple logic all move together.",
  "A good typing test feels focused, fast, and rewarding even when you miss a few characters along the way.",
  "Mechanical keyboards reward deliberate keystrokes and punish hesitation, much like learning to touch type.",
  "The sound of switches bottoming out in a quiet room has a rhythm that becomes muscle memory over time."
];

// ====== DOM ======
const display         = document.getElementById("text-display");
const input           = document.getElementById("hidden-input");
const timeEl          = document.getElementById("time");
const timeRemainingEl = document.getElementById("time-remaining");
const wpmEl           = document.getElementById("wpm");
const accEl           = document.getElementById("accuracy");
const mistakesEl      = document.getElementById("mistakes");
const progressEl      = document.getElementById("progress");
const restartBtn      = document.getElementById("restart");
const statusEl        = document.getElementById("status");
const durationButtons = document.querySelectorAll(".duration-btn");
const testCard        = document.querySelector(".test-card");

// ====== COUNTDOWN OVERLAY ======
const countdownOverlay = document.createElement("div");
countdownOverlay.className = "countdown-overlay";
countdownOverlay.innerHTML = `<div class="countdown-number" id="countdown-num">5</div><div class="countdown-label">Get ready…</div>`;
testCard.style.position = "relative";
testCard.appendChild(countdownOverlay);
const countdownNum = document.getElementById("countdown-num");

// ====== WEB AUDIO ENGINE ======
let audioCtx = null;

function getAudioCtx() {
  if (!audioCtx) {
    audioCtx = new (window.AudioContext || window.webkitAudioContext)();
  }
  if (audioCtx.state === "suspended") audioCtx.resume();
  return audioCtx;
}

// ── Core helper: schedule a gain ramp on a node
function ramp(param, from, to, startT, dur, ctx) {
  param.setValueAtTime(from, startT);
  param.linearRampToValueAtTime(to, startT + dur);
}

function expRamp(param, from, to, startT, dur, ctx) {
  param.setValueAtTime(Math.max(from, 0.0001), startT);
  param.exponentialRampToValueAtTime(Math.max(to, 0.0001), startT + dur);
}

// ── Master compressor to keep levels consistent
function getMasterChain(ctx) {
  const comp = ctx.createDynamicsCompressor();
  comp.threshold.value = -6;
  comp.knee.value = 6;
  comp.ratio.value = 4;
  comp.attack.value = 0.002;
  comp.release.value = 0.05;
  comp.connect(ctx.destination);
  return comp;
}

// ────────────────────────────────────────────────────────────
// RED LINEAR SWITCH
// Physical model: no tactile bump, just stem travel + bottom-out
// Sound: soft high-freq "thwack" (key cap hit) + low body thud
// Reference: Cherry MX Red / Gateron Red character
// ────────────────────────────────────────────────────────────
function playRedSwitch(isError) {
  const ctx = getAudioCtx();
  const dest = getMasterChain(ctx);
  const now = ctx.currentTime;
  const vol = isError ? 0.55 : 0.42;

  // --- Layer 1: Keycap impact — sharp transient (filtered white noise, HP)
  const impactBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.05), ctx.sampleRate);
  const impactData = impactBuf.getChannelData(0);
  for (let i = 0; i < impactData.length; i++) {
    impactData[i] = (Math.random() * 2 - 1);
  }
  const impactSrc = ctx.createBufferSource();
  impactSrc.buffer = impactBuf;

  // High-pass to make it "clacky" at the top end
  const impactHP = ctx.createBiquadFilter();
  impactHP.type = "highpass";
  impactHP.frequency.value = 3800;
  impactHP.Q.value = 0.7;

  const impactLP = ctx.createBiquadFilter();
  impactLP.type = "lowpass";
  impactLP.frequency.value = 9000;

  const impactGain = ctx.createGain();
  impactGain.gain.setValueAtTime(vol * 1.1, now);
  impactGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.025);

  impactSrc.connect(impactHP);
  impactHP.connect(impactLP);
  impactLP.connect(impactGain);
  impactGain.connect(dest);
  impactSrc.start(now);

  // --- Layer 2: Bottom-out body thud — low resonant bump
  const thudBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.08), ctx.sampleRate);
  const thudData = thudBuf.getChannelData(0);
  for (let i = 0; i < thudData.length; i++) {
    thudData[i] = (Math.random() * 2 - 1);
  }
  const thudSrc = ctx.createBufferSource();
  thudSrc.buffer = thudBuf;

  const thudLP = ctx.createBiquadFilter();
  thudLP.type = "lowpass";
  thudLP.frequency.value = 320;
  thudLP.Q.value = 2.5; // slight resonance = "boxy" thud

  const thudGain = ctx.createGain();
  const thudDelay = 0.004; // 4ms after cap hit — linear has almost no pre-travel gap
  thudGain.gain.setValueAtTime(0, now);
  thudGain.gain.setValueAtTime(vol * 0.9, now + thudDelay);
  thudGain.gain.exponentialRampToValueAtTime(0.0001, now + thudDelay + 0.055);

  thudSrc.connect(thudLP);
  thudLP.connect(thudGain);
  thudGain.connect(dest);
  thudSrc.start(now);

  // --- Layer 3: Tonal body resonance (pitched thump) — makes it sound "solid"
  const osc = ctx.createOscillator();
  osc.type = "sine";
  // Red is known for a slightly higher-pitched, tighter sound than brown
  osc.frequency.setValueAtTime(195 + (Math.random() * 15), now + thudDelay);
  osc.frequency.exponentialRampToValueAtTime(90, now + thudDelay + 0.04);

  const oscGain = ctx.createGain();
  oscGain.gain.setValueAtTime(vol * 0.28, now + thudDelay);
  oscGain.gain.exponentialRampToValueAtTime(0.0001, now + thudDelay + 0.05);

  osc.connect(oscGain);
  oscGain.connect(dest);
  osc.start(now + thudDelay);
  osc.stop(now + 0.12);

  // --- Error variant: add a short low buzz/rattle
  if (isError) {
    const errOsc = ctx.createOscillator();
    errOsc.type = "sawtooth";
    errOsc.frequency.value = 130;
    const errGain = ctx.createGain();
    errGain.gain.setValueAtTime(0.18, now);
    errGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.07);
    errOsc.connect(errGain);
    errGain.connect(dest);
    errOsc.start(now);
    errOsc.stop(now + 0.08);
  }
}

// ────────────────────────────────────────────────────────────
// BLUE CLICKY SWITCH
// Physical model: tactile + audible click mechanism (click jacket)
// Two-stage sound: 1) sharp metallic CLICK at actuation point
//                  2) bottom-out THUD ~15-20ms later
// Reference: Cherry MX Blue / Kailh Box White character
// ────────────────────────────────────────────────────────────
function playBlueSwitch(isError) {
  const ctx = getAudioCtx();
  const dest = getMasterChain(ctx);
  const now = ctx.currentTime;
  const vol = isError ? 0.65 : 0.5;

  // --- Layer 1: THE CLICK — the defining sound of a clicky switch
  // Sharp, high-frequency metallic transient — the click jacket snapping
  const clickBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.012), ctx.sampleRate);
  const clickData = clickBuf.getChannelData(0);
  for (let i = 0; i < clickData.length; i++) {
    const env = 1 - (i / clickData.length);
    clickData[i] = (Math.random() * 2 - 1) * Math.pow(env, 0.4);
  }
  const clickSrc = ctx.createBufferSource();
  clickSrc.buffer = clickBuf;

  // The click is very bright and metallic — band-pass around 5-8kHz
  const clickBP = ctx.createBiquadFilter();
  clickBP.type = "bandpass";
  clickBP.frequency.value = 5500;
  clickBP.Q.value = 1.2;

  const clickHP = ctx.createBiquadFilter();
  clickHP.type = "highpass";
  clickHP.frequency.value = 2200;

  const clickGain = ctx.createGain();
  clickGain.gain.setValueAtTime(vol * 2.2, now);
  clickGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.01);

  clickSrc.connect(clickBP);
  clickBP.connect(clickHP);
  clickHP.connect(clickGain);
  clickGain.connect(dest);
  clickSrc.start(now);

  // Add a short tonal "ping" for metallic character
  const pingOsc = ctx.createOscillator();
  pingOsc.type = "sine";
  pingOsc.frequency.setValueAtTime(4200, now);
  pingOsc.frequency.exponentialRampToValueAtTime(2800, now + 0.008);
  const pingGain = ctx.createGain();
  pingGain.gain.setValueAtTime(vol * 0.35, now);
  pingGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.012);
  pingOsc.connect(pingGain);
  pingGain.connect(dest);
  pingOsc.start(now);
  pingOsc.stop(now + 0.015);

  // --- Layer 2: Bottom-out thud (~15ms gap after click = key travel distance)
  const thudT = now + 0.015;
  const thudBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.07), ctx.sampleRate);
  const thudData = thudBuf.getChannelData(0);
  for (let i = 0; i < thudData.length; i++) thudData[i] = (Math.random() * 2 - 1);
  const thudSrc = ctx.createBufferSource();
  thudSrc.buffer = thudBuf;

  const thudLP = ctx.createBiquadFilter();
  thudLP.type = "lowpass";
  thudLP.frequency.value = 400;
  thudLP.Q.value = 1.8;

  const thudGain = ctx.createGain();
  thudGain.gain.setValueAtTime(vol * 0.8, thudT);
  thudGain.gain.exponentialRampToValueAtTime(0.0001, thudT + 0.05);

  thudSrc.connect(thudLP);
  thudLP.connect(thudGain);
  thudGain.connect(dest);
  thudSrc.start(thudT);

  // Tonal body thump at bottom-out
  const bodyOsc = ctx.createOscillator();
  bodyOsc.type = "sine";
  bodyOsc.frequency.setValueAtTime(160 + Math.random() * 20, thudT);
  bodyOsc.frequency.exponentialRampToValueAtTime(75, thudT + 0.045);
  const bodyGain = ctx.createGain();
  bodyGain.gain.setValueAtTime(vol * 0.32, thudT);
  bodyGain.gain.exponentialRampToValueAtTime(0.0001, thudT + 0.05);
  bodyOsc.connect(bodyGain);
  bodyGain.connect(dest);
  bodyOsc.start(thudT);
  bodyOsc.stop(thudT + 0.06);

  // --- Error: second louder click + dissonant tone
  if (isError) {
    const errT = now + 0.005;
    const errOsc = ctx.createOscillator();
    errOsc.type = "sawtooth";
    errOsc.frequency.setValueAtTime(3100, errT);
    errOsc.frequency.exponentialRampToValueAtTime(800, errT + 0.04);
    const errGain = ctx.createGain();
    errGain.gain.setValueAtTime(0.22, errT);
    errGain.gain.exponentialRampToValueAtTime(0.0001, errT + 0.045);
    errOsc.connect(errGain);
    errGain.connect(dest);
    errOsc.start(errT);
    errOsc.stop(errT + 0.05);
  }
}

// ────────────────────────────────────────────────────────────
// BROWN TACTILE SWITCH
// Physical model: tactile bump, NO audible click mechanism
// Sound: soft "thock" with a subtle mid-freq bump before bottom-out
// Reference: Cherry MX Brown / Gateron Brown character
// Sits between red (no feel) and blue (loud click)
// ────────────────────────────────────────────────────────────
function playBrownSwitch(isError) {
  const ctx = getAudioCtx();
  const dest = getMasterChain(ctx);
  const now = ctx.currentTime;
  const vol = isError ? 0.6 : 0.46;

  // --- Layer 1: Tactile bump transient — softer than blue, not a click
  // A rounded mid-freq "thump" not a sharp snap
  const bumpBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.018), ctx.sampleRate);
  const bumpData = bumpBuf.getChannelData(0);
  for (let i = 0; i < bumpData.length; i++) {
    const env = Math.pow(1 - (i / bumpData.length), 0.6);
    bumpData[i] = (Math.random() * 2 - 1) * env;
  }
  const bumpSrc = ctx.createBufferSource();
  bumpSrc.buffer = bumpBuf;

  // Mid-range band — tactile feel is "softer" than click, ~1-3kHz range
  const bumpBP = ctx.createBiquadFilter();
  bumpBP.type = "bandpass";
  bumpBP.frequency.value = 1800;
  bumpBP.Q.value = 1.4;

  const bumpGain = ctx.createGain();
  bumpGain.gain.setValueAtTime(vol * 1.3, now);
  bumpGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.016);

  bumpSrc.connect(bumpBP);
  bumpBP.connect(bumpGain);
  bumpGain.connect(dest);
  bumpSrc.start(now);

  // --- Layer 2: The signature "thock" of a brown — bottom-out, ~10ms after bump
  const thockT = now + 0.010;
  const thockBuf = ctx.createBuffer(1, Math.floor(ctx.sampleRate * 0.09), ctx.sampleRate);
  const thockData = thockBuf.getChannelData(0);
  for (let i = 0; i < thockData.length; i++) thockData[i] = (Math.random() * 2 - 1);
  const thockSrc = ctx.createBufferSource();
  thockSrc.buffer = thockBuf;

  // Brown's bottom-out is warmer / lower than blue — LP at ~280Hz with resonance
  const thockLP = ctx.createBiquadFilter();
  thockLP.type = "lowpass";
  thockLP.frequency.value = 280;
  thockLP.Q.value = 3.5; // more resonance = "thocky" hollow sound

  const thockGain = ctx.createGain();
  thockGain.gain.setValueAtTime(vol * 1.0, thockT);
  thockGain.gain.exponentialRampToValueAtTime(0.0001, thockT + 0.065);

  thockSrc.connect(thockLP);
  thockLP.connect(thockGain);
  thockGain.connect(dest);
  thockSrc.start(thockT);

  // Tonal resonance — brown is "woody", lower pitch than red
  const bodyOsc = ctx.createOscillator();
  bodyOsc.type = "sine";
  bodyOsc.frequency.setValueAtTime(140 + Math.random() * 15, thockT);
  bodyOsc.frequency.exponentialRampToValueAtTime(65, thockT + 0.055);
  const bodyGain = ctx.createGain();
  bodyGain.gain.setValueAtTime(vol * 0.38, thockT);
  bodyGain.gain.exponentialRampToValueAtTime(0.0001, thockT + 0.06);
  bodyOsc.connect(bodyGain);
  bodyGain.connect(dest);
  bodyOsc.start(thockT);
  bodyOsc.stop(thockT + 0.07);

  // --- Error: hollow wooden knock + dissonant low tone
  if (isError) {
    const errOsc = ctx.createOscillator();
    errOsc.type = "triangle";
    errOsc.frequency.setValueAtTime(220, now);
    errOsc.frequency.exponentialRampToValueAtTime(95, now + 0.06);
    const errGain = ctx.createGain();
    errGain.gain.setValueAtTime(0.25, now);
    errGain.gain.exponentialRampToValueAtTime(0.0001, now + 0.07);
    errOsc.connect(errGain);
    errGain.connect(dest);
    errOsc.start(now);
    errOsc.stop(now + 0.08);
  }
}

function playKeystroke(isError = false) {
  try {
    switch (state.currentTheme) {
      case "red":   playRedSwitch(isError);   break;
      case "blue":  playBlueSwitch(isError);  break;
      case "brown": playBrownSwitch(isError); break;
    }
  } catch (e) { /* AudioContext not ready yet */ }
}

// ====== COUNTDOWN ======
function startCountdown(callback) {
  state.countingDown = true;
  input.disabled = true;
  countdownOverlay.classList.add("visible");
  let count = 5;

  function tick() {
    countdownNum.style.animation = "none";
    void countdownNum.offsetWidth;
    countdownNum.style.animation = "countdown-pop 1s ease-out forwards";
    countdownNum.textContent = count;
  }
  tick();

  state.countdown = setInterval(() => {
    count--;
    if (count <= 0) {
      clearInterval(state.countdown);
      state.countdown = null;
      countdownOverlay.classList.remove("visible");
      state.countingDown = false;
      input.disabled = false;
      input.focus();
      callback();
    } else {
      tick();
    }
  }, 1000);
}

// ====== TEXT ======
function pickRandomText() {
  return paragraphs[Math.floor(Math.random() * paragraphs.length)];
}

function loadText() {
  state.text = pickRandomText();
  display.innerHTML = state.text
    .split("")
    .map(letter => `<span class="char">${letter === " " ? "&nbsp;" : letter}</span>`)
    .join("");
}

// ====== RESET / NEW TEST ======
function resetGame() {
  if (state.countdown) {
    clearInterval(state.countdown);
    state.countdown = null;
    state.countingDown = false;
    countdownOverlay.classList.remove("visible");
  }

  clearInterval(state.timer);
  state.timer        = null;
  state.started      = false;
  state.correctChars = 0;
  state.mistakes     = 0;

  input.disabled = true;
  input.value    = "";

  state.timeLeft = state.duration;
  timeEl.textContent          = String(state.timeLeft);
  timeRemainingEl.textContent = `${state.timeLeft} seconds`;
  wpmEl.textContent           = "0";
  accEl.textContent           = "100";
  mistakesEl.textContent      = "0";
  progressEl.style.width      = "0%";
  statusEl.textContent        = "Get ready";

  loadText();
  refreshPreview();

  startCountdown(() => {
    statusEl.textContent = "Ready";
    input.focus();
  });
}

function setDuration(duration) {
  state.duration = duration;
  durationButtons.forEach(btn => {
    btn.classList.toggle("active", Number(btn.dataset.duration) === duration);
  });
  resetGame();
}

// ====== PREVIEW / RENDER ======
// Cursor logic: the | bar lives as ::after on the LAST typed char
// (or ::before on the very first char when nothing typed yet).
// This means: type "hel" → cursor shows AFTER the 'l' = "hel|lo"
// Only ONE element ever gets .cursor-after, ONE gets .cursor-before.
function refreshPreview() {
  const letters = display.querySelectorAll(".char");
  const typed   = input.value;
  const len     = typed.length;

  letters.forEach((letter, index) => {
    const char = typed[index];

    // Clear all state classes
    letter.classList.remove("correct", "wrong", "cursor-after", "cursor-before");

    if (char == null) {
      // untyped territory
      // The very next character (index === len) gets cursor-before
      if (index === len) letter.classList.add("cursor-before");
      return;
    }

    letter.classList.add(char === state.text[index] ? "correct" : "wrong");

    // The last typed character gets cursor-after
    if (index === len - 1) letter.classList.add("cursor-after");
  });
}

// ====== TIMER ======
function startTimer() {
  statusEl.textContent = "Running";
  state.timer = setInterval(() => {
    state.timeLeft--;
    timeEl.textContent          = String(state.timeLeft);
    timeRemainingEl.textContent = `${state.timeLeft} seconds`;
    updateStats();
    updateProgress();

    if (state.timeLeft === 0) {
      clearInterval(state.timer);
      input.disabled = true;
      statusEl.textContent = "Finished";
    }
  }, 1000);
}

function endTest() {
  clearInterval(state.timer);
  input.disabled = true;
  statusEl.textContent = "Finished";
}

// ====== TYPING ENGINE ======
input.addEventListener("input", () => {
  if (state.countingDown) return;

  const typed    = input.value;
  const lastIdx  = typed.length - 1;
  const isError  = lastIdx >= 0 && typed[lastIdx] !== state.text[lastIdx];

  getAudioCtx(); // ensure context alive
  playKeystroke(isError);

  if (!state.started) { startTimer(); state.started = true; }

  state.mistakes     = 0;
  state.correctChars = 0;

  for (let i = 0; i < typed.length; i++) {
    if (typed[i] === state.text[i]) state.correctChars++;
    else state.mistakes++;
  }

  refreshPreview();
  mistakesEl.textContent = state.mistakes;

  if (typed.length >= state.text.length) endTest();

  updateProgress();
  updateStats();
});

// ====== STATS ======
function updateStats() {
  const elapsed  = state.duration - state.timeLeft;
  const minutes  = elapsed > 0 ? elapsed / 60 : 0;
  const wpm      = minutes > 0 ? Math.round((state.correctChars / 5) / minutes) : 0;
  const typed    = input.value.length;
  const accuracy = typed > 0 ? Math.max(0, Math.round((state.correctChars / typed) * 100)) : 100;
  wpmEl.textContent = wpm;
  accEl.textContent = accuracy;
}

// ====== PROGRESS BAR ======
function updateProgress() {
  const percent = state.timeLeft > 0
    ? ((state.duration - state.timeLeft) / state.duration) * 100
    : 100;
  progressEl.style.width = `${percent}%`;
}

// ====== THEME SWITCHING ======
const switchData = {
  red:   { name: "Red Switch",   color: "Red",   desc: "Type clean. Type fast. Every keystroke counts — just like a perfectly lubed linear." },
  blue:  { name: "Blue Switch",  color: "Blue",  desc: "Feel every click. Hear every key. Precision feedback with every satisfying press." },
  brown: { name: "Brown Switch", color: "Brown", desc: "Balance speed and feel. Smooth tactile response without the noise, pure control." }
};

const switchNameEl = document.getElementById("switch-name");
const heroAccentEl = document.getElementById("hero-accent");
const heroDescEl   = document.getElementById("hero-desc");

document.querySelectorAll(".sw-toggle").forEach(btn => {
  btn.addEventListener("click", () => {
    const theme = btn.dataset.theme;
    document.documentElement.dataset.switch = theme;
    state.currentTheme = theme;
    document.querySelectorAll(".sw-toggle").forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
    const data = switchData[theme];
    switchNameEl.textContent = data.name;
    heroAccentEl.textContent = data.color;
    heroDescEl.textContent   = data.desc;
    setTimeout(() => playKeystroke(false), 60);
  });
});

document.addEventListener("click", () => { if (!state.countingDown) input.focus(); });
window.addEventListener("load", resetGame);
restartBtn.addEventListener("click", resetGame);
durationButtons.forEach(btn => {
  btn.addEventListener("click", () => setDuration(Number(btn.dataset.duration)));
});

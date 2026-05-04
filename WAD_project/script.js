// ====== STATE ======
const state = {
  text: "",
  duration: 60,   // ✅ matches the "active" button in HTML (60s)
  timeLeft: 60,
  timer: null,
  started: false,
  correctChars: 0,
  mistakes: 0
};

const paragraphs = [
 "Programming is less about memorizing syntax and more about solving small problems quickly and cleanly.",
 "Typing speed improves when your hands stay relaxed, your eyes stay ahead, and your rhythm stays steady.",
 "Frontend development works best when strong visuals, clear feedback, and simple logic all move together.",
 "A good typing test feels focused, fast, and rewarding even when you miss a few characters along the way."
];

// ====== DOM ======
const display = document.getElementById("text-display");
const input = document.getElementById("hidden-input");
const timeEl = document.getElementById("time");
const timeRemainingEl = document.getElementById("time-remaining");
const wpmEl = document.getElementById("wpm");
const accEl = document.getElementById("accuracy");
const mistakesEl = document.getElementById("mistakes");
const progressEl = document.getElementById("progress");
const restartBtn = document.getElementById("restart");
const statusEl = document.getElementById("status");
const durationButtons = document.querySelectorAll(".duration-btn");

function pickRandomText(){
  return paragraphs[Math.floor(Math.random() * paragraphs.length)];
}

// ====== LOAD TEXT ======
function loadText(){
  state.text = pickRandomText();
  display.innerHTML = state.text
      .split("")
      .map(letter => `<span class="char">${letter === " " ? "&nbsp;" : letter}</span>`)
      .join("");
}

function resetGame(){
  clearInterval(state.timer);

  state.timeLeft = state.duration;
  state.timer = null;
  state.started = false;
  state.correctChars = 0;
  state.mistakes = 0;

  input.disabled = false;
  input.value = "";

  timeEl.textContent = String(state.timeLeft);
  timeRemainingEl.textContent = `${state.timeLeft} seconds`;
  wpmEl.textContent = "0";
  accEl.textContent = "100";
  mistakesEl.textContent = "0";
  progressEl.style.width = "0%";
  statusEl.textContent = "Ready";

  loadText();
  refreshPreview();
  input.focus();
}

function setDuration(duration){
  state.duration = duration;
  durationButtons.forEach(button => {
    button.classList.toggle("active", Number(button.dataset.duration) === duration);
  });
  resetGame();
}

function refreshPreview(){
  const letters = display.querySelectorAll(".char");
  const typed = input.value;

  letters.forEach((letter, index) => {
    const char = typed[index];

    letter.classList.remove("correct", "wrong", "current");

    if (char == null) {
      if (index === typed.length) {
        letter.classList.add("current");
      }
      return;
    }

    if (char === state.text[index]) {
      letter.classList.add("correct");
    } else {
      letter.classList.add("wrong");
    }

    if (index === typed.length - 1) {
      letter.classList.add("current");
    }
  });
}

// ====== TIMER ======
function startTimer(){
  statusEl.textContent = "Running";
  state.timer = setInterval(()=>{
    state.timeLeft--;
    timeEl.textContent = String(state.timeLeft);
    timeRemainingEl.textContent = `${state.timeLeft} seconds`;

    updateStats();
    updateProgress();

    if(state.timeLeft === 0){
      clearInterval(state.timer);
      input.disabled = true;
      statusEl.textContent = "Finished";
    }
  },1000);
}

function endTest(){
  clearInterval(state.timer);
  input.disabled = true;
  statusEl.textContent = "Finished";
}

// ====== TYPING ENGINE ======
input.addEventListener("input", ()=>{
  const typed = input.value;

  if(!state.started){
    startTimer();
    state.started = true;
  }

  state.mistakes = 0;
  state.correctChars = 0;

  for(let index = 0; index < typed.length; index++){
    if(typed[index] === state.text[index]){
      state.correctChars++;
    } else {
      state.mistakes++;
    }
  }

  refreshPreview();
  mistakesEl.textContent = state.mistakes;

  if (typed.length >= state.text.length) {
    endTest();
  }

  updateProgress();
  updateStats();
});

// ====== STATS ENGINE ======
function updateStats(){
  const elapsed = state.duration - state.timeLeft;
  const minutes = elapsed > 0 ? elapsed / 60 : 0;
  const wpm = minutes > 0 ? Math.round((state.correctChars / 5) / minutes) : 0;
  const typedChars = input.value.length;

  const accuracy = typedChars > 0
      ? Math.max(0, Math.round((state.correctChars / typedChars) * 100))
      : 100;

  wpmEl.textContent = wpm;
  accEl.textContent = accuracy;
}

// ====== PROGRESS BAR ======
function updateProgress(){
  const percent = state.timeLeft > 0
      ? ((state.duration - state.timeLeft) / state.duration) * 100
      : 100;
  progressEl.style.width = `${percent}%`;
}

// ====== FORCE FOCUS ======
document.addEventListener("click", () => input.focus());
window.addEventListener("load", () => {
  resetGame();
});
restartBtn.addEventListener("click", resetGame);
durationButtons.forEach(button => {
  button.addEventListener("click", () => {
    setDuration(Number(button.dataset.duration));
  });
});

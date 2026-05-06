import javax.swing.*;
import java.awt.*;

public class GlowButton extends JButton {

    public GlowButton(String text){
        super(text);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBackground(new Color(80,0,160));
        setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(
                0,0,new Color(140,0,255),
                0,getHeight(),new Color(0,200,255));

        g2.setPaint(gp);
        g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);

        super.paintComponent(g);
    }
}

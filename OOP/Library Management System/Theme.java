import java.awt.*;

public class Theme {

    public static boolean darkMode = true;

    // DARK THEME
    public static Color darkBg1 = new Color(10,10,40);
    public static Color darkBg2 = new Color(60,0,90);
    public static Color darkText = Color.WHITE;

    // LIGHT THEME
    public static Color lightBg1 = new Color(220,230,255);
    public static Color lightBg2 = new Color(180,200,255);
    public static Color lightText = Color.BLACK;

    public static Color getTextColor(){
        return darkMode ? darkText : lightText;
    }

    public static Color getBg1(){
        return darkMode ? darkBg1 : lightBg1;
    }

    public static Color getBg2(){
        return darkMode ? darkBg2 : lightBg2;
    }
}

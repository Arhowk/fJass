/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codeeditor;

import com.sun.javafx.tk.FontLoader;
import java.util.Random;
import javafx.scene.text.Font;

/**
 *
 * @author Arhowk
 */
public class MiscUtil {
    public static String mark(){
        return ""+new Random().nextInt();
    }
    public static int getIndexAtWidth(double width, String str, Font ft){
        FontLoader fl = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader();
        float accumWidth = 0;
        int charProgress = 0;
        for (int i = 0; i < str.length(); i++){
            char c = str.charAt(i); 
            accumWidth += fl.computeStringWidth(""+c, ft);
            if(accumWidth > width){
                break;
            }
            charProgress++;
        }
        return charProgress;
    }
    public static double getStringWidth(String str, Font ft){
        return com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(str, ft);
    }
}

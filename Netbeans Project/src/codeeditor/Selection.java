/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codeeditor;

/**
 *
 * @author Arhowk
 */
public class Selection {
    CodeCanvas cvs;
    int sx, sy, gx, gy, prev_sx, prev_sy, prev_gx, prev_gy;
    boolean lastInverted = false, flag = false, flag2 = false;
    public Selection(CodeCanvas canvas, int startX, int startY){
        cvs = canvas;
        sx = startX;
        sy = startY;
        gx = -1;
        gy = -1;
    }
    
    public boolean active(){
        return gx >= 0 && gy >= 0;
    }
    
    public void disable(){
        gx = -1;
        gy = -1;
        prev_sx = -1;
        prev_sy = -1;
        prev_gx = -1;
        prev_gy = -1;
    }
    boolean notInverted = true;
    public void goTo(boolean selectText, int newX, int newY){
        if(selectText){
            if(notInverted && (gy < sy || (gy == sy && gx < sx))){
              // if(sx ){
                  //  lastInverted = true;
                    int tmp = gx;
//                   
                    gx = sx;
                    sx = tmp;
                    tmp = gy;
                    gy = sy;
                    sy = tmp;
                    
                    if(sx == -1) sx = gx;
                    if(sy == -1) sy = gy;
                    notInverted = false;
             //   }
              //  sx = newX;
             //   sy = newY;
            }
            if(!notInverted && (newY < gy || (newY == gy && newX < gx))){
                prev_sx = sx;
                prev_sy = sy;
                sx = newX;
                sy = newY;
            }else{
                notInverted = true;
                prev_gx = gx;
                prev_gy = gy;
                gx = newX;
                gy = newY;
            }
        }else{
                notInverted = true;
            prev_sx = sx;
            prev_sy = sy;
            sx = newX;
            sy = newY;
            disable();
        }
    }
}

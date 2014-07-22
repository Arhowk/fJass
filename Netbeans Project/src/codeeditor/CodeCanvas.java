/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codeeditor;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 *
 * @author Arhowk
 */
public final class CodeCanvas extends ScrollPane{
    final static int DEFAULT_FONT_SIZE = 12,
            DEFAULT_FONT_SPACING = 5,
            DEFAULT_HEIGHT = 500,
            DEFAULT_WIDTH = 500,
            FRAME_TIMEOUT = 31;
    
    
    final Font DEFAULT_FONT = Font.font(DEFAULT_FONT_SIZE);
    Canvas cvs;
    Paint backgrondColor = Color.WHITESMOKE;
    DSArrayList ctxLengthData;
    ArrayList<String> ctxData;
    double fontSpacing;
    double lineSize;
    double fontSize;
    String fontName;
    Font originalFont;
    Font font;
    Selection mouseSel;
    boolean mouseDown;
    GraphicsContext ctx;
    double extent = 0;
    double heightExtent = 0;
    
    private void computeWidthExtent(){
        extent = Math.max(ctxLengthData.getHighest() + 2,getWidth()-2);
        heightExtent = Math.max(ctxData.size() * lineSize, getHeight()-2);
        cvs.setWidth(extent);
        cvs.setHeight(heightExtent);
    }
    
    private int normalizeX(double x, int normalizedY){
        return MiscUtil.getIndexAtWidth(x, ctxData.get(normalizedY), font);
    }
    private int normalizeY(double y){
        int r = (int)(y / lineSize);
        if(r > ctxData.size() - 1){
            return ctxData.size() - 1;
        }else{
            return r;
        }
    }
    private void removeLine(int l){
        ctxData.remove(l);
        ctxLengthData.remove(l);
        renderLines(l,l);
    }
            
    
    private void drawText(int x, int y){
      //  System.out.println("drawText : " + y);
        ctx.setFont(font);
        ctx.setGlobalAlpha(1);
        ctx.setFill(Color.BLUE);
        
        //System.out.println("clear rect : " + (y*lineSize)+"-"+(y*lineSize+lineSize));
        ctx.clearRect(0, (y*lineSize), extent, lineSize);
        ctx.setFill(Color.BLACK);
        try{
            String s = ctxData.get(y);
            ctx.fillText(s, 0,y * (fontSize + fontSpacing) + fontSize) ;
        }catch(Exception e){
            
        }
    }
    
//    private void renderLineRemainder(int line, int charFromZero){
//        String s = ctxData.get(line); 
//        double i = MiscUtil.getStringWidth(s.substring(0,charFromZero), font);
//        ctx.clearRect(i,
//                (line * (fontSize + fontSpacing)),
//                ctxLengthData.getHighest(),
//                ((line+1) * (fontSize + fontSpacing)));
//        drawText(charFromZero, line);
//        
//    }
    
    private void renderLines(int start, int end){
        if(start == end){
             drawText(0, start);
        }else{
            for(int i = start; i <= end; i++){
                renderLines(i,i);
            }
        }
    }
    
    private void renderSelection(Selection sel){
        if(sel.gy == -1){
           return; 
        }
      //      System.out.println("sel s : " + sel.sy + " g " + sel.gy);
        if(sel.sy == sel.gy){
        //    System.out.println("renderSelection");
      //      System.out.println("rndr2");
            if(!sel.flag){
                System.out.println("flag good"+MiscUtil.mark());
                if(sel.sy != 0 && sel.prev_sy == sel.sy - 1){
                    System.out.println("render before");
                    renderLines(sel.prev_sy,sel.sy);
                }else if(sel.prev_gy == sel.gy + 1){
                    System.out.println("render after" + sel.prev_gy);
                    renderLines(sel.gy, sel.prev_gy);
                }else{
                    renderLines(sel.sy,sel.gy);
                }
            }else{
                renderLines(sel.sy,sel.gy);
            }
            ctx.setFill(Color.BLUEVIOLET);
            ctx.setGlobalAlpha(0.2);
            double dny = (sel.sy * lineSize);
            double dny2 = (fontSize + fontSpacing);
            String s = ctxData.get(sel.sy);
            double dnx = MiscUtil.getStringWidth(s.substring(0,sel.sx), font);
            double dnx2;
            if(sel.flag2 || !sel.flag){
                if(sel.gx == s.length()){
                    dnx2 = extent;
                }else{
                    dnx2 = MiscUtil.getStringWidth(s.substring(sel.sx, sel.gx),font);
                }
            }else{
                dnx2 = extent;
            }
            ctx.fillRect(dnx, dny, dnx2, dny2);
        }else{
          //  System.out.println(new Random().nextInt());
          //  System.out.println("else s : " + sel.sy + " this " + sel.gy);
          //  System.out.println("sel.sx : " + sel.sx);
            if(sel.sy != 0 && sel.prev_sy < sel.sy){
                renderLines(sel.prev_sy,sel.sy-1);
            }else if(sel.prev_gy > sel.gy){
                renderLines(sel.prev_gy, sel.gy-1);;
            }
            Selection sl = new Selection(this, sel.sx, sel.sy);
            sl.flag = true;
            sl.flag2 = true;
            sl.goTo(true, ctxData.get(sel.sy).length(), sel.sy);
            renderSelection(sl);
           // System.out.println("done");
            if(sel.gy - sel.sy > 1){
                for(int i = sel.sy + 1; i < sel.gy; i++){
                    sl = new Selection(this, 0, i);
                    sl.flag = true;
                    sl.goTo(true, ctxData.get(i).length(),i);
                    renderSelection(sl);
                }
            }
            sl = new Selection(this, 0, sel.gy);
            sl.flag = true;
            sl.flag2 = true;
            sl.goTo(true, sel.gx, sel.gy);
            renderSelection(sl);
//            renderLineRemainder(sel.sx,sel.sy);
//            renderLines(sel.gy,sel.gy);
//            if(sel.gy-sel.sy > 1){
//                renderLines(sel.sy+1,sel.gy-1);
//            }
        }
    }
    private void addText(int x, int y, String dataToAdd){
        String d = ctxData.get(y);
        d = d.substring(0,x)+dataToAdd+d.substring(x,d.length());
        removeLine(y);
        ctxData.add(y,d);
        ctxLengthData.add(y,(int)MiscUtil.getStringWidth(d, font));
        renderLines(y,y);
    }
    
    private void deleteText(int startX, int startY, int endX, int endY){
        if(startY > endY){
            int temp = startY;
            startY = endY;
            endY = temp;
            temp = startX;
            startX = endX;
            endX = temp;
        }
        if(startY == endY){
            if(startX > endX){
                String s = ctxData.get(startY);
                removeLine(startY);
                ctxData.add(startY, s.substring(0, endX) + s.substring(startX, s.length()));
                ctxLengthData.add(startY, ctxData.get(startY).length());
                mouseSel.sx = endX;
            }else{
                String s = ctxData.get(startY);
                removeLine(startY);
                ctxData.add(startY, s.substring(0, startX) + s.substring(endX, s.length()));
                ctxLengthData.add(startY, ctxData.get(startY).length());
                mouseSel.sx = startX;
            }
            
        }else{
            String start = ctxData.get(startY).substring(0, startX);
            String end = ctxData.get(endY);
            end = end.substring(endX, end.length());
            
            for(int i = startY; i <= endY; i++){
                removeLine(startY);
            }
            ctxData.add(startY, start + end);
            ctxLengthData.add(startY,(start+end).length());
            mouseSel.sx = startX;
            mouseSel.sy = startY;
        }
        renderLines(startY,startY);
    }
    
    
    private void init(){
        ctxData = new ArrayList();
        setContent(cvs);
        ctxLengthData = new DSArrayList();
        ctxData.add("");
        ctxLengthData.add(0);
        mouseSel = new Selection(this,0,0);
        originalFont = font;
        ctx = cvs.getGraphicsContext2D();
        lineSize = fontSpacing + fontSize;
        setFocusTraversable(false);
        cvs.setFocusTraversable(true);
        computeWidthExtent();
//        setOnMouseMoved((a) -> {
//            if(mouseDown){
//                int y = normalizeY(a.getY());
//                mouseSel.goTo(true,normalizeX(a.getX(),y), y);
//            }
//        });
        cvs.setOnMouseMoved((a) -> {
//            System.out.println("x : " + a.getX());
//            System.out.println("y : " + a.getY());
        });
        cvs.setOnMouseDragged((a) -> {
            int y = normalizeY(a.getY());
            if(y > -1 && a.getX() > -1){
                mouseSel.goTo(true,normalizeX(a.getX(),y), y);
                renderSelection(mouseSel);
            }
        });
        setOnMousePressed((a) -> {
            if(mouseSel.active()){
                renderLines(mouseSel.sy, mouseSel.gy);
                mouseSel.disable();
            }
        });
        cvs.setOnMousePressed((a) -> {
            if(mouseSel.active()){
                renderLines(mouseSel.sy, mouseSel.gy);
                mouseSel.disable();
            }
            int y = normalizeY(a.getY());
            mouseSel.goTo(false,normalizeX(a.getX(),y), y);
            mouseDown = true;
        });
        setOnKeyPressed((a) -> {
            if(a.getCode().isLetterKey() || a.getCode().isDigitKey() || isOperator(a.getCode()) || a.getCode() == KeyCode.SPACE){
                if(mouseSel.active()){
                    deleteText(mouseSel.sx, mouseSel.sy, mouseSel.gx, mouseSel.gy);
                }
                addText(mouseSel.sx,mouseSel.sy,a.getText());
                mouseSel.sx = mouseSel.sx + 1;
                mouseSel.disable();
                computeWidthExtent();
            }else if(a.getCode() == KeyCode.TAB){
                if(mouseSel.active()){
                    for(int i = mouseSel.sy; i <= mouseSel.gy; i++){
                        addText(0, i, "    ");
                    }
                }else{
                    addText(mouseSel.sx,mouseSel.sy,"    ");
                mouseSel.sx = mouseSel.sx + 4;
                }
                computeWidthExtent();
            }else if(a.getCode() == KeyCode.BACK_SPACE){
                if(mouseSel.active()){
                    deleteText(mouseSel.sx, mouseSel.sy, mouseSel.gx, mouseSel.gy);
                    mouseSel.disable();
                }else if(!(mouseSel.sx == 0 && mouseSel.sy == 0)){
                    if(mouseSel.sx == 0){
                        String s = ctxData.get(mouseSel.sy - 1);
                        addText(s.length(), mouseSel.sy - 1, ctxData.get(mouseSel.sy));
                        ctxData.remove(mouseSel.sy);
                        ctxLengthData.remove(mouseSel.sy);
                    }else{
                        String s = ctxData.get(mouseSel.sy);
                        s = s.substring(0, mouseSel.sx - 1) + s.substring(mouseSel.sx, s.length());
                        ctxData.remove(mouseSel.sy);
                        ctxData.add(mouseSel.sy, s);
                        ctxLengthData.remove(mouseSel.sy);
                        ctxLengthData.add(mouseSel.sy, (int) MiscUtil.getStringWidth(s, font));
                        renderLines(mouseSel.sy,mouseSel.sy);
                        mouseSel.sx = mouseSel.sx - 1;
                    }
                }
                computeWidthExtent();
            }else if(a.getCode() == KeyCode.ENTER){
                if(mouseSel.sy == ctxData.size() - 1){
                    ctxData.add("");
                    ctxLengthData.add(0);
                    mouseSel.sx = 0;
                    mouseSel.sy = mouseSel.sy + 1;
                }
            }
            
        });
        
        setOnMouseReleased((a) -> {
            mouseDown = false;
        });
    }
    
    
    public CodeCanvas(String fontName, double fontSize, int fontSpacing, int height, int width ){
        cvs = new Canvas(height,width);
        this.fontSize = fontSize; this.fontSpacing = fontSpacing;
        this.fontName = fontName; this.font = Font.font(fontName, fontSize);
        init();
    }
    
    public CodeCanvas(Font font, int fontSpacing, int height, int width){
        cvs = new Canvas(height,width);
        this.fontSize = font.getSize(); this.fontSpacing = fontSpacing;
        this.fontName = font.getFamily(); this.font = font;
        init();
    }
    
    public CodeCanvas(double fontSize, int fontSpacing, int height, int width){
        cvs = new Canvas(height,width);
        this.fontSize = fontSize; this.fontSpacing = fontSpacing;
        this.fontName = DEFAULT_FONT.getFamily(); this.font = Font.font(fontSize);
        init();
    }
    public CodeCanvas(double fontSize, int height, int width){
        cvs = new Canvas(height,width);
        this.fontSize = fontSize; this.fontSpacing = DEFAULT_FONT_SPACING;
        this.fontName = DEFAULT_FONT.getFamily(); this.font = Font.font(fontSize);
        init();
    }
    public CodeCanvas(double fontSize, int fontSpacing){
        cvs = new Canvas(DEFAULT_HEIGHT,DEFAULT_WIDTH);
        this.fontSize = fontSize; this.fontSpacing = fontSpacing;
        this.fontName = DEFAULT_FONT.getFamily(); this.font = Font.font(fontSize);
        init();
    }
    public CodeCanvas(double fontSize){
        cvs = new Canvas(DEFAULT_HEIGHT,DEFAULT_WIDTH);
        this.fontSize = fontSize; this.fontSpacing = DEFAULT_FONT_SPACING;
        this.fontName = DEFAULT_FONT.getFamily(); this.font = Font.font(fontSize);
        init();
    }
    public CodeCanvas(){
        cvs = new Canvas(DEFAULT_HEIGHT,DEFAULT_WIDTH);
        this.fontSize = DEFAULT_FONT_SIZE; this.fontSpacing = DEFAULT_FONT_SPACING;
        this.fontName = DEFAULT_FONT.getFamily(); this.font = DEFAULT_FONT;
        init();
    }
    
    
    
    private static boolean isOperator(KeyCode c){
        return c == KeyCode.BACK_QUOTE
                || c == KeyCode.LESS
                || c == KeyCode.COMMA
                || c == KeyCode.PERIOD
                || c == KeyCode.SEMICOLON
                || c == KeyCode.QUOTE
                || c == KeyCode.BACK_SLASH
                || c == KeyCode.OPEN_BRACKET
                || c == KeyCode.CLOSE_BRACKET
                || c == KeyCode.MINUS
                || c == KeyCode.EQUALS
                || c == KeyCode.SLASH;
               
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codeeditor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Arhowk
 */
public class CodeEditor extends Application {
    
        final int fontSize = 15, fontSpace = 5;
        String counter = "";
        
    public void clear(GraphicsContext ctx, Color cl){
        Paint fill = ctx.getFill();
        double alpha = ctx.getGlobalAlpha();
        ctx.setFill(cl);
        ctx.setGlobalAlpha(1);
        ctx.fillRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
        ctx.setFill(fill);
        ctx.setGlobalAlpha(alpha);
    }
    
    public boolean isOperator(KeyCode c){
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
                || c == KeyCode.EQUALS;
               
    }
    
    public void render(Canvas c){
        GraphicsContext gc = c.getGraphicsContext2D();
        clear(gc,Color.WHITE);
        
        gc.fillText(counter, 75, 75);
        gc.fillText("hello world", 75, 75 + fontSize + fontSpace);
        gc.fillText("hello world", 75, 75 + (fontSize + fontSpace) * 2);
    }
    
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        
        final CodeCanvas canvas = new CodeCanvas(CodeCanvas.DEFAULT_FONT_SIZE, 300, 250);
        
        root.getChildren().add(canvas);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

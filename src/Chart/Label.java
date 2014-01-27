package Chart;

import java.awt.Color;
import processing.core.*;

public class Label {

     float textSize = 10;
     float paddingX =5;
     float paddingY = 2.5f;
     float height = textSize + 2*paddingY; //height allowed for each item
     float offsetX = 10;
     float offsetY = 10;
     float x,y,width;

     String text;
     PApplet parent;

     Label(PApplet parent, String text, float x, float y, Color c) {
          this.parent = parent;
          
          width = 2 *paddingX;
          this.x = x;
          this.y =y;
          this.text =text;
     }

     public void display() {
            parent.stroke(Color.black.getRGB());
            parent.strokeWeight(2);
          //Set up
          String[] tokens = text.split(",");
          parent.textSize(10);
          for(String s:tokens){
               float size = parent.textWidth(s);
               if (size > width - 2*paddingX)
                    width = size + 2*paddingX; 
               }
          
          //Ensure box does not go out of bounds
           if(x+offsetX+width > parent.width)
                offsetX = -offsetX -width;
           
           if(y-offsetY - height*tokens.length < 0)
                offsetY  = -offsetY - height;
           
          
          //Label
          parent.fill(255);
          parent.rect(x + offsetX, y - offsetY-height*tokens.length, width , height*tokens.length);

          //Text
          parent.fill(0);
          for(String s:tokens){
          parent.text(s, x+offsetX + paddingX , y-offsetY - paddingY);
          y -= height;
          }

     }
}

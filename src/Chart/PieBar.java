/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

import processing.core.*;
import java.util.*;
import java.awt.Color;

//add colors
public class PieBar {

     boolean dragging = false; // Is the object being dragged
     boolean rollover = false; // Is the mouse over the bar
     float offsetX, offsetY; // Mouseclick offset
     Map<String, Integer> values; //The actual values
     Map<String, Float> chunk; // The values used to draw graph to scale
     PApplet parent;
     String title;
     float xCord;
     float yCord;
     float width;
     boolean hover;
     static int padding = 5;
     static int titleOffset = 10; //The inital value of the y Cord is where title is drawn the bar is drawn at Ycord + TitleOffset
     float height;
     static Color[] colour = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.YELLOW};

     public PieBar(PApplet parent, Map<String, Integer> values, String title, int x, int y) {
          this.values = values;
          this.title = title;
          this.parent = parent;
          xCord = x + padding;
          yCord = y;

          width = parent.getWidth() - 2 * padding - xCord; // padding needs to be at beging and end
          float total = getTotal();

          chunk = new TreeMap<String,Float>();
          for (String key : values.keySet()) {
               Float value = (float)values.get(key)/total*width;
               chunk.put(key, value);
          }

          offsetX = 0;
          offsetY = 0;
          height = 10;
     }

     public PieBar(PApplet parent, Map<String, Integer> values, String title, int x, int y, float width, float height) {
          this.values = values;
          this.title = title;
          this.parent = parent;
          xCord = x + padding;
          yCord = y;
          this.width = width;
          this.height = height;
          float total = getTotal();
          
          chunk = new TreeMap<String,Float>();
          for (String key : values.keySet()) {
               Float value = (float)values.get(key)/total*width;
               chunk.put(key, value);
          }
          offsetX = 0;
          offsetY = 0;
     }

     private float getTotal() {
          float total = 0;
          for (String key : values.keySet()) {
               total += values.get(key);
          }
          return total;
     }

     public void display() {
          float x = xCord;
          float y = yCord;

          //Change title when dragging or on rollover
          if ((dragging) || (rollover)) {
               parent.fill(0);
          } else {
               parent.fill(255);
          }

          //Drawing title
          parent.textSize(15);
          parent.text(title, x, y);


          y += titleOffset;
          parent.textSize(10);
          int i = 0;

          //Drawing Bar
          for (String key : chunk.keySet()) {
               Float value = chunk.get(key);

               //Rectangle colour
               parent.fill(colour[i].getRGB());

               //Rectangle behaviour on rollover/drag
               if ((dragging) || (rollover)) {
                    parent.stroke(colour[i].getRGB());
                    parent.strokeWeight(3); //Rectange grows
               } else {
                    parent.stroke(0);
                    parent.strokeWeight(1);
               }
               
               parent.rect(x, y, value, height);
               //Text colour
               parent.fill(255);
               //Don't let text over flow borders
               if(parent.textWidth(key) < value)
                    parent.text(key, x + 5, y + 20);
               
               x += value;
               i++;
               if (i > 5) { //used all default colours
                    i = 0;
               }
          }
     }

     public void displayLabel(){
               //On rollover show bar info
          if (rollover) {
               StringBuilder text = new StringBuilder();
               for (String key : values.keySet()) {
                    text.append(key);
                    text.append(" = ");
                    text.append(values.get(key));  
                    text.append(","); 
               }
               text.deleteCharAt(text.length()-1); //remove extra ,
              Label l = new Label(parent, text.toString(), parent.mouseX, parent.mouseY, Color.WHITE);
              l.display();
          }
     }
     //Drag Code taken and modified from  http://www.learningprocessing.com/examples/chapter-5/draggable-shape/
     //Credit:Daniel Shiffman 
     void clicked(int mx, int my, List<PieBar> bars) {

          //Only drag one item at a time
          for (PieBar b : bars) {
               if (b.dragging == true) {
                    return;
               }
          }

          if (mx > xCord && mx < xCord + width && my > yCord && my < yCord + height + titleOffset) {
               dragging = true;
               rollover = false; //rollover should not remain when dragging
               // If so, keep track of relative location of click to corner of rectangle
               offsetX = xCord - mx;
               offsetY = yCord - my;
          }
     }

     // Is a point inside the rectangle (for rollover) and not dragging
     void rollover(int mx, int my) {
          if ( !dragging && (mx > xCord && mx < xCord + width && my > yCord && my < yCord + height)) {
               rollover = true;
          } else {
               rollover = false;
          }
     }

     // Stop dragging
     void stopDragging(List<PieBar> bars) {
          dragging = false;
          for (PieBar bar : bars) {
               //Don't allow bars to overlap
               if (bar != this) {
                    if ((bar.yCord < this.yCord + titleOffset) && (bar.yCord > this.yCord - titleOffset)) ///overlap
                    {
                         this.yCord += height + titleOffset + padding; //padding accouts for text under bar
                    }
               }
          }
     }

     // Drag the rectangle
     void drag(int mx, int my) {
          if (dragging) {
               float tempX = mx + offsetX;
               float tempY = my + offsetY;
               //Don't allow the bar to go out of bounds (into padding)
               if (tempX > padding && tempX + width + padding < parent.width) {
                    xCord = tempX;
               }
               if (tempY > padding && tempY + height + titleOffset + padding < parent.height) {
                    yCord = tempY;
               }
               System.out.println(padding);
               System.out.println(tempY);
               System.out.println(tempY + height + titleOffset + padding);
               System.out.println(parent.height);
          }
     }
}

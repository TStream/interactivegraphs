/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

import static Chart.AbstractChartDrawer.titleOffset;
import java.util.*;
import java.awt.Color;
import java.awt.geom.Point2D;

//add colors
public class StackedBar extends AbstractChartDrawer {

     public StackedBar(Chart parent, Map<String, Integer> values, String title, int x, int y) {
          super(parent, values, title, x, y);
     }

     public StackedBar(Chart parent, Map<String, Integer> values, String title, int x, int y, float width, float height) {
          super(parent, values, title, x, y, width, height);
     }

     @Override
     public void init() {
          float total = getTotal();

          chunk = new TreeMap<String, Float>();
          for (String key : values.keySet()) {
               Float value = (float) values.get(key) / total * width;
               chunk.put(key, value);
          }
     }

     @Override
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
               Color c;
               if (colour != null && i < colour.size()) {
                    c = colour.get(i);
               } else {
                    c = new Color(255, 255, 255);
               }
               parent.fill(c.getRGB());

               //Rectangle behaviour on rollover/drag
               if ((dragging) || (rollover)) {
                    parent.stroke(c.getRGB());
                    parent.strokeWeight(3); //Rectange grows
               } else {
                    parent.stroke(0);
                    parent.strokeWeight(2);
               }

               parent.rect(x, y, value, height);
               //Text colour
               parent.fill(255);
               //Don't let text over flow borders
               if (parent.textWidth(key) < value) {
                    parent.text(key, x + 5, y + height + 10);
               }

               x += value;
               i++;
               if (colour != null && i == colour.size()) { //used all default colours
                    i = 0; //recycle through them
               }
          }
     }

}

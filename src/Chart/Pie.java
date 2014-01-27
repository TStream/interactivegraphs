package Chart;


import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/*Melissa Trebell
 *
 */
public class Pie extends AbstractChartDrawer {

     public Pie(Chart parent, Map<String, Integer> values, String title, int x, int y) {
          super(parent, values, title, x, y);
     }

     public Pie(Chart parent, Map<String, Integer> values, String title, int x, int y, float diameter) {
          super(parent, values, title, x, y, diameter, diameter);
     }

     @Override
     public void display() {
          float a1 = 0, a2;
          
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

          y += titleOffset + height/2;
           
          for (String key : chunk.keySet()) {
               Float a = chunk.get(key);
               // choose a random fill
               parent.fill(a1, 255, 255);
               //put the last leading angle into a2
               a2 = a1;

               //update the current leading angle
               a1 += a;
               parent.arc(x, y, width, height, parent.radians(a2), parent.radians(a1));
          }
     }

     @Override
     public void init() {
          float total = getTotal();

          chunk = new TreeMap<String, Float>();
          for (String key : values.keySet()) {
               Float value = (float) values.get(key) / total * 360;
               chunk.put(key, value);
          }
     }
     
     @Override
     public void drag(int mx, int my) {
          if (dragging) {
               float tempX = mx + offsetX;
               float tempY = my + offsetY;
               //Don't allow the bar to go out of bounds (into padding)
               if (tempX > padding + width/2 && tempX + width/2 + padding < parent.width) {
                    xCord = tempX;
               }
               if (tempY > titleOffset + padding && tempY + height + titleOffset + padding  < parent.height) {
                    yCord = tempY;
               }
          }
     }



}

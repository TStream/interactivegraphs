package Chart;


import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractChartDrawer implements ChartDrawer {

     boolean dragging = false; // Is the object being dragged
     boolean rollover = false; // Is the mouse over the bar
     float offsetX, offsetY; // Mouseclick offset
     Map<String, Integer> values; //The actual values
     Map<String, Float> chunk; // The values used to draw graph to scale
     Chart parent;
     String title;
     float xCord;
     float yCord;
     float width;
     float height;
     boolean hover;
     static int padding = 5;
     static int titleOffset = 10; //The inital value of the y Cord is where title is drawn the bar is drawn at Ycord + TitleOffset
     List<Color> colour = new ArrayList<Color>();

     public AbstractChartDrawer(Chart parent, Map<String, Integer> values, String title, int x, int y) {
          this.values = values;
          this.title = title;
          this.parent = parent;
          xCord = x + padding;
          yCord = y;

          width = parent.getWidth() - 2 * padding - xCord; // padding needs to be at beging and end

          init(); //setup of values

          offsetX = 0;
          offsetY = 0;
          height = 10;
     }

     public AbstractChartDrawer(Chart parent, Map<String, Integer> values, String title, int x, int y, float width, float height) {
          this.values = values;
          this.title = title;
          this.parent = parent;
          xCord = x + padding;
          yCord = y;
          this.width = width;
          this.height = height;
          float total = getTotal();

          init();

          offsetX = 0;
          offsetY = 0;
     }

     protected float getTotal() {
          float total = 0;
          for (String key : values.keySet()) {
                    total += values.get(key);
          }
          return total;
     }

     @Override
     public abstract void display();

     public abstract void init();

     @Override
     public void displayLabel() {
          //On rollover show bar info
          if (rollover) {
               StringBuilder text = new StringBuilder();
               for (String key : values.keySet()) {
                    text.append(key);
                    text.append(" = ");
                    text.append(values.get(key));
                    text.append(",");
               }
               text.deleteCharAt(text.length() - 1); //remove extra ,
               Label l = new Label(parent, text.toString(), parent.mouseX, parent.mouseY, Color.WHITE);
               l.display();
          }
     }

     //Drag Code taken and modified from  http://www.learningprocessing.com/examples/chapter-5/draggable-shape/
     //Credit:Daniel Shiffman 
     @Override
     public void clicked(int mx, int my) {

          //check to see if another bar is being dragged
          if (mx > xCord && mx < xCord + width && my > yCord && my < yCord + height + titleOffset && draggable()) {
               dragging = true;
               rollover = false; //rollover should not remain when dragging
               // If so, keep track of relative location of click to corner of rectangle
               offsetX = xCord - mx;
               offsetY = yCord - my;
          }
     }

     // Is a point inside the rectangle (for rollover) and not dragging
     @Override
     public void rollover(int mx, int my) {
          if (!dragging && (mx > xCord && mx < xCord + width && my > yCord && my < yCord + height)) {
               rollover = true;
          } else {
               rollover = false;
          }
     }

     // Stop dragging
     @Override
     public void stopDragging() {
          dragging = false;
     }

     // Drag the shape
     @Override
     public void drag(int mx, int my) {
          if (dragging) {
               float tempX = mx + offsetX;
               float tempY = my + offsetY;
               //Don't allow the bar to go out of bounds (into padding)
               if (tempX > padding && tempX + width + padding < parent.width) {
                    xCord = tempX;
               }
               if (tempY > padding + titleOffset && tempY + height + titleOffset + 2*padding < parent.height) { //2*padding accounts for text underneath + padding
                    yCord = tempY;
               }
          }
     }

     @Override
     public Point2D getCord() {
          return new Point2D.Float(xCord, yCord);
     }

     ;

     //Set colours to use
     @Override
     public void setColors(List<Color> c) {
          colour = c;
     }

     //add color to list
     @Override
     public void addColor(Color c) {
          colour.add(c);
     }

     public boolean draggable() {
          List<ChartDrawer> bars = parent.getBars();
          //Only drag one item at a time
          for (ChartDrawer b : bars) {
               if (b.dragging()) {
                    return false;
               }
          }
          return true;
     }

     @Override
     public boolean dragging() {
          return dragging;
     }
     
}

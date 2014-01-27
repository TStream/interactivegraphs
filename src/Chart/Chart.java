/*Melissa Trebell
 *Package: Interactive Chart,
 * Chart Allows you to create and add pie charts or stacked bar charts
 * All elements can be moved around and rearranged.
 */
package Chart;

import java.awt.Dimension;
import java.awt.Point;
import processing.core.*;
import java.util.*;
import query.Query;

//Chart label
//Chart square
public class Chart extends PApplet {

     private List<ChartDrawer> bars = new ArrayList<ChartDrawer>();
     private Dimension chartSize;
     private String title;
     private int titleSize = 20;
     private int padding = 50;  //padding between bars
     private int yPos = titleSize + 15; //yPos of next bar

     public Chart(String title, Dimension size) {
          this.chartSize = size;
          this.title = title;
     }

     public Chart(String title, Dimension size, List<ChartDrawer> bars) {
          this.title = title;
          this.chartSize = size;
          this.bars = bars;
     }

     @Override
     public void setup() {
          size(chartSize.width, chartSize.height);
     }

     @Override
     public void init() {
          super.init();
     }

     @Override
     public void draw() {
          stroke(255);
          strokeWeight(1);
          fill(100);
          rect(0, 0, width - 1, height - 1);
          fill(255);
          textSize(20);
          text(title, 20, 20);
          for (ChartDrawer b : bars) {
               b.rollover(mouseX, mouseY);
               b.drag(mouseX, mouseY);
               b.display();
          }
          for (ChartDrawer b : bars) {
               b.displayLabel();
          }
     }

     @Override
     public void mousePressed() {
          for (ChartDrawer b : bars) {
               b.clicked(mouseX, mouseY);
          }
     }

     @Override
     public void mouseReleased() {
          for (ChartDrawer b : bars) {
               b.stopDragging();
          }
     }

     public void addBar(ChartDrawer b) {
          bars.add(b);
     }

     @Override
     public int getWidth() {
          return chartSize.width;
     }

     @Override
     public int getHeight() {
          return chartSize.height;
     }

     public void addValues(String key, Map<String, Integer> values) {
          addBar(new StackedBar(this, values, key, 0, yPos));
          yPos += padding;
     }
     
     protected List<ChartDrawer> getBars() {
          return bars;
     }
}

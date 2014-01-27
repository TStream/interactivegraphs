package cmpt305;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import processing.core.PApplet;
import static processing.core.PApplet.map;

public class barThumb extends PApplet{

     int width, height;
     Map<String, Integer> bars;
     Color c; //fill color of thumb

     public barThumb(int width, int height, Color c) {
          this.height = height;
          this.width = width;
          this.c = c;

     }

     public barThumb(int width, int height, Color c, Map<String, Integer> bars) {
          this.height = height;
          this.width = width;
          this.c = c;
          this.bars = bars;
     }

     @Override
     public void setup() {
          size(width, height);
          int[] background = {Color.BLACK.getRGB()};

     }

     public void draw() {
          display(bars);
     }


     public void display(Map<String, Integer> values) {

          //SetUp
          List<Float> thmbValues = new ArrayList();
          Integer max = getGreatest(values);

          for (String key : values.keySet()) {
               Integer value = values.get(key);

               if (value != null) {
                    thmbValues.add(map(value, 0, max, 0, height));
               }
          }

          //Calculate width of each bar
          float bWidth = width / thmbValues.size() - 1;
          float x = 0;
          float y = height - 5;

          //draw
          for (float f : thmbValues) {
               fill(c.getRGB());
               rect(x, y - f, bWidth, y);
               x += bWidth;
          }



     }

     public void addBars(Map<String, Integer> bars) {
          this.bars = bars;
     }

     Integer getGreatest(Map<String, Integer> values) {
          Integer great = 0;
          for (String key : values.keySet()) {
               Integer temp = values.get(key);
               if (temp != null) {
                    if (temp > great) {
                         great = temp;
                    }
               }

          }
          return great;
     }
     
     public static void main(String args[]) {

          JFrame panel;
          barThumb applet;

          applet = new barThumb(200, 200, Color.BLUE);
          applet.init();
          panel = new JFrame();
          Map<String,Integer> values = new HashMap<String,Integer>();
          values.put("A",23);
          values.put("B",20);
          values.put("C",14);
          values.put("D",30);
          values.put("E",7);
          values.put("F",22);
          values.put("G",13);
          values.put("H",15);
          values.put("I",16);
          values.put("J",17);
             values.put("Aw",23);
          values.put("Bw",20);
          values.put("Cw",12);
          values.put("Dw",30);
          values.put("Ew",27);
          values.put("Fw",22);
          values.put("Gw",12);
          values.put("Hw",15);
          values.put("Iw",16);
          values.put("Jw",17);
          
          applet.addBars(values);
          panel.add(applet);
          panel.setSize(200, 200);
          panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          panel.setVisible(true);
     }
}

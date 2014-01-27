package cmpt305;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import processing.core.PApplet;
import static processing.core.PApplet.map;

/*Melissa Trebell
 *
 */
public class LineThmb extends PApplet {

     int width, height;
     Map<String, List<Integer>> lines; //key [points]
     Color c; //fill color of thumb

     public LineThmb(int width, int height, Color c) {
          this.height = height;
          this.width = width;
          this.c = c;

     }

     public LineThmb(int width, int height, Color c, Map<String, List<Integer>> lines) {
          this.height = height;
          this.width = width;
          this.c = c;
          this.lines = lines;
     }

     @Override
     public void setup() {
          size(width, height);

     }

     public void draw() {
          smooth();
          fill(Color.black.getRGB());
          rect(0,0,width,height);
          display(lines);
     }

     public void display(Map<String, List<Integer>> lines) {
          //SetUp
          List<Float> thmbValues = new ArrayList();
          Integer max = getGreatest(lines);

          for (String k : lines.keySet()) {
               List<Integer> value = lines.get(k);

               if (value != null) {
                    
                    //Fitting point on graph
                    for (Integer v : value) {
                         thmbValues.add(map(v, 0, max, 0, height-10));
                    }

                    //Calculate spacing between each point
                    float lSpacing = width / thmbValues.size() - 1;
                    float x = 0;
                    float y = height - 5;

                    //draw
                    for (int i = 0; i < thmbValues.size() - 1; i++) {
                         stroke(c.getRGB());
                         line(x, y - thmbValues.get(i), x + lSpacing, y - thmbValues.get(i + 1));
                         x += lSpacing;
                    }
               }
          }


     }

     public void addPoints(Map<String, List<Integer>> lines) {
          this.lines = lines;
     }

     Integer getGreatest(Map<String, List<Integer>> values) {
          Integer great = 0;
          for (String key : values.keySet()) {
               List<Integer> temp = values.get(key);
               for (Integer t : temp) {

                    if (t > great) {
                         great = t;
                    }
               }

          }
          return great;
     }

     public static void main(String args[]) {

          JFrame panel;
          LineThmb applet;

          applet = new LineThmb(200, 200, Color.BLUE);
          applet.init();
          panel = new JFrame();
          Map<String, List<Integer>> values = new TreeMap<String, List<Integer>>();
          List<Integer> temp = new ArrayList<Integer>();
          temp.add(34);
          temp.add(23);
          temp.add(60);
          temp.add(30);
          values.put("A", temp);
          temp = new ArrayList<Integer>();
          temp.add(24);
          temp.add(20);
          temp.add(50);
          temp.add(40);
          values.put("B", temp);

          applet.addPoints(values);

          panel.add(applet);
          panel.setSize(200, 200);
          panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          panel.setVisible(true);
     }
}

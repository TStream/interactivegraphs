package cmpt305;

import static cmpt305.LineGraph.getContrastColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import processing.core.*;

public final class BarGraph implements GraphDrawer {

     //PApplet parent;
     int padding = 5;
     float width = 1;
     int margin = 50;
     int titleOffset;
     int[] color;
     List<Bars> graphData;
     Map<String,List<Integer>> removed = new HashMap<String,List<Integer>>();
     Dimension graphWindow;
     Integer[] selectedColor = new Integer[3];
     Integer[] hoverColor = new Integer[3];
     List<Integer[]> unhoveredColor = new ArrayList<Integer[]>();
     Map<String,Integer[]> unhovered = new HashMap<String,Integer[]>(); //Maps every subgroup to a color from unhoveredColor
     Point currentHovered = new Point(-1, -1);
     List<Point> currentSelected;

     public BarGraph() {

          graphData = new ArrayList<Bars>();
          color = new int[]{255, 255, 255};
          List<Color> unhover = new ArrayList<Color>();
          unhover.add(Color.BLACK);
          unhover.add(Color.PINK);
          unhover.add(Color.GREEN);
          SetBarColors(Color.BLUE, Color.RED, unhover);
     }

     public BarGraph(Color hover, Color selected, List<Color> unhovered) {
          this();
          SetBarColors(hover, selected, unhovered);
     }

     @Override
     public void displayValues(PApplet parent, Dimension offset, Dimension graphWindow, float yScale) {
          // my additions

          int x = offset.width + padding;

          for (int i = 0; i < graphData.size(); i++) {
               Bars temp = graphData.get(i);
               width = (graphWindow.width / graphData.size()) - 25 / graphData.size();
               int[] contrast = getContrastColor(color);

               parent.stroke(contrast[0], contrast[1], contrast[2]);
               temp.SetLimits(new Point(x, graphWindow.height + margin), width, yScale);
               temp.Redraw(parent, unhovered, hoverColor, selectedColor);

               if (temp.getMax() > 0) {
                    parent.fill(contrast[0], contrast[1], contrast[2]);
                    
                    String title = temp.getBarTitle().length() > 18 ? 
                            temp.getBarTitle().substring(0, 18) + "..." :
                            temp.getBarTitle();
                    int tSize = width /(title.length() + 2) > 15 ? 15 : (int) width /(title.length() + 2) ;
                    parent.textSize(tSize);
                    int textOffset = (int) (width - parent.textWidth(title)) / 2;
                    parent.text(title, x + textOffset, parent.getHeight() - (offset.height - 17));
               }
               x += width + padding;
          }


          if (currentHovered.x > -1 && currentHovered.y > -1 && currentHovered.y < graphData.get(currentHovered.x).size()) {
               float popSecond = width / graphData.get(currentHovered.x).getLength();
               float popOffset = ((width + padding) * currentHovered.x) + (popSecond * currentHovered.y) + offset.width + padding;
               float popDown = (margin + graphWindow.height) - graphData.get(currentHovered.x).getBarValue(currentHovered.y) * yScale;
               boolean flip = currentHovered.x >= graphData.size() - 1 ? true : false;

               PopUpBox(parent, new Point((int) popOffset, (int) popDown),
                       new Point((int) (popOffset + popSecond), (int) popDown),
                       graphData.get(currentHovered.x).getBarCategory(currentHovered.y),
                       graphData.get(currentHovered.x).getBarTitle(),
                       graphData.get(currentHovered.x).getBarValue(currentHovered.y), flip);
               currentHovered = new Point(-1, -1);
          }
     }

     private void PopUpBox(PApplet graph, Point highlightedLeft, Point highlightedRight, String name, String scaler, int value, boolean oppositeSide) {
          int rectWidth = 240;
          int rectOffset = 0;

          int[] contrast = getContrastColor(color);

          if (oppositeSide) {
               rectOffset = highlightedRight.x - rectWidth;
          } else {
               rectOffset = highlightedLeft.x;
          }

          // start of box creation
          List<String> toWrap = TextWrapper(24, scaler);
          int iOffset = (2 + toWrap.size()) * 15;
          
          int iVerticalOff = highlightedLeft.y;
          int upDown = 10;
          if (iOffset + 15 > highlightedLeft.y - 20)
          {
              iVerticalOff = highlightedLeft.y + iOffset + 25;
              upDown *= -1;
          }
          
          graph.strokeWeight(1);
          graph.fill(contrast[0], contrast[1], contrast[2], 128);
          graph.beginShape();
          graph.vertex(highlightedRight.x, highlightedRight.y);
          graph.vertex(highlightedLeft.x, highlightedLeft.y);


          graph.vertex(rectOffset, highlightedLeft.y - upDown);
          graph.vertex(rectOffset + rectWidth, highlightedRight.y - upDown);

          graph.endShape(PApplet.CLOSE);
          
          graph.fill(255, 255, 255);
          graph.textSize(12);
          
          graph.rect(rectOffset, iVerticalOff - (iOffset + 15),
                  rectWidth, iOffset + 5);
          
          graph.fill(0, 0, 0);
          for (int i = 0; i < toWrap.size(); i++) {
               graph.text(toWrap.get(i), rectOffset + 10, iVerticalOff - (iOffset - i * 15));
          }

          graph.text("cat: " + name, rectOffset + 10, iVerticalOff - 30);
          graph.text("value: " + value, rectOffset + 10, iVerticalOff - 15);


     }

     private List<String> TextWrapper(int characterLimit, String value) {
          String[] words = value.split(" ");
          List<String> wrapped = new ArrayList<String>();
          StringBuilder sb = new StringBuilder();
          sb.append("Sc: ");
          for (String s : words) {
               if (sb.toString().length() >= characterLimit) {
                    wrapped.add(sb.toString());
                    sb = new StringBuilder();
                    sb.append("     ").append(s).append(" ");
               } else {
                    sb.append(s).append(" ");
               }
          }
          wrapped.add(sb.toString());
          return wrapped;
     }

     @Override
     public void select(Point mousePosition, Dimension offset, Dimension graphWindow) {
          Point subsetInfo = GetSubsetInfo(mousePosition, offset, graphWindow);

          graphData.get(subsetInfo.x).select(new Point(subsetInfo.y - padding,
                  mousePosition.y - offset.height));

          Map<String, List<String>> m = selectedMap();
          for (String key : m.keySet()) {
               System.out.println(key + " " + m.get(key));
          }
     }

     @Override
     public void hover(Point mousePosition, Dimension offset, Dimension graphWindow) {
          Point subsetInfo = GetSubsetInfo(mousePosition, offset, graphWindow);
          for (Bars b : graphData) {
               b.hoverReset();
          }
          int innerHover = -1;
          if(subsetInfo.x < graphData.size() && subsetInfo.x > -1)
           innerHover = graphData.get(subsetInfo.x).hover(new Point(subsetInfo.y - padding,
                  mousePosition.y - offset.height));

          if (innerHover > -1) {
               currentHovered = new Point(subsetInfo.x, innerHover);
          }
     }

     private Point GetSubsetInfo(Point mousePosition, Dimension offset, Dimension graphWindow) {
          int actual = mousePosition.x - offset.width;
          float spacer = graphWindow.width / graphData.size();
          return new Point((int)Math.floor(actual / spacer), (int) (actual % spacer));
     }

     @Override
     public int getMax() {
          int max = 0;
          for (int i = 0; i < graphData.size(); i++) {
               if (graphData.get(i).getMax() > max) {
                    max = graphData.get(i).getMax();
               }
          }
          return max;
     }

     @Override
     public void AddTypeValue(String barName, Map<String, Integer> subCategoryValues) {
          Set<String> temp = subCategoryValues.keySet();
          Bars barCluster = new Bars(barName);

          int color = unhovered.keySet().size(); //color to start with
          for (String key : temp) {
               barCluster.AddBar(key, subCategoryValues.get(key));
               if(!unhovered.containsKey(key)){
                    unhovered.put(key,unhoveredColor.get(color));
                    color++;
                    if(color == unhoveredColor.size())
                         color=0;
               }
          }

          graphData.add(barCluster);
          Collections.sort(graphData);
          
          //Add color mapping
          
     }

     public void SetBarSelectedColor(int r, int g, int b) {
          selectedColor[0] = r;
          selectedColor[1] = g;
          selectedColor[2] = b;
     }

     public void SetBarHoverColor(int r, int g, int b) {
          hoverColor[0] = r;
          hoverColor[1] = g;
          hoverColor[2] = b;
     }

     public void SetBarColors(Color hover, Color selected, List<Color> unhovered) {
          SetBarSelectedColor(selected.getRed(), selected.getGreen(), selected.getBlue());
          SetBarHoverColor(hover.getRed(), hover.getGreen(), hover.getBlue());
          unhoveredColor = new ArrayList<Integer[]>();
          for (Color unhover : unhovered) {
               unhoveredColor.add(new Integer[]{unhover.getRed(), unhover.getGreen(), unhover.getBlue()});
          }
     }

     private static int[] getContrastColor(int[] color) {
          double y = (299 * color[0] + 587 * color[1] + 114 * color[2]) / 1000;
          return y >= 128 ? new int[]{0, 0, 0} : new int[]{255, 255, 255};
     }

     @Override
     public void SetBackground(int[] color) {
          this.color = color;
     }

     @Override
     public List<SelectedValues> getPointsSelected() {
          List<GraphDrawer.SelectedValues> sv = new ArrayList<GraphDrawer.SelectedValues>();
          int bar = 0;
          for (Bars b : graphData) {
               for (int i = 0; i < b.getLength(); i++) {
                    if (b.getBarSelected(i)) {
                         sv.add(new GraphDrawer.SelectedValues(b.getBarTitle(), b.getBarCategory(i),
                                 b.getBarValue(i), new Point(bar, i)));
                    }
               }
               ++bar;
          }
          return sv;
     }

     //BarTitle Clusters Value
     @Override
     public Map<String, List<String>> selectedMap() {
          Map<String, List<String>> m = new TreeMap<String, List<String>>();
          int bar = 0;
          for (Bars b : graphData) {
               for (int i = 0; i < b.getLength(); i++) {
                    if (b.getBarSelected(i)) {
                         String key = b.getBarTitle();
                         List<String> add = m.get(key);
                         if (add == null) {
                              add = new ArrayList<String>();
                         }

                         add.add(b.getBarCategory(i));
                         add.add(b.getBarValue(i) + "");
                         m.put(key, add);
                    }
               }
               ++bar;
          }
          return m;
     }

     //selectedMap
     @Override
     public void SetPointsAsSelected(List<SelectedValues> values) {
          for (SelectedValues s : values) {
               graphData.get(s.referenceLoc.x).SetSelected(s.referenceLoc.y, true);
          }
     }

     @Override
     public void selectSet(String key, boolean value) {
          for (Bars b : graphData) {
               for (int i = 0; i < b.size(); i++) {
                    String test = b.getBarCategory(i);
                    if (key.compareTo(test) == 0) {
                         b.SetSelected(i, value);
                    }
               }
          }
     }

     @Override
     public void deleteSet(String key){ 
          List<Integer> temp = new ArrayList<Integer>();
          for (Bars b : graphData) {
               for (int i = 0; i < b.size(); i++) {
                    String test = b.getBarCategory(i);
                    if (key.compareTo(test) == 0) {
                         temp.add(b.getBarValue(i));
                         b.remove(i);
                    }
               }
          }
          removed.put(key, temp);
     }
     
     @Override 
     public void addSet(String key){
          List<Integer> add = removed.get(key);
          if(removed != null){
           int i =0;
           for (Bars b : graphData) {
               b.AddBar(key, add.get(i));
               i++;
          }
          }
     }

     @Override
     public Map<String, Color> colorKey() {
          Map<String, Color> legend = new TreeMap<String, Color>();
          for (String key : unhovered.keySet()) {
               Integer[] temp = unhovered.get(key);
               Color value = new Color(temp[0], temp[1], temp[2]);
               legend.put(key, value);
          }
          return legend;
     }
}

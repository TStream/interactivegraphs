package cmpt305;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import processing.core.*;

public class LineGraph implements GraphDrawer {

     Color[] lineDefaults = new Color[]{Color.CYAN, Color.MAGENTA, Color.YELLOW,
     Color.GREEN, Color.orange, Color.RED,
     Color.PINK, Color.BLUE};
     int icolorIndex = 0;
     int spacing = 1; //Spacing between points one point = one day
     int padding = 15; //space before graph starts
     int offset = 3; //Point will respond to mouse actions in this radius
     int margin;
     int[] backgroundColor = new int[3];
     int[] selectedColor = new int[3];
     int[] hoverColor = new int[3];
     int[] unhoveredColor = new int[3];
     List<Lines> lines;
     List<Lines> removed = new ArrayList<Lines>();
     String[] scale;
     Point currentHover;
     List<GraphDrawer.SelectedValues> currentSelected;

     public LineGraph(List<String> scale) {
          this.margin = Graph.margin;
          lines = new ArrayList<Lines>();

          this.scale = scale.toArray(new String[scale.size()]);

          currentHover = new Point(-1, -1);
          currentSelected = new ArrayList<GraphDrawer.SelectedValues>();
     }

     public LineGraph(List<String> scale, Color hover, Color selected, Color unhovered) {
          this(scale);
          selectedColor = new int[]{selected.getRed(), selected.getGreen(), selected.getBlue()};
          hoverColor = new int[]{hover.getRed(), hover.getGreen(), hover.getBlue()};
          unhoveredColor = new int[]{unhovered.getRed(), unhovered.getGreen(), unhovered.getBlue()};
     }

     @Override
     public void displayValues(PApplet parent, Dimension offset, Dimension graphWindow, float yScale) {
          int width = (int) ((graphWindow.width / (getMaxLength() - 1)) - (25.0 / (getMaxLength() - 1)));
          int x = offset.width;

          int[] contrast = getContrastColor(backgroundColor);
          for (Lines line : lines) {
               line.SetPointPositions(new Point(x, graphWindow.height + margin), width, yScale);
               line.Redraw(parent, unhoveredColor, hoverColor, selectedColor);
          }

          for (int i = 0; i < getMaxLength(); i++) {
               parent.textSize(((width / scale[i].length()) + 2) > 15 ? 15 : (width / scale[i].length()) + 2);
               parent.fill(contrast[0], contrast[1], contrast[2]);
               parent.text(scale[i], (i * width + x) - parent.textWidth(scale[i]) / 2,
                       graphWindow.height + margin + 17);
          }

          if (currentHover.x != -1 && currentHover.y != -1) {
               int comp = lines.get(currentHover.x).getLength() - 1 <= currentHover.y
                       ? currentHover.y - 1 : currentHover.y + 1;

               PopUpBox(parent, lines.get(currentHover.x).getLineLocation(currentHover.y),
                       lines.get(currentHover.x).getLineLocation(comp), lines.get(currentHover.x).getLineTitle(),
                       scale[currentHover.y],
                       lines.get(currentHover.x).getLineValue(currentHover.y),
                       lines.get(currentHover.x).getLength() - 1 <= currentHover.y);
          }
     }

     private void PopUpBox(PApplet graph, Point highlighted, Point next, String name, String scaler, int value, boolean oppositeSide) {
          int offsetI = 10;
          int rectWidth = 120;
          int rectOffset = 10;

          int[] contrast = getContrastColor(backgroundColor);

          if (oppositeSide) {
               rectOffset += (rectWidth);
               rectOffset = -rectOffset;
               offsetI = -offsetI;
          }

          graph.strokeWeight(1);
          graph.fill(contrast[0], contrast[1], contrast[2], 128);
          graph.beginShape();
          graph.vertex(highlighted.x, highlighted.y);

          if (next.y > highlighted.y && highlighted.y > 85) {
               graph.vertex(highlighted.x + offsetI, highlighted.y - 65);
               graph.vertex(highlighted.x + offsetI, highlighted.y - 10);
          } else {
               graph.vertex(highlighted.x + offsetI, highlighted.y + 10);
               graph.vertex(highlighted.x + offsetI, highlighted.y + 65);
          }
          graph.endShape(PApplet.CLOSE);

          graph.fill(255, 255, 255);
          graph.textSize(12);
          if (next.y > highlighted.y && highlighted.y > 85) {
               graph.rect(highlighted.x + rectOffset, highlighted.y - 65, rectWidth, 55);
               graph.fill(0, 0, 0);

               graph.text("value: " + value, highlighted.x + rectOffset + 10, highlighted.y - 20);
               graph.text(name, highlighted.x + rectOffset + 10, highlighted.y - 35);
               graph.text("sc: " + scaler, highlighted.x + rectOffset + 10, highlighted.y - 50);
          } else {
               graph.rect(highlighted.x + rectOffset, highlighted.y + 10, rectWidth, 55);
               graph.fill(0, 0, 0);

               graph.text("value: " + value, highlighted.x + rectOffset + 10, highlighted.y + 60);
               graph.text(name, highlighted.x + rectOffset + 10, highlighted.y + 45);
               graph.text("sc: " + scaler, highlighted.x + rectOffset + 10, highlighted.y + 25);
          }
     }

     @Override
     public void select(Point mousePosition, Dimension offset, Dimension graphWindow) {
          int linePlace = 0;
          for (Lines line : lines) {
               int placement = line.Select(mousePosition);
               if (placement > -1) {
                    if (line.getLineSelected(placement)) {
                         currentSelected.add(new GraphDrawer.SelectedValues(line.getLineTitle(), scale[placement],
                                 line.getLineValue(placement), new Point(linePlace, placement)));
                    } else {
                         for (int i = currentSelected.size() - 1; i >= 0; i--) {
                              GraphDrawer.SelectedValues sv = new GraphDrawer.SelectedValues(line.getLineTitle(),
                                      scale[placement], line.getLineValue(placement), new Point(linePlace, placement));
                              if (currentSelected.get(i).equals(sv)) {
                                   currentSelected.remove(i);
                              }
                         }
                    }
               }
               ++linePlace;
          }
     }

     @Override
     public void hover(Point mousePosition, Dimension offset, Dimension graphWindow) {
          int lin = -1;
          int lineValue = -1;
          int i = 0;
          int temp;

          for (Lines line : lines) {
               temp = line.Hover(mousePosition);
               if (temp > -1) {
                    lin = i;
                    lineValue = temp;
               }
               ++i;
          }
          currentHover = new Point(lin, lineValue);
     }

     @Override
     public int getMax() {
          int max = 0;
          for (Lines line : lines) {
               if (line.getMax() > max) {
                    max = line.getMax();
               }
          }
          return max;
     }

     private int getMaxLength() {
          int max = 0;
          for (Lines line : lines) {
               if (line.getLength() > max) {
                    max = line.getLength();
               }
          }
          return max;
     }

     @Override
     public void AddTypeValue(String name, Map<String, Integer> subCategoryValues) {
          Lines temp = new Lines(name);
          Random rnd = new Random();

          if (icolorIndex < lineDefaults.length) {
               temp.SetLineColor(new int[]{lineDefaults[icolorIndex].getRed(),
                    lineDefaults[icolorIndex].getGreen(),
                    lineDefaults[icolorIndex].getBlue()});
               ++icolorIndex;
          } else {
               temp.SetLineColor(new int[]{(int) (rnd.nextDouble() * 255),
                    (int) (rnd.nextDouble() * 255),
                    (int) (rnd.nextDouble() * 255)});
          }

          List<String> order = new ArrayList<String>();
          for (String s : subCategoryValues.keySet()) {
               order.add(s);
          }
          Collections.sort(order);

          for (String s : order) {
               temp.AddLineValue(subCategoryValues.get(s));
          }

          lines.add(temp);
     }

     @Override
     public void SetBackground(int[] color) {
          backgroundColor = color;
     }

     public static int[] getContrastColor(int[] color) {
          double y = (299 * color[0] + 587 * color[1] + 114 * color[2]) / 1000;
          return y >= 128 ? new int[]{0, 0, 0} : new int[]{255, 255, 255};
     }

     @Override
     public List<SelectedValues> getPointsSelected() {
          return currentSelected;
     }

     @Override
     //Line Name, Dates
     public Map<String, List<String>> selectedMap() {
          //Return Line Name and all values selected on each line
          Map<String, List<String>> m = new TreeMap<String, List<String>>();
          for (SelectedValues s : currentSelected) {
               String key = s.selectedGroupOrLine;
               List<String> add = m.get(key);
               if (add == null) {
                    add = new ArrayList<String>();
               }
               //Update add
               add.add(s.selectedSubGroupOrScaler);

               m.put(key, add);
          }
          return m;
     }

     @Override
     public void SetPointsAsSelected(List<SelectedValues> values) {
          for (SelectedValues s : values) {
               lines.get(s.referenceLoc.x).SetSelected(s.referenceLoc.y, true);
          }
     }

     @Override
     public void selectSet(String key,boolean value) {
          for (Lines l : lines) {
               System.out.println(l.getLineTitle());
               if (l.getLineTitle().compareTo(key) == 0) {
                    for (int i = 0; i < l.size(); i++) {
                         l.SetSelected(i, value);
                    }
               }
          }
     }

     @Override
     public void deleteSet(String key) {
          for (Lines l : lines) {
               if (l.getLineTitle().compareTo(key) == 0) {
                    for (int i = 0; i < l.getLineLength(); i++) {
                         l.SetSelected(i, false);
                    }
                    removed.add(l);
                    lines.remove(l);
                    return;
               }
          }
     }
     
          @Override
     public void addSet(String key) {
           for (Lines l : removed) {
               if (l.getLineTitle().compareTo(key) == 0) {
                    for (int i = 0; i < l.getLineLength(); i++) {
                         l.SetSelected(i, false);
                    }
                    lines.add(l);
                    removed.remove(l);
                    return;
               }
          }
          
     }
     
     @Override
     public Map<String,Color> colorKey(){
          Map<String,Color> legend = new TreeMap<String,Color>();
          for(Lines l : lines){
               String key = l.getLineTitle();
               int[] temp = l.getColor();
               Color value = new Color(temp[0],temp[1],temp[2]);
               legend.put(key, value);
          }
          return legend;
     }


}

package cmpt305;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import processing.core.*;

enum GraphType {

     LINEGRAPH, BARGRAPH
};

public class GraphController extends PApplet {

     private Graph displayGraph;
     private Dimension graphSize = new Dimension();
     String graphTitle;
     String graphXTitle = "";
     String graphYTitle = "";
     Color backgroundColor;
     GraphDrawer type;

     public GraphController(Dimension graphSize, String mainTitle, GraphDrawer type) {
          this.type = type;
          this.graphSize = graphSize;
          graphTitle = mainTitle;

          backgroundColor = Color.WHITE;
     }

     public GraphController(Dimension graphSize, String mainTitle, String valueXAxis, String valueYAxis, GraphDrawer type) {
          this.type = type;
          this.graphSize = graphSize;
          graphTitle = mainTitle;

          graphXTitle = valueXAxis;
          graphYTitle = valueYAxis;
     }

     @Override
     public void setup() {
          size(graphSize.width, graphSize.height);

          Dimension designated = new Dimension(graphSize.width, graphSize.height);
          int[] background = {backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()};

          displayGraph = new Graph(designated, graphTitle, graphXTitle, graphYTitle, background, this, type);

          noLoop(); //stops the draw function from looping
          draw();
     }

     @Override
     public void draw() { //Draws the inital layout as follows
          background(backgroundColor.getRed(),
                  backgroundColor.getGreen(), backgroundColor.getBlue());
          displayGraph.display();
     }

     @Override
     public void mousePressed() {
          System.out.println(mouseX);

          if (mouseX > displayGraph.offset.getWidth()) {
                    type.select(new Point(mouseX, mouseY), displayGraph.offset, displayGraph.graphSpace);
                    redraw();
                    for (GraphDrawer.SelectedValues sv : type.getPointsSelected()) {
                         System.out.println(sv.selectedGroupOrLine + ", " + sv.selectedSubGroupOrScaler + " = " + sv.value);
                    }
                    System.out.println("*************************");
          }
     }

     @Override
     public void mouseMoved() {
          if (mouseX > displayGraph.offset.getWidth()) {
                    type.hover(new Point(mouseX, mouseY), displayGraph.offset, displayGraph.graphSpace);
                    redraw();
          }
     }

     @Override
     public void keyPressed() {
          if ((key == ENTER) || (key == RETURN)) {
               exit();
          }
     }

     public void selectSet(String key, boolean value) {
          type.selectSet(key, value);
          redraw();
     }

     public void deleteSet(String key) {
          type.deleteSet(key);
          redraw();
     }
     
     public void addSet(String key){
          type.addSet(key);
          redraw();
     }

     public Map<String, Color> colorKey() {
          return type.colorKey();
     }

     /**
      * *********************** extra functions
      * *************************************
      */
     public void SetOverrideColorScheme(Color background) {
          backgroundColor = background;
     }
}

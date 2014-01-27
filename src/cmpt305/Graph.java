/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Dimension;
import java.awt.Point;
import processing.core.*;

public class Graph {
     private float yScale = 1;
     public static int margin = 50;
     public static int titleOffset = 20;
     public boolean gridLines = true;
     
     //private
     public Dimension graphSpace;
     public Dimension offset;
     int[] colour = {0, 0, 0}; // black (R B G)
     
     String title;
     String xAxis;
     String yAxis;
     
     PApplet parent;
     GraphDrawer graph;

     public Graph(Dimension graphSpace, String mainTitle, String valueXAxis, String valueYAxis,
                    int[] color, PApplet parent, GraphDrawer type) 
     {
          this.parent = parent;
          graph = type;
          colour = color;
          
          title = mainTitle;
          yAxis = valueYAxis;  
          xAxis = valueXAxis; 
          
          offset = DeterminePositionOffset();
          this.graphSpace = DetermineGraphSpace(offset);

          graph.SetBackground(color);
          setYScale((this.graphSpace.height - 10) / (getMax() + 1)); // By deafault scale so max is at top of graph
     }
     
     public Graph(Dimension graphSpace, String mainTitle, PApplet parent, GraphDrawer type) 
     {
         this(graphSpace, mainTitle, "", "", new int[] {255,255,255}, parent, type);
     } 

     public void display() {
          buildGraph();
          graph.displayValues(parent, offset, graphSpace, yScale);
          displayText();
     }

     private void buildGraph() {         
          int[] contrast = getContrastColor(colour);
          parent.stroke(contrast[0], contrast[1], contrast[2]);
          parent.strokeWeight(4);
          
          parent.line(offset.width, margin + graphSpace.height, 
                  graphSpace.width + offset.width, margin + graphSpace.height);
          
          parent.line(offset.width, margin, offset.width, graphSpace.height + margin);
          
          float interval = (yScale * getMax()) / 10; 
          float scale = (getMax() / 10);
          
          for (int i = 0; i < 11; i++)
          {   
              parent.fill(contrast[0], contrast[1], contrast[2]);
              parent.textSize(10);
              String text = scale * 11 > 10 ? String.format("%.0f", scale * i) : String.format("%.2f", scale * i);
              parent.text(text, offset.width - parent.textWidth(text) - 5,(margin + graphSpace.height + 5) - interval * i);
              if (gridLines && i != 0)
              {
                   parent.stroke((contrast[0] + 100) % 255, (contrast[1] + 100) % 255, (contrast[2] + 100) % 255);
                   parent.strokeWeight(2);
                   parent.line(offset.width + 3, margin + graphSpace.height - interval * i, 
                           graphSpace.width + offset.width - 3,  margin + graphSpace.height - interval * i);
              }
          }
     }

     private void displayText() {
          int[] contrast = getContrastColor(colour);
          parent.fill(contrast[0], contrast[1], contrast[2]);
        if (!title.isEmpty())
        {
            parent.textSize(20);
            parent.text(title, (parent.getWidth() - parent.textWidth(title)) / 2, 20);
        }
          
        parent.textSize(15);  // applies all the way down
        if (!xAxis.isEmpty())    
            parent.text(xAxis, (parent.getWidth() - parent.textWidth(xAxis)) / 2,
                    parent.getHeight() - 17);
               
          parent.translate(10, (parent.getHeight() / 2) - ((yAxis.length() * 10)/2));
          parent.rotate((float) Math.PI/2);
          parent.text(yAxis, 0, 0);
     }

     public void select(Point  mousePos) {
           
          graph.select(mousePos, offset, graphSpace);
     }

     private float getMax() {
        return graph.getMax();
     }

     private void setYScale(float y) {
          yScale = y;
     }
  
     public Dimension GetGraphSpace()
     {
         return graphSpace;
     }
     
     private Dimension DeterminePositionOffset()
     {
          return new Dimension((yAxis.isEmpty() ? margin : margin + titleOffset),
                               (xAxis.isEmpty() ? margin : margin + titleOffset));
     }
     
     private Dimension DetermineGraphSpace(Dimension offset)
     {          
          return new Dimension(parent.width - (margin + offset.width),
                               parent.height - (margin + offset.height));
     }        
     
     public static int[] getContrastColor(int[] color) {
        double y = (299 * color[0] + 587 * color[1] + 114 * color[2]) / 1000;
        return y >= 128 ? new int[] {0,0,0} : new int[] {255, 255, 255};
     }
     
     public void selectSet(String key,boolean value){
          graph.selectSet(key,value);
          parent.draw();
     }
     public void deleteSet(String key){
          graph.deleteSet(key);
          parent.draw();
     }
}

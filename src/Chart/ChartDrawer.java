/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

import static Chart.AbstractChartDrawer.titleOffset;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author fix
 */
public interface ChartDrawer {
     public void display();
     public void displayLabel();
     public void clicked(int mx, int my); 
     public void rollover(int mx, int my); 
     public void drag(int mx, int my);
     public void stopDragging();
     public boolean dragging();
     public void setColors(List<Color> c);
     public void addColor(Color c); 
     public Point2D getCord();
}

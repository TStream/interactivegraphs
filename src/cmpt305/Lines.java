/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author Ian
 */
public class Lines {
    
    public class LinePoint
    {
        public int value;
        public boolean selected;
        public boolean hovered;
        public Point location;
        
        public LinePoint(int value)
        {
            this.value = value;
            this.selected = false;
            this.hovered = false;
            location = new Point(0,0);
        }
    }
    private List<LinePoint> lineValues;
    private String lineName;
    private int[] color;
    
    private float width;
    
    public Lines(String name)
    {
        lineValues = new ArrayList<LinePoint>();
        color = new int[] {0,0,0};
        lineName = name;
        width = 10;
    }
    
    public int size(){
         return lineValues.size();
    }
    
    public void Redraw(PApplet graph, int[] unhovered, int[] hover, int[] selected)
    {
        int i;
        int[] colorMain;
        for (i = 0; i < lineValues.size() - 1; i++)
        {
            graph.strokeWeight(5);
            graph.stroke(color[0], color[1], color[2]);
            
            graph.line(lineValues.get(i).location.x,lineValues.get(i).location.y, 
                    lineValues.get(i + 1).location.x, lineValues.get(i + 1).location.y);          
            
            if (lineValues.get(i).hovered)
               colorMain = hover; 
            else if (lineValues.get(i).selected)
                colorMain = selected;  
            else
                colorMain = unhovered;
                    
            graph.fill(colorMain[0], colorMain[1], colorMain[2]);
            graph.strokeWeight(2);
            
            graph.ellipse(lineValues.get(i).location.x, lineValues.get(i).location.y,
                    width, width);
        }
        
        if (lineValues.get(i).hovered)
           colorMain = hover; 
        else if (lineValues.get(i).selected)
            colorMain = selected;
        else
            colorMain = unhovered;
        
        graph.fill(colorMain[0], colorMain[1], colorMain[2]);
        graph.ellipse(lineValues.get(i).location.x, lineValues.get(i).location.y,
                width, width);        
    }
    

    public void SetPointPositions(Point base, float interval, float yScale)
    {
        int offset = 0;
        for (LinePoint line : lineValues)
        {
            line.location = new Point(base.x + offset, base.y - (int) (yScale * line.value)); 
            offset += interval;
        }
    }
    
    public void SetSelected(int index, boolean state)
    {
        lineValues.get(index).selected = state;
    }
    
    public int Select(Point mousePosition)
    {        
        for (int i = 0; i < lineValues.size(); i++ )  
            if (CheckArea(mousePosition, lineValues.get(i)))  
            {
                lineValues.get(i).selected = !lineValues.get(i).selected;  
                return i;
            }
        return -1;        
    }
    
    public int Hover(Point mousePosition)
    {
        int i = 0;
        int currentHover = -1;
        for (LinePoint point : lineValues)  
        {
            point.hovered = CheckArea(mousePosition, point);
            if (point.hovered)
                currentHover = i;
            ++i;
        }
        return currentHover;
    }
    
    private boolean CheckArea(Point mousePosition, LinePoint point)
    { 
        if (mousePosition.x > point.location.x - 5 && mousePosition.x < point.location.x + width + 5)
            if (mousePosition.y > point.location.y - 5 && mousePosition.y < point.location.y + width + 5)
                return true;
        return false;        
    }
    
    public void AddLineValue(Integer value)
    {
        lineValues.add(new LinePoint(value));
    }

    public void AddLineValues(List<Integer> values)
    {
        for (int value : values)
            AddLineValue(value);
    }
    
    public void SetLineColor(int[] color)
    {
        this.color = color;
    }

    public int getLineValue(int index)
    {
        return lineValues.get(index).value;
    }
    
    public int getLineLength()
    {
        return lineValues.size();
    }
    
    public boolean getLineSelected(int index)
    {
        return lineValues.get(index).selected;
    }
    
    public Point getLineLocation(int index)
    {
        return lineValues.get(index).location;
    }
    
    public int getLength()
    {
        return lineValues.size();
    }

    public String getLineTitle()
    {
        return lineName;
    }
    
    public int[] getColor(){
         return color;
    }

    public int getMax()
    {
        int max = 0;
        for (int i = 0; i < lineValues.size(); i++)
            if (lineValues.get(i).value > max)
                max = lineValues.get(i).value;
        return max;
    } 
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import processing.core.PApplet;

/**
 *
 * @author Ian
 */
public class Bars implements Comparable{

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Bars))
            throw new IllegalArgumentException("Thats not a bar... what is this nonsense?");
        
        Bars b = (Bars) o;
        
        return this.barName.compareTo(b.barName);
    }
    
   public class Element
   {
       public String category;
       public int value;
       public boolean selected;
       public boolean hovered;
       
       public Element(String s, int i)
       {
           category = s;
           value = i;
           selected = false;
           hovered = false;
       }
   }
    
   private List<Element> bars;
   private String barName;
   
   private Point placement;
   private float width;
   private float yScale;
   
    
   public Bars(String name)
   {
       bars = new ArrayList<Element>();
       width = 1;
       yScale = 1;
       placement = new Point(0,0);
       barName = name;
   }
   
   public void select(Point subPosition)
   {
        float inner = width / bars.size();
        int barSelect = (int) Math.floor(subPosition.x / inner);
        bars.get(barSelect).selected = !bars.get(barSelect).selected;      
   }
   
   public int hover(Point subPosition)
   {
       float inner = width / bars.size();
       int barSelect = (int) Math.floor(subPosition.x / inner);
       if(barSelect > -1 && barSelect < bars.size())
          bars.get(barSelect).hovered = true;      
       return barSelect;
   }
    public void hoverReset()
    {
        for (Element b : bars)
           b.hovered = false;
    }
   
   
   public void SetLimits(Point placement, float width, float yScale)
   {
       this.placement = placement;
       this.width = width;
       this.yScale = yScale;
   }
   
    public void SetSelected(int index, boolean state)
    {
        bars.get(index).selected = state;
    }
     
   public void Redraw(PApplet graph, Map<String,Integer[]> unhovered, Integer[] hover, Integer[] selected)
   {
       int inner = (int) width / bars.size();
       int innerOffset = 0;

       for (int i = 0; i < bars.size(); i++)
       {
            Integer[] color = unhovered.get(bars.get(i).category);
            
            if (bars.get(i).selected)
                color = selected;
            if (bars.get(i).hovered)
                color = hover;
                        
            Integer[] contrast = getContrastColor(color);
            graph.fill(color[0], color[1], color[2]);
            graph.rect(placement.x + innerOffset, placement.y, inner, 0 - (bars.get(i).value * yScale));
            
            int sizeRatio = (int) (inner / bars.get(i).category.length());
           
            graph.fill(contrast[0], contrast[1], contrast[2]);
            graph.textSize(sizeRatio);
            graph.text(bars.get(i).category, 
                (placement.x + (inner - graph.textWidth(bars.get(i).category))/2) + innerOffset,
                placement.y - 2);
            
            innerOffset += inner;
       }
   }
   
   public void AddBar(String category, Integer value)
   {
       this.bars.add(new Element(category, value));
   }
   
   public void AddBars(Map<String, Integer> subCategoryValues)
   {
       Set<String> temp = subCategoryValues.keySet();
       
       for(String key : temp)
           this.bars.add(new Element(key, subCategoryValues.get(key)));
   }
   
   public int getBarValue(int index)
   {
       return bars.get(index).value;
   }
   
   public String getBarCategory(int index)
   {
       return bars.get(index).category;
   }
   
   public boolean getBarSelected(int index)
   {
       return bars.get(index).selected;
   }
   
   public int getLength()
   {
       return bars.size();
   }
   
   public String getBarTitle()
   {
       return barName;
   }
   
   public int size(){
        return bars.size();
   }
   
   public int getMax()
   {
       int max = 0;
       for (int i = 0; i < bars.size(); i++)
           if (bars.get(i).value > max)
               max = bars.get(i).value;
       return max;
   }
      
   public static Integer[] getContrastColor(Integer[] color) 
   {
        double y = (299 * color[0] + 587 * color[1] + 114 * color[2]) / 1000;
        return y >= 128 ? new Integer[] {0,0,0} : new Integer[] {255, 255, 255};
   }
   
   //remove the bar at position i
   public void remove(int i){
        bars.remove(i);
   }
}

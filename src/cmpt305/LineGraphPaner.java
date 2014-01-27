/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ian
 */
public class LineGraphPaner implements Paner {
        
    List<Lines> lines;
    String[] scale;
    
    private int paneSpot = 0;
    private int paneLimit = 10;
    public int maxPane = -1;
    
    public LineGraphPaner(List<String> scale, int paneLimit){    
          lines = new ArrayList<Lines>();
          this.scale = scale.toArray(new String[scale.size()]);
          maxPane = this.scale.length;
          this.paneLimit = paneLimit;
    }
    
    @Override
    public void addToPaner(String name, Map<String, Integer> subCategoryValues)
    {
        Lines temp = new Lines(name);
                
        List<String> order = new ArrayList<String>();
        for(String s : subCategoryValues.keySet())
            order.add(s);
        Collections.sort(order);
        
        for (String s : order)
            temp.AddLineValue(subCategoryValues.get(s));        
        
        lines.add(temp);
    }
    
    @Override
    public List<String> GetPaneScale()
    {
        List<String> subScale = new ArrayList<String>();
        int limiter = maxPane >= paneSpot + paneLimit ? paneSpot + paneLimit : maxPane;
        for (int i = paneSpot; i < limiter; i++)
            subScale.add(scale[i]);
        
        return subScale;
    }
    
    @Override
    public Map<String, Map<String, Integer>> GetPaneValues()
    {
       Map<String, Map<String, Integer>> tempValues = new HashMap<String,Map<String, Integer>>(); // the string var is a placement order
       
       for (int i = 0; i < lines.size(); i++)
       {
            Map<String, Integer> value = new HashMap<String, Integer>();
            int limiter = lines.get(i).getLength()  >= paneSpot + paneLimit ? 
                          paneSpot + paneLimit : 
                          lines.get(i).getLength();

            for (Integer j = paneSpot; j < limiter; j++)
                 value.put(j.toString(), lines.get(i).getLineValue(j));
            
            tempValues.put(lines.get(i).getLineTitle(), value);
       }
       return tempValues;
    }
          
    @Override
    public void PaneLimitLeft()
    {  
        if (paneSpot - paneLimit < 0)
        {
            paneSpot = maxPane - (maxPane % paneLimit);
            if (paneSpot == maxPane - 1)
                paneSpot--;
        }
        else
            paneSpot -= paneLimit;  
    }
    
    @Override
    public void PaneLimitRight()
    {
        if (paneSpot + paneLimit >= maxPane)
            paneSpot = 0;
        else
        {
            paneSpot += paneLimit;
        
            if (paneSpot == maxPane - 1)
                paneSpot--;     
        }
    }


    @Override
    public List<GraphDrawer.SelectedValues> getPointsSelected() {
        java.util.List<GraphDrawer.SelectedValues> currentSelected = 
                new java.util.ArrayList<GraphDrawer.SelectedValues>();
        
        int limit = paneSpot + paneLimit <= maxPane ? paneSpot + paneLimit : maxPane;
        
        int lin = 0;
        for (Lines line : lines)
        {
            for (int i = paneSpot; i < limit; i++) 
            {
                if (line.getLineSelected(i)) 
                {
                     currentSelected.add(new GraphDrawer.SelectedValues(line.getLineTitle(), scale[i],
                             line.getLineValue(i), new Point(lin, i - paneSpot)));
                }      
            }
            lin++;
        }
        return currentSelected;
    }

    @Override
    public void SetPointsAsSelected(List<GraphDrawer.SelectedValues> values) {
        for (GraphDrawer.SelectedValues s : values) 
            lines.get(s.referenceLoc.x).SetSelected(s.referenceLoc.y + paneSpot, true);
    }

    @Override
    public boolean CheckPanable() {
        return maxPane <= paneLimit;
    }
}

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
import java.util.Set;

/**
 *
 * @author Ian
 */
public class BarGraphPaner implements Paner {
    
    List<Bars> bars;
    
    private int paneSpot = 0;
    private int paneLimit = 5;
    public int maxPane = -1;
    
    public BarGraphPaner(int paneLimit){    
        bars = new ArrayList<Bars>();
        this.paneLimit = paneLimit;
    }

    @Override
    public void PaneLimitLeft()
    {  
        if (paneSpot - paneLimit < 0)
            paneSpot = maxPane - (maxPane % paneLimit);
        else
            paneSpot -= paneLimit;
    }
    
    @Override
    public void PaneLimitRight()
    {
        if (paneSpot + paneLimit >= maxPane)
            paneSpot = 0;
        else
            paneSpot += paneLimit;
    }

    @Override
    public List<String> GetPaneScale() {
        System.out.println("If you expected anything but null, you are going to have a bad time");
        return null;
    }

    @Override
    public Map<String, Map<String, Integer>> GetPaneValues() {
        Map<String, Map<String, Integer>> tempValues = new HashMap<String,Map<String, Integer>>(); // the string var is a placement order
        
        for (int i = paneSpot; i < paneLimit + paneSpot; i++)
        {
            if (i < maxPane)
            {
                Map<String, Integer> temp = new HashMap<String, Integer>();
                for (int j = 0; j < bars.get(i).getLength(); j++)
                    temp.put(bars.get(i).getBarCategory(j), bars.get(i).getBarValue(j));         
                
                tempValues.put(bars.get(i).getBarTitle(), temp);
            }     
        }
        return tempValues;
    }

    @Override
    public void addToPaner(String name, Map<String, Integer> subCategoryValues) {
       Set<String> temp = subCategoryValues.keySet();
       Bars barCluster = new Bars(name);    
           
       for(String key : temp)
           barCluster.AddBar(key, subCategoryValues.get(key));
       
       bars.add(barCluster);
       
       maxPane = bars.size();
       Collections.sort(bars);
    }
    
    @Override
     public List<GraphDrawer.SelectedValues> getPointsSelected() {
          int count = 0;
          List<GraphDrawer.SelectedValues> sv = new ArrayList<GraphDrawer.SelectedValues>();
          int limit = paneSpot + paneLimit > maxPane ? maxPane : paneSpot + paneLimit;
          for (int j = paneSpot; j < limit; j++) {
               for (int i = 0; i < bars.get(j).getLength(); i++) {
                    if (bars.get(j).getBarSelected(i)) {
                         sv.add(new GraphDrawer.SelectedValues(bars.get(j).getBarTitle(), bars.get(j).getBarCategory(i),
                                 bars.get(j).getBarValue(i), new Point(count, i)));
                    }
               }
               count++;
          }
          return sv;
     }
    
     @Override
     public void SetPointsAsSelected(List<GraphDrawer.SelectedValues> values) {
          for (GraphDrawer.SelectedValues s : values) {
               bars.get(s.referenceLoc.x + paneSpot).SetSelected(s.referenceLoc.y, true);
          }
     }
     
    @Override
    public boolean CheckPanable() {
        return maxPane <= paneLimit;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

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
public class GraphPaner {
    
    public class PaneObject
    {
        String title;
        List<String> content;
        
        public PaneObject(String title, List<String> content)
        {
            this.title = title;
            this.content = content;
        }
    }
    
    private List<String> scaleValues;
    private List<PaneObject> allContent;
    private boolean scaleSeperate = false;  // bargraph default
    
    private int paneSpot = 0;
    private int paneLimit = 5;
    
    public GraphPaner(Map<String, java.util.List<String>> inputContent, boolean scaleSeperate, int paneLimit){    
        this.scaleSeperate = scaleSeperate;
        this.paneLimit = paneLimit;
        
        scaleValues = new ArrayList<String>();
        Set<String> ss = inputContent.keySet();
        for (String s: ss)
            scaleValues.add(s);
        
        Collections.sort(scaleValues);
        
        for (int i = 0; i < ss.size(); i++)
        {
            if (scaleValues.size() > i)
                this.allContent.add(new PaneObject(String.format("%s", scaleSeperate ? i : scaleValues.get(i)), 
                        inputContent.get(scaleValues.get(i))));
        }
    }
    
    public List<String> GetPaneScale()
    {
        List<String> subScale = new ArrayList<String>();
        int limiter = allContent.size() >= paneSpot + paneLimit ? paneSpot + paneLimit : allContent.size();
        for (int i = paneSpot; i < limiter; i++)
            subScale.add(scaleValues.get(i));
        
        return subScale;
    }

    public Map<String, Integer> GetPaneValues()
    {
        Map<String, Integer> tempValues = new HashMap<String,Integer>(); // the string var is a placement order
        
        int limiter = allContent.size() >= paneSpot + paneLimit ? paneSpot + paneLimit : allContent.size();
        for (int i = paneSpot; i < limiter; i++)
             tempValues.put(allContent.get(i).title, allContent.get(i).content.size());
        
        return tempValues;
    }
    
    public void PaneLimitLeft()
    {
        paneSpot = (allContent.size() - paneLimit) % allContent.size();
    }
    
    public void PaneLimitRight()
    {
        paneSpot = (allContent.size() + paneLimit) % allContent.size();
    }
}

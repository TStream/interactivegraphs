/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class GameSCompare extends PopupSuperClass{
    GraphController applet;
    GraphDrawer graph;
    Graph backing;
    
    public GameSCompare (){
        initWindow();
    }
    
    @Override 
    public void initWindow(){
    
   
        // toggling either of these flips between the two graph styles
        //LineGraphSetupExample();
        BarGraphSetupExample();

        applet = new GraphController(new Dimension(this.getWidth() ,420), "things and stuff",
                "thing on x", "thing on y", graph);           
        applet.SetOverrideColorScheme(Color.BLACK);

        applet.init();
        cPanel.add(applet);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
    }
    private void BarGraphSetupExample()
    {
        graph = new BarGraph();        // initialize bar graph
        
        Map<String,Integer> tempValues = new HashMap<String,Integer>();
        tempValues.put("right", 100);
        tempValues.put("wrong", 200);
        tempValues.put("saucy", 400);
        tempValues.put("nogo", 300);
        
        graph.AddTypeValue("bar 1", tempValues);        // add bar cluster value
        
        tempValues = new HashMap<String,Integer>();
        tempValues.put("right", 300);
        tempValues.put("wrong", 200);
        tempValues.put("saucy", 400);
        
        graph.AddTypeValue("bar 2", tempValues);        // add bar cluster value
        
        tempValues = new HashMap<String,Integer>();
        tempValues.put("right", 400);
        tempValues.put("wrong", 100);
        tempValues.put("saucy", 400);
        
        graph.AddTypeValue("bar 3", tempValues);        // add bar cluster value
    }
    
    private void LineGraphSetupExample()
    {
        // setup scale values for line graph
        java.util.List<String> scaleValues = new java.util.ArrayList<String>();
        scaleValues.add("october 3, 2013");
        scaleValues.add("october 6, 2013");
        scaleValues.add("october 23, 2013");
        scaleValues.add("november 4, 2013");
        
        graph = new LineGraph(scaleValues, Color.RED, Color.BLUE, Color.GREEN);  // initialize line graph
        
        Map<String,Integer> tempValues = new HashMap<String,Integer>(); // the string var is a placement order
        tempValues.put("1", 100);
        tempValues.put("2", 250);   
        tempValues.put("3", 400);  
        tempValues.put("4", 50); 

        graph.AddTypeValue("line 1", tempValues);       // add line to graph
        
        tempValues = new HashMap<String,Integer>();
        tempValues.put("1", 300);
        tempValues.put("2", 200);
        tempValues.put("3", 100);   
        tempValues.put("4", 300);      
        
        graph.AddTypeValue("line 2", tempValues);       // add line to graph
        
        tempValues = new HashMap<String,Integer>();
        tempValues.put("1", 100);
        tempValues.put("2", 500);
        tempValues.put("3", 700);   
        tempValues.put("4", 200);      
        
        graph.AddTypeValue("line 3", tempValues);       // add line to graph
    }

    @Override
    public void onActionPerformed(ActionEvent evt){
         
     }
}

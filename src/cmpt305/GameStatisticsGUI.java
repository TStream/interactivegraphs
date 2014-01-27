/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import Chart.Chart;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import query.MDBConnect;
import query.Query;
import legend.Legend;

/**
 *
 * @author Nico
 */
public class GameStatisticsGUI extends GUISuperClass {

     ButtonGroup filterGroup;
     GraphController applet;
     GraphDrawer graph;
     RawDataTable rawData;
     GameSCompare compare;
     JRadioButton ascendingSort;
     JRadioButton descendingSort;
     JRadioButton defaultSort;
     JRadioButton filter1;
     JRadioButton filter2;
     JRadioButton filter3;
     DefaultTableModel t;
     Paner panner;
     
     String xAxisString, yAxisString;
     String graphTitle;
     
     boolean isBarGraph;
     boolean graphInitialized;
     
     JComboBox chooser;
     JComboBox filters;
     
     private int NUM_BARS; //number of bars to display for the bar graph

     public GameStatisticsGUI() {

          filterGroup = new ButtonGroup();
          
          xAxisString = "X Axis";
          yAxisString = "Y Axis";
          graphTitle = "Game Statistics";
          
          isBarGraph = true;
          NUM_BARS = 10;
          graphInitialized = false;

          initWindow();
     }

     @Override
     public void initWindow() {
          this.setTitle("Game Statistics Visualizer");
          
          panLeftButton.setEnabled(false);
          panRightButton.setEnabled(false);
          rawDataButton.setVisible(false);

          //create the question chooser group
        JLabel chooserLabel = new JLabel();
        chooserLabel.setText("Select A Category:");
        String[] chooserValues = { "View Data...", "User Performance", "Difficult Questions", "Answer Times"};
        chooser = new JComboBox(chooserValues);
        chooser.setSelectedIndex(0);
        chooser.addActionListener(this);
        chooser.setActionCommand("Chooser");
        //end question chooser group
        
         JLabel filterLabel1 = new JLabel();
        filterLabel1.setText("Filter by:");
        String[] filterValues = { "N/A" };
        filters = new JComboBox(filterValues);
        filters.setPreferredSize(new Dimension(135,25));
        //filters.setSelectedIndex(0);
        filters.addActionListener(this);
        filters.setActionCommand("Filter");

          GroupLayout fLayout1 = new GroupLayout(filterPanel);
          filterPanel.setLayout(fLayout1);

          fLayout1.setAutoCreateGaps(false);
          fLayout1.setAutoCreateContainerGaps(false);

          fLayout1.setHorizontalGroup(
          fLayout1.createParallelGroup()
             .addGroup(fLayout1.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(chooserLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(chooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
             .addGroup(fLayout1.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(filterLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(filters, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        fLayout1.setVerticalGroup(
           fLayout1.createSequentialGroup()
              .addGroup(fLayout1.createSequentialGroup()
                 .addComponent(chooserLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                 .addComponent(chooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addGroup(fLayout1.createSequentialGroup()
                .addComponent(filterLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(filters, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

          filterPanel.revalidate();

          this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
     }

     @Override
     public void onActionPerformed(ActionEvent evt) {

          if ("Raw Data".equals(evt.getActionCommand())) {
               rawData = new RawDataTable(t);
               rawData.setVisible(true);
               rawData.setTitle("Raw Data");
          } else if ("Compare".equals(evt.getActionCommand())) {
                UserDCompareGUI view = new UserDCompareGUI();
                CompareCntl ctrl = new CompareCntl(view, applet, graph);

          }else if ("Pan Left".equals(evt.getActionCommand()))
          {
              if(!graphInitialized) return;
              panner.PaneLimitLeft();
              refreshGraph();
              
          }else if( "Pan Right".equals(evt.getActionCommand()))
          {
              if(!graphInitialized) return;
              panner.PaneLimitRight();
              refreshGraph();
              
          }else if("Filter".equals(evt.getActionCommand()))
          {
              JComboBox cb = (JComboBox)evt.getSource();
               String selected = (String)cb.getSelectedItem(); //which item was chosen
               if(selected == null) return;
               
               if(selected.equals("Score"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                       queryUserPerformance("Score");
                   }
               }else if(selected.equals("Collisions"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                       queryUserPerformance("Collisions");
                   }
               }else if(selected.equals("Select A Filter.."))
               {
                   if(graphInitialized == true) mainPanel.remove(applet);
                   graphInitialized = false;
                   
                   graphTitle = "";
                    yAxisString = "";
                    xAxisString = "";
                    panLeftButton.setEnabled(false);
                    panRightButton.setEnabled(false);
               }else if(selected.equals("Gender: Male"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                       queryUserPerformance("Male");
                   }
               }else if(selected.equals("Gender: Female"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                       queryUserPerformance("Female");
                   }
               }else if(selected.equals("Gender: Unavailable"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                       queryUserPerformance("Unavailable");
                   }
               }else if(selected.equals("Gender: Male vs Female"))
               {
                   if(chooser.getSelectedItem() == "User Performance")
                   {
                      
                       queryUserPerformance("Male vs Female");
                   }
               
               }
                
          }else if("Chooser".equals(evt.getActionCommand()))
           {
               JComboBox cb = (JComboBox)evt.getSource();
               String selected = (String)cb.getSelectedItem(); //which item was chosen
               
               if(selected == null) return;
               
               if(selected.equals("User Performance"))
               {
                   filters.removeActionListener(this);
                   filters.removeAllItems();
                   filters.addItem("Select A Filter..");
                   filters.addItem("Score");
                   filters.addItem("Collisions");
                   filters.addItem("Gender: Male");
                   filters.addItem("Gender: Female");
                   filters.addItem("Gender: Unavailable");
                   filters.addItem("Gender: Male vs Female");
                   filters.addActionListener(this);
                   
                   panLeftButton.setEnabled(false);
                   panRightButton.setEnabled(false);
                   
               }else if(selected.equals("Difficult Questions"))
               {
                   
                   filters.removeActionListener(this);
                   filters.removeAllItems();
                   filters.addItem("N/A");
                   filters.addActionListener(this);
                   
                   Query Qer = new Query();
                    String title = "", xAxis = "", yAxis = "";
                    panner = new BarGraphPaner(NUM_BARS);
                    int count = 0;

                  java.util.List<java.util.List<Object>> results = Qer.difficultCategories(null);
                  java.util.List<java.util.List<Object>> results2 = Qer.trafficKnowledge(null);

                  for(java.util.List curList : results)
                  {
                      if(curList.size() != 4) continue;
                      if(curList.get(0) == null || curList.get(1) == null || curList.get(2) == null || curList.get(3) == null) continue;

                      Double qID = new Double(curList.get(0).toString());
                      Double col1 = new Double(curList.get(1).toString());
                      Double col2 = new Double(curList.get(2).toString());
                      Double col3 = new Double(curList.get(3).toString());
                      Double right = 0.0; //number of questions answered right on the first try

                      for(java.util.List r2List : results2)
                      {
                          if(r2List.size() != 2) continue;
                          Double id2 = new Double(r2List.get(0).toString());
                          if( id2.intValue() == qID)
                              right = new Double(r2List.get(1).toString());
                      }
                      if(right == 0.0) continue;

                      Double perc = (((col1+col2+col3)/((col1+col2+col3)+right)) * 100.0);
                      //if(perc < 50) continue;

                      String question = Qer.getQuestion(qID.intValue());       
                      Map<String, Integer> graphValues = new HashMap<String, Integer>();
                      graphValues.put("Player ID", perc.intValue()); 
                      panner.addToPaner(String.format("%s. %s", count, question), graphValues);
                      
                      count++;
                  }
                  
                java.util.List<Color> colors = new ArrayList<Color>();
                colors.add(new Color(60, 110, 126)); colors.add(new Color(25, 74, 90)); colors.add(new Color(10, 92, 118));
                graph = new BarGraph(new Color(64, 133, 156), new Color(98, 172, 196), colors);
                for (String s : panner.GetPaneValues().keySet())
                    graph.AddTypeValue(s, panner.GetPaneValues().get(s));

                graphTitle = "Question Difficulty";
                yAxisString = "Times Answered Incorrectly (%)";
                xAxisString = "Player ID";
                
                ResetToBarGraph();
                
                resetLegend(legendPanel);
                legendPanel.setGraph(applet);
                
                graphInitialized = true;
                
                panLeftButton.setEnabled(true);
                panRightButton.setEnabled(true);
                
               }else if(selected.equals("Answer Times"))
               {
                   
                   filters.removeActionListener(this);
                   filters.removeAllItems();
                   filters.addItem("N/A");
                   filters.addActionListener(this);
                   
                   Query Qer = new Query();
            
                    String title = "", xAxis = "", yAxis = "";

                   Map<String, Integer> result = Qer.questionAnswerTime(null);

                   panner = new BarGraphPaner(10);   

                   for(Map.Entry<String, Integer> entry : result.entrySet())
                   {
                       if(entry.getKey() == null || entry.getValue() == null) continue;
                       Map<String, Integer> temp = new HashMap<String, Integer>();
                       temp.put("Question ID", entry.getValue());
                       panner.addToPaner(entry.getKey(), temp);
                   }

                    java.util.List<Color> colors = new ArrayList<Color>();
                    colors.add(new Color(60, 110, 126));// colors.add(new Color(25, 74, 90)); colors.add(new Color(10, 92, 118));
                    graph = new BarGraph(new Color(64, 133, 156), new Color(98, 172, 196), colors);
                    for (String s : panner.GetPaneValues().keySet())
                        graph.AddTypeValue(s, panner.GetPaneValues().get(s));

                    graphTitle = "Question Difficulty: Answer Times";
                    yAxisString = "Average Answer Time (ms)";
                    xAxisString = "Question ID";
                    
                   ResetToBarGraph();
                   
                   resetLegend(legendPanel);
                    legendPanel.setGraph(applet);
                    
                    graphInitialized = true;
                    
                    panLeftButton.setEnabled(true);
                    panRightButton.setEnabled(true);
                   
               }else if(selected.equals("View Data..."))
               {
                   filters.removeActionListener(this);
                   filters.removeAllItems();
                   filters.addItem("N/A");
                   filters.addActionListener(this);
                   
                   resetLegend(legendPanel);
                   
                   if(graphInitialized == true) mainPanel.remove(applet);
                   graphInitialized = false;
                   
                   graphTitle = "";
                    yAxisString = "";
                    xAxisString = "";
                    panLeftButton.setEnabled(false);
                    panRightButton.setEnabled(false);
                   
               }
           }
         }
     
         private void queryUserPerformance(String theCurrentFilter)
         {
             Query Qer = new Query();
            java.util.List<java.util.List<Object>> result = Qer.userPerformance(null);

            panner = new BarGraphPaner(NUM_BARS/2);       
            java.util.List<Color> colors = new ArrayList<Color>();
            colors.add(new Color(10, 60, 80)); colors.add(new Color(20, 116, 140)); colors.add(new Color(30, 60, 200));
            colors.add(new Color(25, 40, 184)); colors.add(new Color(70, 87, 254));
            graph = new BarGraph(new Color(164, 230, 248), new Color(160, 59, 254), colors);
            
            String title = "", xAxis = "", yAxis = "";
            int maleScore = 0;
            int femaleScore = 0;
            int unavailableScore = 0;
            
            for(java.util.List curList : result)
            {

                 Double pID = new Double(curList.get(0).toString());
                 String startTime = curList.get(1).toString();
                 String finishTime = curList.get(2).toString();
                 Double averageScore = new Double(curList.get(3).toString());
                 Double lifetimeScore = new Double(curList.get(4).toString());
                 Double highScore = new Double(curList.get(5).toString());
                 Double averageCollisions = new Double(curList.get(6).toString());
                 Double bestStreak = new Double(curList.get(7).toString()); //highest longest streak
                 String gender = ""; //can be null
                 if(curList.get(8) == null)
                     gender = "Unavailable";
                 else
                     gender = curList.get(8).toString();
                 
                 if(theCurrentFilter.equals("Score"))
                 {
                    Map<String, Integer> graphValues = new HashMap<String, Integer>();
                    graphValues.put("Average", averageScore.intValue()); 
                    graphValues.put("Lifetime", lifetimeScore.intValue()); 
                    graphValues.put("Highest", highScore.intValue()); 
                    panner.addToPaner(""+pID.intValue(), graphValues);
                    graphTitle = "Player Performance: Score";
                    yAxisString = "Score";
                    xAxisString = "Player ID";
                    
                 }else if(theCurrentFilter.equals("Collisions"))
                 {
                    Map<String, Integer> graphValues = new HashMap<String, Integer>();
                    graphValues.put("Average #", averageCollisions.intValue()); 
                    graphValues.put("Best Streak", bestStreak.intValue()); 
                    panner.addToPaner(""+pID.intValue(), graphValues);
                    graphTitle = "Player Performance: Accuracy";
                    yAxisString = "Score";
                    xAxisString = "Player ID";
                 }else if(theCurrentFilter.equals("Male"))
                 {
                     if(gender.equals("male"))
                     {
                         Map<String, Integer> graphValues = new HashMap<String, Integer>();
                        graphValues.put("Average", averageScore.intValue()); 
                        graphValues.put("Lifetime", lifetimeScore.intValue()); 
                        graphValues.put("Highest", highScore.intValue()); 
                        panner.addToPaner(""+pID.intValue(), graphValues);
                        graphTitle = "Player Performance By Gender: Male";
                        yAxisString = "Result";
                        xAxisString = "Player ID";
                     }
                 }else if(theCurrentFilter.equals("Female"))
                 {
                     if(gender.equals("female"))
                     {
                         Map<String, Integer> graphValues = new HashMap<String, Integer>();
                        graphValues.put("Average", averageScore.intValue()); 
                        graphValues.put("Lifetime", lifetimeScore.intValue()); 
                        graphValues.put("Highest", highScore.intValue()); 
                        panner.addToPaner(""+pID.intValue(), graphValues);
                        graphTitle = "Player Performance By Gender: Female";
                        yAxisString = "Result";
                        xAxisString = "Player ID";
                     }
                 }else if(theCurrentFilter.equals("Unavailable"))
                 {
                     if(gender.equals("Unavailable"))
                     {
                        Map<String, Integer> graphValues = new HashMap<String, Integer>();
                        graphValues.put("Average", averageScore.intValue()); 
                        graphValues.put("Lifetime", lifetimeScore.intValue()); 
                        graphValues.put("Highest", highScore.intValue()); 
                        panner.addToPaner(""+pID.intValue(), graphValues);
                        graphTitle = "Player Performance By Gender: Unavailable";
                        yAxisString = "Result";
                        xAxisString = "Player ID";
                     }
                 }else if(theCurrentFilter.equals("Male vs Female"))
                 {
                     if(gender.equals("male"))
                     {
                        maleScore += averageScore;
                     }else if(gender.equals("female"))
                     {
                         femaleScore += averageScore;
                     }else
                         unavailableScore += averageScore;
                 }
            }
            
            if(theCurrentFilter.equals("Male vs Female"))
            {
               
                   Map<String, Integer> graphValues = new HashMap<String, Integer>();
                   graphValues.put("Male", maleScore); 
                   graphValues.put("Female", femaleScore); 
                   graphValues.put("Unavailable", unavailableScore);
                   panner.addToPaner("Gender", graphValues);
                   graphTitle = "Player Performance By Gender: Aggregate";
                   yAxisString = "Average Score";
                   xAxisString = "Gender";
             }

            for (String s : panner.GetPaneValues().keySet())
                     graph.AddTypeValue(s, panner.GetPaneValues().get(s));
            
            ResetToBarGraph();
             
            resetLegend(legendPanel);
             legendPanel.setGraph(applet);
             
             panLeftButton.setEnabled(true);
             panRightButton.setEnabled(true);
         }
         
         private void resetLegend(Legend legend)
         {
             int width = legendPanel.getSize().width;
             int height = legendPanel.getSize().height;
             infoPanel.remove(legend);
             
             GroupLayout infoLayout = new GroupLayout(infoPanel);
            infoPanel.setLayout(infoLayout);
            legendPanel = new Legend();
            infoPanel.add(legendPanel);
            legendPanel.setPreferredSize(new Dimension (width,height));

            infoLayout.setHorizontalGroup(
              infoLayout.createSequentialGroup()
                 .addComponent(legendPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            );
           infoLayout.setVerticalGroup(
              infoLayout.createParallelGroup()
                 .addComponent(legendPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
           );
         }
         
         private void resetGraph()
         {
            java.util.List<Color> colors = new ArrayList<Color>();
            colors.add(new Color(10, 60, 80)); colors.add(new Color(20, 116, 140)); colors.add(new Color(30, 60, 200));
            graph = new BarGraph(new Color(164, 230, 248), new Color(160, 59, 254), colors);
         }
     
         private void refreshGraph()
         {
             resetGraph();
            for (String s : panner.GetPaneValues().keySet())
                graph.AddTypeValue(s, panner.GetPaneValues().get(s));

             ResetToBarGraph();
         }
          
          private void ResetToBarGraph()
            {
                
                if(graphInitialized) mainPanel.remove(applet);

                applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getSize().height), graphTitle,
                xAxisString, yAxisString, graph);
                applet.SetOverrideColorScheme(Color.white);

                applet.init();
                mainPanel.add(applet);
                
                graphInitialized = true;
                
            }
}

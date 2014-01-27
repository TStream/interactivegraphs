package cmpt305;

import Chart.Chart;
import Chart.Pie;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;
import javax.swing.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import query.Query;


/**
 *
 * @author Nico
 */
public class UserDemographicsGUI extends GUISuperClass{
    
     Map<String, java.util.List<String>> filterMap = new HashMap<String,java.util.List<String>>();
          java.util.List<String> filterBy = new ArrayList<String>();
    
    GraphController applet;
    GraphDrawer graph;
    RawDataTable rawData;
    UserDCompareGUI compare;
    DefaultTableModel t;
    
    Chart chart;
    String currentQuery;
    
    JRadioButton ascendingSort;
    JRadioButton descendingSort;
    JRadioButton defaultSort;
    JRadioButton filter1;
    JRadioButton filter2;
    JRadioButton filter3;
    
    JComboBox chooser;
    JComboBox filters;
    
    Paner paner;
    
    public UserDemographicsGUI()
    {
        initWindow();
    }
    
    @Override
    public void initWindow()
    {
        this.setTitle("User Demographics Visualizer");
        
        //create the question chooser group
        JLabel chooserLabel = new JLabel();
        chooserLabel.setText("Select A Category:");
        String[] chooserValues = { "View Data...", "Total Users", "Play Frequency", "Demographics", "Correlations", "Player Usage" };
        chooser = new JComboBox(chooserValues);
        chooser.setPreferredSize(new Dimension(135,25));
        chooser.setSelectedIndex(0);
        chooser.addActionListener(this);
        chooser.setActionCommand("Chooser");
        //end question chooser group
        //Create the "sort order" group of radial buttons
        /*JLabel sortLabel = new JLabel();
        sortLabel.setText("Sort Order:");
        String[] sortOptions = { "View Data...", "Total Users", "Play Frequency", "Demographics", "Correlations" };
        chooser = new JComboBox(sortOptions);
        chooser.setSelectedIndex(0);
        chooser.addActionListener(this);
        chooser.setActionCommand("Chooser");*/
         JLabel filterLabel1 = new JLabel();
        filterLabel1.setText("Filter by:");
        String[] filterValues = {" "};
        filters = new JComboBox(filterValues);
        filters.setPreferredSize(new Dimension(135,25));
        filters.setSelectedIndex(0);
        filters.addActionListener(this);
        filters.setActionCommand("Filter");
         
        //end general filters group
        
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
        
        TotalPlayerLoad(null);  //sets applet as well

        //applet.init();
        //mainPanel.add(applet);
        //legendPanel.setGraph(applet);
        
        //chart = new Chart("Example Chart", new Dimension(200, 500));
        //chartSetUp();
        //chart.init();
        //infoPanel.add(chart);

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
    }
    
    //This is currently an example. It should pull needed queries
    
    
    
    @Override
    public void onActionPerformed(ActionEvent evt)
    {
        if("Raw Data".equals(evt.getActionCommand()))
        { 
            rawData = new RawDataTable(t);
            rawData.setVisible(true);
            rawData.setTitle("Raw Data");
            
        }
        else if ("Compare".equals(evt.getActionCommand())){
            if (currentQuery.equals("TotalPlayer"))
            {
                UserDCompareGUI view = new UserDCompareGUI();
                Chart cntl = TotalPlayerSubLoad();
                
                view.cPanel.add(cntl);
                cntl.init();
                view.setVisible(true);
            }
            else if (currentQuery.equals("PlayerStat"))
            {
                PlayerStatisticSubLoad();     
            }
            else if (currentQuery.equals("Correlation"))
            {
                UserDCompareGUI view = new UserDCompareGUI();
                CompareCntl ctrl = new CompareCntl(view, applet, graph);
            }
        }
        else if ("Filter".equals(evt.getActionCommand())){
            JComboBox cb = (JComboBox)evt.getSource();
            String selected = (String)cb.getSelectedItem(); //which item was chosen
            
            if (selected == null) return;
            
            currentQuery = "Correlation";
            if (chooser.getSelectedItem().equals("Demographics")){
                if ("Everyone".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    PlayerDemographicsLoad(null);
                }
                 if ("Females".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("female");
                    filterMap.put("Gender", filterBy);
                    PlayerDemographicsLoad(filterMap);
                }
                  if ("Males".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("male");
                    filterMap.put("Gender", filterBy);
                   PlayerDemographicsLoad(filterMap);
                }
                   if ("Unavailable".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("unavailable");
                    filterBy.add("null");
                    filterMap.put("Gender", filterBy);
                    PlayerDemographicsLoad(filterMap);
                }
            }
            if (chooser.getSelectedItem().equals("Play Frequency")){
                if ("Everyone".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    PlayerFrequencyLoad(null); 
                    currentQuery = "PlayerStat";
                }
                 if ("Females".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("female");
                    filterMap.put("Gender", filterBy);
                     PlayerFrequencyLoad(filterMap);
                }
                  if ("Males".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("male");
                    filterMap.put("Gender", filterBy);
                    PlayerFrequencyLoad(filterMap);
                }
                   if ("Unavailable".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("unavailable");
                    filterBy.add("null");
                    filterMap.put("Gender", filterBy);
                    PlayerFrequencyLoad(filterMap);
                }
            }
            if (chooser.getSelectedItem().equals("Total Users")){
                if ("Everyone".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    TotalPlayerLoad(null); 
                    currentQuery = "TotalPlayer";
                }
                 if ("Females".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("female");
                    filterMap.put("Gender", filterBy);
                     TotalPlayerLoad(filterMap);
                }
                  if ("Males".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("male");
                    filterMap.put("Gender", filterBy);
                    TotalPlayerLoad(filterMap);
                }
                   if ("Unavailable".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("unavailable");
                    filterBy.add("null");
                    filterMap.put("Gender", filterBy);
                    TotalPlayerLoad(filterMap);
                }
            }
            
            if (chooser.getSelectedItem().equals("Correlations")){
                if ("Everyone".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    CorrelationLoad(null); 
                }
                 if ("Females".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("female");
                    filterMap.put("Gender", filterBy);
                    CorrelationLoad(filterMap);
                }
                  if ("Males".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("male");
                    filterMap.put("Gender", filterBy);
                    CorrelationLoad(filterMap);
                }
                   if ("Unavailable".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("unavailable");
                    filterBy.add("null");
                    filterMap.put("Gender", filterBy);
                    CorrelationLoad(filterMap);
                }
            }   
            
            if (chooser.getSelectedItem().equals("Player Usage")){
                if ("Everyone".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    PlayerUsageOverTimeLoad(null); 
                }
                 if ("Females".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("female");
                    filterMap.put("Gender", filterBy);
                    PlayerUsageOverTimeLoad(filterMap);
                }
                  if ("Males".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("male");
                    filterMap.put("Gender", filterBy);
                    PlayerUsageOverTimeLoad(filterMap);
                }
                   if ("Unavailable".equals(selected)){
                    filterBy.clear();
                    filterMap.clear();
                    filterBy.add("unavailable");
                    filterBy.add("null");
                    filterMap.put("Gender", filterBy);
                    PlayerUsageOverTimeLoad(filterMap);
                }
            }
        }
        else if("Chooser".equals(evt.getActionCommand()))
        {
            JComboBox cb = (JComboBox)evt.getSource();
            String selected = (String)cb.getSelectedItem(); //which item was chosen
            if("Total Users".equals(selected)){
                filters.removeAllItems();
                filters.addItem("Everyone");
                filters.addItem("Females");
                filters.addItem("Males");
                filters.addItem("Unavailable");
                TotalPlayerLoad(null);   
            }
            else if("Play Frequency".equals(selected)){
                filters.removeAllItems();
                filters.addItem("Everyone");
                filters.addItem("Females");
                filters.addItem("Males");
                filters.addItem("Unavailable");
                PlayerFrequencyLoad(null);      
            }
            else if("Demographics".equals(selected)){
                filters.removeAllItems();
                filters.addItem("Everyone");
                filters.addItem("Females");
                filters.addItem("Males");
                filters.addItem("Unavailable");
                PlayerDemographicsLoad(null);
            }   
            else if("Correlations".equals(selected))
            {              
                filters.removeAllItems();
                filters.addItem("Everyone");
                filters.addItem("Females");
                filters.addItem("Males");
                filters.addItem("Unavailable");
                CorrelationLoad(null);
            }
            
            else if("Player Usage".equals(selected)){
                filters.removeAllItems();
                filters.addItem("Everyone");
                filters.addItem("Females");
                filters.addItem("Males");
                filters.addItem("Unavailable");
                PlayerUsageOverTimeLoad(null);
            }
        }
        
       else if ("Pan Left".equals(evt.getActionCommand()))
       {
            paner.SetPointsAsSelected(graph.getPointsSelected());
            paner.PaneLimitLeft();
            ChangePaner();
       }
        
       else if ("Pan Right".equals(evt.getActionCommand()))
       {
            paner.SetPointsAsSelected(graph.getPointsSelected());
            paner.PaneLimitRight();
            ChangePaner();
       }        
    }
    
    private void ChangePaner()
    {
        if (paner instanceof BarGraphPaner)
        {            
            java.util.List<Color> unhover = new java.util.ArrayList<Color>();
            unhover.add(Color.GRAY);
            unhover.add(Color.PINK);
            unhover.add(Color.GREEN);
            
            graph = new BarGraph(Color.BLUE, Color.RED, unhover);
            graph.SetBackground(new int[]{255,255,255});

            for (String s : paner.GetPaneValues().keySet())  
                graph.AddTypeValue(s, paner.GetPaneValues().get(s));
            
            graph.SetPointsAsSelected(paner.getPointsSelected());
            
            String[] tempVals = new String[] { applet.graphTitle, applet.graphXTitle, applet.graphYTitle };
            mainPanel.remove(applet);
            
            applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                    tempVals[0], tempVals[1],  tempVals[2], graph);           
            applet.SetOverrideColorScheme(Color.white);
            applet.init();

            mainPanel.add(applet);    
        }

        else if (paner instanceof LineGraphPaner)
        {
            graph = new LineGraph(paner.GetPaneScale());
            for (String s : paner.GetPaneValues().keySet())  
                graph.AddTypeValue(s, paner.GetPaneValues().get(s));
            
            graph.SetPointsAsSelected(paner.getPointsSelected());
            
            String[] tempVals = new String[] { applet.graphTitle, applet.graphXTitle, applet.graphYTitle };
            mainPanel.remove(applet);
            
            applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                    tempVals[0], tempVals[1],  tempVals[2], graph);           
            applet.SetOverrideColorScheme(Color.white);
            applet.init();

            mainPanel.add(applet); 
        } 
    }
    
    private void TotalPlayerLoad(Map<String, java.util.List<String>> filters)
    {
        Query Qer = new Query();
        java.util.List<java.util.List<Object>> result = Qer.totalPlayers(filters);
        paner = new BarGraphPaner(5);
        Map<String, Integer> input = new HashMap<String, Integer>(); 
        int val = Integer.parseInt(result.get(0).get(0).toString());
        input.put("Total", val);
        paner.addToPaner("Total Players", input);
        
        java.util.List<Color> unhover = new java.util.ArrayList<Color>();
        unhover.add(Color.GRAY);
        graph = new BarGraph(Color.BLUE, Color.RED, unhover);
        graph.SetBackground(new int[]{255,255,255});
        
        for (String s : paner.GetPaneValues().keySet())
            graph.AddTypeValue(s, paner.GetPaneValues().get(s));
        
        if (mainPanel.getComponents().length > 0)
            mainPanel.remove(applet);
        String addTitle = filterMap.values().toString();
        addTitle = addTitle.replaceAll("\\(", ""); 
        addTitle = addTitle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\)", "");
        if (addTitle.length() > 1){
            addTitle = "(" + addTitle + ")";
        }
        if (addTitle.contains("null")){
            addTitle = addTitle.replaceAll(", null", "");
        }
        applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                "Total amount of " + addTitle + " players who have played game",
                "", "Player Count", graph);           
        applet.SetOverrideColorScheme(Color.white);
        applet.init();
        
        mainPanel.add(applet);    
        legendPanel.setGraph(applet);
    }
    
    private Chart TotalPlayerSubLoad()
    {
        compare = new UserDCompareGUI();
        Query Qer = new Query();
        java.util.List<java.util.List<Object>> result = Qer.totalPlayersSplit();
        Map<String, Integer> output = new HashMap<String, Integer>();
        
        for (java.util.List<Object> o : result)
        {
            String s = "unavailable";
            if (o.get(0) != null)
                s = o.get(0).toString();
            
            if (!output.containsKey(s))
                output.put(s, 1);
            else
                output.put(s, output.get(s) + 1);
        }
        
        Chart chart = new Chart("Total Players by Gender", new Dimension(700, 400));
        Pie p = new Pie(chart,output, "Total Players by Gender", 150, 100, 200);
        chart.addBar(p);  
        
        compare = new UserDCompareGUI();
        java.util.List<java.util.List<Object>> result2 = Qer.totalPlayersSplitLocale();
        Map<String, Integer> output2 = new HashMap<String, Integer>();
        
        for (java.util.List<Object> o : result2)
        {
            String s = "unavailable";
            if (o.get(0) != null)
                s = o.get(0).toString();
            
            if (!output2.containsKey(s))
                output2.put(s, 1);
            else
                output2.put(s, output2.get(s) + 1);
        }
        
        Pie p2 = new Pie(chart, output2, "Total Players by Gender", 450, 100, 200);
        chart.addBar(p2); 
        
        return chart;
    }
    
    private void PlayerFrequencyLoad(Map<String, java.util.List<String>> filters)
    {
        Query Qer = new Query();
        java.util.List<java.util.List<Object>> input = Qer.playFrequency(filters);
        
        paner = new BarGraphPaner(5);
        for (java.util.List<Object> o : input)
        {
            String s = o.get(0).toString();
            int i = Integer.parseInt(o.get(1).toString());
            Map<String, Integer> temp = new HashMap<String, Integer>();
            temp.put(" ", i);
            paner.addToPaner(s, temp);
        }
        
        java.util.List<Color> unhover = new java.util.ArrayList<Color>();
        unhover.add(Color.GRAY);
        graph = new BarGraph(Color.BLUE, Color.RED, unhover);
        graph.SetBackground(new int[]{255,255,255});
        
        for (String s : paner.GetPaneValues().keySet())
            graph.AddTypeValue(s, paner.GetPaneValues().get(s));
        
        mainPanel.remove(applet);
        String addTitle = filterMap.values().toString();
        addTitle = addTitle.replaceAll("\\(", ""); 
        addTitle = addTitle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\)", "");
        if (addTitle.length() > 1){
            addTitle = "(" + addTitle + ")";
        }
        if (addTitle.contains("null")){
            addTitle = addTitle.replaceAll(", null", "");
        }
        //System.out.println(addTitle);
        applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                "Player's Who Played Game " + addTitle,
                "Player ID", "Player Play Amount", graph);           
        applet.SetOverrideColorScheme(Color.white);
        applet.init();
        
        mainPanel.add(applet); 
        legendPanel.setGraph(applet);
    }
    
    private void PlayerStatisticSubLoad()
    {
        Query Qer = new Query();
        java.util.List<java.util.List<Object>> input = Qer.playStatisitcs();
        
        int max = 0;
        int min = 10000;
        int average = 0;
        for (java.util.List<Object> o : input)
        {
            int i = Integer.parseInt(o.get(0).toString());
            
            if (i > max)
                max = i;
            
            if (i < min)
                min = i;
            
            average += i;
        }
        
        java.util.List<Color> unhover = new java.util.ArrayList<Color>();
        unhover.add(Color.GRAY);
        graph = new BarGraph(Color.BLUE, Color.RED, unhover);
        graph.SetBackground(new int[]{255,255,255});
        
        Map<String, Integer> temp = new HashMap<String, Integer>();
        temp.put(" ", max);
        graph.AddTypeValue("Most Plays",  temp);
        
        temp = new HashMap<String, Integer>();
        temp.put(" ", min);
        graph.AddTypeValue("Least Plays",  temp);
        
        temp = new HashMap<String, Integer>();
        temp.put(" ", average / input.size());
        graph.AddTypeValue("Average Plays",  temp);
        
        UserDCompareGUI newPanel = new UserDCompareGUI();
        GraphController app = new GraphController(new Dimension(newPanel.getWidth(), newPanel.getHeight()), 
                "Player's Who Played Game",
                "Total Plays", "Play Statistic Extremes ", graph);           
        app.SetOverrideColorScheme(Color.white);
        app.init();
        newPanel.cPanel.add(app);
        newPanel.setVisible(true);
    }
    
    private void PlayerDemographicsLoad(Map<String, java.util.List<String>> filters)
    {
        Query Qer = new Query();
        java.util.Map<String, Map<String, Integer>> result = Qer.playerAge(filters);
        
        paner = new BarGraphPaner(5);
        
        for (String s : result.keySet())
            paner.addToPaner(s, result.get(s));
        
        java.util.List<Color> unhover = new java.util.ArrayList<Color>();
        unhover.add(Color.GRAY);
        unhover.add(Color.PINK);
        unhover.add(Color.GREEN);
        
        graph = new BarGraph(Color.BLUE, Color.RED, unhover);
        graph.SetBackground(new int[]{255,255,255});
        
        for (String s : paner.GetPaneValues().keySet())
            graph.AddTypeValue(s, paner.GetPaneValues().get(s));
        
        mainPanel.remove(applet);
        String addTitle = filterMap.values().toString();
        addTitle = addTitle.replaceAll("\\(", ""); 
        addTitle = addTitle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\)", "");
        if (addTitle.length() > 1){
            addTitle = "(" + addTitle + ")";
        }
        if (addTitle.contains("null")){
            addTitle = addTitle.replaceAll(", null", "");
        }
        applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                "Player Demographics " + addTitle,
                "Player Age Groups", "Player Usage", graph);           
        applet.SetOverrideColorScheme(Color.white);
        applet.init();
        
        mainPanel.add(applet); 
        legendPanel.setGraph(applet);   
    }
    
    private void PlayerUsageOverTimeLoad(Map<String, java.util.List<String>> filters)
    {
        Query Qer = new Query();
        java.util.Map<String,java.util.List<String>> result = Qer.datePlayer(filters);
        t = new DefaultTableModel();
        t.addColumn("Date");
        t.addColumn("Players");
 
        java.util.List<customString> temp = new ArrayList<customString>();
        for(String s : result.keySet())
           temp.add(new customString(s));
        
        Collections.sort(temp);
        
        java.util.List<String> scale = new ArrayList<String>();
        for (customString s : temp)
            scale.add(s.toString());
        
        paner = new LineGraphPaner(scale, 10);
        
        Integer i = 1;
        Map<String, Integer> temp3 = new HashMap<String, Integer>();
        for (String s : scale)
        {
            temp3.put(i.toString(), result.get(s).size());
            Object[] Row = {s,result.get(s).size()};
            t.addRow(Row);
            i++;
        }
        paner.addToPaner("Usage", temp3);
        
        graph = new LineGraph(paner.GetPaneScale());
        graph.SetBackground(new int[]{255,255,255});
        

        for (String s : paner.GetPaneValues().keySet())
            graph.AddTypeValue(s, paner.GetPaneValues().get(s));
        
                
        mainPanel.remove(applet);
         String addTitle = filterMap.values().toString();
        addTitle = addTitle.replaceAll("\\(", ""); 
        addTitle = addTitle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\)", "");
        if (addTitle.length() > 1){
            addTitle = "(" + addTitle + ")";
        }
        if (addTitle.contains("null")){
            addTitle = addTitle.replaceAll(", null", "");
        }
        applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                "Player Demographics " + addTitle,
                "Player Age Groups", "Player Usage", graph);           
        applet.SetOverrideColorScheme(Color.white);
        applet.init();
        
        mainPanel.add(applet); 
        legendPanel.setGraph(applet); 
    }
    
    private void CorrelationLoad(Map<String, java.util.List<String>> filters)
    {
        Query Qer = new Query();
        java.util.List<java.util.List<Object>> result = Qer.agkCorrelation(filters);
        
        Map<String, Map<String, Integer>> out = new HashMap<String, Map<String, Integer>>();
        java.util.List<String> scale = new java.util.ArrayList<String>();
        scale.add("Average Score");
        scale.add("Life Score");
        scale.add("High Score");
        scale.add("Collisions");
        
        t = new DefaultTableModel();
        t.addColumn("Car Type");
        t.addColumn("Average Score");
        t.addColumn("Life Score");
        t.addColumn("High Score");
        t.addColumn("Collisions");
        
        for (java.util.List<Object> ls : result)
        {
            String car = ls.get(1).toString();
            
            int averageScore = (int) Double.parseDouble(ls.get(3).toString());
            int lifeScore = (int) Double.parseDouble(ls.get(4).toString());
            int highScore = (int) Double.parseDouble(ls.get(5).toString());
            int collisions = (int) Double.parseDouble(ls.get(6).toString());
            
            Object[] Row = { car,averageScore,lifeScore,highScore,collisions};
            t.addRow(Row);
            
            Map<String,Integer> line;
            if (!out.containsKey(car))
            {
                line = new HashMap<String, Integer>();
                line.put("1", averageScore);
                line.put("2", lifeScore); 
                line.put("3", highScore); 
                line.put("4", collisions);
                line.put("5", 1);
                out.put(car, line);
            }
            else
            {
                line = out.get(car);
                line.put("1", line.get("1") + averageScore);
                line.put("2", line.get("2") + lifeScore); 
                line.put("3", line.get("3") + highScore); 
                line.put("4", line.get("4") + collisions);
                line.put("5", line.get("5") + 1);
                out.put(car, line);
            }                        
        }
        
        for (String s : out.keySet())
        {
            int temp = 0;
            temp = out.get(s).get("1") / out.get(s).get("5");
            out.get(s).put("1", temp);
            temp = out.get(s).get("2") / out.get(s).get("5");
            out.get(s).put("2", temp);
            temp = out.get(s).get("3") / out.get(s).get("5");
            out.get(s).put("3", temp);
            temp = out.get(s).get("4") / out.get(s).get("5");
            out.get(s).put("4", temp);
        }
        
        paner = new LineGraphPaner(scale, 4);
        
        for (String s : out.keySet())
            paner.addToPaner(s, out.get(s));
        
        graph = new LineGraph(paner.GetPaneScale(), Color.RED, Color.BLUE, Color.BLACK);
        graph.SetBackground(new int[]{255,255,255});
        
        for (String s : paner.GetPaneValues().keySet())
            graph.AddTypeValue(s, paner.GetPaneValues().get(s));
        
        mainPanel.remove(applet);
        String addTitle = filterMap.values().toString();
        addTitle = addTitle.replaceAll("\\(", ""); 
        addTitle = addTitle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\)", "");
        if (addTitle.length() > 1){
            addTitle = "(" + addTitle + ")";
        }
        if (addTitle.contains("null")){
            addTitle = addTitle.replaceAll(", null", "");
        }
        applet = new GraphController(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()), 
                "Player Car Stats " + addTitle,
                "Cars Used", "Player Usage", graph);      
        
        applet.SetOverrideColorScheme(Color.white);
        applet.init();
        
        mainPanel.add(applet); 
        legendPanel.setGraph(applet); 
    }
    
    class customString implements Comparable
    {
        public int year;
        public int month;
        public int day;
        
        public customString(String s)
        {
            String[] temp = s.split("/");
            month = Integer.parseInt(temp[0]);
            day = Integer.parseInt(temp[1]);
            year = Integer.parseInt(temp[2]);
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof customString))
                throw new InvalidParameterException("potatos");
            
            customString cs = (customString) o;
            
            if (cs.year > this.year)
                return -1;
            else if (cs.year < this.year)
                return 1;
            else
            {
                if (cs.month > this.month)
                    return -1;
                else if (cs.month < this.month)
                    return 1;
                else
                {
                    if (cs.day > this.day)
                        return -1;
                    else if (cs.day < this.day)
                        return 1;
                    else
                        return 0;
                }
            }
        }
        
        @Override
        public String toString()
        {
            return String.format("%s/%s/%s", month, day, year);
        }
    }
    
    
}

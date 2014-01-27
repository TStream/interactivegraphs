/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import Chart.Chart;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import query.Query;

/**
 *
 * @author Nico
 */
public class GeneralKnowledgeGUI extends GUISuperClass {

     ButtonGroup filterGroup;
     GraphController applet;
     GraphDrawer graph;
     Chart chart;
     Query q;
     RawDataTable rawData;
     GeneralKCompare compare;
     Paner paner;
     JRadioButton ascendingSort;
     JRadioButton descendingSort;
     JRadioButton defaultSort;
     JRadioButton filter1;
     JRadioButton filter2;
     JRadioButton filter3;

     public GeneralKnowledgeGUI() {

          filterGroup = new ButtonGroup();
          q = new Query();

          initWindow();
     }

     @Override
     public void initWindow() {
          this.setTitle("Player Knowledge Visualizer");


          //Create the "sort order" group of radial buttons
          ButtonGroup sortOrderGroup = new ButtonGroup();
          JLabel sortLabel = new JLabel();
          sortLabel.setText("Display:");
          ascendingSort = new JRadioButton();
          ascendingSort.setText("Correct");
          sortOrderGroup.add(ascendingSort);
          descendingSort = new JRadioButton();
          descendingSort.setText("Incorrect");
          sortOrderGroup.add(descendingSort);
          defaultSort = new JRadioButton();
          defaultSort.setText("Both");
          defaultSort.setSelected(true);
          sortOrderGroup.add(defaultSort);
          //end sort order group

          //Create "General Filters" group of radial buttons
          ButtonGroup genFilterGroup = new ButtonGroup();
          JLabel genFilterLabel = new JLabel();
          genFilterLabel.setText("General Filters:");
          filter1 = new JRadioButton();
          filter1.setText("Female");
          genFilterGroup.add(filter1);
          filter2 = new JRadioButton();
          filter2.setText("Male");
          genFilterGroup.add(filter2);
          filter3 = new JRadioButton();
          filter3.setText("Both");
          filter3.setSelected(true);
          genFilterGroup.add(filter3);
          //end general filters group


          GroupLayout fLayout1 = new GroupLayout(filterPanel);
          filterPanel.setLayout(fLayout1);

          fLayout1.setAutoCreateGaps(false);
          fLayout1.setAutoCreateContainerGaps(false);

          fLayout1.setHorizontalGroup(
                  fLayout1.createSequentialGroup()
                  .addGroup(fLayout1.createParallelGroup(GroupLayout.Alignment.LEADING)
                  .addComponent(sortLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(defaultSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(ascendingSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(descendingSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addGroup(fLayout1.createParallelGroup(GroupLayout.Alignment.LEADING)
                  .addComponent(genFilterLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

          fLayout1.setVerticalGroup(
                  fLayout1.createParallelGroup()
                  .addGroup(fLayout1.createSequentialGroup()
                  .addComponent(sortLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(defaultSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(ascendingSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(descendingSort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addGroup(fLayout1.createSequentialGroup()
                  .addComponent(genFilterLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(filter3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

          filterPanel.revalidate();


          AnswerGraph();

          applet = new GraphController(new Dimension(this.mainPanel.getWidth(), this.mainPanel.getHeight()), "User Answers",
                  "Question", "Times Answered", graph);
          applet.SetOverrideColorScheme(Color.BLACK);

          applet.init();
          mainPanel.add(applet);
          legendPanel.setGraph(applet);

          chart = new Chart("Example Chart", new Dimension(200, 500));
          chartSetUp();
          chart.init();

          this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

     }
     
      private void chartSetUp() {
          Map<String, Integer> tempValues = new HashMap<String, Integer>();
          tempValues.put("right", 100);
          tempValues.put("wrong", 200);
          tempValues.put("saucy", 400);
          tempValues.put("nogo", 600);
          chart.addValues("My First Bar", tempValues);
          tempValues = new HashMap<String, Integer>();
          tempValues.put("right", 100);
          tempValues.put("wrong", 500);
          tempValues.put("saucy", 400);
          tempValues.put("nogo", 300);
          chart.addValues("Another Bar!!!", tempValues);
     }

     private void AnswerGraph() {

          paner = new BarGraphPaner(3);
          Map<String, Map<String, Integer>> result = q.playerAge(null);


          for (String key : result.keySet()) {
               paner.addToPaner(key, result.get(key));        // add bar cluster value
          }

          graph = new BarGraph();        // initialize bar graph

          for (String s : paner.GetPaneValues().keySet()) {
               graph.AddTypeValue(s, paner.GetPaneValues().get(s));
          }
     }

     @Override
     public void onActionPerformed(ActionEvent evt) {
          if ("Raw Data".equals(evt.getActionCommand())) {
               Query q = new Query();
               q.playFrequency(null);
               //q.playerAge();
               q.playerGenderCount(null);
               q.playerLocales(null);
               //rawData = new RawDataTable();
              // rawData.setVisible(true);
               //rawData.setTitle("Raw Data");
          } else if ("Compare".equals(evt.getActionCommand())) {
               compare = new GeneralKCompare();
               compare.setVisible(true);
               compare.setTitle("Compare");

          }
     }

     @Override
     public void actionPerformed(ActionEvent evt) {
          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
}

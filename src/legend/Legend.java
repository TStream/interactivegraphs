/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package legend;

import cmpt305.GraphController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 *
 * @author fix
 */
public class Legend extends JScrollPane {

     GraphController graph; //Used to select and remove items
     JPanel content;
     int total =0; //Number of items visable

     public Legend() {
          init();
     }

     public Legend(GraphController graph) {
          init();
          setGraph(graph);
          total = this.getComponentCount();
          content.setLayout(new FlowLayout(FlowLayout.LEADING));
     }

     public void setGraph(GraphController graph) {
          this.graph = graph;
          content.removeAll();
          Map<String, Color> keys = graph.colorKey();
          for (String key : keys.keySet()) {
               addItem(keys.get(key), key);
          }
     }

     public void addItem(Color colour, String key) {
          content.add(new Row(this, key, colour));
          content.revalidate();
          content.repaint();          
          
          //Don't allow deletion if only one item is added
          if (content.getComponentCount() == 1) {
               Row last = (Row) content.getComponent(0);
               last.setDelete(false);
          }

          //Allow Deletion of first item if another is added
          if (content.getComponentCount() == 2) {
               Row last = (Row) content.getComponent(0);
               last.setDelete(true);
          }
     }

     public void deleteItem(String key) {
//          content.remove(r);
//          content.revalidate();
//          content.repaint();
          graph.selectSet(key, false);
          graph.deleteSet(key);   

          //Don't Allow Deletion if only one item in table or only 1 visible
          if (content.getComponentCount() == 1) {
               Row last = (Row) content.getComponent(0);
               last.setDelete(false);
          }
          total--; //Number of items visible
     }
     
     
          public void addGraphItem(String key) {
          graph.addSet(key);
          total++; //Items visable
          }

     public void selectItem(String key) {
          graph.selectSet(key, true);
     }

     public void deSelectItem(String key) {
          graph.selectSet(key, false);
     }

     private void init() {
          content = new JPanel();
          content.setLayout(new GridLayout(0, 1)); //limit to one collunm
          setViewportView(content);
          this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
          this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
          this.setBorder(BorderFactory.createTitledBorder("Legend"));

          this.setVisible(true);
     }
}

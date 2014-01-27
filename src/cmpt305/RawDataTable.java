/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class RawDataTable extends PopupSuperClass {
    
    JTable table;
    
    public RawDataTable (DefaultTableModel t){
         cPanel.setLayout(new BorderLayout());
        table = new JTable(t);
        table.setAutoCreateRowSorter(true);
        initListSelectionModel();
        initWindow();
    } 
    
    @Override
     public void initWindow() {
          this.setTitle("Raw Data");
          JScrollPane pane = new JScrollPane(table);
          pane.setPreferredSize(this.getPreferredSize());
          cPanel.add(new JScrollPane(table));
          table.setVisible(true);
          cPanel.setVisible(true);
          this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
     }
    
         private void initListSelectionModel() {
          ListSelectionModel lsm = table.getSelectionModel();
          lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     }
     
     @Override
     public void onActionPerformed(ActionEvent evt){
         
     }  
}

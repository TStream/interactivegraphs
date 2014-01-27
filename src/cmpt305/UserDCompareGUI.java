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
public class UserDCompareGUI extends PopupSuperClass{
    
    
    public UserDCompareGUI (){
        initWindow();
    }
    
    @Override 
    public void initWindow(){
         this.setPreferredSize(new Dimension(500,400));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);    
    }
 
    public void addGraph(GraphController g){
         this.cPanel.add(g);
         this.validate();
         this.repaint();
    }
    
     public void onActionPerformed(ActionEvent evt){
         
     }
    
}

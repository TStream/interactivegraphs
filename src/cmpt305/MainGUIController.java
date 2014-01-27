/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.event.*;

/**
 *
 * @author Nico
 */
public class MainGUIController implements ActionListener {
    
    MainGUI mGUI;
    
    GameStatisticsGUI gameStatsGUI;
    GeneralKnowledgeGUI genKnowGUI;
    UserDemographicsGUI userDemGUI;
    
    MainGUIController(MainGUI gui)
    {
        mGUI = gui;
        
        mGUI.getGameStatsButton().addActionListener(this);
        //mGUI.getGenKnowledgeButton().addActionListener(this);
        mGUI.getUserDemButton().addActionListener(this);
        
        mGUI.setVisible(true); 
    }
    
    @Override
    public void actionPerformed(ActionEvent evt)
    {
        if("Game Stats".equals(evt.getActionCommand()))
        {
            gameStatsGUI = new GameStatisticsGUI();
            gameStatsGUI.setVisible(true);
            
        }else if("Gen Know".equals(evt.getActionCommand()))
        {
            genKnowGUI = new GeneralKnowledgeGUI();
            genKnowGUI.setVisible(true);
            
        }else if("User Dem".equals(evt.getActionCommand()))
        {
            userDemGUI = new UserDemographicsGUI();
            userDemGUI.setVisible(true);
        }
    }
    
}

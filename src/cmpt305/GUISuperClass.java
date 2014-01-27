/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import legend.Legend;

/**
 *
 * @author Nico
 */
public abstract class GUISuperClass extends JFrame implements ActionListener{
    
    JPanel filterPanel;
    JPanel buttonPanel; //contains the compare and raw data buttons
    JPanel infoPanel; //for the legend
    JButton compareButton;
    JButton rawDataButton;
    protected JButton panLeftButton;
    protected JButton panRightButton;
    
    Legend legendPanel;
    
    public GUISuperClass() {
         initComponents();
         
         //this.setSize(900, 600); //main window size
         mainPanel.setPreferredSize(new Dimension(830, 607));
         //lowerContentPanel.setPreferredSize(new Dimension(140, 550));
         
         filterPanel = new JPanel();
         lowerContentPanel.add(filterPanel);
         filterPanel.setPreferredSize(new Dimension(140, 200));

         infoPanel = new JPanel();
         lowerContentPanel.add(infoPanel);
         infoPanel.setPreferredSize(new Dimension(140, 290));
         
         //create the button panel
         buttonPanel = new JPanel();
         lowerContentPanel.add(buttonPanel);
         buttonPanel.setPreferredSize(new Dimension(140, 117));
         
         GroupLayout infoLayout = new GroupLayout(infoPanel);
         infoPanel.setLayout(infoLayout);
         legendPanel = new Legend();
         infoPanel.add(legendPanel);
         legendPanel.setPreferredSize(new Dimension (140,240));
         
         infoLayout.setHorizontalGroup(
           infoLayout.createSequentialGroup()
              .addComponent(legendPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
         );
        infoLayout.setVerticalGroup(
           infoLayout.createParallelGroup()
              .addComponent(legendPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

         GroupLayout lcpLayout = new GroupLayout(lowerContentPanel);
         lowerContentPanel.setLayout(lcpLayout);

         lcpLayout.setAutoCreateGaps(false);
         lcpLayout.setAutoCreateContainerGaps(false);

         lcpLayout.setHorizontalGroup(
           lcpLayout.createParallelGroup()
              .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        lcpLayout.setVerticalGroup(
           lcpLayout.createSequentialGroup()
              .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

         compareButton = new JButton();
         compareButton.setText("Compare");
         buttonPanel.add(compareButton);
         compareButton.setPreferredSize(new Dimension(130,25));
         rawDataButton = new JButton();
         rawDataButton.setText("Raw Data");
         buttonPanel.add(rawDataButton);
         rawDataButton.setPreferredSize(new Dimension(130,25));
         JLabel panLabel = new JLabel();
         panLabel.setText("Pan Graph:");
         buttonPanel.add(panLabel);
         panLeftButton = new JButton();
         panLeftButton.setText("Prev");
         panLeftButton.setPreferredSize(new Dimension(62, 25));
         buttonPanel.add(panLeftButton);
         panRightButton = new JButton();
         panRightButton.setText("Next");
         panRightButton.setPreferredSize(new Dimension(62, 25));
         buttonPanel.add(panRightButton);

         compareButton.setActionCommand("Compare");
         rawDataButton.setActionCommand("Raw Data");
         panLeftButton.setActionCommand("Pan Left");
         panRightButton.setActionCommand("Pan Right");
         
         compareButton.addActionListener(this);
         rawDataButton.addActionListener(this);
         panLeftButton.addActionListener(this);
         panRightButton.addActionListener(this);

         GroupLayout bpLayout = new GroupLayout(buttonPanel);
         buttonPanel.setLayout(bpLayout);

         bpLayout.setAutoCreateGaps(true);
         bpLayout.setAutoCreateContainerGaps(false);

         bpLayout.setHorizontalGroup(
           bpLayout.createParallelGroup()
              .addComponent(panLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addGroup(bpLayout.createSequentialGroup()
                 .addComponent(panLeftButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                 .addComponent(panRightButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addComponent(compareButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(rawDataButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        bpLayout.setVerticalGroup(
           bpLayout.createSequentialGroup()
              .addComponent(panLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addGroup(bpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panLeftButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(panRightButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 6, 6)
              .addComponent(compareButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(rawDataButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

    }
     
    abstract void initWindow();
     
    @Override
    public void actionPerformed(ActionEvent evt)
    {
        this.onActionPerformed(evt);
    }
    
    abstract void onActionPerformed(ActionEvent evt);

     /**
      * This method is called from within the constructor to initialize the
      * form. WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        lowerContentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setPreferredSize(new java.awt.Dimension(830, 607));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lowerContentPanel.setPreferredSize(new java.awt.Dimension(140, 600));
        lowerContentPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout lowerContentPanelLayout = new javax.swing.GroupLayout(lowerContentPanel);
        lowerContentPanel.setLayout(lowerContentPanelLayout);
        lowerContentPanelLayout.setHorizontalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        lowerContentPanelLayout.setVerticalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lowerContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lowerContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel lowerContentPanel;
    protected javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}

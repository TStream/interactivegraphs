package legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*Melissa Trebell
 *
 */
public class Row extends JPanel {

     static Legend parent;
     JButton select;
     JButton delete;
     int height = 25;
     int width = 200;
     String key;
     Color c = Color.BLACK;

     public Row(Legend parent, String key, Color c) {
          this.parent = parent;
          this.key = key;
          this.c = c;
          setPreferredSize(new Dimension(width, height));
          setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

          //Make sure label fits in panel
          JLabel line = new JLabel(key);
          line.setMinimumSize(new Dimension(80, 15));
          line.setMaximumSize(new Dimension(80, 15));
          line.setToolTipText(key); //If Part of the Label is cut of let user see on hover

          BufferedImage buttonIcon;

          //Creating Custom Buttons
          try {
               buttonIcon = ImageIO.read(new File(".\\img\\delete.png"));
          } catch (Exception e) {
               System.out.println(e.getMessage());
               buttonIcon = null;
          }

          if (buttonIcon != null) {
               delete = new JButton(new ImageIcon(buttonIcon));
          } else {
               delete = new JButton("Delete");
          }

          delete.addActionListener(new deleteAction(key,delete));
          delete.setPreferredSize(new Dimension(15, 15));
          delete.setToolTipText("Delete");
          delete.setActionCommand("delete");
          delete.setBorder(BorderFactory.createEmptyBorder());
          delete.setContentAreaFilled(false);

          try {
               buttonIcon = ImageIO.read(new File("img\\select.png"));
          } catch (Exception e) {
               System.out.println(e.getMessage());
               buttonIcon = null;
          }
          if (buttonIcon != null) {
               select = new JButton(new ImageIcon(buttonIcon));
          } else {
               select = new JButton("Select");
          }

          select.addActionListener(new selectAction(key, select));
          select.setActionCommand("select");
          select.setPreferredSize(new Dimension(15, 15));
          select.setToolTipText("Select");
          select.setBorder(BorderFactory.createEmptyBorder());
          select.setContentAreaFilled(false);

          add(Box.createRigidArea(new Dimension(25, 15)));
          add(line);
          add(Box.createRigidArea(new Dimension(5, 15)));
          add(delete);
          add(Box.createRigidArea(new Dimension(5, 15)));
          add(select);
          add(Box.createHorizontalGlue());
     }

     @Override
     public void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawRect(5, 5, 15, 15);
          g.setColor(c);
          g.fillRect(5, 5, 15, 15);
     }

     public void buildButton(JButton button, String function) {
          BufferedImage buttonIcon;

          try {
               buttonIcon = ImageIO.read(new File("img\\"+ function + ".png"));
          } catch (Exception e) {
               System.out.println(e.getMessage());
               buttonIcon = null;
          }
          if (buttonIcon != null) {
               button.setIcon(new ImageIcon(buttonIcon));
          } else {
               button.setIcon(null);
               button.setText(function);
          }
          button.setToolTipText(function);
          button.setActionCommand(function);
     }

     public void setDelete(boolean value) {
          delete.setEnabled(value);
     }

     public class deleteAction implements ActionListener {
          String key;
          JButton button;

          deleteAction(String key, JButton button) {
               this.key = key;
               this.button = button;
          }

          @Override
          public void actionPerformed(ActionEvent e) {
               String action = e.getActionCommand();
               if (action.equals("add")) {
                    Row.parent.addGraphItem(key);
                    buildButton(button, "delete");
               } else{
                    Row.parent.deleteItem(key);
                    buildButton(button, "add");
               }
          }     
     }
     
     

     public class selectAction implements ActionListener {

          String key;
          JButton button;

          selectAction(String key, JButton button) {
               this.key = key;
               this.button = button;
          }

          @Override
          public void actionPerformed(ActionEvent e) {
               String action = e.getActionCommand();
               if (action.equals("select")) {
                    Row.parent.selectItem(key);
                    buildButton(button, "deselect");
               } else{
                    Row.parent.deSelectItem(key);
                    buildButton(button, "select");
               }
          }
     }
}

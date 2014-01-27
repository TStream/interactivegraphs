package legend;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*Melissa Trebell
*
*/

public class LegendTest {

    int total = 0;
     final Legend panel = new Legend();
     JFrame frame;
     
    public static void main(String[] args) {
         LegendTest test = new LegendTest();
         test.createAndShowUI();
   }

    private void createAndShowUI() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents(frame);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents(final JFrame frame) {

        JButton button = new JButton("Add label");
        panel.addItem(Color.BLACK,total+" Lets make this title really really stupid long");
         panel.addItem(Color.BLACK,total+" Lets make this title really really stupid long");
        
        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               panel.addItem(Color.BLACK,total+" Lets make this title really really stupid long");
               total++;
            }
        });
        panel.setVisible(true);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(button, BorderLayout.SOUTH);
    }
}

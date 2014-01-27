package Chart;


import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainApp {

     public static void main(String args[]) {

         JFrame frame;
         JPanel panel;
          Chart applet;
          Map<String,Integer> values = new TreeMap<String,Integer>();
          values.put("Male",500);
          values.put("Female",500);
          applet= new Chart("Test Chart",new Dimension(500,200));
          applet.addBar(new Pie(applet,values, "Test",100,100,150));
          applet.addBar(new StackedBar(applet,values, "Test", 0, 100,300,20));
          applet.addBar(new StackedBar(applet,values, "Test", 0, 150,100,10));
          applet.init();
          frame = new JFrame();
          panel = new JPanel();
          panel.add(applet);
          panel.setSize(750, 550);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.add(panel);
          frame.setVisible(true);
     }
}

package cmpt305;

import Chart.Chart;
import cmpt305.GraphDrawer.SelectedValues;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import query.Query;

public class CompareCntl {

     UserDCompareGUI view;
     GraphController c;
     GraphDrawer g;
     Chart details;
     List<SelectedValues> values;

     public CompareCntl(UserDCompareGUI view, GraphController cntl,GraphDrawer graph) {
          this.view = view;
          this.values = graph.getPointsSelected();
          Map<String, Map<String, Integer>> groups = new HashMap<String, Map<String, Integer>>();
          Map<String, Integer> subgroups;
          for (SelectedValues v : values) {
               subgroups = groups.get(v.selectedSubGroupOrScaler);
               if (subgroups == null) {
                    subgroups = new HashMap<String, Integer>();
               }
               subgroups.put(v.selectedGroupOrLine, v.value);
               groups.put(v.selectedSubGroupOrScaler, subgroups);
          }

          List<Color> unhovered = greyScale(groups.keySet().size());
          g = new BarGraph(Color.RED, Color.BLUE, unhovered);
          for (String key : groups.keySet()) {
               subgroups = groups.get(key);
               System.out.println(key + " " + subgroups);
               if (subgroups != null) {
                    g.AddTypeValue(key, subgroups);
               }
          }

          c = new GraphController(new Dimension(view.getWidth(), 420), "",
                  cntl.graphXTitle, cntl.graphYTitle, g);
          c.SetOverrideColorScheme(Color.BLACK);

          c.init();

          view.addGraph(c);
          view.setTitle("Compare");
          view.setVisible(true);
     }
     
    private void chartSetup(String type,Set<String> keys){
//         Query q = new Query();
//         //Need queries
//         Map<String,List<String>> result;
//         if(type.compareToIgnoreCase("date") == 0)
//              result = q.datePlayer();
//         else
//              result = q.questionPlayer();
//         
//         //Total for each bar
//         for(String s: keys){
//              List<String> players = result.get(s);
//              //Gender where PID == players
//              //Locale where PID == players
//              //Age Range where PID == Players
//         }
//         
//         //use bar totals to get my total
 }
    
    //Generate a list of grey scale colors to use in graph
    private List<Color> greyScale(int n){
         List<Color> cList = new ArrayList<Color>();
         Color c = Color.WHITE;
         System.out.println(c.getBlue());
         for(int i = 2 ; i<n+2 ; i++){
              Color temp = new Color(c.getRed()/i,c.getGreen()/i,c.getBlue()/i);
              cList.add(temp);
         }
         return cList;
    }
}

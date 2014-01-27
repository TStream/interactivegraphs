/*Melissa Trebell
*
*/

package cmpt305;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import query.*;

public class MainApp{

     /**
      * @param args the command line arguments
      */
    
    private static MainGUIController mGUIController;
    private static MainGUI mainWindow;
    
     public static void main(String[] args) {   
         
          mainWindow = new MainGUI();
          Query q = new Query();
          Map<String, List<String>> filters = new HashMap<String,List<String>>();
          List<String> L = new ArrayList<String>();
          
          L.add("female");
          filters.put("Gender", L);
          //q.playerAge(null);
          //q.playerAge(filters);
          //q.trafficKnowledge(null);
          //q.difficultCategories(null);
          //q.agkCorrelation(null);
          mGUIController = new MainGUIController(mainWindow);
     }
}

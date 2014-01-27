/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;

public interface GraphDrawer {
    public class SelectedValues
    {
        String selectedGroupOrLine;
        String selectedSubGroupOrScaler;
        int value;
        Point referenceLoc;
        
        public SelectedValues(String selectedGroupOrLine, String selectedSubGroupOrScaler, int value, Point loc)
        {
            this.selectedGroupOrLine = selectedGroupOrLine;
            this.selectedSubGroupOrScaler = selectedSubGroupOrScaler;
            this.value = value;
            referenceLoc = loc;
        }
        
        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof SelectedValues))
                return false;
            
            SelectedValues sv = (SelectedValues) o;
            if (sv.selectedGroupOrLine.equals(selectedGroupOrLine) &&
                    sv.selectedSubGroupOrScaler.equals(selectedSubGroupOrScaler) &&
                    sv.value == value)
                return true;
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + (this.selectedGroupOrLine != null ? this.selectedGroupOrLine.hashCode() : 0);
            hash = 79 * hash + (this.selectedSubGroupOrScaler != null ? this.selectedSubGroupOrScaler.hashCode() : 0);
            hash = 79 * hash + this.value;
            return hash;
        }
    }
    
     public void select(Point mousePosition, Dimension offset, Dimension graphWindow);
     public void hover(Point mousePosition, Dimension offset, Dimension graphWindow);
     public void displayValues(PApplet parent, Dimension offset, Dimension graphWindow, float yScale);
     public void AddTypeValue(String name, Map<String, Integer> subCategoryValues);
     public void SetBackground(int[] color);
     public int getMax();
     public List<SelectedValues> getPointsSelected();
     public void SetPointsAsSelected(List<SelectedValues> values);
     
     public Map selectedMap();
     public Map<String,Color> colorKey();
     public void selectSet(String key,boolean value);
     public void deleteSet(String key);
     public void addSet(String key);
     // my interfaces
}



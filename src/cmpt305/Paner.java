/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;

import java.awt.Point;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ian
 */
public interface Paner {
    public void PaneLimitRight();
    public void PaneLimitLeft();
    public List<String> GetPaneScale();
    public Map<String, Map<String, Integer>> GetPaneValues();
    public void addToPaner(String name, Map<String, Integer> subCategoryValues);
    public List<GraphDrawer.SelectedValues> getPointsSelected();
    public void SetPointsAsSelected(List<GraphDrawer.SelectedValues> values);
    public boolean CheckPanable();   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;



import javax.swing.SwingWorker;

/**
 *
 * @author user
 */
public class AnimationWorker extends SwingWorker<Void, Void>{
    RotatingArcs arcs;
    MainGUI gui;
    Title title;
    
    AnimationWorker(MainGUI g){
        gui = g;
    }
    
    @Override
    protected Void doInBackground() throws Exception { 
        arcs = new RotatingArcs();
        title = new Title();
        title.init();
        arcs.init(); 
        return null;
        }
    
    @Override
    protected void done() {
        try {
            //gui.AddTitle(title);
            gui.AddRA(arcs);
        } catch (Exception ex) {

        }
    }

}

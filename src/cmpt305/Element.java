
package cmpt305;

public class Element {

     public float value;
     public boolean selected;
     public String title;

     public Element(float value) {
          this.value = value;
          selected = false;
          title = "";
     }
     
     public Element(float value, String title) 
     {
         this(value);
         this.title = title;
     }
}

package cmpt305;

import processing.core.*;

public class Label {

     Label(PApplet parent, String txt, float x, float y) {
          int height = 10;
          int padding =5;
          parent.textSize(height);
          float width = parent.textWidth(txt);
          int offset = 15;
          // check if label would go beyond screen dims
          if (x + width + 20 > parent.width) {
               x -= width + 20;
          }

          //Label
          parent.fill(255);
          parent.rect(x, y, (width + 2*padding), height);

          //Text
          parent.fill(0);
          //Off set so mouse does not cover
          parent.text(txt, x + offset, y - offset);
     }
}

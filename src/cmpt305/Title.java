/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmpt305;
import processing.core.*;
/*
 *
 * @author user
 */
public class Title extends PApplet {
    
String word = "Choose A Category";
 
Letter[] letters;
float cushion = 10;
float mouseRad = 180;
 
float maxHeight = 30;
 
@Override
public void setup()
{
  size(191, 60);
  textSize(18);
  letters = new Letter[word.length()];
   
  noStroke();
  colorMode(HSB, 360, 1.0f, 1.0f);
  float minX = cushion;
  float maxX = width - cushion - textWidth("t");
  for (int i =0; i < word.length(); i++)
  {
    letters[i] = new Letter(word.charAt(i));
     
    letters[i].x = lerp(minX, maxX, (float)i / (word.length() - 1.0f));
    letters[i].y = height / 2;
     
    int centerIndex = (int)(word.length() / 2);
    float dist = abs(centerIndex - i);
    letters[i].hue = 80 + (dist / centerIndex) * 40;
  }
}
 
@Override
public void draw()
{
  background(335);
   
  for (int i = 0; i < word.length(); i++)
  {
    letters[i].Draw();
  }
}
 
class Letter
{
  char letter;
  public float x;
  public float y;
  public float hue;
   
  float hueAmount;
   
  public Letter(char letter)
  {
    this.letter = letter;
  }
   
  public void Draw()
  {
    float influence = dist(x, y, mouseX, mouseY) / mouseRad;
    influence = constrain(influence, 0, 1);
    influence = 1 - influence;
     
    y = lerp(y, height / 1.1f - maxHeight, .05f * influence);
    y = lerp(y, height / 1.1f, .035f * (1.0f - influence * influence));
     
    float hueInterp = map(y, height / 2 - maxHeight, height / 2, 1,0);
     
    hueInterp = pow(hueInterp, .4f);
    fill(240 + hue * hueInterp, 1, 1);
    text(letter, x, y);
  }

}
}

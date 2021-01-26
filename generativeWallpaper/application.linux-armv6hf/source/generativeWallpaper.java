import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class generativeWallpaper extends PApplet {


ControlP5 cp5;
/*Bailey Hwa
 project 1 for Columbia Creative Embedded Systems
 
 Generative art
 requires controlP5
 */ 
long time = millis();
boolean errMsg = false;
abstractTree theTree;
public void setup() {
  print("fuckoff");
  
  theTree = new abstractTree();
  theTree.init();
  ControlFont cf2 = new ControlFont(createFont("Times",width/35));
  cp5 = new ControlP5(this);
  cp5.addTextfield("Search City").setPosition(width/35, height/10).setSize(width/3, width/35).setAutoClear(true).setFont(cf2);
  cp5.addBang("Submit").setPosition((width/35)+(width/3), height/10).setSize(width/8, width/35).setFont(cf2); 
}

public void draw() {
  theTree.display();
  //println((millis()-time));
  if ((millis()-time) > 4000){
    time = millis();
    theTree.drawLeafs(10, PApplet.parseInt(theTree.twidth/11));
  }
  if (errMsg){
    
    text("No search results",(width/35)+(width/3),(height/10)+(width/35));
  }
  
}

class abstractTree {
  float n;
  int twidth;
  int theight;
  int minLevel;
  int maxLevel;
  PGraphics bg;
  PGraphics tree;
  PGraphics leaves;
  ArrayList<Leaf> leafs = new ArrayList<Leaf>();
  int leafCol;
  //float minHue;
  //float maxHue;
  //http://localhost:8888/
  abstractTree() {
    this.n = 0; // noise input
    this.twidth = PApplet.parseInt(width);//windowWidth;
    this.theight = PApplet.parseInt(height);//windowHeight;
    this.bg = createGraphics(this.twidth, this.theight); // background
    this.tree = createGraphics(this.twidth, this.theight);
    this.leaves = createGraphics(this.twidth, this.theight);
    this.minLevel = 4;
    this.maxLevel = 10;
    //print();
  }
  public void init() {
    clear();

    image(this.bg, 0, 0); // 
    this.ground(); 
    this.tree = createGraphics(this.twidth, this.theight);
    //scale tree size to fullscreen
    this.createTree(this.twidth/2, this.theight, PApplet.parseInt(this.twidth/10), 4);
    this.drawLeafs(10, PApplet.parseInt(this.twidth/11));  // big leaves/250
  }

  public void reset() {
    this.leafs  = new ArrayList<Leaf>();//= [];
    this.init();
  }
  public void newLeafs() {
  }
  public void display() {
    image(this.bg, 0, 0);
    image(this.tree, 0, 0);
    image(this.leaves, 0, 0);
  }
  public void keyPressed() {
  }
  public void createTree( float x, float y, float bLength, float nBranches) {//if 250
    //tree.beginDraw();
    this.tree.beginDraw();
    this.tree.noStroke();//int((crt*70)/250)
    this.tree.background(0, 0);  // clear PGraphics
    for (int i = 0; i < nBranches-1; i++) {
      this.tree.fill(map(i, 0, 2, 60, 20));//front and back layers of branches, front has darker gradient
      this.branch(this.twidth/2, this.theight, PApplet.parseInt((bLength*70)/random(248, 255)), -HALF_PI, random(bLength, bLength+10), 0);
    }
    this.tree.endDraw();
  }


  public float lerpInc(float a, float b, float d) {
    //a is higher bound, b lower bound, d distance
    return (a-b)/d;
  }
  public void branch(float x, float y, float bSize, float theta, float bLength, int level) {
    // make branches
    float  diam = bSize;
    this.tree.ellipse(x, y, diam, diam);//draw starting ellipse
    if (bSize > 0.6f) {//if branch growth is less than 0.6 quit 
      float tDiam = diam;
      for (float cnt = 0, dInc = this.lerpInc(bSize, (0.7f*bSize), bLength); cnt <bLength; cnt++, diam-=dInc, 
        x += cos(theta + random(-PI/10, PI/10)), 
        y += sin(theta + random(-PI/10, PI/10))) {
        //make branch along a vector
        n += 0.01f;//more noise for higher branches
        tDiam = diam * map(noise(this.n), 0, 1, 0.4f, 1.6f);//add texture
        this.tree.ellipse(x, y, tDiam, tDiam);
      }
      //next starting branch diameter for next branch
      diam = tDiam;
      //this.leafs.add(new Leaf(x,y,map(level,this.minLevel,this.maxLevel,0,1)));


      if (level < this.minLevel) {//below branch levels, guaranteed to have branches
        for (float j = 0, dInit = 1, dInc = this.lerpInc(1, -1, level+1); j<= level+1; dInit-=dInc, j++) {//lerp between 0 and level+1, while level is <= 4, number of child branches is level+1
          float direc = dInit*random(PI/15, PI/5);
          this.branch(x, y, random(0.5f, 0.7f)*bSize, theta + direc, random(0.6f, 0.8f)*bLength, level+1);
        }
      } else if (level<=this.maxLevel) {
        this.leafs.add(new Leaf(x, y, map(level, this.minLevel, this.maxLevel, 0.25f, 1)));
        float constraint = map(level, this.minLevel+1, this.maxLevel, 1, 0);
        boolean leftBranch = random(1)*constraint > 0.1f;
        boolean rightBranch = random(1)*constraint > 0.1f;
        //with each new branch created, the size of new one will be 0.5-0.7 times smaller
        if (leftBranch) this.branch(x, y, random(0.5f, 0.7f)*bSize, theta - random(PI/15, PI/5), random(0.6f, 0.8f)*bLength, level+1);
        if (rightBranch) this.branch(x, y, random(0.5f, 0.7f)*bSize, theta + random(PI/15, PI/5), random(0.6f, 0.8f)*bLength, level+1);
        if (!leftBranch && !rightBranch) {  // if none of the branchs are drawn, draw a tip
          this.drawTip(x, y, diam, theta);
        }
      } else {
        //Draw tip
        this.drawTip(x, y, diam, theta);
      }
    }
  }
  public void drawTip(float x, float y, float diam, float theta) {
    this.tree.push();
    this.tree.translate(x, y);
    this.tree.rotate(theta);
    this.tree.quad(0, -diam/2, 2*diam, -diam/6, 2*diam, diam/6, 0, diam/2);
    this.tree.pop();
    //this.leafs.add(new Leaf(x,y,map(level,this.minLevel,this.maxLevel,0,1)));
  }



  public void drawLeafs( float minDiam, float maxDiam) {
    this.leaves = createGraphics(this.twidth, this.theight);
    this.leaves.beginDraw();
    this.leaves.noStroke();
    this.leaves.colorMode(RGB);
    float r = random(150, 255);
    float g = random(150, 225);
    float b = random(125);
    //greens, reds, 
    for (Leaf l : this.leafs) {
      float a = 25*l.a*random(0.8f, 1);
      this.leaves.fill(r*l.a+random(-25, 25), g*l.a+random(-25, 25), b*l.a, (1-l.a)*25);
      float  diam = random(minDiam/2, maxDiam/2);
      float shiftX = random(-30, 30);
      float shiftY = random(-30, 30);
      this.leaves.ellipse(l.x + shiftX, l.y + shiftY, diam*random(1), diam);
    }
    this.leaves.endDraw();
  }
  public float average(float a, float b) {
    return (a+b)/2;
  }


  public void ground() {
    //create background
    this.bg.beginDraw();
    this.bg.strokeWeight(3);
    float offset = 1;
    float horiz = this.twidth/10;
    for (int x=0; x<this.twidth; x++) {
      for (int r=0; r<=15; r++) {
        this.bg.stroke(200-(15*r));
        this.bg.line(x, this.theight/2+(50*r)+horiz*noise(x / horiz, offset * r), x, this.theight);
      }
      offset+=0.0005f;//lower offset, less dramatic mountains
    }



    this.bg.endDraw();
  }
}
class Leaf {
  float x;
  float y;
  float a;
  float pers;
  Leaf(float x, float y, float a) {
    this.x=x;
    this.y=y;
    this.a=a;
  }
}
//gui/external comm
public void Submit() {
  print("the following text was submitted :");
  String url1 = cp5.get(Textfield.class,"Search City").getText();
  print(" textInput 1 = " + url1);
  println();
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "generativeWallpaper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

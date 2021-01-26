import controlP5.*;
ControlP5 cp5;
/*Bailey Hwa
 project 1 for Columbia Creative Embedded Systems
 
 Generative art
 requires controlP5
 */ 
long time = millis();
boolean errMsg = false;
abstractTree theTree;
void setup() {
  print("fuckoff");
  fullScreen();
  theTree = new abstractTree();
  theTree.init();
  ControlFont cf2 = new ControlFont(createFont("Times",width/35));
  cp5 = new ControlP5(this);
  cp5.addTextfield("Search City").setPosition(width/35, height/10).setSize(width/3, width/35).setAutoClear(true).setFont(cf2);
  cp5.addBang("Submit").setPosition((width/35)+(width/3), height/10).setSize(width/8, width/35).setFont(cf2); 
}

void draw() {
  theTree.display();
  //println((millis()-time));
  if ((millis()-time) > 4000){
    time = millis();
    theTree.drawLeafs(10, int(theTree.twidth/11));
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
  color leafCol;
  //float minHue;
  //float maxHue;
  //http://localhost:8888/
  abstractTree() {
    this.n = 0; // noise input
    this.twidth = int(width);//windowWidth;
    this.theight = int(height);//windowHeight;
    this.bg = createGraphics(this.twidth, this.theight); // background
    this.tree = createGraphics(this.twidth, this.theight);
    this.leaves = createGraphics(this.twidth, this.theight);
    this.minLevel = 4;
    this.maxLevel = 10;
    //print();
  }
  void init() {
    clear();

    image(this.bg, 0, 0); // 
    this.ground(); 
    this.tree = createGraphics(this.twidth, this.theight);
    //scale tree size to fullscreen
    this.createTree(this.twidth/2, this.theight, int(this.twidth/10), 4);
    this.drawLeafs(10, int(this.twidth/11));  // big leaves/250
  }

  void reset() {
    this.leafs  = new ArrayList<Leaf>();//= [];
    this.init();
  }
  void newLeafs() {
  }
  void display() {
    image(this.bg, 0, 0);
    image(this.tree, 0, 0);
    image(this.leaves, 0, 0);
  }
  void keyPressed() {
  }
  void createTree( float x, float y, float bLength, float nBranches) {//if 250
    //tree.beginDraw();
    this.tree.beginDraw();
    this.tree.noStroke();//int((crt*70)/250)
    this.tree.background(0, 0);  // clear PGraphics
    for (int i = 0; i < nBranches-1; i++) {
      this.tree.fill(map(i, 0, 2, 60, 20));//front and back layers of branches, front has darker gradient
      this.branch(this.twidth/2, this.theight, int((bLength*70)/random(248, 255)), -HALF_PI, random(bLength, bLength+10), 0);
    }
    this.tree.endDraw();
  }


  float lerpInc(float a, float b, float d) {
    //a is higher bound, b lower bound, d distance
    return (a-b)/d;
  }
  void branch(float x, float y, float bSize, float theta, float bLength, int level) {
    // make branches
    float  diam = bSize;
    this.tree.ellipse(x, y, diam, diam);//draw starting ellipse
    if (bSize > 0.6) {//if branch growth is less than 0.6 quit 
      float tDiam = diam;
      for (float cnt = 0, dInc = this.lerpInc(bSize, (0.7*bSize), bLength); cnt <bLength; cnt++, diam-=dInc, 
        x += cos(theta + random(-PI/10, PI/10)), 
        y += sin(theta + random(-PI/10, PI/10))) {
        //make branch along a vector
        n += 0.01;//more noise for higher branches
        tDiam = diam * map(noise(this.n), 0, 1, 0.4, 1.6);//add texture
        this.tree.ellipse(x, y, tDiam, tDiam);
      }
      //next starting branch diameter for next branch
      diam = tDiam;
      //this.leafs.add(new Leaf(x,y,map(level,this.minLevel,this.maxLevel,0,1)));


      if (level < this.minLevel) {//below branch levels, guaranteed to have branches
        for (float j = 0, dInit = 1, dInc = this.lerpInc(1, -1, level+1); j<= level+1; dInit-=dInc, j++) {//lerp between 0 and level+1, while level is <= 4, number of child branches is level+1
          float direc = dInit*random(PI/15, PI/5);
          this.branch(x, y, random(0.5, 0.7)*bSize, theta + direc, random(0.6, 0.8)*bLength, level+1);
        }
      } else if (level<=this.maxLevel) {
        this.leafs.add(new Leaf(x, y, map(level, this.minLevel, this.maxLevel, 0.25, 1)));
        float constraint = map(level, this.minLevel+1, this.maxLevel, 1, 0);
        boolean leftBranch = random(1)*constraint > 0.1;
        boolean rightBranch = random(1)*constraint > 0.1;
        //with each new branch created, the size of new one will be 0.5-0.7 times smaller
        if (leftBranch) this.branch(x, y, random(0.5, 0.7)*bSize, theta - random(PI/15, PI/5), random(0.6, 0.8)*bLength, level+1);
        if (rightBranch) this.branch(x, y, random(0.5, 0.7)*bSize, theta + random(PI/15, PI/5), random(0.6, 0.8)*bLength, level+1);
        if (!leftBranch && !rightBranch) {  // if none of the branchs are drawn, draw a tip
          this.drawTip(x, y, diam, theta);
        }
      } else {
        //Draw tip
        this.drawTip(x, y, diam, theta);
      }
    }
  }
  void drawTip(float x, float y, float diam, float theta) {
    this.tree.push();
    this.tree.translate(x, y);
    this.tree.rotate(theta);
    this.tree.quad(0, -diam/2, 2*diam, -diam/6, 2*diam, diam/6, 0, diam/2);
    this.tree.pop();
    //this.leafs.add(new Leaf(x,y,map(level,this.minLevel,this.maxLevel,0,1)));
  }



  void drawLeafs( float minDiam, float maxDiam) {
    this.leaves = createGraphics(this.twidth, this.theight);
    this.leaves.beginDraw();
    this.leaves.noStroke();
    this.leaves.colorMode(RGB);
    float r = random(150, 255);
    float g = random(150, 225);
    float b = random(125);
    //greens, reds, 
    for (Leaf l : this.leafs) {
      float a = 25*l.a*random(0.8, 1);
      this.leaves.fill(r*l.a+random(-25, 25), g*l.a+random(-25, 25), b*l.a, (1-l.a)*25);
      float  diam = random(minDiam/2, maxDiam/2);
      float shiftX = random(-30, 30);
      float shiftY = random(-30, 30);
      this.leaves.ellipse(l.x + shiftX, l.y + shiftY, diam*random(1), diam);
    }
    this.leaves.endDraw();
  }
  float average(float a, float b) {
    return (a+b)/2;
  }


  void ground() {
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
      offset+=0.0005;//lower offset, less dramatic mountains
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
void Submit() {
  print("the following text was submitted :");
  String url1 = cp5.get(Textfield.class,"Search City").getText();
  print(" textInput 1 = " + url1);
  println();
}

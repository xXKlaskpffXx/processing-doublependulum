float r1 = 200;
float r2 = 200;
float m1 = 40;
float m2 = 40;
float angle1 = 0;
float angle2 = 0;
float angle1v = 0;
float angle2v = 0;
float maxRad = r1+r2;
float mouseAngle = 0;
float tracerX = sin(angle1)*r1 + sin(angle2)*r2;
float tracerY = cos(angle1)*r1 + cos(angle2)*r2;

PVector mouseVec = new PVector();
PVector normalYVec = new PVector(0, 1);

float relation = r1/(r1+r2);


float x1;
float x2;
float y1;
float y2;

float g = 1;

PGraphics tracer;


void setup() {
  size(1200, 1200);
  tracer = createGraphics(1200, 1200);
  tracer.beginDraw();
  tracer.background(255);
  tracer.endDraw();
}

void draw() {
  image(tracer, 0, 0);

  // Das mit der Maus:
  
  if (mousePressed) {
    translate(600, 600);
    angle1v = 0;
    angle2v = 0;
    
    mouseVec.x = mouseX - 600;
    mouseVec.y = mouseY - 600;
    
    if (lengt(mouseVec) > maxRad) {
      mouseAngle = vecAngle(normalYVec, mouseVec);
      
      if (mouseVec.x > 0) {
        angle1 = mouseAngle;
        angle2 = mouseAngle;
      }else {
        angle1 = -mouseAngle;
        angle2 = -mouseAngle;
      }
      
    } else {
      
      float intersect = lengt(mouseVec) * relation;
      float tempAngle = acos(intersect / r1);
      
      x1 = r1*sin(angle1);
      y1 = r1*cos(angle1);
   
      PVector tempVec = new PVector(mouseVec.x - x1, mouseVec.y - y1);
      
      if (mouseVec.x > 0) {
        angle1 = (vecAngle(normalYVec, mouseVec) - tempAngle);
      } else {
        angle1 = -(vecAngle(normalYVec, mouseVec) - tempAngle);
      }
      
      if (mouseVec.x - x1 > 0) {
        angle2 = vecAngle(normalYVec, tempVec);
      }else {
        angle2 = -(vecAngle(normalYVec, tempVec));
      }
    }


    //
    
    x2 = x1 + sin(angle2)*r2;
    y2 = y1 + cos(angle2)*r2;
    x1 = sin(angle1)*r1;
    y1 = cos(angle1)*r1;
    
    
    line(0, 0, x1, y1);
    line(x1, y1, x2, y2);
    circle(x1, y1, m1);
    circle(x2, y2, m2);
    tracerX = x2;
    tracerY = y2;
  } else {

    // Pendelformeln
    
    float angle1a = -g * (2*m1 + m2) * sin(angle1) - m2 * g * sin(angle1 - 2*angle2) - 2 * sin(angle1 - angle2) * m2 * (angle2v * angle2v * r2 + angle1v * angle1v * r1 * cos(angle1 - angle2));
    angle1a /= r1 * (2*m1 + m2 - m2*cos(2*angle1-2*angle2));

    float angle2a = 2*sin(angle1-angle2)*(angle1v*angle1v*r1*(m1+m2)+g*(m1+m2)*cos(angle1)+angle2v*angle2v*r2*m2*cos(angle1-angle2));
    angle2a /= r2*(2*m1+m2-m2*cos(2*angle1-2*angle2));

    //
    
    stroke(0);
    strokeWeight(2);

    translate(600, 600);

    x1 = sin(angle1)*r1;
    y1 = cos(angle1)*r1;

    x2 = x1 + sin(angle2)*r2;
    y2 = y1 + cos(angle2)*r2;

    line(0, 0, x1, y1);
    line(x1, y1, x2, y2);
    circle(x1, y1, m1);
    circle(x2, y2, m2);

    angle1v += angle1a;
    angle2v += angle2a;
    angle1 += angle1v;
    angle2 += angle2v;
    
    angle1v *= 0.999;
    angle2v *= 0.999;

    tracer.beginDraw();
    tracer.translate(600, 600);
    tracer.strokeWeight(1);
    tracer.stroke(0,50);
    tracer.line(tracerX, tracerY, x2, y2);
    tracer.endDraw();
    tracerX = x2;
    tracerY = y2;
  }
}

// Vectorrechnungsoperatoren

float vecAngle(PVector v1, PVector v2) {
  return acos(scalar(v1, v2) / (lengt(v1)*lengt(v2)));
}

float scalar(PVector v1, PVector v2) {
  return (v1.x * v2.x)+(v1.y * v2.y);
}

float lengt(PVector v) {
  return sqrt(pow(v.x, 2) + pow(v.y, 2));
}

PVector vecAdd(PVector v1, PVector v2) {
  return new PVector(v1.x + v2.x, v1.y, v2.y);
}

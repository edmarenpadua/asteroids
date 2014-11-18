/*
* Java Applet implementation of Asteroids using
* http://students.cs.byu.edu/~nodnarb5/AsteroidsManual/Asteroids%20Manual.pdf
*/

import java.awt.*;
import java.awt.geom.Point2D;

public class Laser {
   
    final double laserSpeed = 10; //speed of laser
   
    double x, y; //positional coordinates
    double xVelocity, yVelocity, angle; //movement
   
    int lifeLeft; //limits how far laser can fly
    boolean active; //flag to check whether the laser is active
   
    final double[] startingXPts = {8,2,-2,-8,-2,2};
    final double[] startingYPts = {0,2,2,0,-2,-2};
    double laserRadius = 4;
    int[] xPts, yPts; //need this to hold int values to be passed to fillPolygon()
   
    public Laser(double x, double y, double angle, double shipXVel, double shipYVel, int lifeLeft) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        xVelocity = laserSpeed * Math.cos(angle) + shipXVel;
        yVelocity = laserSpeed * Math.sin(angle) + shipYVel;
        this.lifeLeft = lifeLeft;
        active = true;
       
        //set aside space for the coordinate holder arrays
        xPts = new int[6];
        yPts = new int[6];
    }
   
    public void draw(Graphics g) {
        for(int i=0 ; i<6 ; i++) {
            xPts[i] = (int)(startingXPts[i] * Math.cos(angle) - startingYPts[i] * Math.sin(angle) + x + 0.5);
            yPts[i] = (int)(startingXPts[i] * Math.sin(angle) + startingYPts[i] * Math.cos(angle) + y + 0.5);
        }
        g.setColor(Color.BLUE);
        g.fillPolygon(xPts, yPts, 6);
                   
    }
   
    public void move(int scrnWidth, int scrnHeight) {
        if(active) {
            x += xVelocity;
            y += yVelocity;
        }
       
        if(x < 0) {                     //wraps the laser around to the opposite side of the screen
            x += scrnWidth; //when it goes out of the screen's bounds
        } else if(x > scrnWidth) {
            x -= scrnWidth;
        }
        if(y < 0) {
            y += scrnHeight;
        } else if (y > scrnHeight) {
            y -= scrnHeight;
        }
       
        lifeLeft--;
        if (lifeLeft==0)
            active = false;
    }
   
    //getters and setters
   
    public boolean getActive() {
        return active;
    }
   
    public Point2D getCenter() {
        return new Point2D.Double(x, y);
    }
   
    public double getRadius() {
        return laserRadius;
    }

}


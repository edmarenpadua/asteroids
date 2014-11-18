/*
* Resource:
* Java Applet implementation of Asteroids using
* http://students.cs.byu.edu/~nodnarb5/AsteroidsManual/Asteroids%20Manual.pdf
*/

import java.awt.*;
import java.awt.geom.Point2D;

public class Asteroid {

    int size; //denotes how big the asteroid is
    double x, y; //positional coordinates
    double angle, rotationalSpeed; //used to rotate the ship
    double xVelocity, yVelocity;  //speed

    boolean active; //flag to check whether the asteroid is alive

    //polygon points for drawing the asteroid
    double[] startingXPts = {-30,-10,10,30,30,10,-10,-30};
    double[] startingYPts = {10,30,30,10,-10,-30,-30,-10};
    double asteroidRadius = 30;
    int[] xPts, yPts; //need this to hold int values to be passed to fillPolygon()

    //constructor statement
    public Asteroid (int size, double x, double y, double angle, double rotationalSpeed, double xVelocity, double yVelocity) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.rotationalSpeed = rotationalSpeed;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        active = true;
       
        //used size variable to determine how big the asteroid polygon should be
        if(size < 1) {
                for(int i = 0 ; i < startingXPts.length ; i++) {
                        startingXPts[i] /= 2;
                        startingYPts[i] /= 2;
                }
                asteroidRadius /= 2;
                this.xVelocity *= 1.5;
                this.xVelocity *= 1.5;
        } else if (size > 1) {
                for(int i = 0 ; i < startingXPts.length ; i++) {
                        startingXPts[i] *= 2;
                        startingYPts[i] *= 2;
                }
                asteroidRadius *= 2;
                this.xVelocity /= 2;
                this.yVelocity /= 2;
                this.rotationalSpeed /= 2;
        }
       
        //set aside space for the coordinate holder arrays
        xPts = new int[8];
        yPts = new int[8];
    }

    public void draw(Graphics g) {
        if(active) {
            for(int i=0 ; i<8 ; i++) {
                xPts[i] = (int)(startingXPts[i] * Math.cos(angle) - startingYPts[i] * Math.sin(angle) + x + 0.5);
                yPts[i] = (int)(startingXPts[i] * Math.sin(angle) + startingYPts[i] * Math.cos(angle) + y + 0.5);
            }
            g.setColor(Color.GRAY);
            g.drawPolygon(xPts, yPts, 8);
        }
    }

    public void move(int sWidth, int sHeight) {
        if(active) {
            angle += rotationalSpeed;
            x += xVelocity;
            y += yVelocity;
        }
       
        if(x<(0-(asteroidRadius*2)))
            x += sWidth+(asteroidRadius*2);
        else if(x>(sWidth+(asteroidRadius*2)))
            x -= sWidth+(asteroidRadius*2);
       
        if(y<(0-(asteroidRadius*2))) {
            y += sHeight+(asteroidRadius*2);
        }
        else if(y>(sHeight+(asteroidRadius*2)))
            y -= sHeight+(asteroidRadius*2);
    }


    //getters and setters
    public AsteroidExplosion explode() {
            double xVel;
            double yVel;
                    xVel = Math.random();
                    yVel = Math.random();
                     int xDir = (int)(Math.random() * 2);
                     int yDir = (int)(Math.random() * 2);
                            if(xDir == 1)
                                    xVel *= -1;
                            if(yDir == 1)
                                    yVel *= -1;
            return new AsteroidExplosion(x, y, xVel, yVel, 0, .1);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Point2D getCenter() {
        return new Point2D.Double(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return asteroidRadius;
    }

    public int getSize() {
        return size;
    }
}

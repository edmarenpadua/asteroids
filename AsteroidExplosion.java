import java.awt.*;

public class AsteroidExplosion {
       
        double lifeLeft = 150;
       
        double x, y; //positional coordinates
        double xVelocity, yVelocity, angle, rotationalSpeed; //movement
       
        final double startingXPts[] = {-5, 5};
        final double startingYPts[] = {-5, 5};
        int[] xPts, yPts;
       
        public AsteroidExplosion(double x, double y, double xVelocity, double yVelocity, double angle, double rotationalSpeed) {
                this.x = x;
                this.y = y;
                this.xVelocity = xVelocity;
                this.yVelocity = yVelocity;
                this.angle = angle;
                this.rotationalSpeed = rotationalSpeed;
               
                xPts = new int[2];
                yPts = new int[2];
        }
       
        public void draw(Graphics g) {
                for(int i = 0 ; i < 2 ; i++) {
                        xPts[i] = (int)(startingXPts[i] * Math.cos(angle) - startingYPts[i] * Math.sin(angle) + x + 0.5);
                        yPts[i] = (int)(startingXPts[i] * Math.sin(angle) + startingYPts[i] * Math.cos(angle) + y + 0.5);
                }
                g.setColor(Color.GRAY);
                g.drawPolygon(xPts, yPts, 2);
        }
       
        public void move() {
                angle += rotationalSpeed;
                x += xVelocity;
                y += yVelocity;
                lifeLeft--;
        }

        //getters and setters
        public double getLifeLeft() {
                return lifeLeft;
        }
}

/**
* Author: PADUA, Edmaren F.
*			2011-38803
*
* Laser, Spaceship and Asteroid Physics from
* http://www.htmlgoodies.com/html5/client/how-to-build-asteroids-with-the-impact-html5-game-engine-modules.html#fbid=mPYv2D1d-z8
* Java Applet implementation of Asteroids using http://students.cs.byu.edu/~nodnarb5/AsteroidsManual/Asteroids%20Manual.pdf
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import javax.swing.Timer;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Asteroids extends JFrame{ 
	GamePanel gamepanel;
	BGPanel bgpanel;
	Footer footer;
	Header header;
	LPanel left;
	RPanel right;
	public Asteroids(){
		super("Asteroid");
		Container c = getContentPane();
		bgpanel = new BGPanel();
		header = new Header();
		gamepanel = new GamePanel(header);				//holds the game area
		footer = new Footer();						//holds the stats of player(s)
		left = new LPanel();						//just for space (?)
		right = new RPanel();						//holds the chat
		c.add(bgpanel,BorderLayout.CENTER);
		bgpanel.add(right, BorderLayout.EAST);
		bgpanel.add(left, BorderLayout.WEST);
		bgpanel.add(gamepanel,BorderLayout.CENTER);
		bgpanel.add(header, BorderLayout.NORTH);
		bgpanel.add(footer, BorderLayout.SOUTH);
		//sets the size of the frame
		this.setSize(1000, 720); 					//rightmost 200 pixels  will be for chat
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// centers the frame in screen
		this.setLocationRelativeTo(null);
		this.setVisible(true); 
		this.setResizable(false);
	}
}

class BGPanel extends JPanel{
	Image background;
	public BGPanel(){
		setLayout(new BorderLayout());
		//background = Toolkit.getDefaultToolkit().getImage("bg3.jpg");	//for background
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//setBackground(Color.black);
	}
}

class LPanel extends JPanel{
	public LPanel(){
		//this.setOpaque(false);
		this.setPreferredSize(new Dimension(0, 0));
	}
}


/*
* Chat area
*
*/
class RPanel extends JPanel{
	public RPanel(){
		//this.setOpaque(false);
		this.setPreferredSize(new Dimension(200, 0));
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//setBackground(Color.green);			//for background of the chat
	}
}

/*
* Stats area
* Lives, levels, etc
*/
class Header extends JPanel{
	int level;
	int lives;
	int score;
	public Header(){
		//setOpaque(false);
		this.setPreferredSize(new Dimension(0, 50));
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.WHITE);
        //g.drawString("Level : " + level, 0, 0);
       // g.drawString("Lives : " + lives, 110, 690);
        //g2d.drawString("Score : " + score, 210, 690);
	}
}

class Footer extends JPanel implements ActionListener{
	//Help helpp;
	JButton help;
	JButton about;
	public Footer(){
		setOpaque(false);
		help =  new JButton("How to play");
		about = new JButton("About");
		this.add(help);
		help.addActionListener(this);
		this.add(about);
		about.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==help){
			//helpp = new Help();
		}
		else if(e.getSource()==about){
			JOptionPane.showMessageDialog(null,"Asteroid\nCMSC 137 CD2L\nProject2014\nLast Update: Nov. 18, 2014");
		}
	}
}

/*
* The gaming area :3
* Must accept @params from header(score, level, lives)
*/
class GamePanel extends JPanel implements Runnable, KeyListener{
        //timing variables
        Thread thread;
        long startTime, endTime, framePeriod;
       
        //graphics variables
        Image img;
        int width, height;
        Graphics g;
       
        //text items
        int level, lives, score;
       
        SpaceShip ship;
        boolean shipCollision, shipExplode;
       
        //ArrayList to hold asteroids
        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        int numOfAsteroids = 2;
       
        //ArrayList to hold the lasers
        ArrayList<Laser> lasers = new ArrayList<Laser>();
        final double rateOfFire = 10; //limits rate of fire
        double rateOfFireRemaining; //decrements rate of fire
       
        //ArrayList to hold explosion particles
        ArrayList<AsteroidExplosion> explodingLines = new ArrayList<AsteroidExplosion>();
       
        //ArrayList to hold ship explosions
        ArrayList<ShipExplosion> shipExplosion = new ArrayList<ShipExplosion>();

        Header header;

    public GamePanel(Header header){
		this.header = header;
		this.setOpaque(false);
        width = 800;
        height = 650;
        framePeriod = 25; 			//set refresh rate
       
        addKeyListener(this);		//to get commands from keyboard
        setFocusable(true);
       
        ship = new SpaceShip(width/2, height/2, 0, .15, .5, .15, .98); //add ship to game(msut be dynamic if multiplayer)
        shipCollision = false;
        shipExplode = false;
        level = numOfAsteroids;
        lives = 3;
        addAsteroids();

	    thread = new Thread(this);
        thread.start();

        header.level =  level;
        header.lives =  lives;
        header.score = 0;
	}

	public void paint(Graphics gfx) {
        Graphics2D g2d = (Graphics2D) gfx;
        //give the graphics smooth edges
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
       
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height); 						//add a black background    

        g2d.setColor(Color.WHITE);
        g2d.drawString("Level : " + level, 10, 20);
        g2d.drawString("Lives : " + lives, 110, 20);
        g2d.drawString("Score : " + score, 210, 20);
       
        for(Asteroid a: asteroids) { //draw asteroids
                a.draw(g2d);
        }
       
        for(Laser l : lasers) { //draw lasers
                l.draw(g2d);
        }
       
        for(AsteroidExplosion e : explodingLines) {
                e.draw(g2d);
        }
       
        for(ShipExplosion ex : shipExplosion)
                ex.draw(g2d);
       
        ship.draw(g2d); //draw ship
        if(shipCollision) {
                shipExplosion.add(new ShipExplosion(ship.getX(), ship.getY(), 10, 10));
                        ship.setX(width/2);
                        ship.setY(height/2);
                        shipCollision = false;
                        lives--;
        }
       
        gfx.drawImage(img, 0, 0, this); //draw the off-screen image (double-buffering)
        }
       
    public void update(Graphics gfx) {
        paint(gfx); //gets rid of white flickering
    }
   
    public void run() {
            for( ; ; ) {									//run foreverr :3
                    startTime = System.currentTimeMillis(); //timestamp
                    ship.move(width, height);               //ship movement
                    for(Asteroid a : asteroids) {           //asteroid movement
                            a.move(width, height);
                    }
                    for(Laser l : lasers) {                         //laser movement
                            l.move(width, height);
                    }
                    for(int i = 0 ; i<lasers.size() ; i++) {        //laser removal
                            if(!lasers.get(i).getActive())
                                    lasers.remove(i);
                    }
                    for(AsteroidExplosion e : explodingLines) {                     //asteroid explosion floating lines movement
                            e.move();
                    }
                    for(int i = 0 ; i<explodingLines.size(); i++) {         //asteroid explosion floating lines removal
                            if(explodingLines.get(i).getLifeLeft() <= 0)
                                    explodingLines.remove(i);
                    }
                    for(ShipExplosion ex : shipExplosion){  //ship explosion expansion
                            ex.expand();
                    }
                    for(int i = 0 ; i<shipExplosion.size() ; i++) {        
                            if(shipExplosion.get(i).getLifeLeft() <= 0)
                                    shipExplosion.remove(i);
                    }
                    rateOfFireRemaining--;
                    collisionCheck();
                    if(asteroids.size() == 0) {
                            numOfAsteroids++;
                            addAsteroids();
                            level = numOfAsteroids;
                    }
                    repaint();
                    try {
                            endTime = System.currentTimeMillis(); //new timestamp
                            if(framePeriod - (endTime-startTime) > 0)                               //if there is time left over after repaint, then sleep
                                    Thread.sleep(framePeriod - (endTime - startTime));      //for whatever is  remaining in framePeriod
                    } catch(InterruptedException e) {}
            }
    }
   
    public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            //fires laser
            if(key == KeyEvent.VK_SPACE) {
                    if(rateOfFireRemaining <= 0 ) {
                            lasers.add(ship.fire());
                            rateOfFireRemaining = rateOfFire;
                    }
            }
            if(key == KeyEvent.VK_UP)
                    ship.setAccelerating(true);
            if(key == KeyEvent.VK_RIGHT)
                    ship.setTurningRight(true);
            if(key == KeyEvent.VK_LEFT)
                    ship.setTurningLeft(true);
            if(key == KeyEvent.VK_DOWN)
                    ship.setDecelerating(true);
    }
   
    public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_UP)
                    ship.setAccelerating(false);
            if(key == KeyEvent.VK_RIGHT)
                    ship.setTurningRight(false);
            if(key == KeyEvent.VK_LEFT)
                    ship.setTurningLeft(false);
            if(key == KeyEvent.VK_DOWN)
                    ship.setDecelerating(false);
    }
   
    public void keyTyped(KeyEvent e) {
           
    }

    public void addAsteroids() {
        int numAsteroidsLeft = numOfAsteroids;
        int size;
       
        for(int i=0 ; i<numOfAsteroids ; i++) {         //add asteroids to game
            //randomize starting position
            int asteroidX = (int)(Math.random() * width) + 1;
            int asteroidY = (int)(Math.random() * height) + 1;
                   
            //randomize speed and direction
            double xVelocity = Math.random() + 1; //horizontal velocity
            double yVelocity = Math.random() + 1; //vertical velocity
            //used starting direction
            int xDirection = (int)(Math.random() * 2);
            int yDirection = (int)(Math.random() * 2);
                //randomize horizontal direction
                if (xDirection == 1)
                        xVelocity *= (-1);
                //randomize vertical direction
                if (yDirection == 1)
                        yVelocity *= (-1);
           
            //if there are more then four asteroids, any new ones are MEGA asteroids
            if(numAsteroidsLeft > 4) {
                size = 2;
            } else { size = 1;
            }
           
            asteroids.add(new Asteroid(size, asteroidX, asteroidY, 0, .1, xVelocity, yVelocity));
            numAsteroidsLeft--;
               
    		//Make sure that no asteroids can appear right on top of the ship
            //get center of recently created asteroid and ship and check the distance between them
            Point2D asteroidCenter = asteroids.get(i).getCenter();
            Point2D shipCenter = ship.getCenter();
            double distanceBetween = asteroidCenter.distance(shipCenter);
           
            //if asteroid center is within 80 pixels of ship's center, remove it from the ArrayList and rebuild it
            if(distanceBetween <= 80) {
                    asteroids.remove(i);
                    i--;
                    numAsteroidsLeft++;
            }
               
        }
    }
   
    public void collisionCheck() {
        //cycle through active asteroids checking for collisions
        for(int i = 0 ; i < asteroids.size() ; i++) {
            Asteroid a = asteroids.get(i);
            Point2D aCenter = a.getCenter();
           
            //check for collisions between lasers and asteroids
            for(int j = 0 ; j < lasers.size() ; j++) {
                Laser l = lasers.get(j);
                Point2D lCenter = l.getCenter();
               
                double distanceBetween = aCenter.distance(lCenter);
                if(distanceBetween <= (a.getRadius() + l.getRadius())) {
                    //split larger asteroids into smaller ones, remove smaller asteroids from screen
                    if(a.getRadius() >= 60) {
                            for(int k = 0 ; k < 3 ; k++)
                                    explodingLines.add(a.explode());
                            split(i);
                            score += 50;
                    } else if(a.getRadius() >= 30){
                            for(int k = 0 ; k < 3 ; k++)
                                    explodingLines.add(a.explode());
                            split(i);
                            score += 100;
                    } else {
                            for(int k = 0 ; k < 3 ; k++)
                                    explodingLines.add(a.explode());
                            asteroids.remove(i);
                            score += 200;
                    }
                   
                    lasers.remove(j); //remove laser from screen
                }
            }
           
            //check for collisions between ship and asteroid
            Point2D sCenter = ship.getCenter();
            double distanceBetween = aCenter.distance(sCenter);
            if(distanceBetween <= (a.getRadius() + ship.getRadius())) {
                    shipCollision = true;
                    shipExplode = true;
            }
        }
    }
   
    public void split(int i) {
        Asteroid a = asteroids.get(i);
        double bigAsteroidX = a.getX();
        double bigAsteroidY = a.getY();
        int size = (a.getSize() / 2);
        asteroids.remove(i);
        for(int j = 0 ; j<2 ; j++) {
            //randomize speed and direction
            double xVelocity = Math.random() + 1; //horizontal velocity
            double yVelocity = Math.random() + 1; //vertical velocity
            //used randomize starting direction
            int xDirection = (int)(Math.random() * 2);
            int yDirection = (int)(Math.random() * 2);
            //randomize horizontal direction
            if (xDirection == 1)
                    xVelocity *= (-1);
            //randomize vertical direction
            if (yDirection == 1)
                    yVelocity *= (-1);
            asteroids.add(new Asteroid(size, bigAsteroidX, bigAsteroidY, 0, .1, xVelocity, yVelocity));
        }
    }   
}


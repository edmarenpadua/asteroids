import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartGame{
	public static void main( String[] args){
		Start frame = new Start();
	}
}

class Start extends JFrame{
	Panel1 gameMenu;
	public Start() {
		super("Asteroid: Menu");
		this.setSize(1000, 720); 
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		gameMenu= new Panel1(this);
	}
}

class Panel1 extends JPanel implements ActionListener {
	Start frame;
	JButton play;
	JButton help;
	JButton exit;
	Asteroids player;
	//Help helpp;
	//Image background = Toolkit.getDefaultToolkit().getImage("menu.jpg");
	
	public Panel1(Start frame) {
		this.setLayout(null);
		this.frame = frame;
		this.setSize(790, 575);
		frame.setContentPane(this);
		play= new JButton("Play!");
		this.add(play);
		play.addActionListener(this);
		help= new JButton("How to play game");
		this.add(help);
		help.addActionListener(this);
		exit= new JButton("Quit");
		this.add(exit);
		exit.addActionListener(this);
		play.setBounds(275, 300, 220, 50);
		help.setBounds(275, 360, 220, 50);
		exit.setBounds(275, 420, 220, 50);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == play){
			frame.setVisible(false);
			player = new Asteroids();
		}
		if(e.getSource() == help){
			//helpp = new Help();
		}
		if(e.getSource() == exit){
			System.exit(0);
		}		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//g.drawImage(background,0, 0,this);
	}
}
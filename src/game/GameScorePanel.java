package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameScorePanel extends JPanel implements ActionListener {
	public static int score = 0;
	private JLabel scoreLabel;
	private Timer scoreAdditionTimer;
	
	public GameScorePanel() {
		super();
		
		this.scoreLabel = new JLabel("Score: " + String.valueOf(score));
		this.scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 40));
		this.scoreLabel.setBounds(0, 0, this.scoreLabel.getPreferredSize().width, 50);
		this.scoreLabel.setForeground(Color.black);
		add(this.scoreLabel);
		this.setBackground(new Color(255, 94, 0, 200));
		setSize(this.scoreLabel.getPreferredSize().width, 68);
		setLocation(30, 10);
		//setBounds(30, 10, this.scoreLabel.getPreferredSize().width, this.scoreLabel.getPreferredSize().height);
		setLayout(new FlowLayout());
		setVisible(true);
		setOpaque(true);
		
		this.scoreAdditionTimer = new Timer(100, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GameScorePanel.score += 1;
		this.scoreLabel.setText("Score: " + String.valueOf(score));
		setSize(this.scoreLabel.getPreferredSize().width, 68);
	}
	
	public void stop() {
		this.scoreAdditionTimer.stop();
	}
	
	public void start() {
		this.scoreAdditionTimer.start();
	}
}

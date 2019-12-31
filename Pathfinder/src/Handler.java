import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/* ------------------------------------------------
 * THIS CLASS CONTAINS THE CODE USED TO CONTROL ALL
 * COMPONENTS ON THE CONTROL PANEL IN THE BOTTOM OF
 * THE WINDOW. used as a way way to clean code from 
 * 					  Frame.Java
 * ------------------------------------------------
 * */

public class Handler {
	
	//variables
	private Frame frame;
	private JLabel modeT, speedT, speedC, openT, closedT, pathT, noPathT, //T = text
			   openC, closedC, pathC;									  //C = constant
	private JCheckBox diagonalCheck, trigCheck;
	private JSlider speed;
	private JButton run, clr; //clr button is unavailable for now, only clears start and end nodes and not walls
	private ArrayList<JLabel> labels;
	private ArrayList<JCheckBox> checkboxes;  //reason for array list creation is,
	private ArrayList<JSlider> slider;        //for most possible efficient code;
	private ArrayList<JButton> buttons;       //getters and setter more efficient,
	Dimension npD;							  //loop through array lists.
	
	//constructor
	public Handler(Frame frame) {
		this.frame = frame;	
		labels = new ArrayList<JLabel>();
		checkboxes = new ArrayList<JCheckBox>();
		slider = new ArrayList<JSlider>();
		buttons = new ArrayList<JButton>();
		
		//set up JLabels
		modeT = new JLabel("Mode: ");
		modeT.setName("modeText");
		modeT.setFont(Style.bigText);
		modeT.setForeground(Style.darkText);
		modeT.setVisible(true);
		
		speedT = new JLabel("Speed: ");
		speedT.setName("modeText");
		speedT.setFont(Style.nums);
		speedT.setVisible(true);
		
		speedC = new JLabel("50");
		speedC.setName("speedC");
		speedC.setFont(Style.nums);
		speedC.setVisible(true);
		
		openT = new JLabel("Open");
		openT.setName("openT");
		openT.setFont(Style.nums);
		openT.setVisible(true);
		
		openC = new JLabel("0");
		openC.setName("openC");
		openC.setFont(Style.nums);
		openC.setVisible(true);
		
		closedT = new JLabel("Closed");
		closedT.setName("closedT");
		closedT.setFont(Style.nums);
		closedT.setVisible(true);
		
		closedC = new JLabel("0");
		closedC.setName("closedC");
		closedC.setFont(Style.nums);
		closedC.setVisible(true);
		
		pathT = new JLabel("Path");
		pathT.setName("pathT");
		pathT.setFont(Style.nums);
		pathT.setVisible(true);
		
		pathC = new JLabel("0");
		pathC.setName("pathC");
		pathC.setFont(Style.nums);
		pathC.setVisible(true);
		
		noPathT = new JLabel("NO PATH");
		noPathT.setName("noPathT");
		noPathT.setForeground(Color.white);
		noPathT.setFont(Style.biggerText);
		npD = noPathT.getPreferredSize();
		
		//add JCheckBoxes
		diagonalCheck = new JCheckBox();
		diagonalCheck.setText("Diagonal ////");
		diagonalCheck.setName("diagonalCheck");
		diagonalCheck.setOpaque(false);
		diagonalCheck.setSelected(true);
		diagonalCheck.setFocusable(false);
		diagonalCheck.setVisible(true);
		
		trigCheck = new JCheckBox();
		trigCheck.setText("Trig");
		trigCheck.setName("trigCheck");
		trigCheck.setOpaque(false);
		trigCheck.setFocusable(false);
		trigCheck.setVisible(true);
		
		// Set up JSlider
		speed = new JSlider();
		speed.setName("speed");
		speed.setOpaque(false);
		speed.setVisible(true);
		speed.setFocusable(false);
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				speed.setValue(source.getValue());
				frame.setSpeed();
				frame.repaint();
			}
		});
		
		// Add JSlider to list
		slider.add(speed);	
		
		//addJLabels to list
		labels.add(modeT);
		labels.add(speedT);
		labels.add(speedC);
		labels.add(openT);
		labels.add(openC);
		labels.add(closedT);
		labels.add(openT);
		labels.add(pathT);
		labels.add(noPathT);
		
		// Add JCheckboxes to list
		checkboxes.add(diagonalCheck);
		checkboxes.add(trigCheck);
		
		// Set up JButtons
		run = new JButton();
		run.setText("Run");
		run.setName("Run");
		run.setFocusable(false);
		run.addActionListener(frame);
		run.setMargin(new Insets(0,0,0,0));
		run.setVisible(true);
		
		clr = new JButton();
		clr.setText("Clear Board");
		clr.setName("ClearBoard");
		clr.setFocusable(true);
		clr.addActionListener(frame);
		run.setMargin(new Insets(0, 0, 0, 0));
		clr.setVisible(true);	
		
		// Add JButtons to list
		buttons.add(run);
		buttons.add(clr);
		
	}	

	//get specific JLabel by name
	public JLabel getL(String s) {
		for(int i = 0; i < labels.size(); i++) {
			if(labels.get(i).getName().equals(s)) return labels.get(i);
		}
		return null;
	}
	
	//get specific JCheckbox by name
	public JCheckBox getC(String s) {
		for(int i = 0; i < checkboxes.size(); i++) {
			if(checkboxes.get(i).getName().equals(s)) return checkboxes.get(i);
		}
		return null;
	}
	
	//get specific JSlider by name
	public JSlider getS(String s) {
		for(int i = 0; i < slider.size(); i++) {
			if(slider.get(i).getName().equals(s)) return slider.get(i);
		}
		return null;
	}
	
	//get specific JButton by name
	public JButton getB(String s) {
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).getName().equals(s)) return buttons.get(i);
		}
		return null;
	}
	
	public void noPathTBounds() {
		noPathT.setBounds((int)((frame.getWidth()/2)), (int)((frame.getHeight()/ 2)-70), (int)npD.getWidth(), (int)npD.getHeight());
	}
	
	public void position() {
		//set label bounds
		//setBounds(x, y, width, height)
		speedT.setBounds(180, frame.getHeight()-90, 60, 20);
		speedC.setBounds(224, frame.getHeight()-90, 60, 20);
		openT.setBounds(254, frame.getHeight()-90, 60, 20);
		openC.setBounds(300, frame.getHeight()-90, 60, 20);
		closedT.setBounds(254, frame.getHeight()-74, 60, 20);
		closedC.setBounds(300, frame.getHeight()-74, 60, 20);
		pathT.setBounds(254, frame.getHeight()-58, 60, 20);
		pathC.setBounds(300, frame.getHeight()-58, 60, 20);
		Dimension size = modeT.getPreferredSize();
		modeT.setBounds(20, frame.getHeight() -45, size.width, size.height);
		
		//set check box bounds
		diagonalCheck.setBounds(20, frame.getHeight()-90, 90, 20);
		trigCheck.setBounds(20, frame.getHeight()-65, 60, 20);
		
		//set slider bounds
		speed.setBounds(175, frame.getHeight()-66, 68, 20);
		
		//set button bound
		run.setBounds(110, frame.getHeight()-90, 62, 22);
		clr.setBounds(70, frame.getHeight()-65, 102, 22);
	}
	
	//set JLabels to lightText
	public void hoverColour() {
		modeT.setForeground(Style.lightText);
		diagonalCheck.setForeground(Style.lightText);
		trigCheck.setForeground(Style.lightText);
		speed.setForeground(Style.lightText);
		speedT.setForeground(Style.lightText);
		speedC.setForeground(Style.lightText);
		openT.setForeground(Style.lightText);
		openC.setForeground(Style.lightText);
		closedT.setForeground(Style.lightText);
		closedC.setForeground(Style.lightText);
		pathT.setForeground(Style.lightText);
		pathC.setForeground(Style.lightText);
	}
	
	//set JLabels to darkText
	public void nonHoverColour() {
		modeT.setForeground(Style.darkText);
		diagonalCheck.setForeground(Style.darkText);
		trigCheck.setForeground(Style.darkText);
		speed.setForeground(Style.darkText);
		speedT.setForeground(Style.darkText);
		speedC.setForeground(Style.darkText);
		openT.setForeground(Style.darkText);
		openC.setForeground(Style.darkText);
		closedC.setForeground(Style.darkText);
		closedT.setForeground(Style.darkText);
		pathT.setForeground(Style.darkText);
		pathC.setForeground(Style.darkText);
	}
	
	public void addAll() {
		frame.add(diagonalCheck);
		frame.add(trigCheck);
		frame.add(run);
		//frame.add(clr); //this button is glitchy, will be fixed in future 
		frame.add(modeT);
		frame.add(openT);
		frame.add(openC);
		frame.add(closedT);
		frame.add(closedC);
		frame.add(pathT);
		frame.add(pathC);
		frame.add(speed);
		frame.add(speedT);
		frame.add(speedC);
	}
}

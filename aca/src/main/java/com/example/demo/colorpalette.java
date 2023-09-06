/** 
 * ColorPalette.java
 *
 * Description:	
 * @author			lijia
 * @version			
 */

package com.example.demo;

import java.awt.*;
import javax.swing.*;

class ColorPalette extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1618113487030721757L;
	private JPanel basePanel = new JPanel();
	private JSlider redSlider = new JSlider();
	private JSlider greenSlider = new JSlider();
	private JSlider blueSlider = new JSlider();
	private JTextField redTextField = new JTextField();
	private JTextField greenTextField = new JTextField();
	private JTextField blueTextField = new JTextField();
	private JComboBox stateComboBox = new JComboBox();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JPanel colorPanel = new JPanel();
// END GENERATED CODE
	private Board board;
	private Color[] myColor;
	private int nstate;
	private int selectedstate;
	
	public ColorPalette(Board br, Color[] col, int ns) {
		this.board = br;
		this.myColor = col;
		this.nstate = ns;
		
		try{
			initComponents();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.stateComboBox.addItem(new String("background"));
		this.stateComboBox.addItem(new String("foreground"));
		String prefix = new String("state ");
		for(int i=1; i<this.nstate; i++){
			this.stateComboBox.addItem(prefix + Integer.toString(i));
		}
		
		this.stateComboBox.addActionListener(new ColorPaletteActionEvent(this,0));
		this.redTextField.addActionListener(new ColorPaletteActionEvent(this,1));
		this.greenTextField.addActionListener(new ColorPaletteActionEvent(this,2));
		this.blueTextField.addActionListener(new ColorPaletteActionEvent(this,3));
		this.redSlider.addChangeListener(new ColorPaletteSliderEvent(this,4));
		this.greenSlider.addChangeListener(new ColorPaletteSliderEvent(this,5));
		this.blueSlider.addChangeListener(new ColorPaletteSliderEvent(this,6));
		setSelectedState(0);
		this.addWindowListener(new CellSpaceClosedEvent(this.board, "color"));
		this.setResizable(false);
					
	}

	private void initComponents() throws Exception {
// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
// every time the form is saved. All manual changes will be overwritten.
// BEGIN GENERATED CODE
		// the following code sets the frame's initial state
		basePanel.setLayout(null);
		basePanel.setSize(new Dimension(180, 160));
		basePanel.setLocation(new Point(0, 0));
		//basePanel.setBackground(Color.lightGray);
		basePanel.setVisible(true);
		redSlider.setSize(new Dimension(90, 20));
		redSlider.setLocation(new Point(38, 52));
		redSlider.setMajorTickSpacing(10);
		//redSlider.setBackground(Color.white);
		//redSlider.setBorder(BorderFactory.createLineBorder(Color.red));
		//redSlider.setValue(100);
		redSlider.setMaximum(255);
		redSlider.setVisible(true);
		greenSlider.setSize(new Dimension(90, 20));
		greenSlider.setLocation(new Point(38, 76));
		greenSlider.setMajorTickSpacing(10);
		//greenSlider.setBackground(Color.white);
		//greenSlider.setBorder(BorderFactory.createLineBorder(Color.green));
		//greenSlider.setValue(100);
		greenSlider.setMaximum(255);
		greenSlider.setVisible(true);
		blueSlider.setSize(new Dimension(90, 20));
		blueSlider.setLocation(new Point(38, 100));
		blueSlider.setMajorTickSpacing(10);
		//blueSlider.setBackground(Color.white);
		//blueSlider.setBorder(BorderFactory.createLineBorder(Color.blue));
		//blueSlider.setValue(100);
		blueSlider.setMaximum(255);
		blueSlider.setVisible(true);
		redTextField.setFont(new Font("Serif", 0, 12));
		redTextField.setSize(new Dimension(24, 20));
		redTextField.setLocation(new Point(140, 52));
		//redTextField.setText("100");
		redTextField.setVisible(true);
		greenTextField.setFont(new Font("Serif", 0, 12));
		greenTextField.setSize(new Dimension(24, 20));
		greenTextField.setLocation(new Point(140, 76));
		//greenTextField.setText("100");
		greenTextField.setVisible(true);
		blueTextField.setFont(new Font("Serif", 0, 12));
		blueTextField.setSize(new Dimension(24, 20));
		blueTextField.setLocation(new Point(140, 100));
		//blueTextField.setText("100");
		blueTextField.setVisible(true);
		stateComboBox.setFont(new Font("Serif", 0, 12));
		stateComboBox.setSize(new Dimension(85, 20));
		stateComboBox.setLocation(new Point(60, 17));
		stateComboBox.setBackground(Color.white);
		//stateComboBox.setBorder(BorderFactory.createLineBorder(Color.black));
		stateComboBox.setVisible(true);
		jLabel1.setFont(new Font("Dialog", 1, 16));
		jLabel1.setSize(new Dimension(18, 20));
		jLabel1.setLocation(new Point(18, 52));
		jLabel1.setText("R");
		//jLabel1.setBorder(BorderFactory.createRaisedBevelBorder());
		jLabel1.setForeground(Color.red);
		jLabel1.setVisible(true);
		jLabel2.setFont(new Font("Dialog", 1, 16));
		jLabel2.setSize(new Dimension(18, 20));
		jLabel2.setLocation(new Point(18, 76));
		jLabel2.setText("G");
		jLabel2.setForeground(Color.green);
		jLabel2.setVisible(true);
		jLabel3.setFont(new Font("Dialog", 1, 16));
		jLabel3.setSize(new Dimension(18, 20));
		jLabel3.setLocation(new Point(18, 100));
		jLabel3.setText("B");
		jLabel3.setForeground(Color.blue);
		jLabel3.setVisible(true);
		colorPanel.setLayout(null);
		colorPanel.setSize(new Dimension(35, 35));
		colorPanel.setLocation(new Point(10, 10));
		colorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		colorPanel.setBackground(new Color(100, 100, 100));
		colorPanel.setVisible(true);
		getContentPane().setLayout(null);
		setLocation(new Point(0, 0));
		this.setTitle("Color Palette");

		basePanel.add(redSlider);
		basePanel.add(greenSlider);
		basePanel.add(blueSlider);
		basePanel.add(redTextField);
		basePanel.add(greenTextField);
		basePanel.add(blueTextField);
		basePanel.add(stateComboBox);
		basePanel.add(jLabel1);
		basePanel.add(jLabel2);
		basePanel.add(jLabel3);
		basePanel.add(colorPanel);
		getContentPane().add(basePanel);

		setSize(new Dimension(180, 160));

		// event handling
		

// END GENERATED CODE
	}
	
	public void setSelectedState(int it){
		
		int ns = this.nstate+1;
		
		if((it>=0) && (it<ns)){
			this.selectedstate = it;
			int redc = this.myColor[it].getRed();
			int greenc = this.myColor[it].getGreen();
			int bluec = this.myColor[it].getBlue();		
			this.colorPanel.setBackground(this.myColor[it]);
			this.redSlider.setValue(redc);
			this.redTextField.setText(Integer.toString(redc));
			this.greenSlider.setValue(greenc);
			this.greenTextField.setText(Integer.toString(greenc));
			this.blueSlider.setValue(bluec);
			this.blueTextField.setText(Integer.toString(bluec));
		}
	}
	
	public void changeSelectedState(int name, int value){
		
		int it = this.selectedstate;
		int redc = this.myColor[it].getRed();
		int greenc = this.myColor[it].getGreen();
		int bluec = this.myColor[it].getBlue();
		
		switch(name){
			case 1: // RedTextField
				redc = value;
				this.redSlider.setValue(value);
				break;
			case 2: //GreenTextField
				greenc = value;
				this.greenSlider.setValue(value);
				break;
			case 3://BlueTExtField
				bluec = value;
				this.blueSlider.setValue(value);
				break;
			case 4://RedSlider
				redc = value;
				this.redTextField.setText(Integer.toString(value));
				break;
			case 5://GreenSlider
				greenc = value;
				this.greenTextField.setText(Integer.toString(value));
				break;
			case 6://RedSlider
				bluec = value;
				this.blueTextField.setText(Integer.toString(value));
				break;
		}
		
		Color newcolor = new Color(redc,greenc,bluec);
		this.colorPanel.setBackground(newcolor);
		this.myColor[it] = newcolor;
		this.board.repaint();	
	
	}
		
		
			
		
		
		
  
  	
}

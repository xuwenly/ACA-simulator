/** 
 * Aca.java
 *
 * Description:	
 * @author			lijia
 * @version			
 */

package com.example.demo;
import javax.swing.*;

import java.util.*;
import java.awt.*;

public class Aca extends JFrame{
		
		private JMenu FileMenu, EditMenu, ModelMenu; // LogMenu;
		private JMenuItem loadrulesitem = new JMenuItem();
		private JMenuItem openitem = new JMenuItem();
		private JMenuItem saveitem = new JMenuItem();
		private JMenuItem imgsaveitem = new JMenuItem();
		private JMenuItem clearitem = new JMenuItem();
		private JMenuItem randomitem = new JMenuItem();
		private JMenuItem exititem = new JMenuItem();
		private JMenuItem coloritem = new JMenuItem();
		private JMenuItem savecoloritem = new JMenuItem();
		private JMenuItem loadcoloritem = new JMenuItem();
		private JMenuItem clipitem = new JMenuItem();
					
		private JButton stepbutton = new JButton();
		private JButton startbutton = new JButton();
		private JButton backbutton = new JButton();
		private JButton forwardbutton = new JButton();
			
		private JRadioButtonMenuItem radiototalistic = new JRadioButtonMenuItem();
		private JRadioButtonMenuItem radiorotsym = new JRadioButtonMenuItem();
		private JRadioButtonMenuItem radioisomo = new JRadioButtonMenuItem();
		private JRadioButtonMenuItem radiounsym = new JRadioButtonMenuItem();
		//	
		private JSlider updatedelayslider = new JSlider();
		private JSlider updaterateslider = new JSlider();
		//
		private JLabel updateratelabel = new JLabel();
		private JLabel updatedelaylabel = new JLabel();
		private JLabel framelabel = new JLabel();
		private JLabel framecounterlabel = new JLabel();
		
		private ImageIcon starticon, stopicon, editicon, stepicon; // backicon;
	
		private Board board;
		//private CellularAutomatonArray cellulararray;
				
		private static final long serialVersionUID = 1L;
	
			
	private void initComponents() throws Exception
	{ 
		JPanel mainpanel = new JPanel();
		JPanel framepanel = new JPanel();
		JPanel updatemodepanel = new JPanel();
		//JLabel jLabel1 = new JLabel();
		JLabel paintdelaynamepanel = new JLabel();
		JLabel updatedelaynamepanel = new JLabel();
		JLabel nictlabel = new JLabel();

		System.out.println("load images...");
		
		Image img0 = ImageLoader.initImage(this, "icon/step.jpg");
		Image img1 = ImageLoader.initImage(this, "icon/start.jpg");
		Image img2 = ImageLoader.initImage(this, "icon/back.jpg");
		Image img3 = ImageLoader.initImage(this, "icon/forward.jpg");
		Image img7 = ImageLoader.initImage(this, "icon/lee.jpg");
		Image img8 = ImageLoader.initImage(this, "icon/stop.jpg");
		Image img9 = ImageLoader.initImage(this, "icon/edit.JPG");
				
      	mainpanel.setBorder(BorderFactory.createEtchedBorder(0,Color.white, new Color(142,142,142)));
		mainpanel.setLayout(null);
		mainpanel.setLocation(new Point(5, 40));
		mainpanel.setSize(new Dimension(267, 150));
		mainpanel.setVisible(true); //mainpanel
		
		framepanel.setLayout(null);
		framepanel.setLocation(new Point(5,191));
		framepanel.setSize(new Dimension(267,20));
		
		updatemodepanel.setBorder(BorderFactory.createTitledBorder(
										BorderFactory.createLineBorder(Color.black,1),
										"UPDATE RATE",
										4,
										2,
										new Font("Serif",0,12),
										Color.blue)	);
		
		updatemodepanel.setFont(new Font("Dialog", 3, 12));
		updatemodepanel.setLayout(null);
		updatemodepanel.setLocation(new Point(10, 10));
		updatemodepanel.setSize(new Dimension(172, 75));
		updatemodepanel.setVisible(true); //updatemodepanel
		
		final JSeparator ss = new JSeparator(JSeparator.VERTICAL);
		ss.setSize(new Dimension(2,130));
		ss.setLocation(new Point(192,10));
		
		radiototalistic.setActionCommand("7"); //totalistic
		radiototalistic.setText("Totalistic");
		radiototalistic.setVisible(true); //totalisticRadioButton
		radiorotsym.setActionCommand("5"); //rotation
		radiorotsym.setText("rotation symmetic"); 
		radiorotsym.setVisible(true); //rotation-symButton
		radioisomo.setActionCommand("6"); //isotropic
		radioisomo.setText("Isotropic");
		radioisomo.setSelected(true);
		radioisomo.setVisible(true);//isomopicButton
		radiounsym.setActionCommand("8"); //asymmetry
		radiounsym.setText("Asymmetric");
		radiounsym.setVisible(true);//AsymmetricButton
					
		backbutton.setActionCommand("3"); //back
		backbutton.setToolTipText("previous");
		backbutton.setIcon(new ImageIcon(img2));
		backbutton.setLocation(new Point(12, 95));
		backbutton.setSize(new Dimension(40,45));
		backbutton.setVisible(true);

		forwardbutton.setActionCommand("4"); //forward
		forwardbutton.setToolTipText("next");
		forwardbutton.setIcon(new ImageIcon(img3));
		forwardbutton.setLocation(new Point(53, 95));
		forwardbutton.setSize(new Dimension(40,45));
		forwardbutton.setVisible(true);
		
		starticon = new ImageIcon(img1);
		stopicon = new ImageIcon(img8);
		editicon = new ImageIcon(img9);
		stepicon = new ImageIcon(img0);
		//backicon = new ImageIcon(img2);
		
		startbutton.setActionCommand("1"); //start
		startbutton.setIcon(starticon);
		startbutton.setLocation(new Point(94, 95));
		startbutton.setSize(new Dimension(45, 45));
		//startbutton.setToolTipText("simulation start or stop");
		startbutton.setVisible(true);//startbutton
		
		stepbutton.setActionCommand("2"); //step
		stepbutton.setIcon(stepicon);
		stepbutton.setLocation(new Point(140, 95));
		stepbutton.setSize(new Dimension(40, 45));
		stepbutton.setToolTipText("step");
		stepbutton.setVisible(true);//stepbutton		
		
		updatedelaynamepanel.setFont(new Font("Serif", 0, 10));
		updatedelaynamepanel.setForeground(Color.blue);
		updatedelaynamepanel.setHorizontalTextPosition(JLabel.LEFT);
		updatedelaynamepanel.setLocation(new Point(200,15));
		updatedelaynamepanel.setSize(new Dimension(60, 12));
		updatedelaynamepanel.setText("update delay");
		updatedelaynamepanel.setVisible(true);//jlabel5
		
		updatedelayslider.setInverted(true);
		//updatedelayslider.setToolTipText("Update delay");
		updatedelayslider.setLocation(new Point(203,30));
		updatedelayslider.setMajorTickSpacing(100);
		updatedelayslider.setMinorTickSpacing(50);
		updatedelayslider.setPaintTicks(true);
		updatedelayslider.setMaximum(400);
		updatedelayslider.setMinimum(0);
		updatedelayslider.setOrientation(JSlider.VERTICAL);
		updatedelayslider.setSize(new Dimension(30,100));
		updatedelayslider.setValue(20);
		updatedelayslider.setVisible(true); //updatedelayslider
				
		updatedelaylabel.setBorder(BorderFactory.createLoweredBevelBorder());
		updatedelaylabel.setFont(new Font("Serif", 0, 10));
		//updatedelaylabel.setBackground(Color.white);
		updatedelaylabel.setForeground(Color.blue);
		updatedelaylabel.setHorizontalAlignment(JLabel.CENTER);
		updatedelaylabel.setHorizontalTextPosition(JLabel.CENTER);
		updatedelaylabel.setLocation(new Point(235, 33));
		updatedelaylabel.setSize(new Dimension(20, 15));
		updatedelaylabel.setText("20");
		updatedelaylabel.setVisible(true);//updateratelabel
				
		Hashtable<Integer, JLabel> labeltable = new Hashtable<Integer, JLabel>();
		JLabel ratelabel1 = new JLabel("0");
		ratelabel1.setFont(new Font("serif", 0, 10));
		JLabel ratelabel2 = new JLabel("0.5");
		ratelabel2.setFont(new Font("serif", 0, 10));
		JLabel ratelabel3 = new JLabel("1");
		ratelabel3.setFont(new Font("serif", 0, 10));
		labeltable.put(0, ratelabel1);
		labeltable.put(50, ratelabel2);
		labeltable.put(100, ratelabel3);
		updaterateslider.setLabelTable(labeltable);
		
		updaterateslider.setLocation(new Point(10, 25));
		updaterateslider.setMajorTickSpacing(50);
		updaterateslider.setMinorTickSpacing(25);
		updaterateslider.setPaintTicks(true);
		updaterateslider.setPaintLabels(true);
		updaterateslider.setMaximum(100);
		updaterateslider.setMinimum(0);
		updaterateslider.setSize(new Dimension(100, 42));
		updaterateslider.setValue(50);
		updaterateslider.setVisible(true);//updaterateslider
		
		updateratelabel.setBorder(BorderFactory.createLoweredBevelBorder());
		updateratelabel.setFont(new Font("Serif", 0, 12));
		updateratelabel.setForeground(Color.blue);
		updateratelabel.setHorizontalAlignment(JLabel.CENTER);
		updateratelabel.setVerticalAlignment(JLabel.CENTER);
		updateratelabel.setHorizontalTextPosition(JLabel.CENTER);
		updateratelabel.setLocation(new Point(115, 22));
		updateratelabel.setSize(new Dimension(40, 20));
		updateratelabel.setText("0.5");
		updateratelabel.setVisible(true);//updateratelabel
		
		framelabel.setSize(80,18);
		framelabel.setLocation(0,0);
		framelabel.setHorizontalAlignment(JLabel.RIGHT);
		framelabel.setVerticalAlignment(JLabel.CENTER);
		framelabel.setFont(new Font("Dialog",0,12));
		framelabel.setForeground(Color.red);
		//framelabel.setText("frame count: ");
		framecounterlabel.setSize(50,18);
		framecounterlabel.setLocation(81,0);
		framecounterlabel.setHorizontalAlignment(JLabel.LEFT);
		framecounterlabel.setVerticalAlignment(JLabel.CENTER);
		framecounterlabel.setFont(new Font("Dialog",0,12));
		framecounterlabel.setForeground(Color.red);
		//framecounterlabel.setText("0");
		
		openitem.setActionCommand("open");				
		openitem.setText("Open configuration");
		//openitem.setToolTipText("Load configuration");
		openitem.setVisible(true);// openbutton
		
		loadrulesitem.setActionCommand("loadrule");
		loadrulesitem.setText("Load rules");
		loadrulesitem.setVisible(true); //loadRulesButton
		
		saveitem.setActionCommand("save");
		saveitem.setText("Save configuration");
		saveitem.setToolTipText("Save configuration to text file");
		saveitem.setVisible(true);//savebutton
		
		imgsaveitem.setActionCommand("imgsave");
		imgsaveitem.setText("Save as image");
		imgsaveitem.setToolTipText("Save configuration as image");
		imgsaveitem.setVisible(true);//savebutton
		
		clearitem.setActionCommand("9"); //clear
		clearitem.setText("Clear");
		clearitem.setToolTipText("Clear configuration");
		clearitem.setVisible(true);//clartbutton
		
		randomitem.setActionCommand("10"); //random
		randomitem.setText("Randomize");
		randomitem.setToolTipText("Generate a random configuration");
		randomitem.setVisible(true);//randombutton
		
		coloritem.setActionCommand("color");
		coloritem.setText("Open color palette");
		//coloritem.setToolTipText("open color palette");
		coloritem.setVisible(true);//colorbutton
		
		savecoloritem.setActionCommand("savecolor");
		savecoloritem.setText("Save color palette");
		savecoloritem.setVisible(true);
		
		loadcoloritem.setActionCommand("loadcolor");
		loadcoloritem.setText("Load colors from file");
		loadcoloritem.setVisible(true);
				
		exititem.setActionCommand("11"); //exit
		exititem.setText("Exit");
		exititem.setToolTipText("exit simulation");
		exititem.setVisible(true);//exitbutton
		
		clipitem.setActionCommand("clip");
		clipitem.setText("Start saving frames");
		clipitem.setVisible(true);//clipbutton
								
		FileMenu = new JMenu("FILE");
		FileMenu.add(this.openitem);
		FileMenu.add(loadrulesitem);
		FileMenu.addSeparator();
		FileMenu.add(saveitem);
		FileMenu.add(imgsaveitem);
		FileMenu.addSeparator();
		FileMenu.add(clipitem);
		FileMenu.addSeparator();
		FileMenu.add(exititem);
		
		EditMenu = new JMenu("EDIT");
		EditMenu.addSeparator();
		EditMenu.add(clearitem);
		EditMenu.add(randomitem);
		EditMenu.addSeparator();
		EditMenu.add(loadcoloritem);
		EditMenu.add(savecoloritem);
		EditMenu.addSeparator();
		EditMenu.add(coloritem);
		
		
		ModelMenu = new JMenu("MODEL");
		ButtonGroup group = new ButtonGroup();
		
		group.add(this.radiorotsym);
		//this.radiorotsym.setSelected(true);
		ModelMenu.add(radiorotsym);
		group.add(this.radioisomo);
		ModelMenu.add(radioisomo);
		group.add(radiototalistic);
		ModelMenu.add(radiototalistic);
		group.add(radiounsym);
		ModelMenu.addSeparator();
		ModelMenu.add(radiounsym);
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(FileMenu);
		menubar.add(EditMenu);
		menubar.add(ModelMenu);
		//menubar.add(LogMenu);
		menubar.setSize(new Dimension(160,35));
		menubar.setLocation(new Point(5,0));
		
		mainpanel.add(updatedelayslider);
		mainpanel.add(backbutton);
		mainpanel.add(forwardbutton);
		mainpanel.add(startbutton);
		mainpanel.add(stepbutton);
		mainpanel.add(updatemodepanel);
		mainpanel.add(ss);
		mainpanel.add(paintdelaynamepanel);
		mainpanel.add(updatedelaynamepanel);
		mainpanel.add(updatedelaylabel);
				
		framepanel.add(framelabel);
		framepanel.add(framecounterlabel);
				
		updatemodepanel.add(updaterateslider);
		updatemodepanel.add(updateratelabel);
		
		nictlabel.setIcon(new ImageIcon(img7));			
		nictlabel.setLocation(new Point(204, 0));
		nictlabel.setSize(new Dimension(103, 35));
		nictlabel.setVisible(true);//jlabel7
		
		this.getContentPane().setLayout(null);
		this.setLocation(new Point(700,0));
		this.setTitle("Controller");
		this.getContentPane().add(menubar);		
		this.getContentPane().add(nictlabel);
		this.getContentPane().add(mainpanel);
		this.getContentPane().add(framepanel);		
		this.setSize(new Dimension(285, 240));
		

		// event handling
		
// END GENERATED CODE
	}
	
	private void setEventHandler(){
		
		CellSpaceActionEvent ca = new CellSpaceActionEvent(this.board);
		CellSpaceFileEvent cf = new CellSpaceFileEvent(this, this.board);
		
		this.startbutton.addActionListener(ca); //1-(start) 0-(end)
		this.stepbutton.addActionListener(ca); // 2
		this.backbutton.addActionListener(ca); //3
		this.forwardbutton.addActionListener(ca); //4
		this.radiorotsym.addActionListener(ca);  //5
		this.radioisomo.addActionListener(ca);   //6
		this.radiototalistic.addActionListener(ca); //7
		this.radiounsym.addActionListener(ca); //8
		this.clearitem.addActionListener(ca); //9
		this.randomitem.addActionListener(ca); //10
		this.exititem.addActionListener(ca); //11
							
		this.openitem.addActionListener(cf);
		this.saveitem.addActionListener(cf);
		this.clipitem.addActionListener(cf);
		this.imgsaveitem.addActionListener(cf);
		this.loadrulesitem.addActionListener(cf);
		this.coloritem.addActionListener(cf);
		this.savecoloritem.addActionListener(cf);
		this.loadcoloritem.addActionListener(cf);
			
		this.updaterateslider.addChangeListener(new CellSpaceSliderEvent(board, updateratelabel, "updaterate"));
		this.updatedelayslider.addChangeListener(new CellSpaceSliderEvent(board, updatedelaylabel, "updatedelay"));
		this.addWindowListener(new CellSpaceClosedEvent(board, "aca"));
	}
	
	public Aca(CParameter parameter) {
		super("Controller");
		
		try{
			initComponents();
		}
		catch(Exception e)
		{
			System.err.println("Program suspended with error " + e.getMessage());
			System.exit(1);
		}
	
		try{
			this.board = new Board(this, parameter);
		}
		catch(Exception e)
		{
			ACAConstructor.ErrorMsg("failed to open simulator window!!");
			System.exit(1);
		}
		
		setEventHandler();
		enableButtonsVisible(0);
		setEditButtons(0);
				
		this.setResizable(false);
		this.setVisible(true);
	
	}
	
	public void setRateItems(int rate, boolean allow_totalistic)
	{
		this.updaterateslider.setValue(rate);
		if(rate==0)
		{
			this.updateratelabel.setText("async.");
			this.updaterateslider.setEnabled(false);
			
		}
		this.radiototalistic.setVisible(allow_totalistic);
				
	}
	
	public void enableButtonsVisible(int ss){// ss= 0..true, 1.. false, 2..false+ is_tracing
		
		boolean state = (ss==0? true : false);
		
		this.FileMenu.setEnabled(state);
		this.EditMenu.setEnabled(state);
		this.ModelMenu.setEnabled(state);
		this.openitem.setEnabled(state);
		this.saveitem.setEnabled(state);
		this.imgsaveitem.setEnabled(state);
		this.clearitem.setEnabled(state);
		this.randomitem.setEnabled(state);
		this.coloritem.setEnabled(state);
		this.savecoloritem.setEnabled(state);
		this.loadcoloritem.setEnabled(state);
		this.loadrulesitem.setEnabled(state);
		this.clipitem.setEnabled(state);
						
		if( ss == 0){
			this.startbutton.setIcon(this.starticon);
			this.startbutton.setActionCommand("1");
			this.startbutton.setToolTipText("run");
			this.stepbutton.setActionCommand("2"); //step
			this.stepbutton.setIcon(stepicon);
			this.stepbutton.setToolTipText("step");
			this.stepbutton.setEnabled(state);
		}else if(ss==1){
			this.startbutton.setIcon(this.stopicon);
			this.startbutton.setActionCommand("0"); //0
			this.startbutton.setToolTipText("stop");
			this.stepbutton.setEnabled(state);
		}else{
			this.startbutton.setIcon(this.stopicon);
			this.startbutton.setActionCommand("13"); //0
			this.startbutton.setToolTipText("cancel");
			this.stepbutton.setIcon(this.editicon);
			this.stepbutton.setActionCommand("12"); //12
			this.stepbutton.setToolTipText("undo");
		}
		
	}
	
	public void setEditButtons(int state){
		
		boolean b, f;
		
		switch(state){
		
			case 1: b = true;
					f = false;
					break;
					//
			case 2: b = true;
					f = true;
					break;
					//
			case 3: b = false;
					f = true;
					break;
					//
			case 4: b = true;
					f = true;
					break;
					//
			default: b = false;
					 f = false;
		}
		
		this.backbutton.setEnabled(b);
		this.forwardbutton.setEnabled(f);
				
	}
		
	
	public void setFrameCounter(int admit,int count){
		
		switch(admit){
			case 0: this.clipitem.setText("Stop saving frames");
					this.clipitem.setActionCommand("unclip");
					//
			case 1: this.framelabel.setText("frame count: ");
			        this.framecounterlabel.setText(Integer.toString(count));
			        break;
			default:
					this.clipitem.setActionCommand("clip");
					this.clipitem.setText("Start saving frames");
			        this.framelabel.setText("");
			        this.framecounterlabel.setText("");
			        break;
		}
	}

	
	// Main entry point
	public	static  void main(String[] args) {
		
		
		int argslength = args.length;
		int nstate = -1;
		String model = new String("none");
		int gsize = -1;
		Dimension space = null;
		int buffer = -1;
		int index = 0;
		//迭代次数。
		int cTimes = 10;
		String ss = "none";
		try{
			while(index < argslength){
				String argument = String.valueOf(args[index]);
				
				if(argument.equals("-m")){
					index++;
					if(index < argslength) model = (String)args[index];
				}else if(argument.equals("-s")){
					index++;
					if(index < argslength) nstate = Integer.valueOf(args[index]).intValue();
				}else if(argument.equals("-d")){
					int nx=0, ny=0;
					index++;
					if(index < argslength) nx = Integer.valueOf(args[index]).intValue();
					index++;
					if(index < argslength){
						ny = Integer.valueOf(args[index]).intValue();
						space = new Dimension(nx,ny);
					}
				}else if(argument.equals("-g")){
					index++;
					if(index < argslength) gsize = Integer.valueOf(args[index]).intValue();
				}else if(argument.equals("-b")){
					index++;
					if(index < argslength) buffer = Integer.valueOf(args[index]).intValue();
				}else if(argument.equals("-t")){
					index++;
					if(index < argslength) cTimes = Integer.valueOf(args[index]).intValue();
				}else if(argument.equals("-ss")){
					index++;
					if(index < argslength) ss = args[index];
				}else{
					index++;
				}

			}
		}catch (NumberFormatException e){
			ACAConstructor.ErrorMsg("invalid arguments !!");
			return;
		}
		
		if(model.equals("none")){
			System.out.println("Default model is vnca");
			model = new String("vnca");
		}
		if(space == null || space.width < 1 || space.height < 1){
			System.out.println("Space dimension is 30x30 (default)");
			space = new Dimension(30,30);
		}
		if(gsize < 1){
			System.out.println("Grid size is 8 (default)");
			gsize = 8;
		}
		if(buffer < 10){
			System.out.println("Clipboard capacity is 20 (default)");
			 buffer = 20;
		}
		if(nstate <2){
			System.out.println("The least number of states is 4 (default)");
			nstate = 5;
		}
		if(ss.equals("none")){
			ss = "2";
		}
		new Aca(new CParameter(model, nstate, space, gsize, buffer, ss, cTimes));
			
	}
	
	
	
	
}

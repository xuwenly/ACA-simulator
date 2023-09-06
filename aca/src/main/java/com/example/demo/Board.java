/** 
 * Board.java
 *
 * Description:	
 * @author			lijia
 * @version			
 */

package com.example.demo;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.*;

public class Board extends JPanel{
 	 
 	/**
	 * 
	 */
	private static final long serialVersionUID = 3986352490420157999L;
	protected Aca aca;
 	protected CellularAutomatonArray cellulararray;
 	protected EditPanel editPanel;
 	protected ColorPalette colorpalette;
 	protected JFrame frame;
 	protected Dimension SimulatorWindow;
 	protected Color myColor[]; 	
		
	protected Insets panelinsets;
	protected AcaRunThread runthread;
	protected int updatedelay = 20;
	protected volatile boolean Simulator_is_Running = false;
	protected volatile boolean Simulator_is_Tracing = false;
	protected int symmetry = 2;
	protected int framecount = 0;
	protected int tracestepcount = 0;
	protected boolean Frame_is_Clipping = false;
	protected String[] framestring;
	protected CellSpaceMouseEvent mo;
	protected CellSpaceResizeEvent ro;

	 	
 	//public Board(Aca a, CellularAutomatonArray c) throws Exception
	public Board(Aca a, CParameter parameter) throws Exception
 	{    // Constructor
 		
 		this.aca = a;
 		this.cellulararray = ACAConstructor.Create(this.aca, this, parameter);
 		this.SimulatorWindow = this.cellulararray.getPreferredCellSpaceDimension();
 		initColors(); 		
 		
 		try{
 			initComponents();
 		}
 		catch(Exception e)
 		{
 			throw new Exception("failed to create Board");
 		}
 	 		
 		this.cellulararray.Trivialoutput();
 	
  	}
	
	private void initComponents() throws Exception
	{	 		
 		this.frame = new JFrame("Simulation Window");
 		frame.getContentPane().setLayout(new BorderLayout());
 		frame.getContentPane().add(this);
 		this.setPreferredSize(this.SimulatorWindow);
 		this.setLayout(new FlowLayout(FlowLayout.CENTER));
 		this.setFocusable(true);
 		 
 		panelinsets = this.getInsets(); 		
 		this.mo = new CellSpaceMouseEvent(this);
 		this.ro = new CellSpaceResizeEvent(this);
 		enableMouseListener(true);
		
		this.addComponentListener(ro);
		frame.addWindowListener(new CellSpaceClosedEvent(this, "board"));
 		frame.pack();
 		frame.setLocation(100,250);
 		frame.setVisible(true); 
 		
		//frame.getLayeredPane().add(editPanel);
 		this.colorpalette = null;
 		this.editPanel = new EditPanel(this);
 		this.editPanel.setFocusable(true);
 		this.editPanel.setVisible(false);
 		
	}
	
	private void enableMouseListener(boolean admit)
	{
		if(admit)
		{
			this.addMouseListener(mo);
			this.addMouseMotionListener(mo);
		}
		else
		{
			this.removeMouseListener(mo);
			this.removeMouseMotionListener(mo);
		}
	}
 	  	
  	public synchronized void setBoardParameter(String paraname, int value){
  		
  		if(paraname == "updaterate"){
  			this.cellulararray.SetUpdateRate(value);
  		}else if(paraname == "updatedelay"){
  			this.updatedelay = value;
  			if(this.runthread != null) this.runthread.setUpdateDelay(value);
  		}
  	}
  	
	public synchronized void ResizeBoard(Dimension d){
		
		int nxsize = (int)d.getWidth()-panelinsets.left-panelinsets.right;
		int nysize = (int)d.getHeight()-panelinsets.top-panelinsets.bottom;
		Dimension nwindow = new Dimension(nxsize, nysize);
		this.SimulatorWindow = nwindow;					
		//new method 
		if(this.Simulator_is_Running){
			startIterateCASimulator(false);
		}
		
		enableMouseListener(false);
		if(this.cellulararray.ResizeCellularArray(nwindow)){
			if(this.Simulator_is_Tracing){
				KeyEvent(13);
			}else repaint();
			
		}
		enableMouseListener(true);
		//repaint();
  		startIterateCASimulator(this.Simulator_is_Running);	
  				
	}
	
	public synchronized void MouseEvent(int x, int y, int action, int button){
	
	//(action) click: 0, press: 1, release: 2, drag: 3, fail: 4
	//(button) left: 0, right: 1, middle: 2						
	
		int xs = x-panelinsets.left;
		int ys = y-panelinsets.left;
		this.cellulararray.MouseEventFromBoard(xs,ys,action,button);
			
		if(!this.Simulator_is_Running) repaint();
		
	}
	
	public synchronized void editPanelEvent(String action){
		if(this.Simulator_is_Tracing) return;
		this.cellulararray.clipBoardEvent(Integer.parseInt(action));
		
		if(!this.Simulator_is_Running) repaint();
		
	}
	
	public int getTrackCount()
	{
		return this.tracestepcount;
	}
	
	public synchronized void KeyEvent(int action){
		//0..end, 1..start, 2..step, 3..back, 4..forward, 5..rotational
		//6..isotropic, 7..totalistic, 8..asymmetric, 9..clear, 10..random, 11..exit
		if(this.Simulator_is_Tracing){
			switch(action){
				case 3:
					this.cellulararray.traceCellSpaceConfiguration(1, false);
					tracestepcount--;
					break;
				case 4:
					this.cellulararray.traceCellSpaceConfiguration(2, false);
					tracestepcount++;
					break;
				default:
					this.cellulararray.traceCellSpaceConfiguration((action>12? 4 : 3), true);					
					this.Simulator_is_Tracing = false;
					this.aca.enableButtonsVisible(0);
					tracestepcount = 0;					
					this.frame.setResizable(true);
					enableMouseListener(true);
					this.addComponentListener(ro);
				}
			if(!this.Simulator_is_Running) repaint();
			return;
		}
		
		switch(action){
			case 1:
				System.out.println("strart!!");
				this.Simulator_is_Running = true;
				this.aca.enableButtonsVisible(1);
				this.editPanel.enableButtonsVisible(false);
				//this.cellulararray.setRunningStatus(true);
				startIterateCASimulator(true);
				this.cellulararray.initStatisticVar();
				break;
			case 0:
				System.out.println("stop!!!");
				this.Simulator_is_Running = false;
				startIterateCASimulator(false);
				this.aca.enableButtonsVisible(0);
				this.editPanel.enableButtonsVisible(true);
				break;
			case 2:
				if(!this.Simulator_is_Running){
					this.cellulararray.cellStateTransitions(this.symmetry);
				}
				break;
			case 5:
				this.symmetry = 1;
				break;
			case 6:
				this.symmetry = 2;
				break;
			case 7:
				this.symmetry = 0;
				break;
			case 8:
				this.symmetry = -1;
				break;
			case 11:
				startIterateCASimulator(false);
				System.exit(0);
				break;
			case 3:
				if(!this.Simulator_is_Running){
					this.Simulator_is_Tracing = true;
					enableMouseListener(false);
					this.removeComponentListener(ro);
					this.frame.setResizable(false);
					this.aca.enableButtonsVisible(2);
					this.cellulararray.clipBoardEvent(12);
					this.cellulararray.traceCellSpaceConfiguration(0, false);
					this.tracestepcount--;
				}
				break;
			case 9:
				if(!this.Simulator_is_Running) this.cellulararray.setRandom(false);
				this.cellulararray.stateCount.clear();
				break;
			case 10:
				if(!this.Simulator_is_Running) this.cellulararray.setRandom(true);
				break;
		}
		
		if(!this.Simulator_is_Running) repaint();
		
	}

	public synchronized void handleBoardConfigurations(String action, File file){
	
		if(action.equals("open")){
			this.cellulararray.setConfigurationFromBoard(loadFile(file));
			this.cellulararray.file = file;
			repaint();
		}else if(action.equals("save")){
			saveFile(file, this.cellulararray.printConfigurationFromBoard());
		}else if(action.equals("imgsave")){
			saveImageToFile(file);
		}else if(action.equals("loadrule")){
			this.cellulararray.setTransitionRules(loadFile(file), this.symmetry);
		}else if(action.equals("color")){
			cellStatesColorSetting(false);
		}else if(action.equals("cpcolor")){
			cellStatesColorSetting(true);			
		}else if(action.equals("clip")){
			this.aca.setFrameCounter(0,0);
			enableSaveFrames(true,file);
			//this.Frame_is_Clipping = true;
		}else if(action.equals("unclip")){
			enableSaveFrames(false,null);
			this.aca.setFrameCounter(2,0);
		}else if(action.equals("loadcolor")){
			loadColors(loadFile(file));
			if(!this.Simulator_is_Running) repaint();
		}else if(action.equals("savecolor")){
			saveFile(file, saveColors());
		}

	}
	
	public synchronized void setAcaEditButtons(int state){
		this.aca.setEditButtons(state);
	}
	
	private void saveImageToFile(File file)
	{
		Dimension dwindow = this.SimulatorWindow;
		String filename = file.getName();
		int dot = filename.lastIndexOf('.');
		String suffix = filename.substring(dot+1);
		
		BufferedImage image = new BufferedImage(dwindow.width,dwindow.height,BufferedImage.TYPE_INT_RGB);
		
		this.cellulararray.createCellSpaceImage(image.getGraphics(),myColor,false);
	  	
		try{
	  		ImageIO.write(image,suffix,file);
	  	}catch(IOException e){
			System.err.println("IO error: failed to save the configuration");
		}
	}
	
	private void enableSaveFrames(boolean admission, File file){
		
		if(!admission){
			this.Frame_is_Clipping = false;
			return;
		}
					
		this.framestring = new String [2];
		String path = new String(file.getParent());
		String filename = (String)file.getName();
		int dot = filename.lastIndexOf('.');
		
		this.framestring[0] = new String(path+'\\'+filename.substring(0,dot));//prefix
		this.framestring[1] = filename.substring(dot+1); //suffix
		
		this.framecount = 0;
		this.Frame_is_Clipping = true;
	}
	
	private void saveFrame(Image image){
		
		StringBuffer fname = new StringBuffer(100);
		fname.append(this.framestring[0]);
		String index = Integer.toString(this.framecount);
		for(int j=index.length(); j<4; j++){
			fname.append('0');
		}
		fname.append(index);
		fname.append('.');
		fname.append(this.framestring[1]);
		File file = new File(new String(fname));
		
		try{
  	  		ImageIO.write((BufferedImage)image,this.framestring[1],file);
  	  	}catch(IOException e){
  	  		ErrorMsg("File IO error");
		}
		this.aca.setFrameCounter(1,this.framecount);
		this.framecount++;
	}
	
	
	private void cellStatesColorSetting(boolean close){
		
		if(close){
			this.colorpalette.dispose();
			this.colorpalette = null;
			//System.out.println("The color palette is closed");
		 	return;
		 }
			
		if(this.colorpalette != null) return;
		
		int ns = this.cellulararray.getNumberofCellStates();
		this.colorpalette = new ColorPalette(this,myColor,ns);
		Point acp = this.aca.getLocation();
		Dimension acd = this.aca.getSize();
		this.colorpalette.setLocation(new Point(acp.x+acd.width+10,acp.y+10));
		this.colorpalette.setVisible(true);
	}
	
	
	private VectorString loadFile(File file){
		VectorString buf = new VectorString(200);
		StringBuffer tmp = new StringBuffer(100);
		FileInputStream fis;
		int b;
		
		try{
			fis = new FileInputStream(file);
			
			try{  
				while((b=fis.read()) != -1){
					if((char)b != '\n'){
						if((('0' <= b) && (b <='9'))  || 
						   (('A' <= b) && (b <= 'Z')) || 
						                     (b==':') || 
						                     (b=='#') || 
						                     (b == ','))
							tmp.append((char)b);
					}else{
						if(tmp.charAt(0) != '#' && tmp.length() > 1){
							buf.add(tmp.toString());
						}
						tmp = new StringBuffer(100);
					}
				}
			}catch(IOException e){
				ErrorMsg("File IO error");
			}finally{
				fis.close();
			}
		}catch(FileNotFoundException e){
			ErrorMsg("File: " + file.getName()+ " not found!");
		}catch(IOException e){
			ErrorMsg("Failed to close file");
		}
		return buf;
	}
	
	private void ErrorMsg(final String msg)
	{
		System.err.println("Error : " + msg);
		//System.exit(code);		
	}
	
	private boolean saveFile(File file, VectorString confvec){
		int size = confvec.size();
		FileOutputStream fos;
		try{
			fos = new FileOutputStream(file);
			try{
				for(int i=0; i<size; i++){
					String tmp = confvec.at(i);
					int len = tmp.length();
					byte b[] = new byte[len];
					for(int k=0; k<len; k++){
						b[k] = (byte)tmp.charAt(k);
					}
					fos.write(b);
					fos.write('\n');
				}
			}catch(IOException e){
				ErrorMsg("IO error");
			}finally{
				fos.close();
			}
		}catch(IOException e){
			ErrorMsg("failed to save file");
		}
		return true;
	}

	public void setEditPanelInvisible(boolean permission){
		if(permission){
			this.editPanel.setLocation(10,10);
		}
		this.editPanel.setVisible(permission);
		if(permission){
			this.editPanel.requestFocus();
		}else{
			this.requestFocus();
		}
	}
	
	public boolean isRunning()
	{
		return this.Simulator_is_Running;
	}
		
	public void update(Graphics g){
		paintComponent(g);
	}
  	
  	public void paintComponent(Graphics g){
  	  	//super.paintComponent(g);
  	  	Dimension dwindow = this.SimulatorWindow;
  	  	Image image = createImage(dwindow.width, dwindow.height);
  	  	
  	  	this.cellulararray.createCellSpaceImage(image.getGraphics(), myColor, this.Simulator_is_Tracing);
  	  	super.paintComponent(g);
  		g.drawImage(image,panelinsets.left,panelinsets.top,this);
  		if(this.Frame_is_Clipping){
  			saveFrame(image);
  		}
  		g.dispose();
  		
  	}
  	  	
   	private void startIterateCASimulator(boolean admission){
  		if(admission){
  			if(runthread == null){
  				//this.cellulararray.setConfigureAsInitial();
  				runthread = new AcaRunThread(this, this.cellulararray, this.updatedelay, this.symmetry);
  				runthread.start();
  			}
  			
  		}else{
  			if(runthread != null){
				 runthread.stopThread();
				 runthread.interrupt();
//				 while(runthread.isAlive()){
//					try{
//						Thread.sleep(1000);
//						System.out.println("sleep");
//					}
//					catch (InterruptedException e){ }
//				}
			}
			runthread =  null;
		}
	}
   	
   	
   	
	private void initColors()
 	{
		
		this.myColor = new Color[21];
		this.myColor[0] = new Color(255,255,255);
		this.myColor[1] = new Color(160,160,160);
 		this.myColor[2] = new Color(0,0,0);
 		this.myColor[3] = Color.blue;
 		this.myColor[14] = Color.yellow;
 		this.myColor[4] = Color.red;
 		this.myColor[9] = new Color(28,168,28);
 		this.myColor[6] = Color.pink;
 		this.myColor[13] = new Color(255,0,255);
 		this.myColor[11] = new Color(255,127,0);
 		this.myColor[10] = Color.gray;
 		this.myColor[7] = Color.magenta;
 		this.myColor[12] = new Color(0,255,255);
 		this.myColor[8] = new Color(24,132,62);
 		this.myColor[5] = Color.lightGray;
 		this.myColor[15] = new Color(191,10,15);
 		this.myColor[16] = new Color(191,232,15);
 		this.myColor[17] = new Color(20,151,102);
 		this.myColor[18] = new Color(122,17,131);
		this.myColor[19] = new Color(232,217,131);
		this.myColor[20] = new Color(92,104,174);		
 	
 	}
	
	private void loadColors(VectorString colors)
	{
		if(colors != null && colors.size() > 0)
		{
			this.myColor = new Color[colors.size()];
			int j=0;
			
			for(int i=0; i<colors.size(); i++){
				
				String str = colors.at(i);
				
				int dot1 = str.indexOf(',');
				if(dot1<0) continue;
				int dot2 = str.indexOf(',', dot1+1);
				if(dot2<0 || dot2>=str.length()-1) continue;
				int r = Integer.parseInt(str.substring(0,dot1));
				int g = Integer.parseInt(str.substring(dot1+1, dot2));
				int b = Integer.parseInt(str.substring(dot2+1));
				
				if(r<0 || r>255 || g<0 || g>255 || b<0 || b>255) continue;
				
				myColor[j++] = new Color(r,g,b);
			}						
		}	
	}
	
	
	private VectorString saveColors()
	{
		int len = this.myColor.length;
		VectorString colors = new VectorString(len);
		
		for(int i=0; i<len; i++)
		{
			colors.add(Integer.toString(myColor[i].getRed())+','+
					   Integer.toString(myColor[i].getGreen())+','+
					   Integer.toString(myColor[i].getBlue()));
			
		}
		
		return colors;	
		
	}
	
  	
  
  	
 
};
  

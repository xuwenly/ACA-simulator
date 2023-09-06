package com.example.demo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.*;

class CellSpaceMouseEvent implements MouseListener, MouseMotionListener{
	private Board board;
	//private String button, action;
	private int button, action;
	//(action) click: 0, press: 1, release: 2, drag: 3, fail: 4
	//(button) left: 0, right: 1, middle: 2
	
	public CellSpaceMouseEvent(Board board){
		this.board = board;
	}
	
	private int whichButton(MouseEvent e){
		if(e.isMetaDown()){
			return(1); //right
		}else if(e.isAltDown()){
			return(2); // middle
		}else return(0); //left
	}
	
	public void mouseClicked(MouseEvent e){
		action = 0;  //click
		button = whichButton(e);
		if(!e.isShiftDown()){
			board.MouseEvent(e.getX(), e.getY(), action, button);
		}
	}
	
	public void mousePressed(MouseEvent e){
		action = 1; //press
	  	button = whichButton(e);
	  	if(e.isShiftDown()){
	  		board.MouseEvent(e.getX(), e.getY(), action, button);
	  	}
	}

	public void mouseReleased(MouseEvent e){
	 	 action = 2; //release
	 	 button = whichButton(e);
	 	 if(e.isShiftDown()){
	 	 	board.MouseEvent(e.getX(), e.getY(), action, button);
	 	 }else{
	 	 	board.MouseEvent(0, 0, 4, button);
	 	 }
	}

	public void mouseDragged(MouseEvent e){
		 action = 3; //drag
	  	 button = whichButton(e);
	  	 
	  	 if(e.isShiftDown()){
	  	 	board.MouseEvent(e.getX(),e.getY(),action,button);
	  	 }else{
	  	 	board.MouseEvent(0, 0, 4, button);
	  	 }
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}

	public void mouseMoved(MouseEvent e){
	}
} /*end of mouse*/

/*
class CellSpaceKeyboardEvent implements KeyListener{
	private Board board;
		
	public CellSpaceKeyboardEvent(Board board){
		this.board = board;
	}
	
	public void keyPressed(KeyEvent e){
		int keycode = e.getKeyCode();
		//System.out.println(keycode);
		
		switch(keycode){
			case KeyEvent.VK_UP: board.KeyEvent("up"); //UP Key
                                          break;
                                     //
           		case KeyEvent.VK_DOWN: board.KeyEvent("down"); //DOWN key
                        		  break;
                                       //
             		case KeyEvent.VK_LEFT: board.KeyEvent("left");  //LEFT Key
                        		  break;
                                       //
            		case KeyEvent.VK_RIGHT: board.KeyEvent("right");  //RIGHT Key
                        		  break;
                                       //
             		//case 155:                 
           		case KeyEvent.VK_R : board.KeyEvent("rotation"); //0(Ins) Ley
                        		  break;
                                     //
             		case KeyEvent.VK_F:
           		case KeyEvent.VK_ENTER:   board.KeyEvent("reflection"); //ENETR Key
                                        break;
                       // case KeyEvent.VK_S: 	board.KeyEvent("start"); //S Key
                        //		break;
                       // case KeyEvent.VK_END: 	board.KeyEvent("end"); //End Key
                        //		break;
                        case KeyEvent.VK_I:	board.KeyEvent("zoomin"); //I key
                        		break;
                        case KeyEvent.VK_O: 	board.KeyEvent("zoomout"); //O key
                        		break;
                        case KeyEvent.VK_Q: 	board.KeyEvent("quit"); //Q Key
                        		break;
                        case KeyEvent.VK_DELETE: board.KeyEvent("undo"); //Del Key
                        		break;
                        case KeyEvent.VK_HOME: board.KeyEvent("step");
                        		break;
                       
    		}
    	}
    	
    	public void keyTyped(KeyEvent e){
    		int keycode = e.getKeyCode();
    		
    		switch(keycode){
             //
             		case 24:                          //Clt-X
                     			 board.KeyEvent("Cut");
                      			 break;
                      //             
            		 case 13:                         //Clt-M
                     			 board.KeyEvent("Move");
                     //
             		case 22:                           //Clt-V
                     			 board.KeyEvent("paste");
		                         break;
           	 	case 17:                        //Clt-Q
                    		         board.KeyEvent("quit" );
		                         break;
        
     		}
     	}
     	
     	public void keyReleased(KeyEvent e){
     	}
     	
 }end of key  */
 
 class CellSpaceResizeEvent implements ComponentListener{
 	private Board board;
 	
 	public CellSpaceResizeEvent(Board board){
 		this.board = board;
 	}
 	
 	public void componentResized(ComponentEvent e){
 		
 		Dimension d = ((JPanel)(e.getSource())).getSize();
 		board.ResizeBoard(d); 		
 	}
 	
 	public void componentHidden(ComponentEvent e){}
 	public void componentMoved(ComponentEvent e){}
 	public void componentShown(ComponentEvent e){}
}

class CellSpaceClosedEvent implements WindowListener{
	private Board board;
	private String name;
	
	public CellSpaceClosedEvent(Board board, String name){
		this.board = board;
		this.name = name;
	}
	
	public void windowClosing(WindowEvent e){
		if(name.equals("color")){
 			this.board.handleBoardConfigurations("cpcolor",null);
		}else{
		//if(name.equals("board")){
			System.out.println("The simulator window is closed");
			this.board.KeyEvent(0);
 			System.exit(0);
 		}
 	}
 	
 	public void windowClosed(WindowEvent e){}
 	public void windowIconified(WindowEvent e){
 		if(this.name.equals("board")){
 			System.out.println("The simulator windows is iconified");
 			this.board.KeyEvent(0);
 		} 		
 		
 	}
 	public void windowDeiconified(WindowEvent e){
 		if(this.name.equals("board")){
 			this.board.KeyEvent(0);
 			JFrame frame = new JFrame();
 			JOptionPane.showMessageDialog(frame, "Simulation will abort if window (de)iconified.",
 				"Window Message", JOptionPane.INFORMATION_MESSAGE);
 		}
 	}
 	public void windowActivated(WindowEvent e){}
 	public void windowDeactivated(WindowEvent e){}
 	public void windowOpened(WindowEvent e){}
 }
 
 class CellSpaceActionEvent implements ActionListener{
 	private Board board;
 	
 	public CellSpaceActionEvent(Board board){
 		this.board = board;
 	}
 	
 	public void actionPerformed(ActionEvent e){
 		AbstractButton b = (AbstractButton)e.getSource();
 		String action = b.getActionCommand();
 		
 		this.board.KeyEvent(Integer.parseInt(action));
 	}
 }
 
 class EditPanelActionEvent implements ActionListener{
 	private Board board;
 	
 	public EditPanelActionEvent(Board board){
 		this.board = board;
 	}
 	
 	public void actionPerformed(ActionEvent e){
 		AbstractButton b = (AbstractButton)e.getSource();
 		String action = b.getActionCommand();
 		
 		this.board.editPanelEvent(action);
 	}
 }
 
 class ColorPaletteActionEvent implements ActionListener{
 	private ColorPalette colorpalette;
 	private int name;
 	
 	public ColorPaletteActionEvent(ColorPalette cp, int pos){
 		this.colorpalette = cp;
 		this.name = pos;
 	}
 	
 	public void actionPerformed(ActionEvent e){
 		if(this.name == 0){ //ComboBox
 			JComboBox cb = (JComboBox)e.getSource();
 			int it = (int)cb.getSelectedIndex();
 			this.colorpalette.setSelectedState(it);
 			//System.out.println("selseced " + it);
 		}else{
 			JTextField cj = (JTextField)e.getSource();
 			String col = (String)cj.getText();
 			int ns = -1;
 			try{
 				ns = Integer.parseInt(col);
 			}catch(NumberFormatException ee){
 				System.out.println("unrational inputs");
 			}
 			if(ns<0){
 				cj.setText("0"); 				
 				ns = 0;
 			}else if(ns>255){
 				cj.setText("255");
 				ns = 255;
 			}
 			this.colorpalette.changeSelectedState(this.name,ns);
 			//System.out.println("value = " + col);
 		}
 	}
 }
 		
 		
 
 class CellSpaceFileEvent implements ActionListener{
 	private Aca aca;
 	private Board board;
 	private JFileChooser chooser;
 	private JFileChooser imgchooser;
 	
 	public CellSpaceFileEvent(Aca aca, Board board){
 		this.aca = aca;
 		this.board = board;
 		this.chooser = new JFileChooser();
 		this.imgchooser = new JFileChooser();
 		this.chooser.addChoosableFileFilter(new TextFilter());
 		this.imgchooser.addChoosableFileFilter(new ImageFilter());
 	}	
 		
 	
 	public void actionPerformed(ActionEvent e){
 		AbstractButton b = (AbstractButton)e.getSource();
 		String action = b.getActionCommand();
 		int state = JFileChooser.CANCEL_OPTION;
 		int imgstate = JFileChooser.CANCEL_OPTION;
 		
 		if(action.equals("open")){
 			//this.chooser.setName("Open file of configurations");
 			this.chooser.setApproveButtonToolTipText("Open file of configurations");
 			this.chooser.setApproveButtonText("OPEN CONFIGURATION");
 			state = this.chooser.showOpenDialog(this.aca);
 		}else if(action.equals("save")){
 			//this.chooser.setName("Save configuration to file");
 			this.chooser.setApproveButtonToolTipText("Save configuration to file");
 			this.chooser.setApproveButtonText("SAVE CONFIGURATION AS");
 			state = this.chooser.showSaveDialog(this.aca);
 		}else if(action.equals("imgsave")){
 			//this.chooser.setName("Save configuration to file");
 			this.imgchooser.setApproveButtonToolTipText("Print out configuration as image");
 			this.imgchooser.setApproveButtonText("PRINT CONFIGURATION AS");
 			imgstate = this.imgchooser.showSaveDialog(this.aca);
 		}else if(action.equals("loadrule")){
 			//this.chooser.setName("Load file of transition rules");
 			this.chooser.setApproveButtonToolTipText("Load file of transition rules");
 			this.chooser.setApproveButtonText("LOAD RULES");
 			state = this.chooser.showOpenDialog(this.aca);
 		}else if(action.equals("clip")){
 			this.imgchooser.setApproveButtonToolTipText("Print the initial frame as image");
 			this.imgchooser.setApproveButtonText("SAVE FIRST FRAME AS");
 			imgstate = this.imgchooser.showSaveDialog(this.aca);
 		}else if(action.equals("unclip")){
 			this.board.handleBoardConfigurations("unclip",null);
 			return;
 		}else if(action.equals("color")){
 			this.board.handleBoardConfigurations(action, null);
 			return;
 		}else if(action.equals("loadcolor")){
			this.chooser.setApproveButtonToolTipText("Load file of colors representing cell states");
 			this.chooser.setApproveButtonText("LOAD COLORS");
 			state = this.chooser.showOpenDialog(this.aca);
 		}else if(action.equals("savecolor")){
			this.chooser.setApproveButtonToolTipText("Save colors to file");
 			this.chooser.setApproveButtonText("SAVE COLORS AS");
 			state = this.chooser.showSaveDialog(this.aca);
 		}
 		
 		File file = this.chooser.getSelectedFile();
 		File imgfile = this.imgchooser.getSelectedFile();
 		
 		if((file != null) && (state == JFileChooser.APPROVE_OPTION)){
 			this.board.handleBoardConfigurations(action, (File)file);
 			//System.out.println("Open: " + file.getPath());
 		}
 		
 		if((imgfile != null) && (imgstate == JFileChooser.APPROVE_OPTION)){
 			this.board.handleBoardConfigurations(action, (File)imgfile);
 			//System.out.println("Open: " + file.getPath());
 		}
 			
 	}
 
 	
	 class TextFilter extends javax.swing.filechooser.FileFilter{
 
 		public boolean accept(File f){
 			if(f.isDirectory()) return true;
 		
 			String filename = f.getName();
 			int i = filename.lastIndexOf('.');
 			String extension = new String();
 		
 			if((i>0) && (i<(filename.length()-1))){
 				extension = filename.substring(i+1).toLowerCase();
 			}
 		
 			if(extension != null){
 				if(extension.equals("txt") ||
				   extension.equals("dat") ||
				   extension.equals("conf") ||
				   extension.equals("rule")){
			   		return true;
				}else return false;
			}
			return false;
		}
	
		public String getDescription(){
			return "Text Files(*.txt, *.dat, *.conf, *.rule)";
		}
	}
	
	class ImageFilter extends javax.swing.filechooser.FileFilter{
 		private String[] formats = ImageIO.getWriterFormatNames();
 		public boolean accept(File f){
 			if(f.isDirectory()) return true;
 		
 			String filename = f.getName();
 			int i = filename.lastIndexOf('.');
 			String extension = new String();
 		
 			if((i>0) && (i<(filename.length()-1))){
 				extension = filename.substring(i+1);
 			}
 		
 			if(extension != null){
 				for(int k=0; k<formats.length; k++){
 					if(extension.equals(formats[k])) return true;
				}
				//if(extension.equals("eps") || extension.equals("EPS")) return true;
				//return false;
			}
			return false;
		}
	
		public String getDescription(){
			StringBuffer news = new StringBuffer(100);
			for(int i=0; i<formats.length; i++){
				news.append("*.");
				news.append(formats[i]);
				news.append(", ");
			}
			//news.append("*.eps, *.EPS");
			return news.toString();
		}
	}
 
 }
 
 
 class CellSpaceSliderEvent implements ChangeListener{
 	private Board board;
 	private JLabel valuelabel;
 	private String slidername;
 	
 	public CellSpaceSliderEvent(Board board, JLabel label, String name){
 		this.board = board;
 		this.valuelabel = label;
 		this.slidername = name;
 	}
 	
 	public void stateChanged(ChangeEvent e){
 		JSlider s = (JSlider)e.getSource();
 		int value = s.getValue();
 		
 		if(this.slidername == "updaterate"){
 			if(value == 0){
 				this.valuelabel.setText("async.");
 			}else if(value == 100){
 				this.valuelabel.setText("sync.");
 			}else{
 				this.valuelabel.setText(Float.toString((float)value/100));
 			}
 		}else{
 		
 			Point lpos = valuelabel.getLocation();
 			Point spos = s.getLocation();
 			Dimension size = s.getSize();
 			int min = s.getMinimum();
 			int max = s.getMaximum();
 			
 			Point nlpos = new Point(lpos.x, (spos.y)+((value-1)*(size.height-15))/(max-min));
 			//System.out.println("Silder x = "+spos.x + " y="+ spos.y + " height " + size.height);
 			//System.out.println(min + " " + max + " NPLOS x = " + nlpos.x + " NLPOS y= "+nlpos.y);
 			this.valuelabel.setLocation(nlpos);
 	
 			this.valuelabel.setText(Integer.toString(value));
 		}
 		
 		this.board.setBoardParameter(slidername, value);
 	
 	}
 }
 
 class ColorPaletteSliderEvent implements ChangeListener{
 	private ColorPalette colorpalette;
 	private int name;
 	
 	public ColorPaletteSliderEvent(ColorPalette cp,int n){
 		this.colorpalette = cp;
 		this.name = n;
 	}
 	
 	public void stateChanged(ChangeEvent e){
 		JSlider s = (JSlider)e.getSource();
 		int value = s.getValue();
 		this.colorpalette.changeSelectedState(this.name,value);
 	}
 }
 		
 		
 	
 	
 		
 	
     	
     	
	
	
	
	
	
	
		
		

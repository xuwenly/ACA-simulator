/** 
 * Frame1.java
 *
 * Description:	
 * @author			lijia
 * @version			
 */

package com.example.demo;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

class EditPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9065443475859311735L;
	private JPanel EditController = new JPanel();
	private JButton undobutton = new JButton();
	private JButton cutbutton = new JButton();
	private JButton pastebutton = new JButton();
	private JButton movebutton = new JButton();
	//private JPanel jPanel1 = new JPanel();
	private JButton leftbutton = new JButton();
	private JButton upbutton = new JButton();
	private JButton rightbutton = new JButton();
	private JButton downbutton = new JButton();
	private JButton rotbutton = new JButton();
	private JButton refbutton = new JButton();
	private JButton zoomoutbutton = new JButton();
	private JButton quitbutton = new JButton();
	private JButton zoominbutton = new JButton();
	private Board board;
// END GENERATED CODE

	public EditPanel(Board board){// throws Exception{
		super("Edit Panel");
		this.board = board;
		try{
			initComponents();
		}
		catch(Exception e)
		{
			throw new NullPointerException("failed to open edit panel");
		}
	}	

		
	private void initComponents() throws Exception
	{
		//Create GUI
		Image img0 = ImageLoader.initImage(this, "icon/nzoomin.jpg");
		Image img1 = ImageLoader.initImage(this, "icon/nzoomout.jpg");
		Image img2 = ImageLoader.initImage(this, "icon/retry.jpg");
		Image img3 = ImageLoader.initImage(this, "icon/cut.gif");
		Image img4 = ImageLoader.initImage(this, "icon/paste.gif");
		Image img5 = ImageLoader.initImage(this, "icon/move.gif");
		//Image img6 = ImageLoader.initImage(this, "icon/nstop.jpg");
		Image img7 = ImageLoader.initImage(this, "icon/nup.jpg");
		Image img8 = ImageLoader.initImage(this, "icon/ndown.jpg");
		Image img9 = ImageLoader.initImage(this, "icon/nleft.jpg");
		Image img10 = ImageLoader.initImage(this, "icon/nright.jpg");
		Image img11 = ImageLoader.initImage(this, "icon/nref.jpg");
		Image img12 = ImageLoader.initImage(this, "icon/nrot.jpg");
	
		EditController.setBorder(BorderFactory.createRaisedBevelBorder());
		EditController.setLayout(null);
		EditController.setLocation(new Point(0, 0));
		EditController.setSize(new Dimension(462, 38));
		EditController.setVisible(true);
		EditController.setBackground(Color.lightGray);
			
		upbutton.setActionCommand("5"); //"up"
		upbutton.setMnemonic(KeyEvent.VK_UP);
		upbutton.setBorder(new javax.swing.border.SoftBevelBorder(0));
		upbutton.setIcon(new ImageIcon(img7));
		upbutton.setLocation(new Point(5, 4));
		upbutton.setSize(new Dimension(30, 30));
		upbutton.setToolTipText("Upward");
		upbutton.setVisible(true);//upbutton
		
		downbutton.setActionCommand("7"); //"down"
		downbutton.setMnemonic(KeyEvent.VK_DOWN);
		downbutton.setBorder(new javax.swing.border.SoftBevelBorder(0));
		downbutton.setIcon(new ImageIcon(img8));
		downbutton.setLocation(new Point(37, 4));
		downbutton.setSize(new Dimension(30, 30));
		downbutton.setToolTipText("Move downward");
		downbutton.setVisible(true);//downbutton
		
		leftbutton.setActionCommand("4"); //"left";
		leftbutton.setMnemonic(KeyEvent.VK_LEFT);
		leftbutton.setBorder(new javax.swing.border.SoftBevelBorder(0));
		leftbutton.setIcon(new ImageIcon(img9));
		leftbutton.setLocation(new Point(69, 4));
		leftbutton.setSize(new Dimension(30, 30));
		leftbutton.setToolTipText("Leftward");
		leftbutton.setVisible(true); //leftbutton
				
		rightbutton.setActionCommand("6"); //"right"
		rightbutton.setMnemonic(KeyEvent.VK_RIGHT);
		rightbutton.setBorder(new javax.swing.border.SoftBevelBorder(0));
		rightbutton.setIcon(new ImageIcon(img10));
		rightbutton.setLocation(new Point(101, 4));
		rightbutton.setSize(new Dimension(30, 30));
		rightbutton.setToolTipText("Move rightward");
		rightbutton.setVisible(true);//rightbutton
		
		JSeparator ss = new JSeparator(JSeparator.VERTICAL);
		ss.setSize(new Dimension(4,34));
		ss.setLocation(new Point(136,2));
		ss.setForeground(Color.gray);
		this.EditController.add(ss);
				
		rotbutton.setActionCommand("8"); //"rotation"
		rotbutton.setMnemonic(KeyEvent.VK_R);
		rotbutton.setBorder(BorderFactory.createEtchedBorder(1,Color.white,new Color(142,142,142)));
		rotbutton.setIcon(new ImageIcon(img12));
		rotbutton.setLocation(new Point(144, 4));
		rotbutton.setSize(new Dimension(30, 30));
		rotbutton.setToolTipText("Rotate");
		rotbutton.setVisible(true);//rotbutton
		
		refbutton.setActionCommand("9"); //reflection
		refbutton.setMnemonic(KeyEvent.VK_F);
		refbutton.setBorder(BorderFactory.createEtchedBorder(1,Color.white,new Color(142,142,142)));
		refbutton.setIcon(new ImageIcon(img11));
		refbutton.setLocation(new Point(175, 4));
		refbutton.setSize(new Dimension(30, 30));
		refbutton.setToolTipText("Reflect");
		refbutton.setVisible(true);//refbutton
				
		ss = new JSeparator(JSeparator.VERTICAL);
		ss.setSize(new Dimension(4,34));
		ss.setLocation(new Point(210,2));
		ss.setForeground(Color.gray);
		this.EditController.add(ss);
		
		zoominbutton.setActionCommand("11"); //"zoomin"
		zoominbutton.setMnemonic(KeyEvent.VK_I);
		zoominbutton.setBorder(BorderFactory.createEtchedBorder(1,Color.white,new Color(142,142,142)));
		zoominbutton.setIcon(new ImageIcon(img0));
		zoominbutton.setLocation(new Point(218, 4));
		zoominbutton.setSize(new Dimension(30, 30));
		zoominbutton.setToolTipText("Zoom in");
		zoominbutton.setVisible(true);//zoominbutton
		
		zoomoutbutton.setActionCommand("10"); //"zoomout"
		zoomoutbutton.setMnemonic(KeyEvent.VK_O);
		zoomoutbutton.setBorder(BorderFactory.createEtchedBorder(1,Color.white,new Color(142,142,142)));
		zoomoutbutton.setIcon(new ImageIcon(img1));
		zoomoutbutton.setLocation(new Point(249, 4));
		zoomoutbutton.setSize(new Dimension(30, 30));
		zoomoutbutton.setToolTipText("Zoom out");
		zoomoutbutton.setVisible(true);//zoomoutbutton
		
		ss = new JSeparator(JSeparator.VERTICAL);
		ss.setSize(new Dimension(4,34));
		ss.setLocation(new Point(284,2));
		ss.setForeground(Color.gray);
		this.EditController.add(ss);
		
		movebutton.setActionCommand("3"); //"move"
		movebutton.setIcon(new ImageIcon(img5));
		movebutton.setLocation(new Point(292, 4));
		movebutton.setSize(new Dimension(30, 30));
		movebutton.setToolTipText("Cut & Paste");
		movebutton.setVisible(true);//movebutton
		
		pastebutton.setActionCommand("2"); //"paste";
		pastebutton.setIcon(new ImageIcon(img4));
		pastebutton.setLocation(new Point(323, 4));
		pastebutton.setSize(new Dimension(30, 30));
		pastebutton.setToolTipText("Paste");
		pastebutton.setVisible(true);//pastebutton
						
		cutbutton.setActionCommand("1"); //"cut"
		cutbutton.setIcon(new ImageIcon(img3));
		cutbutton.setLocation(new Point(354, 4));
		cutbutton.setSize(new Dimension(30, 30));
		cutbutton.setToolTipText("Cut");
		cutbutton.setVisible(true);//cutbutton
				
		undobutton.setActionCommand("0"); //"undo"
		undobutton.setMnemonic(KeyEvent.VK_DELETE);
		undobutton.setIcon(new ImageIcon(img2));
		undobutton.setLocation(new Point(385, 4));
		undobutton.setSize(new Dimension(30, 30));
		undobutton.setToolTipText("Undo");
		undobutton.setVisible(true);//undobutton
		
		quitbutton.setActionCommand("12"); //"quit"
		quitbutton.setMnemonic(KeyEvent.VK_Q);
		quitbutton.setBorder(BorderFactory.createEtchedBorder(1,Color.white,new Color(142,142,142)));
		//quitbutton.setIcon(new ImageIcon(img6));
		quitbutton.setText("QUIT");
		quitbutton.setLocation(new Point(418, 4));
		quitbutton.setSize(new Dimension(40, 30));
		quitbutton.setVisible(true);//quitbutton
				
		this.EditController.add(leftbutton);
		this.EditController.add(upbutton);
		this.EditController.add(rightbutton);
		this.EditController.add(downbutton);
		this.EditController.add(rotbutton);
		this.EditController.add(refbutton);
		this.EditController.add(undobutton);
		this.EditController.add(cutbutton);
		this.EditController.add(pastebutton);
		this.EditController.add(movebutton);
		this.EditController.add(zoomoutbutton);
		this.EditController.add(quitbutton);
		this.EditController.add(zoominbutton);
		
		this.getContentPane().setLayout(null);
		this.getContentPane().add(EditController);
		this.setUndecorated(true);
		this.setSize(this.EditController.getSize());
		this.setResizable(false);
		
		
		EditPanelActionEvent ae = new EditPanelActionEvent(this.board);
		
		this.zoominbutton.addActionListener(ae);
		this.zoomoutbutton.addActionListener(ae);
		this.quitbutton.addActionListener(ae);
		this.upbutton.addActionListener(ae);
		this.downbutton.addActionListener(ae);
		this.leftbutton.addActionListener(ae);
		this.rightbutton.addActionListener(ae);
		this.rotbutton.addActionListener(ae);
		this.refbutton.addActionListener(ae);
		this.undobutton.addActionListener(ae);
		this.cutbutton.addActionListener(ae);
		this.pastebutton.addActionListener(ae);
		this.movebutton.addActionListener(ae);
		
		
// END GENERATED CODE
	}
  
  	public void enableButtonsVisible(boolean state){
  		this.cutbutton.setEnabled(state);
  		this.pastebutton.setEnabled(state);
  		this.movebutton.setEnabled(state);
  	}
  	
  	
}
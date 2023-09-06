package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.util.List;

abstract class CellularAutomatonArray{
				
		protected byte[][] cellularArray;
		protected ImageStack imgArrayBuffer;
		protected Board board;
		protected CellClipBoard clipBoard = null;
		protected CellsUpdateBuffer updatebuffer;
		protected TransitionRuleTable RuleTable = null;
		protected int totalStateNumber;
		protected Dimension LatticeScale, ArraySize, GridSize;
	
		private int updateRate = 100;
		private Random random;
		private IntEventResponsor eventResponsor;
		private Automaton automaton;
		//增加统计信息
		protected HashMap<Byte, Integer> stateCount;//统计状态变化次数
		protected long lastUpdTime = 0L;
		protected long timeStep = 0L;//统计步数
		protected HashMap<Point, Boolean> finalCells =  null;
		protected byte finalState ;
		protected byte stopState;
		protected int finalCount = 0;//检测到结束标识次数
		protected File file = null;
		protected int cTimes = 0;
		protected int curTimes;
	protected CellularAutomatonArray(Board b, CParameter p, int stacklength)
	{
		this.board = b;
		this.totalStateNumber = p.states;
		this.LatticeScale = setLatticeByScale(p.space);
		this.GridSize= new Dimension(p.gridsize, p.gridsize);
		this.ArraySize = setArrayByLattice(this.LatticeScale);
		this.stateCount = new HashMap<>(p.states);
		this.finalCells = new HashMap<>();
		this.finalState = new Byte(String.valueOf(p.states -1));
		this.stopState = new Byte(String.valueOf(p.stopState));
		this.cTimes = p.cTimes;
		try{
			initialize();
			this.imgArrayBuffer = new ImageStack(this, this.board, p.bufferlength, stacklength);
		}
		catch(Exception e)
		{
			throw new NullPointerException("failed to initialize cellular array");
		}		
	}
	
	private void initialize() throws Exception
	{	
		this.cellularArray = new byte[this.ArraySize.width][this.ArraySize.height];
		for(int i=0; i<this.ArraySize.width; i++){
	      	for(int j=0; j<this.ArraySize.height; j++){
	       		this.cellularArray[i][j] = 0;
	        }
	   	}
		this.random = new Random();			
		this.updatebuffer = new CellsUpdateBuffer(this.LatticeScale);
		this.eventResponsor = new IntEventResponsor();
		this.automaton = new Automaton();					    
		
	}
	public void initStatisticVar(){}
	
	//***********************************************
	//***********************************************
	//public methods
		
	public void   cellStateTransitions(final int symmetry){
		
		if(this.RuleTable==null || this.updatebuffer.size()<1) return;
  		
		try{				
			this.imgArrayBuffer.popin();

			switch(this.updateRate)
			{
				case 0:
					this.automaton.update_async((symmetry==0? true : false));
					break;
				case 100:
					this.automaton.update_sync((symmetry==0? true : false));
					break;
				default:
					this.automaton.update_stochastic(this.updateRate, (symmetry==0? true : false));
			}
									
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			this.imgArrayBuffer.popout();
		}
		catch(Exception e)
		{
			throw new RuntimeException("failed to update cells");
		}
		finally
		{
			this.imgArrayBuffer.verifyTopItem();
		}
		
		for(int k=updatebuffer.size(); k>0; k--){
					
			CPoint p = updatebuffer.get(k-1); 
			addNeighboringCellsToBuffer(p.x, p.y);
		}
		//增加步数统计
		this.timeStep ++;
		//增加停止状态查询
//		if(true == this.checkStopState() ){
//			//stop
//			board.KeyEvent(0);//stop
//			curTimes ++;
//			System.out.println("已经运行次数" + curTimes);
//			this.saveStatisticResult();
//			//start
//			if (curTimes < cTimes){
//				board.KeyEvent(9);//clear
//				board.handleBoardConfigurations("open", this.file);
//				board.repaint();
//				board.KeyEvent(1);//start
//			}
//		}
		
	}
	private void saveStatisticResult(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
		String date = simpleDateFormat.format(new Date());
		String filename = "statistics_" + date + ".csv";
		File file = new File(filename);
		String title= null;
		String value = this.stateCount.values().toString();
		value = value.substring(1, value.length()-1) + "," + String.valueOf(this.timeStep);
		FileOutputStream outputStream = null;
		OutputStreamWriter streamWriter = null;
		try{
			if (!file.exists()){
				title = this.stateCount.keySet().toString();
				title = title.substring(1, title.length()-1) + ", Times";
			}
			outputStream = new FileOutputStream(file, true);
			streamWriter = new OutputStreamWriter(outputStream, "UTF-8");
			if(null!= title)streamWriter.write(title + "\n");
			streamWriter.write(value + "\n");
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				streamWriter.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clipBoardEvent(int action){
		
		this.eventResponsor.clipBoardEvent(action, this);
					  	
	}
	
	public void createCellSpaceImage(Graphics g, Color[] myColor, boolean is_Tracing){
		
		if(!is_Tracing){
			if(this.imgArrayBuffer.size()<1){
				this.board.setAcaEditButtons(0);
			}else{
				this.board.setAcaEditButtons(1);				
			}
		}
				
		drawCellSpaceImage(g, myColor);
		
		if(is_Tracing){
			g.setColor(Color.blue);
			g.fillRect(8,8,36,20);
			g.setFont(new Font("Serif",1,16));
			g.setColor(Color.yellow);
			g.drawString(Integer.toString(this.board.getTrackCount()), 12, 24);
		}
		
		this.eventResponsor.drawDraggedArea(g, myColor);
		  		
  		//System.out.println(this.updateConfPosition +" " + this.paintConfPosition);
	}
	
	public final int getNumberofCellStates(){
		return this.totalStateNumber;
	}
       
    public Dimension getPreferredCellSpaceDimension(){
	
		return(new Dimension(this.LatticeScale.width*this.GridSize.width, 
							 this.LatticeScale.height*this.GridSize.height));
	}
	
	public void MouseEventFromBoard(int x, int y, int action, int button){
	
		//(action) click: 0, press: 1, release: 2, drag: 3, fail: 4
		//(button) left: 0, right: 1, middle: 2
		
		this.eventResponsor.MouseEventFromBoard(x, y, action, button);
	}	

	public VectorString printConfigurationFromBoard(){
		if(!this.board.isRunning()){
			int lx = this.LatticeScale.width;
			int ly = this.LatticeScale.height;
						
			VectorString confvec = new VectorString(200);
			
			for(int i=0; i<lx; i++){
				for(int j=0; j<ly; j++){
					try{
						if(isQuiescent(i,j)) continue;
							String ss = getStatesFromString(i,j);
							confvec.add(Integer.toString(i)+','+Integer.toString(j)+',' + ss);
					}catch(ArrayIndexOutOfBoundsException e)
					{
						//throw e;
					}
				}
			}
			
			return confvec;
		}
		return null;
	}
	
	public boolean ResizeCellularArray(Dimension nsize){
				
		Dimension newl = new Dimension(nsize.width / this.GridSize.width, 
				                       nsize.height / this.GridSize.height);
		Dimension newLatticeScale = setLatticeByScale(newl);
								 		
		if((this.LatticeScale.width != newLatticeScale.width) || 
		   (this.LatticeScale.height != newLatticeScale.height))
		{			
			Dimension newArraySize = setArrayByLattice(newLatticeScale);
			byte[][] cellularArray0 = new byte [newArraySize.width][newArraySize.height];
									
			for(int i=0; i<newArraySize.width; i++){
				for(int j=0; j<newArraySize.height; j++){
					//cellularArray0[pos][i][j] = 0;
					if(i<this.ArraySize.width && j<this.ArraySize.height)
						cellularArray0[i][j] = this.cellularArray[i][j];
					else 
						cellularArray0[i][j] = 0;
						
				}
			}
			
			this.LatticeScale = new Dimension(newLatticeScale);
			this.ArraySize = new Dimension(newArraySize);
			this.cellularArray = cellularArray0;
			this.updatebuffer = new CellsUpdateBuffer(this.LatticeScale);
			this.clipBoardEvent(12);	
			this.imgArrayBuffer.clean();
			this.board.setAcaEditButtons(0);
			initializeBuffer();
					
			return true;
					
		}
		
		return false;
		
	}
			
	public void setConfigurationFromBoard(VectorString confvec){
		if(!this.board.isRunning()){
			int size = confvec.size();
			if(size<1) return;
			int lx = this.LatticeScale.width;
			int ly = this.LatticeScale.height;
			this.updatebuffer.removeAllCellsFromBuffer();
			this.imgArrayBuffer.popin();
									
			for(int i=0; i<size; i++){
				
				String vec = confvec.at(i);
				
				int dot1 = vec.indexOf(',');
				if(dot1<0) continue;
				int dot2 = vec.indexOf(',', dot1+1);
				if(dot2<0) continue;
				int xx = Integer.parseInt(vec.substring(0,dot1));
				int yy = Integer.parseInt(vec.substring(dot1+1, dot2));
				
				if(xx<0 || yy<0) continue;
				if(xx>=lx || yy>=ly) continue;
				
				String ss = vec.substring(dot2+1);
				
				setStatesByString(xx,yy,ss);
			}
			
			this.imgArrayBuffer.verifyTopItem();
			initializeBuffer();
		}
	}
	
	public void setRandom(boolean admit){
    	
    	if(this.board.isRunning()) return;
		
		int nstate = this.totalStateNumber;
		byte[][] cellularArray0 = this.cellularArray;
		this.updatebuffer.removeAllCellsFromBuffer();
		this.imgArrayBuffer.clean();
		
		for(int i=0; i<this.ArraySize.width; i++){
			for(int j=0; j<this.ArraySize.height; j++){
				if(admit){
					cellularArray0[i][j]= (byte)this.random.nextInt(nstate);
				}else{
					cellularArray0[i][j] = 0;
				}
			}
		}
		
		if(admit) initializeBuffer();
					
	}
	
	public void setTransitionRules(VectorString rulevec, int flag){
		
		if(rulevec==null) return;
		TransitionRuleList ruleList = generateRuleList(rulevec,flag);
		if(ruleList==null) return;
		this.RuleTable = new TransitionRuleTable(ruleList,this.totalStateNumber);
		this.updatebuffer.removeAllCellsFromBuffer();
		initializeBuffer();
		
		System.out.println("Transition rules loaded");
							
	}
	
	public void SetUpdateRate(int flag){
		if(flag > 100) flag = 100;
	  	this.updateRate = flag;
	}
		
	public void traceCellSpaceConfiguration(int flag, boolean finished){
		
		this.imgArrayBuffer.traceConfiguration(this.cellularArray, flag);
		if(finished){
			this.updatebuffer.removeAllCellsFromBuffer();
			initializeBuffer();
		}
			
	}
	
	public void Trivialoutput(){
		System.out.println("This is a cellular space");
	}
	
	//******************************************
	//******************************************
	//protected methods
		
	
	protected void addNeighboringCellsToBuffer(int i, int j){}
	
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
	 	return null;
	}
	
	protected void constructClipBoard(Dimension ca, Point cp){
	
		this.clipBoard = new CellClipBoard(ca, cp, this.GridSize);
	
	}
	
	protected void drawCellSpaceImage(Graphics g, Color[] myColor){
		
		this.drawSquareGridImage(g, myColor);		
	}
		
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return null;
	}
	
	protected Point getCellByPosition(int x, int y){
		return new Point(getCellGridPosition(x,y));
	}
	
	protected Point getCellGridPosition(int x, int y){
	
		int i = x / this.GridSize.width;
		int j = y / this.GridSize.height;
		
		i = Math.max(i, 0);
		i = Math.min(i, this.LatticeScale.width - 1);
		j = Math.max(j, 0);
		j = Math.min(j, this.LatticeScale.height - 1);
				
		return(new Point(i,j));
	}



	protected String getStatesFromString(int i, int j){
		
		byte val = this.cellularArray[i][j];
		String ss = Character.toString((char)(val<10? val+'0' : val-10+'A'));
		
		return ss;
	}
	
	protected void initializeBuffer(){
		int lx = this.LatticeScale.width;
		int ly = this.LatticeScale.height;
				
		for(int i=0; i<lx; i++){
			for(int j=0; j<ly; j++){
				try{
					if(isQuiescent(i,j)) continue;
					addNeighboringCellsToBuffer(i, j);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("catched " + i + " " + j);
				}
			}
		}
		
	}
	
		
	protected boolean isQuiescent(int i, int j){
		
		boolean res = true;
		
		try{
			if(this.cellularArray[i][j]>0) res = false;;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw e;
		}
		return res;
	}
	
	protected Dimension setArrayByLattice(Dimension lsize){
	
		return new Dimension(lsize);			
	}
		
	protected Dimension setLatticeByScale(Dimension lsize){
		
		return new Dimension(lsize);
				
	}
	
	protected void setStatesByString(int i, int j, String str){
				
		char ss = str.charAt(0);
		if(ss<'0') return;
		if(ss>'9'){
			if(ss<'A' || ss>'Z') return;
		}
		byte val = (byte)(ss<'A'? ss-'0' : ss-'A'+10);
		
		try{
			if(val==this.cellularArray[i][j]) return;
			this.imgArrayBuffer.addItem(new Cell(i,j,this.cellularArray[i][j]));
			this.cellularArray[i][j] = val;
		}catch(ArrayIndexOutOfBoundsException e)
		{
			//throw e;
		}				
		
	}	
	
	protected boolean setTransitionRth(CPoint p, int key){
	 	return false;
	}
	public void initFinalCells(){
	}

	protected boolean checkStopState(){
		return false;
	}

  	private void shiftState(int x, int y, boolean req_shift){
		if(!this.board.isRunning()){
			Point pc = getCellGridPosition(x,y);
			Point ps = getCellByPosition(x,y);
			int nstate = this.totalStateNumber;
		
			try{
				byte ss = this.cellularArray[ps.x][ps.y];
				
				if(req_shift){
					this.imgArrayBuffer.popin();
					this.imgArrayBuffer.addItem(ps.x,ps.y,ss);
					ss++;
					this.cellularArray[ps.x][ps.y] = (ss<nstate? ss : 0);
				}else{
					if(ss>0){
						this.imgArrayBuffer.popin();
						this.imgArrayBuffer.addItem(ps.x,ps.y,ss);
					}
					this.cellularArray[ps.x][ps.y] = 0;
				}
				
				addNeighboringCellsToBuffer(pc.x,pc.y);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				//throw e;
			}
		
		}
	}
  	
  	private void drawSquareGridImage(Graphics g, Color[] myColor){
  		byte[][] arraybuffer = this.cellularArray;
  		
		int lx = this.LatticeScale.width;
		int ly = this.LatticeScale.height;
		int dx = this.GridSize.width;
		int dy = this.GridSize.height;
		int xsize = lx*dx;
		int ysize = ly*dy;
			
		g.setColor(myColor[0]);
		g.fillRect(0,0,xsize,ysize);
							
		for(int i=0; i<lx; i++){
			for(int j=0; j<ly; j++){
				if(arraybuffer[i][j]>0){
					g.setColor(myColor[arraybuffer[i][j]+1]);
					g.fillRect( i*dx, j*dy, dx, dy);
				}
			}
		}
	
		if(dx >= 4){
			g.setColor(myColor[1]);
			for(int i=0; i<=lx; i++){
				g.drawLine(i*dx, 0, i*dx, ysize);
			}
			for(int i=0; i<=ly; i++){
				g.drawLine(0, i*dy, xsize, i*dy);
			}
		}
  		
  		
  	}
  	
  	//private internal classes
  	//******************/
  	
  	
  	private class Automaton
  	{  	
  		public void update_sync(final boolean is_totalistic)
  		{
  			VectorInt updateStack = new VectorInt(updatebuffer.size());
							
			for(int k=updatebuffer.size(); k>0; k--)
			{
				byte[] statebytes;
					
				try{
					statebytes = consTransitionLfh(updatebuffer.get(k-1), is_totalistic);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{ 
					statebytes = null;
				}
					
				int key = RuleTable.get(statebytes);
				if(key >= 0) 
				{
					updateStack.add(key);
					continue;
				}
				
				updatebuffer.removeCellFromBuffer(k-1);
			}
			proprocess(updateStack);
  		}
  		
  		public void update_async(final boolean is_totalistic)
  		{
			updatebuffer.ranShuffle(random);
			int low = updatebuffer.size() >> 1;
				
			for(int k=updatebuffer.size(); k>low; k--)
			{				
				CPoint p = updatebuffer.get(k-1);
				try{					
					byte[] statebytes = consTransitionLfh(p, is_totalistic);
					if(setTransitionRth(p, RuleTable.get(statebytes))) continue;	
					
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					
				}
				
				updatebuffer.removeCellFromBuffer(k-1);
			}
  			
  		}
			
		public void update_stochastic(final int rate, final boolean is_totalistic)
	  	{
	  		VectorInt updateStack = new VectorInt(updatebuffer.size());
								
			for(int k=updatebuffer.size(); k>0; k--)
			{
				byte[] statebytes;
				
				try{
					statebytes = consTransitionLfh(updatebuffer.get(k-1), is_totalistic);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{ 
					statebytes = null;
				}
					
				int key = RuleTable.get(statebytes);
				if(key >= 0) 
				{
					updateStack.add((random.nextInt(100)<rate? key : -1));
					continue;
				}
				
				updatebuffer.removeCellFromBuffer(k-1);
								
			}
			
			proprocess(updateStack);
	  	}
  		
  		private void proprocess(VectorInt updateStack)
  		{
  			int max = updatebuffer.size();
  			
  			for(int k=max; k>0; k--)
  			{
				CPoint p = updatebuffer.get(k-1);
					
				try{
					setTransitionRth(p, updateStack.at(max-k));
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
						//throw e;
				}
			}
  		}
   		
  		
  	};
  	
  	private class IntEventResponsor
  	{
  		private Point bpos, epos;
		private boolean is_copied = false, is_zoomed = false;
		private boolean is_pressed = false,  is_dragged = false;		
				
		public void MouseEventFromBoard(int x, int y, int action, int button){
			
			//(action) click: 0, press: 1, release: 2, drag: 3, fail: 4
			//(button) left: 0, right: 1, middle: 2
			
			if(button == 0){ //left
				if (action == 1){ //press
					if(!this.is_copied){
						if(!this.is_pressed){
							this.is_pressed = true;
							this.bpos = (Point)getCellGridPosition(x,y);
						}
					}else if(!this.is_zoomed && !this.is_pressed){
						
						if(clipBoard.ClipBoardMousePressed(x, y)){
							this.is_zoomed = true;
							this.is_pressed = true;
						}
						
					}
				}else if(action == 3){ //drag
					if(!this.is_copied){
						if(this.is_pressed){
							this.epos = (Point)getCellGridPosition(x,y);
							this.is_dragged = true;
						}
					}else if(this.is_zoomed && this.is_pressed){
						
						clipBoard.ClipBoardMouseDragged(x, y);
					}
						
				}else if(action == 2){ //release
					if((!this.is_copied) && (this.is_dragged)){
						if(!(this.epos.equals(this.bpos))){
							int i = Math.min(this.bpos.x, this.epos.x);
							int j = Math.min(this.bpos.y, this.epos.y);
							int w = Math.abs(this.bpos.x - this.epos.x);
							int l = Math.abs(this.bpos.y - this.epos.y);
							 
							constructClipBoard(new Dimension(w, l), new Point(i, j));
							
							this.is_copied = true;
							board.setEditPanelInvisible(true);						
							
						}else{
							this.is_dragged = false;
							//this.is_pressed = false;
						}
					}
					this.is_zoomed = false;
					this.is_pressed = false;
				}else if(action == 4){ //fail
					if(!this.is_copied) this.is_dragged = false;
					this.is_pressed = false;
				 	this.is_zoomed = false;
				}
			}
			
			if(!this.is_copied){
				if(!board.isRunning() && action==0){ //click
					if(button == 0) shiftState(x,y,true); //left
					if(button == 1) shiftState(x,y,false); //right
					//this.is_updated = true;
				}
			}else{
				if(!board.isRunning() && action==0){ //click
					Point clickpos = (Point)clipBoard.ClipBoardMouseClicked(x, y);
					
					if(clickpos != null){
						//this.is_updated = true;				
						if(button == 0) //left
							shiftState(clickpos.x, clickpos.y, true);
						else if(button == 1) //right
							shiftState(clickpos.x, clickpos.y, false);
					}
				}
			}
		}
		
		public void clipBoardEvent(int action, CellularAutomatonArray ca){
			
			if(!this.is_copied || clipBoard==null) return;
			
			switch(action)
			{
				case 1:   //cut
				case 2:   //paste
				case 3:   //move
					if(board.isRunning()) return;
					imgArrayBuffer.popin();
					clipBoard.ClipBoardAdministrate(cellularArray,
							                        imgArrayBuffer,
							                        ArraySize,
							                        action);
					imgArrayBuffer.verifyTopItem();
					initializeBuffer();
					break;
				default:
					clipBoard.keyEvent(action, ca);
					
			}
			
			if((action>0&&action<4) || action>11)
			{
				board.setEditPanelInvisible(false);
				this.is_copied = false;
				this.is_dragged = false;
	  			this.is_pressed = false;
	  			this.is_zoomed = false;
	  			clipBoard = null;
	  		}
						  	
		}
		
		public void drawDraggedArea(Graphics g, Color[] colors){
			
			if(!this.is_dragged) return;
	  		
			g.setColor(Color.blue);
	  		
	  		int i = Math.min(this.bpos.x, this.epos.x);
	  		int j = Math.min(this.bpos.y, this.epos.y);
	  		int w = Math.abs(this.epos.x - this.bpos.x);
	  		int l = Math.abs(this.epos.y - this.bpos.y);
	  		int dx = GridSize.width;
	  		int dy = GridSize.height;
	  		
	  		g.drawRect(i*dx, j*dy, (w+1)*dx, (l+1)*dy);
	  		
	  		if(this.is_copied && clipBoard!=null) 
	  		{
	  			clipBoard.createClipBoardImage(cellularArray, g, colors);
	  		}	  		
	  	}		 		
  		
  	};
  
} //end of  CellularArray

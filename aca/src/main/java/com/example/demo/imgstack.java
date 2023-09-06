package com.example.demo;

import java.awt.Dimension;

class ImageStack extends VectorType<VectorType<Cell>>{
	
	private CellularAutomatonArray cellarray;
	private Board board;
	private int trackposition = -1;
	private boolean is_trackdown;
	private int stacklength;
	
	public ImageStack(CellularAutomatonArray ca, Board b, int buf, int stack){
		
		super((buf<10? 10 : buf));
		this.cellarray = ca;
		this.board = b;
		this.stacklength = (stack<1? 1 : stack);
	
	}
	
	public synchronized boolean popin(){
		
		if(this.size() == this.capacity()) this.remove(0);
		
		this.add(new VectorType<Cell>(this.stacklength));
		return true;
	}
	
	public void addItem(int x, int y, byte s){
		addItem(new Cell(x, y, s));
	}
	
	public void addItem(Cell c)
	{
		this.at(this.size()-1).add(c);
	}
	
	public synchronized boolean popout(){
		return this.pop_out();
	}
	
	public int getSize(int pos){
	   return this.at(pos).size();
	}
	
	public Cell get(int pos, int k)
	{
		return this.at(pos).at(k);
	}
	
	public int getX(int pos, int k){
		return this.at(pos).at(k).x;
	}
	
	public int getY(int pos, int k){
		return this.at(pos).at(k).y;
	}
	
	public byte getS(int pos, int k){
		return this.at(pos).at(k).s;
	}
	
	public void setS(int pos, int k, byte ov){
		this.at(pos).at(k).s = ov;
	}
	
	public void clean(){
		this.clear();
	}
	
	public void verifyTopItem()
	{
		
		if(this.size()>0 && this.getSize(this.size()-1)<1)
			this.popout();
	}
		
	public synchronized void traceConfiguration(byte[][] cellulararray, int flag){
		
		int nsize = this.size() - 1;
		
		switch(flag){
			case 0: this.trackposition = nsize;
					this.is_trackdown= true;
					//initialization
					//break;
			case 1:	if(!this.is_trackdown){
						this.is_trackdown = true;
						this.trackposition--;
					}	
					
					if(this.trackposition >= 0){						
						setArrayByImgBuffer(cellulararray, this.trackposition);
						this.trackposition--;						
					}
					if(this.trackposition < 0){
						//this.trackposition = 0;
						this.board.setAcaEditButtons(3);
					}else{
						this.board.setAcaEditButtons(2);
					}
					break;                
					//back;
			case 2: if(this.is_trackdown){
						this.is_trackdown = false;
						this.trackposition++;
					}
					
					if(this.trackposition <= nsize){
						setArrayByImgBuffer(cellulararray, this.trackposition);
						this.trackposition++;												
					}
					if(this.trackposition > nsize){
						this.board.setAcaEditButtons(1);
					}else{
						this.board.setAcaEditButtons(2);
					}
					break; 
					//forward
			case 3:	if(!this.is_trackdown) this.trackposition--;
					this.pop(this.size()-this.trackposition-1);
					if(this.trackposition >= 0){
						this.board.setAcaEditButtons(1);
					}else{
						clean();
					}
					break;   //exit
					//
			default:
					if(this.is_trackdown) this.trackposition++;
					this.is_trackdown = false;
					for(int i=this.trackposition; i<this.size(); i++)
					{
						setArrayByImgBuffer(cellulararray, i);
					}
		}
		
		//System.out.println("track "+ this.trackposition);
		
	}
	
	private void setArrayByImgBuffer(byte[][] cellularArray, int pos){
						
		try{
			int nsize = getSize(pos);
			
			for(int i=0; i<nsize; i++){
				int j = (this.is_trackdown? nsize-i-1 : i);
				try{
					Cell c = get(pos, j);
					byte os = cellularArray[c.x][c.y];
					cellularArray[c.x][c.y] = c.s;
					setS(pos,j,os);	
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					//this.at(pos).remove(j);
				}
			}
		}
		catch(VecOutOfBoundException e)
		{
			return;			
		}
			
	}

};
	
	

package com.example.demo;

import java.util.*;
import java.awt.*;

class CellsUpdateBuffer extends VectorType<CPoint>{
	
	private byte[][] updateIndicator;
	private Dimension latticescale;
			
	public CellsUpdateBuffer(Dimension l){
		
		super(l.width*l.height);
		this.latticescale = new Dimension(l);
		int nx = latticescale.width;
		int ny = latticescale.height;
				
		this.updateIndicator = new byte [nx][ny];
		initIndicator(nx, ny);			
	}
		
	public void addCellToBuffer(int x, int y){
	
		try{
			if(this.updateIndicator[x][y] > 0) return;
	
			this.push_back(new CPoint(x,y));
			this.updateIndicator[x][y] = 1;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw e;
		}
	}	
	
	public CPoint get(int pos)
	{
		return this.at(pos);
	}
	
	public int getX(int pos){
		
		return this.at(pos).x;
	}
	
	public int getY(int pos){
		
		return this.at(pos).y;
	}
	
	public void removeCellFromBuffer(int pos){
		
		try{
			CPoint c = this.get(pos);
			this.updateIndicator[c.x][c.y] = 0;
			this.remove(pos);
		}
		catch(VecOutOfBoundException e)
		{
			throw e;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			this.remove(pos);
			throw e;
		}
		
	}
	
	public void removeAllCellsFromBuffer(){
		
		this.removeAll();
		initIndicator(this.latticescale.width, this.latticescale.height);
	}
		
	public void ranShuffle(Random random){
	
		for(int last=this.size(); last>1; last--){
			int tmpid = random.nextInt(last);
			if(tmpid<(last-1)){
				this.swap(tmpid,last-1);
			}
		}
	}
	
	private void initIndicator(int nx, int ny)
	{
		for(int i=0; i<nx; i++){
			for(int j=0; j<ny; j++){
				this.updateIndicator[i][j] = 0;
			}
		}
	}
	
};


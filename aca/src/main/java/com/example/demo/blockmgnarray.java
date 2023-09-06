package com.example.demo;

import java.awt.*;

class BlockMgnArray extends MnArray{
	public static BlockMgnArray Create(Aca a, Board b, CParameter p)
	{
		BlockMgnArray c = new BlockMgnArray(b, p, 100);
		a.setRateItems(0, false);
		c.SetUpdateRate(0);
		
		return c;	
	}
	
	protected BlockMgnArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new TransitionRuleList(rules, flag);
	}
	
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
		
		int ix = (p.x<(nx-1)? p.x+1 : 0);
		int iy = (p.y<(ny-1)? p.y+1 : 0);
						
		byte[] statebytes = new byte[5];
		statebytes[0] = this.cellularArray[p.x][p.y];				
		statebytes[1] = this.cellularArray[p.x][iy];
		statebytes[2] = this.cellularArray[ix][iy]; 
		statebytes[3] = this.cellularArray[ix][p.y]; 
		statebytes[4] = -1;
		
		if(is_totalistic){
			int nstate = this.totalStateNumber;
			byte[] stateindex = new byte[nstate+1];
			
			for(int t=1; t<(nstate+1); t++){
				stateindex[t] = 0;
			}
			//stateindex[0] = statebytes[0];
			for(int m=0; m<4; m++){
				stateindex[statebytes[m]]++;
			}
			stateindex[nstate] = -1;
			return stateindex;
		}
		
		return statebytes;
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
		int ix = (p.x<(nx-1)? p.x+1 : 0);
		int iy = (p.y<(ny-1)? p.y+1 : 0);
		
		this.imgArrayBuffer.addItem(new Cell(p.x, p.y, this.cellularArray[p.x][p.y]));
		this.imgArrayBuffer.addItem(new Cell(p.x, iy, this.cellularArray[p.x][iy]));
		this.imgArrayBuffer.addItem(new Cell(ix, iy, this.cellularArray[ix][iy]));
		this.imgArrayBuffer.addItem(new Cell(ix, p.y, this.cellularArray[ix][p.y]));
		
		this.cellularArray[p.x][p.y] = newstate[0];
	    this.cellularArray[p.x][iy] = newstate[1];
	    this.cellularArray[ix][iy] = newstate[2];
	    this.cellularArray[ix][p.y] = newstate[3];	
		
		return true;
	}
	
	public void addNeighboringCellsToBuffer(int i, int j){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
		int ii = (i<(nx-1)? i+1 : 0);
		int jj = (j<(ny-1)? j+1 : 0);
		
		super.addNeighboringCellsToBuffer(i, j);
		super.addNeighboringCellsToBuffer(i, jj);
		super.addNeighboringCellsToBuffer(ii, jj);
		super.addNeighboringCellsToBuffer(ii, j);
	}
	
	public void Trivialoutput(){
		System.out.println("This is a blocked Margolus-neighborhood cellular space");
	}
	
};
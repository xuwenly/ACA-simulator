package com.example.demo;

import java.awt.*;

class VnArray extends CellularAutomatonArray{

	public static VnArray Create(Aca a, Board b, CParameter p)
	{
		VnArray c = new VnArray(b, p, 200);
		a.setRateItems(50, true);
		c.SetUpdateRate(50);
		
		return c;	
	}
	
	protected VnArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new VnTransitionRuleList(rules,flag);
	}
	
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
						
		byte[] statebytes = new byte[6];
		statebytes[0] = this.cellularArray[p.x][p.y];				
		statebytes[1] = this.cellularArray[p.x][(p.y>0? p.y-1 : ny-1)];
		statebytes[2] = this.cellularArray[(p.x>0? p.x-1 : nx-1)][p.y]; 
		statebytes[3] = this.cellularArray[p.x][(p.y<(ny-1)? p.y+1 : 0)]; 
		statebytes[4] = this.cellularArray[(p.x<(nx-1)? p.x+1 : 0)][p.y];
		statebytes[5] = -1;
		
		if(is_totalistic){
			int nstate = this.totalStateNumber;
			byte[] stateindex = new byte[nstate+2];
			
			for(int t=1; t<(nstate+2); t++){
				stateindex[t] = 0;
			}
			stateindex[0] = statebytes[0];
			for(int m=1; m<5; m++){
				stateindex[statebytes[m]+1]++;
			}
			stateindex[nstate+1] = -1;
			return stateindex;
		}
		
		return statebytes;
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		byte oldstate = this.cellularArray[p.x][p.y];
		this.imgArrayBuffer.addItem(new Cell(p.x, p.y, oldstate));
		this.cellularArray[p.x][p.y] = newstate[0];
		Integer count = this.stateCount.getOrDefault(newstate[0], 0);
		stateCount.put(newstate[0], count + 1);
		return true;
	}

	public void  initStatisticVar(){
		this.initFinalCells();
		this.finalCount = 0;
		this.timeStep = 0;
		this.stateCount.clear();
	}

	public void initFinalCells(){
		this.finalCells.clear();
		for (int i = 0; i < this.LatticeScale.width; i++){
			for(int j = 0; j < this.LatticeScale.height; j++){
				if(this.cellularArray[i][j] == finalState){
					this.finalCells.put(new Point(i, j), false);
				}
			}
		}
	}

	protected boolean checkStopState(){
		for(Point p : this.finalCells.keySet()){
			int ww = (p.x -2 + this.LatticeScale.width) % this.LatticeScale.width;
			int nn = (p.y -2 + this.LatticeScale.height) % this.LatticeScale.height;
			int ee = (p.x +2 + this.LatticeScale.width) % this.LatticeScale.width;
			int ss = (p.y +2 + this.LatticeScale.height) % this.LatticeScale.height;
			if(this.finalCells.get(p) == false &&(this.cellularArray[ww][p.y] == this.stopState
					||this.cellularArray[ee][p.y] == this.stopState
					||this.cellularArray[p.x][nn] == this.stopState
					||this.cellularArray[p.x][ss] == this.stopState)){
				finalCount++;
				this.finalCells.put(p, true);
			}
		}
		if(this.finalCells.size() == 0 || this.finalCells.size() > this.finalCount){
			return false;
		}
		System.out.println("检测到信号结束，开始下一次测试");
		return true;
	}

	public void addNeighboringCellsToBuffer(int i, int j){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
				
		this.updatebuffer.addCellToBuffer(i, j);
		this.updatebuffer.addCellToBuffer(i, (j>0? j-1 : ny-1));
		this.updatebuffer.addCellToBuffer((i>0? i-1 : nx-1), j);
		this.updatebuffer.addCellToBuffer( i, (j<(ny-1)? j+1 : 0));
		this.updatebuffer.addCellToBuffer((i<(nx-1)? i+1 : 0), j);
	}

/*
	protected void drawCellSpaceImage(Graphics g, Color[] myColor){
	
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
*/
	
	public void Trivialoutput(){
		System.out.println("This is a Neumann-neighborhood cellular space");
	}
	
};

class VnTransitionRuleList extends TransitionRuleList{
	
	public VnTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String getLeftHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<1 || dot==(rule.length()-1)) return null;
		String r = rule.substring(dot+1);
       	return (String)(rule.charAt(0)+r);
	}
	
	protected String getRightHand(String rule){
		if(rule.length()<2) return null;
		return Character.toString(rule.charAt(1));
	}
	
	protected String rotLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+rotate(r));
	}
	
	protected String rotRight(String right){
		return right;
	}
	
	protected String revLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+reverse(r));
	}
		
	protected String revRight(String right){
		return right;
	}


};

/*End */
			
		
		
		
	
			
		
package com.example.demo;

import java.awt.*;

class PcaTransitionRuleList extends TransitionRuleList{
	
	public PcaTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String getRightHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<0 || dot==(rule.length()-1)) return null;
		String right = rule.substring(dot+1);
		if(right.length()<4) return null;
		return right;
	}
	
	protected String rotLeft(String left){
		int mid = left.length()>>1;
		String fore = left.substring(0,mid);
		String post = left.substring(mid);
				
		return rotate(fore)+rotate(post);
	}
		
	
	protected String revLeft(String left){
		int mid = left.length()>>1;
		String fore = left.substring(0,mid);
		String post = left.substring(mid);
		
		return reverse(fore)+reverse(post); 
	}
		
}


class PcaArray extends CellularAutomatonArray{
	
	public static PcaArray Create(Aca a, Board b, CParameter p)
	{
		PcaArray c = new PcaArray(b, p, 200);
		a.setRateItems(50, false);
		c.SetUpdateRate(50);
		
		return c;	
	}
	
	protected PcaArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new PcaTransitionRuleList(rules,flag);
	}
	
	protected Dimension setArrayByLattice(Dimension lsize){
		int i = lsize.width*4;
		int j = lsize.height;
		
		return(new Dimension(i, j));
	}
	
	protected void constructClipBoard(Dimension ca, Point cp){
		this.clipBoard = new pcaClipBoard(ca, cp, this.GridSize);
	}
	
	protected boolean isQuiescent(int i, int j){
		
		boolean res = true;
		
		try{
			int pos = 4*i;
			for(int k=0; k<4; k++){
				if(this.cellularArray[pos+k][j]>0){
					res = false;
					break;			
				}				
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw e;
		}
		return res;
	}
	
	protected Point getCellByPosition(int x, int y){
		
		Point pc = getCellGridPosition(x, y);
		int dx = this.GridSize.width;
		int dy = this.GridSize.height;
		
		int w = x - pc.x*dx;
		int l = y - pc.y*dy;
		int k = 0;
		
		if(w>l && (dx-w)>l){
			k = 0;
		}else if(w>l && (dx-w)<l){
			k = 3;
		}else if(w<l && (dx-w)<l){
			k = 2;
		}else if(w<l && (dx-w)>l){
			k = 1;
		}
			
		return new Point(4*pc.x+k, pc.y);
	}
	
	protected void setStatesByString(int i, int j, String str){
		
		if(str.length()<4) return;
		int pos = 4*i;	
		for(int h=0; h<4; h++){
			
			char ch = str.charAt(h);
			byte val = (byte)(ch<'A'?  ch-'0' : ch-'A'+10);
			try{
				if(this.cellularArray[pos+h][j]==val) continue;
				this.imgArrayBuffer.addItem(pos+h,j,this.cellularArray[pos+h][j]);
				this.cellularArray[pos+h][j] = val;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//throw e;
			}
		}
	}
	
	protected String getStatesFromString(int i, int j){
		
		StringBuffer bits = new StringBuffer(10);
		for(int h=0; h<4; h++){
			byte val = this.cellularArray[4*i+h][j];
			bits.append((char)(val<10? val+'0' : val-10+'A'));
		}
		
		return bits.toString();
	}
	
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
		final int lx = this.LatticeScale.width;
		final int ly = this.LatticeScale.height;
				
		byte[] statebytes = new byte[9];
		
		int inx =4*p.x+2, iny = (p.y>0? p.y-1 : ly-1);
		int iwx = 4*(p.x>0? p.x-1 : lx-1)+3,  iwy = p.y;
		int isx = 4*p.x,  isy = (p.y<(ly-1)? p.y+1 : 0);
		int iex = 4*(p.x<(lx-1)? p.x+1 : 0)+1,  iey = p.y; 
		
		/*
		statebytes[0] = this.cellularArray[inx][iny];
		statebytes[1] = this.cellularArray[iwx][iwy];
		statebytes[2] = this.cellularArray[isx][isy];
		statebytes[3] = this.cellularArray[iex][iey];
		statebytes[4] = this.cellularArray[4*p.x][p.y];
		statebytes[5] = this.cellularArray[4*p.x+1][p.y];
		statebytes[6] = this.cellularArray[4*p.x+2][p.y];		
		statebytes[7] = this.cellularArray[4*p.x+3][p.y];
		*/
		
		statebytes[0] = this.cellularArray[4*p.x][p.y];
		statebytes[1] = this.cellularArray[4*p.x+1][p.y];
		statebytes[2] = this.cellularArray[4*p.x+2][p.y];		
		statebytes[3] = this.cellularArray[4*p.x+3][p.y];
		statebytes[4] = this.cellularArray[inx][iny];
		statebytes[5] = this.cellularArray[iwx][iwy];
		statebytes[6] = this.cellularArray[isx][isy];
		statebytes[7] = this.cellularArray[iex][iey];		
		
		statebytes[8] = -1;
		
		return statebytes;
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		int pos = 4*p.x;
		
		for(int h=0; h<4; h++){
			if(this.cellularArray[pos][p.y]!=newstate[h]){
				this.imgArrayBuffer.addItem(new Cell(pos,p.y,this.cellularArray[pos][p.y]));
				this.cellularArray[pos][p.y] = newstate[h];
			}
			pos++;
		}
		
		return true;
	}
	
		
	protected void drawCellSpaceImage(Graphics g, Color[] myColor){
	
		byte[][] arraybuffer = this.cellularArray;
		
		int lx = this.LatticeScale.width;
		int ly = this.LatticeScale.height;
		int dx = this.GridSize.width;
		int dy = this.GridSize.height;
		int xsize = lx*dx;
		int ysize = ly*dy;
		int[][] x = new int[4][3];
		int[][] y = new int[4][3];
				
		g.setColor(myColor[0]);
		g.fillRect(0,0,xsize,ysize);
					
		for(int i=0; i<lx; i++){
			for(int j=0; j<ly; j++){
				x[0][0] = i*dx; x[0][1] = (i+1)*dx; x[0][2] = i*dx+dx/2;
				y[0][0] = j*dy; y[0][1] = y[0][0]; y[0][2] = j*dy+dy/2;
				x[3][0] = x[0][1]; x[3][1] = x[3][0]; x[3][2] = x[0][2];
				y[3][0] = y[0][0]; y[3][1] = (j+1)*dy; y[3][2] = y[0][2];
				x[2][0] = x[0][1]; x[2][1] = x[0][0]; x[2][2] = x[0][2];
				y[2][0] = y[3][1]; y[2][1] = y[3][1]; y[2][2] = y[0][2];
				x[1][0] = x[0][0]; x[1][1] = x[0][0]; x[1][2] = x[0][2];
				y[1][0] = y[3][1]; y[1][1] = y[0][0]; y[1][2] = y[0][2];
				
				for(int k=0; k<4; k++){
				  	if(arraybuffer[i*4+k][j]>0){
				 	 	g.setColor(myColor[arraybuffer[i*4+k][j]+1]);
				 	 	g.fillPolygon(x[k],y[k],3);
					 }
				}
			}
		}
				
				
		if(dx > 7){
			g.setColor(myColor[1]);
			
			for(int i=0; i<=lx; i++){
				g.drawLine(i*dx, 0, i*dx, ysize);
			}
			for(int i=0; i<=ly; i++){
				g.drawLine(0, i*dy, xsize, i*dy);
			}
						
			for(int i=0; i<lx; i++){
				for(int j=0; j<ly; j++){
					g.drawLine(i*dx,j*dy,(i+1)*dx,(j+1)*dy);
					g.drawLine((i+1)*dx,j*dy,i*dx,(j+1)*dy);
				}
			}
		}
		
	}
	
	public void addNeighboringCellsToBuffer(int i, int j){
		int nx = this.LatticeScale.width;
		int ny = this.LatticeScale.height;
				
		this.updatebuffer.addCellToBuffer(i, j);
		this.updatebuffer.addCellToBuffer(i, (j>0? j-1 : ny-1));
		this.updatebuffer.addCellToBuffer((i>0? i-1 : nx-1), j);
		this.updatebuffer.addCellToBuffer( i, (j<(ny-1)? j+1 : 0));
		this.updatebuffer.addCellToBuffer((i<(nx-1)? i+1 : 0), j);
	}
	
	
	public void Trivialoutput(){
		System.out.println("This is a partitioned cellular space");
	}
	
	
} /*end of PCA class */


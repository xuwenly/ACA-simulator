package com.example.demo;

class BlockMnTransitionRuleList extends BlockVnTransitionRuleList{
	
	public BlockMnTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String rotLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+rotate(rotate(r)));
	}
	
};

class BlockMnArray extends MnArray{
	
	public static BlockMnArray Create(Aca a, Board b, CParameter p)
	{
		BlockMnArray c = new BlockMnArray(b, p, 100);
		a.setRateItems(0, false);
		c.SetUpdateRate(0);
		
		return c;	
	}
	
	protected BlockMnArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new BlockMnTransitionRuleList(rules,flag);
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
	
		int inx = p.x,iny = (p.y>0? p.y-1 : ny-1);
		int iwx = (p.x>0? p.x-1 : nx-1), iwy = p.y;
		int isx = p.x, isy = (p.y<(ny-1)? p.y+1 : 0);
		int iex = (p.x<(nx-1)? p.x+1 : 0), iey = p.y;
		int[] xx = new int[9];
		int[] yy = new int[9];
		
		xx[0] = p.x; yy[0] = p.y;
		xx[1] = inx; yy[1] = iny;
		xx[2] = iwx; yy[2] = iny;
		xx[3] = iwx; yy[3] = iwy;
		xx[4] = iwx; yy[4] = isy;
		xx[5] = isx; yy[5] = isy;
		xx[6] = iex; yy[6] = isy;
		xx[7] = iex; yy[7] = iey;
		xx[8] = iex; yy[8] = iny;
			
		for(int k=0; k<9; k++){
			this.imgArrayBuffer.addItem(new Cell(xx[k],yy[k],this.cellularArray[xx[k]][yy[k]]));
			this.cellularArray[xx[k]][yy[k]] = newstate[k];
		}
		
		return true;
	}
	
	public void addNeighboringCellsToBuffer(int i, int j){
		int nx = this.ArraySize.width;
		int ny = this.ArraySize.height;
		int inx = i, iny = (j>0? j-1 : ny-1);
		int iwx = (i>0? i-1 : nx-1), iwy = j;
		int isx = i, isy = (j<(ny-1)? j+1 : 0);
		int iex = (i<(nx-1)? i+1 : 0), iey = j;
		
		super.addNeighboringCellsToBuffer(i, j);
		super.addNeighboringCellsToBuffer(inx, iny);
		super.addNeighboringCellsToBuffer(iwx, iwy);
		super.addNeighboringCellsToBuffer(isx, isy);
		super.addNeighboringCellsToBuffer(iex, iey);
		super.addNeighboringCellsToBuffer(iwx, iny);
		super.addNeighboringCellsToBuffer(iwx, isy);
		super.addNeighboringCellsToBuffer(iex, isy);
		super.addNeighboringCellsToBuffer(iex, iny);
		
	}
	
	public void Trivialoutput(){
		System.out.println("This is a blocked Moore-neighborhood cellular space");
	}
	
	
	
}
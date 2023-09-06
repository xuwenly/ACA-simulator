package com.example.demo;

class BlockVnTransitionRuleList extends TransitionRuleList{
	
	public BlockVnTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String rotLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+rotate(r));
	}
	
	protected String rotRight(String right){
		return rotLeft(right);
	}
	
	protected String revLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+reverse(r));
	}
		
	protected String revRight(String right){
		return revLeft(right);
	}
};


class BlockVnArray extends VnArray{
	
	public static BlockVnArray Create(Aca a, Board b, CParameter p)
	{
		BlockVnArray c = new BlockVnArray(b, p, 100);
		a.setRateItems(0, false);
		c.SetUpdateRate(0);
		
		return c;	
	}
	
	protected BlockVnArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}	
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new BlockVnTransitionRuleList(rules,flag);
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
		int[] xx = new int[5];
		int[] yy = new int[5];
		
		xx[0] = p.x; yy[0] = p.y;
		xx[1] = p.x; yy[1] = (p.y>0? p.y-1 : ny-1);
		xx[2] = (p.x>0? p.x-1 : nx-1);  yy[2] = p.y;
		xx[3] = p.x; yy[3] = (p.y<(ny-1)? p.y+1 : 0);
		xx[4] = (p.x<(nx-1)? p.x+1 : 0); yy[4] = p.y;
			
		for(int k=0; k<5; k++){
			this.imgArrayBuffer.addItem(new Cell(xx[k],yy[k],this.cellularArray[xx[k]][yy[k]]));
			this.cellularArray[xx[k]][yy[k]] = newstate[k];
		}
		
		return true;
	}
	
	public void addNeighboringCellsToBuffer(int i, int j){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
				
		super.addNeighboringCellsToBuffer(i, j);
		super.addNeighboringCellsToBuffer(i, (j>0? j-1 : ny-1));
		super.addNeighboringCellsToBuffer((i>0? i-1 : nx-1), j);
		super.addNeighboringCellsToBuffer(i, (j<(ny-1)? j+1 : 0));
		super.addNeighboringCellsToBuffer((i<(nx-1)? i+1 : 0), j);
	}
		
	public void Trivialoutput(){
		System.out.println("This is a blocked Neumann-neighborhood cellular space");
	}
	
};
	
	
	
	

       		
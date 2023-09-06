package com.example.demo;

class MnArray extends VnArray{
	
	public static MnArray Create(Aca a, Board b, CParameter p)
	{
		MnArray c = new MnArray(b, p, 200);
		a.setRateItems(50, true);
		c.SetUpdateRate(50);
		
		return c;	
	}
	
	protected MnArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}

	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new MnTransitionRuleList(rules,flag);
	}
		
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
				
		byte[] statebytes = new byte[10];
		
		int inx = p.x,iny = (p.y>0? p.y-1 : ny-1);
		int iwx = (p.x>0? p.x-1 : nx-1), iwy = p.y;
		int isx = p.x, isy = (p.y<(ny-1)? p.y+1 : 0);
		int iex = (p.x<(nx-1)? p.x+1 : 0), iey = p.y;
					
		statebytes[0] = this.cellularArray[p.x][p.y];
		statebytes[1] = this.cellularArray[inx][iny];
		statebytes[2] = this.cellularArray[iwx][iny];
	    statebytes[3] = this.cellularArray[iwx][iwy];
		statebytes[4] = this.cellularArray[iwx][isy];
		statebytes[5] = this.cellularArray[isx][isy];
		statebytes[6] = this.cellularArray[iex][isy];
		statebytes[7] = this.cellularArray[iex][iey];
		statebytes[8] = this.cellularArray[iex][iny];
		statebytes[9] = -1;
		
		if(is_totalistic){
			int nstate = this.totalStateNumber;
			byte[] stateindex = new byte[nstate+2];
			
			for(int t=1; t<(nstate+2); t++){
				stateindex[t] = 0;
			}
			stateindex[0] = statebytes[0];
			for(int m=1; m<9; m++){
				stateindex[statebytes[m]+1]++;
			}
			stateindex[nstate+1] = -1;
			return stateindex;
		}
		
		return statebytes;
			
	}
		
	public void addNeighboringCellsToBuffer(int i, int j){
		int nx = this.ArraySize.width;
		int ny = this.ArraySize.height;
		int inx = i, iny = (j>0? j-1 : ny-1);
		int iwx = (i>0? i-1 : nx-1), iwy = j;
		int isx = i, isy = (j<(ny-1)? j+1 : 0);
		int iex = (i<(nx-1)? i+1 : 0), iey = j;
		
		this.updatebuffer.addCellToBuffer(i, j);
		this.updatebuffer.addCellToBuffer(inx, iny);
		this.updatebuffer.addCellToBuffer(iwx, iwy);
		this.updatebuffer.addCellToBuffer(isx, isy);
		this.updatebuffer.addCellToBuffer(iex, iey);
		this.updatebuffer.addCellToBuffer(iwx, iny);
		this.updatebuffer.addCellToBuffer(iwx, isy);
		this.updatebuffer.addCellToBuffer(iex, isy);
		this.updatebuffer.addCellToBuffer(iex, iny);
		
	}
	
	
	
	public void Trivialoutput(){
		System.out.println("This is a Moore-neighborhood cellular space");
	}

};

class MnTransitionRuleList extends VnTransitionRuleList{
	
	public MnTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String rotLeft(String left){
		String r = left.substring(1);
		return (String)(left.charAt(0)+rotate(rotate(r)));
	}
	
	
};
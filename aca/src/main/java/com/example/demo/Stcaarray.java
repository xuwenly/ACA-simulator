package com.example.demo;

class StcaTransitionRuleList extends PcaTransitionRuleList{
	
	public StcaTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, flag);
	}
	
	protected String getRightHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<0 || dot==(rule.length()-1)) return null;
		String right = rule.substring(dot+1);
		if(right.length()<8) return null;
		return rule.substring(dot+1);
	}
		
	protected String rotRight(String right){
		return rotLeft(right);
	}
	
	protected String revRight(String right){
		return revLeft(right);
	}

};

class StcaArray extends PcaArray{
	
	public static StcaArray Create(Aca a, Board b, CParameter p)
	{
		StcaArray c = new StcaArray(b, p, 100);
		a.setRateItems(0, false);
		c.SetUpdateRate(0);
		
		return c;	
	}
	
	protected StcaArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);					
	}
		
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new StcaTransitionRuleList(rules,flag);
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		if(key<0) return false;
		
		byte[] newstate = this.RuleTable.getBody(key);
		int[] xx = new int [8];

		int[] yy = new int [8];
		
		/*
		xx[4] = 4*p.x; yy[4] = p.y;
		xx[5] = xx[4]+1; yy[5] = p.y;
		xx[6] = xx[4]+2; yy[6] = p.y;
		xx[7] = xx[4]+3; yy[7] = p.y;
		xx[0] = xx[4]+2; yy [0] = (p.y>0? p.y-1 : this.LatticeScale.height-1);
		xx[1] = 4*(p.x>0? p.x-1 :  this.LatticeScale.width-1)+3; yy[1] = p.y;
		xx[2] = xx[4]; yy[2] = (p.y<(this.LatticeScale.height-1)? p.y+1 : 0);
		xx[3] = 4*(p.x<(this.LatticeScale.width-1)? p.x+1 : 0)+1; yy[3] = p.y;
		*/
		
		xx[0] = 4*p.x; yy[0] = p.y;
		xx[1] = xx[0]+1; yy[1] = p.y;
		xx[2] = xx[0]+2; yy[2] = p.y;
		xx[3] = xx[0]+3; yy[3] = p.y;
		xx[4] = xx[0]+2; yy [4] = (p.y>0? p.y-1 : this.LatticeScale.height-1);
		xx[5] = 4*(p.x>0? p.x-1 :  this.LatticeScale.width-1)+3; yy[5] = p.y;
		xx[6] = xx[0]; yy[6] = (p.y<(this.LatticeScale.height-1)? p.y+1 : 0);
		xx[7] = 4*(p.x<(this.LatticeScale.width-1)? p.x+1 : 0)+1; yy[7] = p.y;
		
		
		for(int h=0; h<8; h++){
			this.imgArrayBuffer.addItem(new Cell(xx[h],yy[h],this.cellularArray[xx[h]][yy[h]]));
			this.cellularArray[xx[h]][yy[h]] = newstate[h];
		}		
		return true;
	}
	
	public void Trivialoutput(){
		System.out.println("This is a self-timed cellular space");
	}
	
	
};



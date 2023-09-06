package com.example.demo;

class AcaRunThread extends Thread{
	protected Board board;
	protected CellularAutomatonArray cellulararray;
	protected volatile boolean run_permission = true;
	protected volatile int updatedelay;
	protected int flag;
	
	public AcaRunThread(Board bd, CellularAutomatonArray ca, int ud, int fg){
		this.board = bd;
		this.cellulararray = ca;
		this.updatedelay = ud;
		this.flag = fg;
	}
	
	public void stopThread(){
		run_permission = false;
	}
	
	public void setUpdateDelay(int udelay){
		this.updatedelay = udelay;
	}
	
	public void run(){
		while(run_permission){
			cellulararray.cellStateTransitions(this.flag);
			board.repaint();
			if(run_permission){
			
				if(this.updatedelay<20){
					int count = this.updatedelay*2000;
					int temp = 0;
					for(int i=0; i<count; i++){
						temp++;
					}
				}else{
					try{
						Thread.sleep(this.updatedelay-19);
					}catch(InterruptedException e){ }
				}
			}
	    }
	}
	
}


	
	
	
	
	


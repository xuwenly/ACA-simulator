package com.example.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

class CellClipBoard{

	protected Dimension copiedarea;
	protected Dimension origscale, zoomscale;
	protected Point copyposition, zoomposition, dragposition;
	protected int rotation_f, reflection_f;
		
	public CellClipBoard(Dimension ca, Point cp, Dimension gd){
		
		this.copiedarea = new Dimension(ca);
		this.copyposition = new Point(cp);
		this.origscale = new Dimension(gd);
		this.zoomposition = new Point(gd.width*cp.x, gd.height*cp.y);
		this.zoomscale = new Dimension(gd);
		
		this.rotation_f = 0;
		this.reflection_f = 0;
	}
	
	protected void ClipBoardZoom(boolean zoomin){
		if(zoomin){
			this.zoomscale.width += 1;
			this.zoomscale.height +=1;
			this.zoomposition.x -= (this.copiedarea.width + 1)/2;
			this.zoomposition.y -= (this.copiedarea.height + 1)/2;
		}else{
			if((this.zoomscale.width > 0) && (this.zoomscale.height > 0)){
				this.zoomscale.width -=1;
				this.zoomscale.height -=1;
				this.zoomposition.x += (this.copiedarea.width + 1)/2;
				this.zoomposition.y += (this.copiedarea.height + 1)/2;
			}
		}
	}
	
	protected void ClipBoardRestore(){
		this.zoomscale = new Dimension(this.origscale);
		this.zoomposition = new Point(this.copyposition.x*this.origscale.width, this.copyposition.y*this.origscale.height);
		this.rotation_f = 0;
		this.reflection_f = 0;
	}
	
	protected void ClipBoardMove(int direction){
		
		switch(direction){
			case 0:	this.zoomposition.y -= this.origscale.height;  //up
					break;
			case 1: this.zoomposition.y += this.origscale.height; //down
					break;
			case 2:	this.zoomposition.x -= this.origscale.width; //left
					break;
			case 3:	this.zoomposition.x += this.origscale.width; //right
					break; 
			case 4: this.rotation_f = (this.rotation_f+1)%4; //rotate
					break;
			case 5:	this.reflection_f = (this.reflection_f==0? 10 : 0); //reflect
					break;
		}
	}
	
	//protected void ClipBoardAdministrate(CellularAutomatonArray ca, int action){
	public void ClipBoardAdministrate( byte[][] currentArray,
									   ImageStack imgArrayBuffer,
	                                   Dimension ArraySize,
	                                   int action)	
	{
		//byte[][] currentArray = ca.getArray();
		//Dimension ArraySize = ca.getArraySize();
		//ImageStack imgArrayBuffer = ca.getImgStack();
		       
		int nx = this.copiedarea.width+1;
		int ny = this.copiedarea.height+1;
		int nnx = ArraySize.width;
		int nny = ArraySize.height;
		int origx = this.copyposition.x;
		int origy = this.copyposition.y;
		int max = (nx<ny? ny : nx);
		int rot = this.rotation_f;
		int ref = this.reflection_f;
		
		boolean erase = (action==2? false : true);
			
		byte[][] temp = new byte[max][max];
										
		for(int i=0; i<nx; i++){
			for(int j=0; j<ny; j++){
				try{
					byte value = currentArray[origx+i][origy+j];
					if(erase){
						currentArray[origx+i][origy+j] = 0;
						if(value>0) imgArrayBuffer.addItem(origx+i,origy+j,value);
					}					
						
					switch(rot+ref){
						case 0:	temp[i][j] = value;
								break;
								//
						case 10: temp[i][ny-1-j] = value;
								break;
								//
						case 1:	temp[j][nx-1-i] = value;
								break;
									//
						case 11: temp[j][i] = value;
								break;
								//
						case 2:	temp[nx-1-i][ny-1-j] = value;
								break;
								//
						case 12: temp[nx-1-i][j] = value;
								break;
								//
						case 3:	temp[ny-1-j][i] = value;
								break;
									//
						case 13: temp[ny-1-j][nx-1-i] = value;
								break;
					}
					
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					//throw e;
				}					
			}
		}
		
		if(action < 2) return;
			
		if((rot%2) == 1){
			int t = nx; nx = ny; ny = t;
		}
				//System.out.println(action);
		int xx = this.zoomposition.x/this.origscale.width;
		int yy = this.zoomposition.y/this.origscale.height;
						
		for(int i=0; i<nx; i++){
			for(int j=0; j<ny; j++){
				if(temp[i][j]>0){
					int ii = xx+i;
					int jj = yy+j;
						
					if(ii<0) ii = nnx + ii%nnx;
						else if(ii>(nnx-1)) ii = ii%nnx;
					if(jj<0) jj = nny + jj%nny;
						else if(jj>(nny-1)) jj = jj%nny;
						
					try{
						if(currentArray[ii][jj]==temp[i][j]) continue;
						imgArrayBuffer.addItem(new Cell(ii,jj,currentArray[ii][jj]));	
						currentArray[ii][jj] = temp[i][j];
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
							//throw e;
					}
						
				}
			}
		}		
			
	}

	
	public boolean ClipBoardMousePressed(int x, int y){
		int xs = this.zoomposition.x;
		int ys = this.zoomposition.y;
		int ws = (this.copiedarea.width+1)*this.zoomscale.width;
		int ls = (this.copiedarea.height+1)*this.zoomscale.height;
		
		if(this.rotation_f %2 == 1){
			int temp = ws;
			ws = ls;
			ls = temp;
		}
		if((xs<x) && (x<(xs+ws)) && (ys<y) && (y<(ys+ls))){
			this.dragposition = new Point(x,y);
			return true;
		}
		return false;
	}
	
	public void ClipBoardMouseDragged(int x, int y){
		int xs = this.zoomposition.x + x - this.dragposition.x;
		int ys = this.zoomposition.y + y - this.dragposition.y;
					
		this.zoomposition = new Point(xs, ys);
		this.dragposition = new Point(x, y);
	}
	
	public Point ClipBoardMouseClicked(int x, int y){
		int dx = this.origscale.width;
		int dy = this.origscale.height;
		int ddx = this.zoomscale.width;
		int ddy = this.zoomscale.height;
		int xx = this.zoomposition.x;
		int yy = this.zoomposition.y;
		int dxx = this.copyposition.x*dx;
		int dyy = this.copyposition.y*dy;
		int rot = this.rotation_f;
		int ref = this.reflection_f;
		int nx, ny, origx=0, origy=0;
				
		if(rot%2 == 0){
			nx = this.copiedarea.width;
			ny = this.copiedarea.height;
		}else{
			ny = this.copiedarea.width;
			nx = this.copiedarea.height;
		}
		
		if((x<=xx) || (x>=(xx+(nx+1)*ddx)) || (y<=yy) || (y>=(yy+(ny+1)*ddy)))
			return null; 			
				
		switch(rot+ref){
		
			case 0:	origx = (x-xx)*dx/ddx +dxx;
					origy = (y-yy)*dy/ddy+dyy;
					break;
			case 10: origx = (x-xx)*dx/ddx+dxx;
					 origy = ((ny+1)*ddy-y+yy)*dy/ddy+dyy;
					break;
			case 1:	origx = ((ny+1)*ddy-y+yy)*dy/ddy+dxx;
					origy = (x-xx)*dx/ddx+dyy;
					break;
			case 11: origx = (y-yy)*dy/ddy+dxx;
					 origy = (x-xx)*dx/ddx+dyy;
					break;
			case 2:	origx = ((nx+1)*ddx-x+xx)*dx/ddx+dxx;
					origy = ((ny+1)*ddy-y+yy)*dy/ddy+dyy;
					break;
			case 12: origx = ((nx+1)*ddx-x+xx)*dx/ddx+dxx;
					 origy = (y-yy)*dy/ddy+dyy;
					break;
			case 3:	origx = (y-yy)*dy/ddy+dxx;
					origy = ((nx+1)*ddx-x+xx)*dx/ddx+dyy;
					break;
			case 13: origx = ((ny+1)*ddy-y+yy)*dy/ddy+dxx;
					 origy = ((nx+1)*ddx-x+xx)*dx/ddx+dyy;
					break;
		
		}
		
		return(new Point(origx, origy));
	}
	
	public void keyEvent(int action, CellularAutomatonArray ca){
		
		switch(action){
			case 0:   //undo
					ClipBoardRestore();
					break;
			case 1:   //cut
			case 2:   //paste
			case 3:   //move
					//ClipBoardAdministrate(ca, action);
					break;		  			
			case 4:   //left
					ClipBoardMove(2);
					break;
			case 5:   //up
					ClipBoardMove(0);
					break;
			case 6:   //right
					ClipBoardMove(3);
					break;
			case 7:   //down
					ClipBoardMove(1);
					break;
			case 8:   //rotation
					ClipBoardMove(4);
					break;
			case 9:   //reflection
					ClipBoardMove(5);
					break;
			case 10:  //zoomout
					ClipBoardZoom(false);
					break;
			case 11:  //zoomin
					ClipBoardZoom(true);
					break;
		
		}
	}
		
				
	//public void createClipBoardImage(CellularAutomatonArray ca, Graphics g, Color[] myColor){
	public void createClipBoardImage(byte[][] arraybuffer, Graphics g, Color[] myColor){
		
		//byte[][] arraybuffer = ca.getArray();
		int dx = this.zoomscale.width;
  		int dy = this.zoomscale.height;
  		int ddx = this.origscale.width;
  		int ddy = this.origscale.height;
  		int nx =  this.copiedarea.width + 1;
  		int ny = this.copiedarea.height + 1;
  		int nnx = (this.rotation_f%2 == 1? ny :  nx);
  		int nny = (this.rotation_f%2 == 1? nx :  ny);
  		int origx = this.copyposition.x;
  		int origy = this.copyposition.y;
  		int posx = this.zoomposition.x;
  		int posy = this.zoomposition.y;
  		int rotref = this.rotation_f + this.reflection_f;
  	  		
		if((ddx != dx) || (ddy != dy)){
  			g.setColor(myColor[1]);
  			g.fillRect(posx, posy, nnx*dx, nny*dy);
  		}
  		
  		switch(rotref){
			case 0:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+i*dx, posy+j*dy, dx, dy);
							}
						}
					}
					break;
					//
			case 10:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+i*dx, posy+(ny-1-j)*dy, dx,dy);
							}
						}
					}
					break;
					//
			case 1: 	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect(posx+j*dx, posy+(nx-1-i)*dx, dx, dy);
							}
						}
					}
					break;
					//
			case 11:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+j*dy, posy+i*dy, dx, dy);
							}
						}
					}
					break;
					//
			case 2:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+(nx-1-i)*dx, posy+(ny-1-j)*dy, dx, dy);
							}
						}
					}
					break;
					//
			case 12:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+(nx-1-i)*dx, posy+j*dy, dx, dy);
							}
						}
					}
					break;
					//
			case 3:	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+(ny-1-j)*dx, posy+i*dy, dx, dy);
							}
						}
					}
					break;
					//
			case 13: 	for(int i=0; i<nx; i++){
						for(int j=0; j<ny; j++){
							if(arraybuffer[i+origx][j+origy]>0){
								g.setColor(myColor[arraybuffer[i+origx][j+origy]+1]);
								g.fillRect( posx+(ny-1-j)*dx, posy+(nx-1-i)*dy, dx, dy);
							}
						}
					}
					break;
		}
		
		g.setColor(Color.red);
  		g.drawRect(posx, posy, nnx*dx, nny*dy);
  		g.drawString("Clip Window", posx, posy);
		
		if((ddx < dx) || (ddy < dy)){
  			g.setColor(myColor[0]); 
  			for(int i=1; i<nnx; i++){
  				g.drawLine(posx+i*dx, posy, posx+i*dx, posy+nny*dy);
  			}
  			for(int i=1; i<nny; i++){
  				g.drawLine(posx, posy+i*dy, posx+nnx*dx, posy+i*dy);
  			}
  		}
  	}
  
  
  }
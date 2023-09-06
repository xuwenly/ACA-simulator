package com.example.demo;

import java.awt.*;

class pcaClipBoard extends CellClipBoard{
	private int[][] dxx, dyy;
			
	public pcaClipBoard(Dimension ca, Point cp, Dimension gd){
		super(ca, cp, gd);
		dxx = new int[4][];
		dyy = new int[4][];
	}
	
	public void ClipBoardAdministrate(byte[][] currentArray,
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
		int origx = 4*this.copyposition.x;
		int origy = this.copyposition.y;
		int max = Math.max(nx, ny);
		int rot = this.rotation_f;
		int ref = this.reflection_f;
		
		boolean erase = (action==2? false : true);
				
		byte[][] temp = new byte[4*max][4*max];
		byte[] value = new byte[4];
							
		for(int i=0; i<nx; i++){
			for(int j= 0; j<ny; j++){
				try{
					for(int k=0; k<4; k++){
						int posx = origx+4*i+k, posy = origy+j;
						
						value[k] = currentArray[posx][posy];
						if(erase){
							currentArray[posx][posy] = 0;
							if(value[k]>0) imgArrayBuffer.addItem(posx,posy,value[k]);
						}
					}
					int ii, jj;				
					
					switch(rot+ref){
						case 0:	ii=4*i; jj=j;
								temp[ii][jj] = value[0]; temp[ii+1][jj] = value[1];
								temp[ii+2][jj] = value[2]; temp[ii+3][jj] = value[3];
								break;
								//
						case 10:	ii=4*i; jj=ny-1-j;
								temp[ii][jj] = value[2]; temp[ii+1][jj] = value[1];
								temp[ii+2][jj] = value[0]; temp[ii+3][jj] = value[3];
								break;
								//									
						case 1:	ii=4*j; jj=nx-1-i;
								temp[ii][jj] = value[3]; temp[ii+1][jj] = value[0];
								temp[ii+2][jj] = value[1]; temp[ii+3][jj] = value[2];
								break;
								//
						case 11:	ii=4*j; jj=i;
								temp[ii][jj] = value[1]; temp[ii+1][jj] = value[0];
								temp[ii+2][jj] = value[3]; temp[ii+3][jj] = value[2];
								break;
								//
						case 2:	ii=4*(nx-1-i); jj=ny-1-j;
								temp[ii][jj] = value[2]; temp[ii+1][jj] = value[3];
								temp[ii+2][jj] = value[0]; temp[ii+3][jj] = value[1];
								break;
								//
						case 12:	ii=4*(nx-1-i); jj=j;
								temp[ii][jj] = value[0]; temp[ii+1][jj] = value[3];
								temp[ii+2][jj] = value[2]; temp[ii+3][jj] = value[1];
								break;
								//
						case 3:	ii=4*(ny-1-j); jj=i;
								temp[ii][jj] = value[1]; temp[ii+1][jj] = value[2];
								temp[ii+2][jj] = value[3]; temp[ii+3][jj] = value[0];
								break;
								//
						case 13:	ii=4*(ny-1-j); jj=nx-1-i;
								temp[ii][jj] = value[3]; temp[ii+1][jj] = value[2];
								temp[ii+2][jj] = value[1]; temp[ii+3][jj] = value[0];
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
			
		int zx = 4*(this.zoomposition.x/this.origscale.width);
		int zy = this.zoomposition.y/this.origscale.height;
					
		for(int i=0; i<4*nx; i++){
			for(int j=0; j<ny; j++){
				if(temp[i][j]>0){
						
					int ii = zx+i;
					int jj = zy+j;
						
					if(ii<0) ii = nnx + ii%nnx;
						else if(ii>(nnx-1)) ii = ii%nnx;
					if(jj<0) jj = nny + jj%nny;
						else if(jj>(nny-1)) jj = jj%nny;
					try{
						if(currentArray[ii][jj]==temp[i][j]) continue;
						imgArrayBuffer.addItem(ii,jj,currentArray[ii][jj]);	
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

	
	private void setIndividualCellPosition(int rotref, int origx, int origy, int dx, int dy){
		
		int[][] sdx = new int[4][3];
		int[][] sdy = new int[4][3];
				
		sdx[0][0] = origx+dx/2; sdx[0][1] = origx; sdx[0][2] = origx+dx;
		sdy[0][0] = origy+dy/2; sdy[0][1] = origy; sdy[0][2] = origy;
		sdx[1][0] = sdx[0][0]; sdx[1][1] = origx; sdx[1][2] = origx;
		sdy[1][0] = sdy[0][0]; sdy[1][1] = origy+dy; sdy[1][2] = origy;
		sdx[2][0] = sdx[0][0]; sdx[2][1] = sdx[0][2]; sdx[2][2] = origx;
		sdy[2][0] = sdy[0][0]; sdy[2][1] = sdy[1][1]; sdy[2][2] = sdy[1][1];
		sdx[3][0] = sdx[0][0]; sdx[3][1] = sdx[0][2]; sdx[3][2] = sdx[0][2];
		sdy[3][0] = sdy[0][0]; sdy[3][1] = origy; sdy[3][2] = sdy[1][1];
		
				
		switch(rotref){
			case 0:	dxx[0] = sdx[0]; dxx[1] = sdx[1]; dxx[2] = sdx[2]; dxx[3] = sdx[3];
					dyy[0] = sdy[0]; dyy[1] = sdy[1]; dyy[2] = sdy[2]; dyy[3] = sdy[3];
					break;
			case 10:dxx[2] = sdx[0]; dxx[1] = sdx[1]; dxx[0] = sdx[2]; dxx[3] = sdx[3];
					dyy[2] = sdy[0]; dyy[1] = sdy[1]; dyy[0] = sdy[2]; dyy[3] = sdy[3];
					break;	
			case 1:	dxx[0] = sdx[1]; dxx[1] = sdx[2]; dxx[2] = sdx[3]; dxx[3] = sdx[0];
					dyy[0] = sdy[1]; dyy[1] = sdy[2]; dyy[2] = sdy[3]; dyy[3] = sdy[0];
					break;
			case 11:dxx[0] = sdx[1]; dxx[3] = sdx[2]; dxx[2] = sdx[3]; dxx[1] = sdx[0];
					dyy[0] = sdy[1]; dyy[3] = sdy[2]; dyy[2] = sdy[3]; dyy[1] = sdy[0];
					break;
			case 2:	dxx[0] = sdx[2]; dxx[1] = sdx[3]; dxx[2] = sdx[0]; dxx[3] = sdx[1];
					dyy[0] = sdy[2]; dyy[1] = sdy[3]; dyy[2] = sdy[0]; dyy[3] = sdy[1];
					break;
			case 12:dxx[2] = sdx[2]; dxx[1] = sdx[3]; dxx[0] = sdx[0]; dxx[3] = sdx[1];
					dyy[2] = sdy[2]; dyy[1] = sdy[3]; dyy[0] = sdy[0]; dyy[3] = sdy[1];
					break;
			case 3:	dxx[0] = sdx[3]; dxx[1] = sdx[0]; dxx[2] = sdx[1]; dxx[3] = sdx[2];
					dyy[0] = sdy[3]; dyy[1] = sdy[0]; dyy[2] = sdy[1]; dyy[3] = sdy[2];
					break;
			case 13:dxx[0] = sdx[3]; dxx[3] = sdx[0]; dxx[2] = sdx[1]; dxx[1] = sdx[2];
					dyy[0] = sdy[3]; dyy[3] = sdy[0]; dyy[2] = sdy[1]; dyy[1] = sdy[2];
					break;
		
		}
		
		
	}
		
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
  		//boolean is_Running = this.stca.getRunningStatus();
  		
  		if((ddx != dx) || (ddy != dy)){
  			g.setColor(myColor[1]);
  			g.fillRect(posx, posy, nnx*dx, nny*dy);
  		} //else is_Running = true;
  		
  		  		
  		for(int i=0; i<nx; i++){
			for(int j=0; j<ny; j++){
				
				switch(rotref){
					case 0:	 setIndividualCellPosition(0,posx+i*dx,posy+j*dy, dx, dy);
							 break;
							//		
					case 10: setIndividualCellPosition(10,posx+i*dx,posy+(ny-1-j)*dy, dx, dy);
					         break;
							//
					case 1:  setIndividualCellPosition(1,posx+j*dx,posy+(nx-1-i)*dy, dx, dy);
							 break;
							//
					case 11: setIndividualCellPosition(11,posx+j*dy,posy+i*dy, dx, dy);
							 break;
							//
					case 2:	 setIndividualCellPosition(2,posx+(nx-1-i)*dy,posy+(ny-1-j)*dy, dx, dy);
							 break;
							//
					case 12: setIndividualCellPosition(12,posx+(nx-1-i)*dy,posy+j*dy, dx, dy);
							 break;
							//
					case 3:  setIndividualCellPosition(3,posx+(ny-1-j)*dy,posy+i*dy, dx, dy);
							 break;
							//
					case 13: setIndividualCellPosition(13,posx+(ny-1-j)*dy,posy+(nx-1-i)*dy, dx, dy);
							 break;
				}
				
				for(int k=0; k<4; k++){
					if(arraybuffer[(i+origx)*4+k][j+origy]>0){
						g.setColor(myColor[arraybuffer[(i+origx)*4+k][j+origy]+1]);
						g.fillPolygon(dxx[k],dyy[k],3);
					}
				}
			}
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
  			for(int i=0; i<nnx; i++){
  				for(int j=0; j<nny; j++){
  					g.drawLine(posx+i*dx,posy+j*dy,posx+(i+1)*dx,posy+(j+1)*dy);
  					g.drawLine(posx+(i+1)*dx,posy+j*dy,posx+i*dx,posy+(j+1)*dy);
  				}
  			}
  		}
		
		
	}
	
}
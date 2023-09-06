package com.example.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;

class NcArray extends VnArray{
	
	private int[] m_nindex;
	private int m_number;
	private Random m_random;
		
	public static NcArray Create(Aca a, Board b, CParameter p)
	{
		NcArray c = new NcArray(b, p, 200);
		a.setRateItems(50, true);
		c.SetUpdateRate(50);
		return c;	
	}
	
	protected NcArray(Board b, CParameter p, int stacklength){
		super(b, p, stacklength);
		m_nindex = new int [5];
		m_number = 0;
		m_random = new Random();
	}
	
	protected TransitionRuleList generateRuleList(VectorString rules, int flag){
		return new NcTransitionRuleList(rules,flag);
	}
	
	protected byte[] consTransitionLfh(CPoint p, boolean is_totalistic){
		
		return null;
	}
	
	protected boolean setTransitionRth(CPoint p, int key){
		final int nx = this.ArraySize.width;
		final int ny = this.ArraySize.height;
		int[] xx = new int[5];
		int[] yy = new int[5];
		
		xx[0] = p.x; yy[0] = p.y;
		xx[1] = p.x; yy[1] = (p.y>0? p.y-1 : ny-1);
		xx[2] = (p.x>0? p.x-1 : nx-1); yy[2] = p.y;
		xx[3] = p.x; yy[3] = (p.y<(ny-1)? p.y+1 : 0);
		xx[4] = (p.x<(nx-1)? p.x+1 : 0); yy[4] = p.y;
		
		m_number = 0;		
		byte[] statebytes = new byte[3];
		
		for(int k=1; k<5; k++)
		{
			statebytes[0] = this.cellularArray[xx[0]][yy[0]];				
		    statebytes[1] = this.cellularArray[xx[k]][yy[k]];
		    statebytes[2] = -1;
		    m_nindex[k] = 1;
		    
		    int pos = this.RuleTable.get(statebytes);
		    
		    if(pos<0)
		    {
		    	byte t = statebytes[1];				
			    statebytes[1] = statebytes[0];
			    statebytes[0] = t;
			    m_nindex[k] = -1;
			    pos = this.RuleTable.get(statebytes);			    
		    }
		    
		    if(pos<0){
		    	m_nindex[k] = 0;
		    	continue;
		    }
		    
		    //System.out.println("states : "+ statebytes[0]+statebytes[1]);
		   
		   m_nindex[k] *= (int)this.RuleTable.getBody(pos)[0];
		   if(m_nindex[k]!=0) m_number++;
		   //System.out.println("change : "+ pos + " " + this.RuleTable.getBody(pos)[0]+ " "+ m_nindex[k]);
		}
		
		if(m_number<1) return false;
		int index = m_random.nextInt(m_number);
		
		for(int k=1; k<5; k++)
		{
			if(m_nindex[k]==0) continue;
			if(index>0){
				index--;
			}else{
				this.imgArrayBuffer.addItem(new Cell(xx[0], yy[0], this.cellularArray[xx[0]][yy[0]]));
				this.imgArrayBuffer.addItem(new Cell(xx[k], yy[k], this.cellularArray[xx[k]][yy[k]]));
				int v0 = bTi(this.cellularArray[xx[0]][yy[0]]);
				int v1 = bTi(this.cellularArray[xx[k]][yy[k]]);
					
				this.cellularArray[xx[0]][yy[0]] = iTb(v0+m_nindex[k]);
				this.cellularArray[xx[k]][yy[k]] = iTb(v1-m_nindex[k]);	
				//System.out.println("new states : "+ this.cellularArray[xx[0]][yy[0]]+this.cellularArray[xx[k]][yy[k]]);
				break;
			}
				
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
			
		g.setColor(myColor[0]);
		g.fillRect(0,0,xsize,ysize);
							
		for(int i=0; i<lx; i++){
			for(int j=0; j<ly; j++){
				if(arraybuffer[i][j]>0){
					//g.setColor(myColor[arraybuffer[i][j]+1]);
					//g.fillRect( i*dx, j*dy, dx, dy);
					String val = Integer.toString(arraybuffer[i][j]<'A'? arraybuffer[i][j] : arraybuffer[i][j]-'A'+10);
					
					switch(Integer.parseInt(val))
					{
					case 7:
					case 8:
					case 9:
					case 15:
						g.setColor(myColor[3]);
						break;
					default:
						g.setColor(myColor[2]);
					}
						
					
					g.setFont(new Font("Serif", Font.BOLD, 10));
					FontMetrics fo = g.getFontMetrics();
					int w = fo.stringWidth(val);
					int h = fo.getAscent()-fo.getLeading();
					g.drawString(val, i*dx+(dx-w)/2+1, j*dy+(dy+h)/2);
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
	
	private int bTi(byte v)
	{
		return (int)(v<'A'? v-'0' : v-'A'+10);
	}
	
	private byte iTb(int c)
	{
		return (byte)(c<10? '0'+c : c+'A'-10);
	}
	
};

class NcTransitionRuleList extends VnTransitionRuleList{
	
	public NcTransitionRuleList(VectorString rulevec, int flag){
		super(rulevec, 0);
	}
	
	protected String getLeftHand(String rule){
		int dot1 = rule.indexOf(':');
		int dot2 = rule.lastIndexOf(':');
		if(dot1<1 || dot1==(rule.length()-1) || dot2-dot1<2) return null;
		int r1 = Integer.parseInt(rule.substring(0,dot1));
		int r2 = Integer.parseInt(rule.substring(dot1+1, dot2));
		//System.out.print("left: "+ r1 + " " + r2 + " right: ");
		if(r1>0xFF || r2>0xFF) return null;
		char b1 = (char) (r1<10? '0'+r1 : 'A'+r1-10);
		char b2 = (char) (r2<10? '0'+r2 : 'A'+r2-10);
       	return Character.toString(b1)+b2;
	}
	
	protected String getRightHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<1 || dot==(rule.length()-1)) return null;
		int r = Integer.parseInt(rule.substring(dot+1));
		
		if(r>0xFF) return null;
		String rr = Character.toString((char)(r<10? '0'+r : 'A'+r-10));
		//System.out.println(rr);
		return rr;
	}
	
	
		
};
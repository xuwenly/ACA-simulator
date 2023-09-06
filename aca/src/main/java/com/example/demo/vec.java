package com.example.demo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;

class VectorType<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3362561944240254481L;
	private T[] theVector;
	private int theSize;
	private int maxSize;
	private int MAX_INT;
	
	private void resize(int ns){
		if(ns<this.theSize) return;
		
		this.maxSize = ns;
		T[] newVector = newArray(maxSize);
		for(int i=0; i<theSize; i++){
			newVector[i] = theVector[i];
		}
		this.theVector = newVector;
	}
	
	@SuppressWarnings("unchecked")
	private T[] newArray(int sz)
	{
		return (T[]) new Object [sz];
	}
	
	public VectorType(int ss){
		this.theVector = newArray(ss);
		this.maxSize = ss;
		this.theSize = 0;
		this.MAX_INT = ss;
	}
	
	public VectorType(VectorType<T> vec){
		this.maxSize = vec.maxSize;
		this.MAX_INT = vec.MAX_INT;
		this.theSize = vec.theSize;
		
		this.theVector = newArray(maxSize);
		for(int i=0; i<theSize; i++){
			this.theVector[i] = vec.theVector[i];
		}
	}
	
	public void add(T item){
		if(this.theSize == this.maxSize){
			resize(this.maxSize+this.MAX_INT);
		}
		
		this.theVector[theSize] = item;
		this.theSize++;
	}
	
	public void push_back(T item)
	{
		add(item);
	}
	
	public boolean  pop_out()
	{
		if(this.theSize > 0){
			this.theSize--;
			return true;
		}
		return false;
	}
	
	public boolean pop(int itemNum)
	{
		if(itemNum<1 || itemNum>this.theSize) return false;
		this.theSize -= itemNum;
		return true;
	}
	
	public void set(T item, int pos){
		check(pos, 4);
		this.theVector[pos] = item;
	}
	
	public void insert(T item, int pos){
		
		check(pos, 3);
				
		int ss = this.theSize-1;
		add(this.theVector[ss]);
		for(int i=ss; i>pos; i--){
			this.theVector[i] = this.theVector[i-1];
		}
		this.theVector[pos] = item;
			
	}
	
	public void swap(int pos1, int pos2){
		check(pos1, 2);
		check(pos2, 2);
		
		T tmp = this.theVector[pos1];
		this.theVector[pos1] = this.theVector[pos2];
		this.theVector[pos2] = tmp;
		
	}
	
	public void remove(int pos){
		
		check(pos, 1);
		
		for(int i=pos+1; i<this.theSize; i++){
			this.theVector[i-1] = this.theVector[i];
		}
		this.theSize--;
	}
	
	public void removeAll(){
		
		this.theSize=0;
	}
	
	public T at(int pos){
		check(pos, 0);
		return this.theVector[pos];
		
	}
	
	public T get(int pos){
		return at(pos);
	}
	
	public int size(){
		return this.theSize;
	}
	
	public int capacity()
	{
		return this.maxSize;
	}
	
		
	public void clear(){
		this.maxSize = this.MAX_INT;
		this.theSize = 0;
		this.theVector = newArray(maxSize);
	}
	
	public void ensureCapacity(int nn){
		if(nn>0) this.MAX_INT = nn;
		resize(nn);
	}
	
	public void optimizeCapacity(){
		if(this.maxSize>this.theSize){
			resize(this.theSize);
		}
	}
	
	private void check(int pos, int code)
	{
		if(pos < 0 || pos >= this.theSize)
			throw new VecOutOfBoundException(code, "invalid index");
	}

};

class VectorInt extends VectorType<Integer>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6978085184162780037L;
	VectorInt(int ss)
	{
		super(ss);
	}
	VectorInt(VectorInt vec)
	{
		super(vec);
	}

};

class VectorShort extends VectorType<Short>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2991101860997870480L;
	VectorShort(int ss)
	{
		super(ss);
	}
	VectorShort(VectorShort vec)
	{
		super(vec);
	}
	
};

class VectorByte extends VectorType<Byte>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6991467265524496378L;
	VectorByte(int ss)
	{
		super(ss);
	}
	VectorByte(VectorByte vec)
	{
		super(vec);
	}

};

class VectorString extends VectorType<String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1360312729771548126L;
	VectorString(int ss)
	{
		super(ss);
	}
	VectorString(VectorString vec)
	{
		super(vec);
	}

};

class VectorBytes extends VectorType<byte[]>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2215975246561455462L;
	VectorBytes(int ss)
	{
		super(ss);
	}
	VectorBytes(VectorBytes vec)
	{
		super(vec);
	}

};

class Cell
{
	public short x;
	public short y;
	public byte s;
	
	public Cell(int xx, int yy, byte ss)
	{
		this.x = (short)xx;
		this.y = (short)yy;
		this.s = ss; 
	}
};

class CPoint
{
	public short x;
	public short y;
	public CPoint(int xx, int yy)
	{
		this.x = (short)xx;
		this.y = (short)yy;
	}	
	
};

class ImageLoader{
	public static Image initImage(Object obj, final String filename) throws Exception
	{
		Image image = null;
		
		try{
			//final URL fileurl = ClassLoader.getSystemResource(filename);
			final URL fileurl = obj.getClass().getClassLoader().getResource(filename);
				
			//System.out.println("Image file path " + fileurl.getPath());
			image = Toolkit.getDefaultToolkit().getImage(fileurl);
			
		}
		catch(Exception e)
		{
			String errmsg = "Unable to load image file with name: " + filename;
			throw new NullPointerException(errmsg);
		}		
					
		return image;
	}
};

class ACAConstructor
{
	public static CellularAutomatonArray Create(Aca a, Board b, CParameter p) throws Exception
	{
		if(p.model.equals("vnca")){
			return VnArray.Create(a, b, p);
		}
		else if(p.model.equals("blocvnca")){
			return BlockVnArray.Create(a, b, p);
		}
		else if(p.model.equals("blocmnca")){
			return BlockMnArray.Create(a, b, p);
		}
		else  if(p.model.equals("mnca")){
			return MnArray.Create(a, b, p);
		}
		else if(p.model.equals("pca")){
			return PcaArray.Create(a, b, p);
		}
		else if(p.model.equals("stca")){
			return StcaArray.Create(a, b, p);
		}
		else if(p.model.equals("spstca")){
			return SpstcaArray.Create(a, b, p);
		}
		else if(p.model.equals("ncca")){
			return NcArray.Create(a, b, p);
		}
		else if(p.model.equals("blocmgnca"))
			return BlockMgnArray.Create(a, b, p);
		else {
			ErrorMsg("Unknown model!!");
			throw new NullPointerException("failed to create cellular space");
		}
			
	}
	
	public static void ErrorMsg(final String msg)
	{
		System.err.println("Program abort with error emssage: " + msg);
		System.err.println(" ");
		System.err.println("usage: java [VM options: -Xmx***m] -jar <jarfile> ");
		System.err.println("                                   -m <vnca, mnca, pca, stca, blocvnca, blocmnca, blocmgnca>");
		System.err.println("                                   -s <state number>");
		System.err.println("                       [OPTIONS:");
		System.err.println("                                   -b <clipboard capacity>");
		System.err.println("                                   -d <width> <height>");
		System.err.println("                                   -g <grid size>]");
				
	}
};

class CParameter
{
	public String model;
	public int states;
	public Dimension space;
	public int gridsize;
	public int bufferlength;
	public String stopState;
	public int cTimes;
	
	CParameter(String m, int s, Dimension d, int g, int b, String ss, int t)
	{
		this.model = new String(m);
		this.states = s;
		this.space = new Dimension(d);
		this.gridsize = g;
		this.bufferlength = b;
		this.stopState = ss;
		this.cTimes = t;
	}
};

class VecOutOfBoundException extends ArrayIndexOutOfBoundsException
{
	private final int code;
	public VecOutOfBoundException(int code, String msg)
	{
		super(msg);
		this.code = code;
	}
	
	public final int code()
	{
		return this.code;
	}	
	
};

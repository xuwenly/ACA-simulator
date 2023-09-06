package com.example.demo;

import java.awt.*;

class TransitionRuleTable{
	
	private Point[] prefix;
	private int nstate;
	private VectorBytes keyTable;
	private VectorBytes bodyTable;
		
	public TransitionRuleTable(TransitionRuleList ruleList, int ns){
		
		this.keyTable = new VectorBytes(100);
		this.bodyTable = new VectorBytes(100);
		this.nstate = ns;
		
		this.nstate = ns;
		this.prefix = new Point[this.nstate];
		generateRuleTable(ruleList);
		confirmTable();
	}
	
	private void generateRuleTable(TransitionRuleList ruleList){
				
		int rulesize = ruleList.getRuleListSize();
				
		for(int k=0; k<rulesize; k++){
			
			String rule = ruleList.getRule(k);
			int dot = rule.lastIndexOf(':');
			String key = rule.substring(0,dot);
			String body = rule.substring(dot+1);
			
			this.keyTable.add(convertToBytes(key));
			this.bodyTable.add(convertToBytes(body));
			//put(key,body);
		}
					
	}
	
	
	private void confirmTable(){
		this.keyTable.optimizeCapacity();
		this.bodyTable.optimizeCapacity();
		
		int ll, rr=0;
		int max = this.keyTable.size();
				
		for(int i=0; i<this.nstate; i++){
			ll = rr;
			while(rr<max){
				byte[] key = this.keyTable.at(rr);
				if(key[0]==i) rr++; else break;
			}
			this.prefix[i] = new Point(ll,rr);
		}
				
		/*
		System.out.println("Total number of rules: " + this.keyTable.size());
		for(int i=0; i<this.nstate; i++){
			int s = prefix[i].x;
			int t = prefix[i].y;
			if(t>s){
				System.out.println(" Rules for state " + i + " : from " + s + " to " + (t-1));
			}else{
				System.out.println(" Rules for state " + i + " : none");
			}
			
		}
		*/
			
	}
	
	private byte[] convertToBytes(String str){
		int len = str.length();
		byte[] bytes = new byte[len+1];
		for(int i=0; i<len; i++){
			char ch = str.charAt(i);
			bytes[i] = (byte)(ch<'A'? ch-'0' : ch-'A'+10);
		}
		bytes[len] = -1;
		return bytes;
	}
	
	private int compare(byte[] key1, byte[] key2){
		int index = 0;
		
		while(key1[index]>=0 && key2[index]>=0){
			if(key1[index]>key2[index]) return 1;
			if(key1[index]<key2[index]) return -1;
			index++;
		}
		
		return 0;
	}
		
	public byte[] getKey(byte[] key){
		
		int k = get(key);
		return (k<0? null : this.keyTable.at(k));		
	}
	
	public int get(byte[] key){
		
		if(key == null) return -1;
		int index = key[0];
		int first = this.prefix[index].x;
		int last = this.prefix[index].y - 1;		
		if(first > last) return -1;
				
		while(last > first)
		{
			int mid = (first+last)>>1;
			switch(compare(this.keyTable.at(mid), key))
			{
				case 0: 
					return mid;
				case 1:
					last = mid;
					break;
				default:
					if(first < mid)
						first = mid;
					else
						first++;
			}
		}
		
		if(compare(this.keyTable.at(first), key) == 0)
				return first;
		
		return -1;
	
	}			
	
	public byte[] getBody(int pos){
		return this.bodyTable.at(pos);
	}	
	
	
	
}
		
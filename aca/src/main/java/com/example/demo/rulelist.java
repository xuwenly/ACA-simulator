
package com.example.demo;

class TransitionRuleList{

	protected VectorString RuleList;
	
	public TransitionRuleList(VectorString v, int flag){
		this.RuleList = v;
	     consRuleList(flag);
	     sortRuleList();
	     System.out.print("Load " + this.RuleList.size());
	     System.out.println(" rules " + "with symmetricity " + flag);
	     						
	}
       
    public int getRuleListSize(){
       		return(RuleList.size());
    }
       
    public String getRule(int i){
       		return RuleList.at(i);
    }
    
    
	protected String getLeftHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<1 || dot==(rule.length()-1)) return null;
       	return rule.substring(0,dot);
	}
	
	protected String getRightHand(String rule){
		int dot = rule.lastIndexOf(':');
		if(dot<0 || dot==(rule.length()-1)) return null;
		return rule.substring(dot+1);
	}
	
	protected String rotLeft(String left){
		return rotate(left);
	}
	
	protected String rotRight(String right){
		return rotate(right);
	}
	
	protected String revLeft(String left){
		return reverse(left);
	}
		
	protected String revRight(String right){
		return reverse(right);
	}
		
    private void consRuleList(int rrflag){
       		VectorString buf = new VectorString(this.RuleList.size());
       		int rulesize = RuleList.size();
       		    		
       		for(int i=0; i<rulesize; i++){
       			String r0 = (String)RuleList.at(i);
       			String lfh = getLeftHand(r0);
       			String rth = getRightHand(r0);
       			if(lfh==null || rth==null) continue;
       			
       			buf.add(lfh+':'+rth);
       			if(rrflag > 0){
       				if(rrflag == 2){
       					buf.add(revLeft(lfh)+':'+revRight(rth));
       				}
       			    String ll = rotLeft(lfh);
       			    String rr = rotRight(rth);
       			         			        			    			    
       			    while(!(lfh.equals(ll)) || !(rth.equals(rr))){
       			   
       			    	buf.add(ll + ':' + rr);
       					if(rrflag == 2){
       						buf.add(revLeft(ll)+':'+revRight(rr));
       					}
       					ll = rotLeft(ll);
       					rr = rotRight(rr);
       				}
       				
       			}
       		}
       		      		
			this.RuleList = buf;
						
	}
	
	private void sortRuleList(){
		boolean bubble;
		
		do{
			bubble = false;
			for(int i=this.RuleList.size()-1; i>0; i--){
							
				switch(compare(RuleList.at(i), RuleList.at(i-1))){
					case 0: this.RuleList.remove(i);
							break;
					case 1: this.RuleList.swap(i,i-1);
							bubble = true;
							break;
					default: //
				}
			}
		
		
		}while(bubble);

	}
	
	private int compare(String rule1, String rule2){
		int len = (rule1.length()<rule2.length()? rule1.length() : rule2.length());
		
		for(int s=0; s<len; s++){
			int diff = rule1.charAt(s)-rule2.charAt(s);
			
			if(diff>0) return 2; //rule1>rule2
			if(diff<0) return 1; //rule1<rule2
			if(rule1.charAt(s)==':') return 0;
		}
		
		if(rule1.length()==rule2.length()) return 0;
		if(rule1.length()<rule2.length()) return  1;
		return 2;
	}
		
	
	protected String rotate(String s){
		int len = s.length();
		//System.out.println(len);
		StringBuffer r = new StringBuffer(len);
		
		for(int i=1; i<len; i++){
			r.append(s.charAt(i));
		}
		r.append(s.charAt(0));
		return r.toString();
	}
	
	protected String reverse(String s){
		int len = s.length();
		StringBuffer r = new StringBuffer(len);
		r.append(s.charAt(0));
		
		for(int i=(len-1); i>0; i--){
			r.append(s.charAt(i));
		}
		return r.toString();
	}
	
}
	
	
	
			
			
       		
       		
       		
       		
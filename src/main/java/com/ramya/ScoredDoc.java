package com.ramya;

public class ScoredDoc implements Comparable{
	public String w;
	public Double score;
	public ScoredDoc(String _w, Double _score)
	{
		w = _w;
		score = _score;
	}
	
	@Override
	public int compareTo(Object o) {
		if(!(o instanceof ScoredDoc)) return -1;
		double oScore = ((ScoredDoc)o).score;
		if(this.score - oScore < 0) return -1;
		else if(this.score - oScore > 0) return 1;
		return 0;
	}
	
	@Override
	public String toString()
	{
		return "["+w+":"+score+"]";
	}
	
}

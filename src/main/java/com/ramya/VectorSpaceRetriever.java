package com.ramya;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class VectorSpaceRetriever {
	
	public static Map<String, String> index = new HashMap<String, String>();
	public static Map<String, HashMap<String, Integer>> tfHashMap = new HashMap<String, HashMap<String, Integer>>();
	public static Map<String, Vector<Double>> vectorHashMap = new HashMap<String, Vector<Double>>();

	public void loadIndex(String indexFilename){

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(indexFilename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	     String sCurrentLine;
	     try {
			while ((sCurrentLine = br.readLine()) != null) {
				
				String sKey = sCurrentLine.substring(0,sCurrentLine.indexOf(':'));
				String sValue = sCurrentLine.substring(sCurrentLine.indexOf(':')+1);
				sKey = sKey.substring(0,sKey.indexOf(','));
				sValue = sValue.substring(1,sValue.length()-1);
				index.put(sKey, sValue);
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	    index = sortHashMapByComparator(index);
	}

	public double getTF(String t, String d){
		
		HashMap<String, Integer> freqMap;
		int maxFreq = 1;

		if(!tfHashMap.isEmpty() && tfHashMap.containsKey(d)){
			freqMap = tfHashMap.get(d);
			maxFreq = freqMap.get("!THEMAXIMUMVALUE");
			
		}else{
			String[] tokenedDoc = DocUtils.tokenize(d);
			
			freqMap = new HashMap<String, Integer>();

			for(String s: tokenedDoc){

				if(freqMap.containsKey(s)){
					int currentFreq = freqMap.get(s);
					freqMap.put(s, currentFreq+1);
					if(currentFreq+1 > maxFreq) maxFreq = currentFreq+1;
				}
				else{
					freqMap.put(s, 1);
				}
			}
			freqMap.put("!THEMAXIMUMVALUE", maxFreq);
			tfHashMap.put(d, freqMap);
		}
		
	    int ft = 0;
	    if(freqMap.containsKey(t)) ft = freqMap.get(t);
	    
		return 0.5+((0.5*ft)/(maxFreq));
	}

	public double getIDF(String t){
		String listOfDoc = "";
		if(index.containsKey(t)) listOfDoc = index.get(t);
		int docFreq = 0;

		docFreq = listOfDoc.split(",").length;
		Double totalNoOfDocs = 1200.0;
		return Math.log(totalNoOfDocs/docFreq);
	}

	public double[] getVector(String doc){ 
		Vector<Double> tokens;

		if(vectorHashMap.containsKey(doc)){
			tokens =  vectorHashMap.get(doc);
		} else{
			tokens = new Vector<Double>();
		    for (Map.Entry<String, String> entry : index.entrySet())
		    {
		    	String term = entry.getKey().toString();
			    tokens.add(getTF(term,doc)*getIDF(term));
		    }
		}
		
	    
		double ret[] = new double[tokens.size()];
		for(int i=0;i<tokens.size();i++){
			ret[i] = tokens.get(i).doubleValue();
		}

		return ret;
	}

	public double getCosineSimScore(double[] vectorA, double[] vectorB){
		int n = vectorA.length;
		double sumAB = 0;
		double A2 = 0;
		double B2 = 0;
		for(int i=0;i<n;i++){
			double Ai = vectorA[i];
			double Bi = vectorB[i];
			sumAB += Ai*Bi;
			A2 += Math.pow(Ai, 2);
			B2 += Math.pow(Bi, 2);
		}
		
		return sumAB/(Math.sqrt(A2)*Math.sqrt(B2));
	}

	public ScoredDoc[] retrieve(String query, int K){

		System.out.println(query);
		double[] queryVector = getVector(query);
		List<ScoredDoc> list = new ArrayList<ScoredDoc>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\Sruthi\\Downloads\\SpringMVC\\SpringMVC\\src\\main\\java\\com\\ramya\\documents.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    String sCurrentLine;
	    try {
	    	while ((sCurrentLine = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(sCurrentLine,"\t");
				String docID = st.nextToken();
				String docWord = st.nextToken();
				list.add(new ScoredDoc(docWord,getCosineSimScore(getVector(docWord),queryVector)));
	    	}
		    br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	    Collections.sort(list);
	    Collections.reverse(list);
	    ScoredDoc[] ret = new ScoredDoc[K];
	    for(int i=0;i<K;i++){
	    	ret[i] = list.get(i);
	    }
		return ret;
	}

	public HashMap<String, String> sortHashMapByComparator(Map<String, String> unsortMap) {

		List<Map.Entry <String, String>> list =
			new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		HashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Iterator<Map.Entry<String, String>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
}

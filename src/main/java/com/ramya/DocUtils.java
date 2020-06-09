package com.ramya;//******************** DO NOT MODIFY THIS FILE ***********************//

import org.tartarus.snowball.ext.PorterStemmer;

import java.util.Arrays;
import java.util.Vector;

public class DocUtils {

	public static String[] tokenize(String doc)
	{
		if(doc == null) return null;
		//lower casing and removing punctuation
		String[] terms = doc.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		PorterStemmer stemmer = new PorterStemmer();
		Vector<String> tokens = new Vector<String>();
		for(String term: terms)
		{
			stemmer.setCurrent(term);
			stemmer.stem();
			term = stemmer.getCurrent();
			if(!term.isEmpty()) tokens.add(term);
		}
		
		return tokens.toArray(new String[0]);
	}
	

}


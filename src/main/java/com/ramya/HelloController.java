package com.ramya;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class HelloController {
	ArrayList<Double> scores = new ArrayList<Double>();
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<String> words = new ArrayList<String>();

	@RequestMapping("/search")
	public String display(@RequestParam("query") String query, Model m)
	{
		VectorSpaceRetriever vt = new VectorSpaceRetriever();
		vt.loadIndex("C:\\Users\\Sruthi\\Downloads\\SpringMVC\\SpringMVC\\src\\main\\java\\com\\ramya\\index.txt");
		ScoredDoc[] result = vt.retrieve(query, 10);

		int retrelevant = 0;
		for(ScoredDoc res : result)	{


			String [] val = res.w.split(":",2);
			titles.add(val[0]);

			if(val[0].compareTo("been nut A pig ") != 0)
			words.add(val[1]);

			if(isRelevant(words,query))  {
				retrelevant ++;
			}


			scores.add(res.score);
		}
		int precision = retrelevant/totalRelevant(q);
		int recall = retrelevant/10;


		m.addAttribute("words",words);
		m.addAttribute("titles",titles);

		m.addAttribute("scores",scores);
		m.addAttribute("query",query);
		m.addAttribute("precision", precision);
		m.addAttribute("recall", recall);

		return "search_result";

	}
	@RequestMapping("/display")
	public String display(@RequestParam("i") int i, Model m) {

		System.out.println(i);
		m.addAttribute("title",titles.get(i));
		m.addAttribute("word",words.get(i));
		m.addAttribute("score",scores.get(i));

		titles.clear();
		words.clear();
		scores.clear();

		return "display";
	}

}

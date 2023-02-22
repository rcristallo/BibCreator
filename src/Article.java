import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Article {

	// ATTRIBUTES
	private Map<String, String> articleMap;
	
	// DEFAULT CONSTRUCTOR
	public Article() {
		
	}
	
	// PARAMETERIZED CONSTRUCTOR
	public Article(ArrayList<KeyValuePair> pairArray){
		
		this.articleMap = new HashMap<String, String>();
		
		for (KeyValuePair item : pairArray) {			
			// Place key and value pairs inside Map (dictionary) Object from passed array
			this.articleMap.put(item.getKey(), item.getValue());
		}
		
	}

	
	// GETTERS & SETTERS
	public Map<String, String> getArticleMap() {
		return articleMap;
	}
}
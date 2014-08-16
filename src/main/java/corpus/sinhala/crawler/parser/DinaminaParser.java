package corpus.sinhala.crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DinaminaParser implements Parser{
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;

	public DinaminaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("h2").first();
		p = titleElement.parent();
		arr = url.split("/");

	}

	public String getTitle(){
		return titleElement.text();
	}

	public String getAuthor(){
		String author = doc.select("p[class=byline]").first().text();
		if(url.contains("samakaya")){
		author = doc.select("p[class=byline]").get(1).text();
		}
		if(author == null){
			author = "";
		}
		return author;
	}

	public String getContent(){
		Elements contents = p.select("p");
		String content="";
		for(int i=0;i<contents.size();i++){
			if(!contents.get(i).attr("class").equals("navS"))
			content = content + contents.get(i).text();
		}
		return content;
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		if(arr.length==7)
			return arr[3];
		else
			return "";
	}

	public String getMonth(){
		if(arr.length==7)
			return arr[4];
		else
			return "";
	}

	public String getDate(){
		if(arr.length==7)
			return arr[5];
		else
			return "";
	}

}

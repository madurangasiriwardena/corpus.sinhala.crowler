package corpus.sinhala.crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AlokaUParser implements Parser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;

	public AlokaUParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("h2").first();
		if(titleElement.select("img").size()==0){
			titleElement = doc.select("h2").first();
		}else{
			titleElement = doc.select("h2").get(1);
		}
		p = titleElement.parent();
		arr = url.split("/");

	}

	public String getTitle(){
		return titleElement.text();
	}

	public String getAuthor(){
		String author;
		if(doc.select("p[class=byline]").first() == null){
			author = "";
		}else{
			author = doc.select("p[class=byline]").first().text();
		}
		return author;
	}

	public String getContent(){
		Elements contents = p.select("p");
		String content="";
		for(int i=0;i<contents.size();i++){
			content = content + contents.get(i).text();
		}
		return content;
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		return arr[4];
	}

	public String getMonth(){
		return arr[5];
	}

	public String getDate(){
		return arr[6];
	}

}

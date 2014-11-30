package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;

public class MawbimaParser implements Parser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;
	
	public MawbimaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("span[class=subtitle newsdetailssubtitle").first();
		p = titleElement.parent();
		
	}
	
	public String getTitle(){
		return titleElement.text();
	}
	
	public String getAuthor(){
		
		return p.select("span[class=bold]").first().text();
	}
	
	public String getContent(){
		
		return p.text();
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getYear(){
		return url.split("-")[3];
	}
	
	public String getMonth(){
		return url.split("-")[4];
	}
	
	public String getDate(){
		return url.split("-")[2];
	}
	
	public String getCategory() {
		return "NEWS";
	}
}

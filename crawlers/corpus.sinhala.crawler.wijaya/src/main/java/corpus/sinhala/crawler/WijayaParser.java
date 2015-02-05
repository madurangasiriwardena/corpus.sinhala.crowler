package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;

public class WijayaParser implements Parser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;

	public WijayaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
	}
	
	public String getTitle(){
		
			titleElement =  doc.select("div[class=pra]").first();
		
			return titleElement.text();
	}
	
	public String getAuthor(){
		return "";
	}
	
	public String getContent(){
		
		
		return doc.select("div[class=text]").first().text();
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getYear(){
		
		return doc.select("div[class=dateline").first().text().split(" ")[4].substring(0,4);
	}
	
	public String getMonth(){
		String month = doc.select("div[class=dateline").first().text().split(" ")[2].split("m")[1].substring(2);
		
		if(month.equals("January")){
			return "1";
		}else if(month.equals("February")){
			return "2";
		}else if(month.equals("March")){
			return "3";
		}else if(month.equals("April")){
			return "4";
		}else if(month.equals("May")){
			return "5";
		}else if(month.equals("June")){
			
			return "6";
		}else if(month.equals("July")){
			return "07";
			//Unicode need to be changed after july
		}else if(month.equals("August")){
			return "8";
		}else if(month.equals("September")){
			return "9";
		}else if(month.equals("October")){
			return "10";
		}else if(month.equals("November")){
			return "11";
		}else if(month.equals("December")){
			return "12";
		}
		return "";
	}
	
	public String getDate(){
		
		return doc.select("div[class=dateline").first().text().split(" ")[3].split(",")[0];
	}
	
	public String getCategory() {
		return "ACADEMIC";
	}

}

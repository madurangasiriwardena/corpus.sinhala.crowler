package corpus.sinhala.crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AdaDeranaParser implements Parser{
	Document doc;
	String url;

	public AdaDeranaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
	}

	public String getTitle(){
		return doc.select("h2[class=completeNewsTitle]").first().text();
	}

	public String getAuthor(){
		return "";
	}

	public String getContent(){
		return doc.select("div[class=newsContent]").first().text() ;
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		return doc.select("p[class=newsDateStamp]").first().text().split(" ")[2].substring(0, 4);
	}

	public String getMonth(){
		String month = doc.select("p[class=newsDateStamp]").first().text().split(" ")[0];
		if(month.equals("January")){
			return "01";
		}else if(month.equals("February")){
			return "02";
		}else if(month.equals("March")){
			return "03";
		}else if(month.equals("April")){
			return "04";
		}else if(month.equals("May")){
			return "05";
		}else if(month.equals("June")){
			return "06";
		}else if(month.equals("July")){
			return "07";
		}else if(month.equals("August")){
			return "08";
		}else if(month.equals("September")){
			return "09";
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
		return doc.select("p[class=newsDateStamp]").first().text().split(" ")[1].substring(0,2);
	}

}

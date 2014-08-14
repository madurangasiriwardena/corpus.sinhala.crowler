package corpus.sinhala.crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AdaParser implements Parser{
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;

	public AdaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
//		titleElement = doc.select("h2").first();
//		if(titleElement.select("img").size()==0){
//			titleElement = doc.select("h2").first();
//		}else{
//			titleElement = doc.select("h2").get(1);
//		}
//		p = titleElement.parent();
//		arr = url.split("/");

	}

	public String getTitle(){
		return doc.select("div[class=heading]").first().text();
	}

	public String getAuthor(){
		String author = doc.select("div[class=left]").first().text().split("\\(")[1].split("\\)")[0];
		if(author.equals(" . . ")){
			return "";
		}
		return author;


	}

	public String getContent(){
		return doc.select("div[class=description]").first().text() ;
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		return doc.select("div[class=left]").first().text().split("\\)")[1].substring(1,5);
	}

	public String getMonth(){
		return doc.select("div[class=left]").first().text().split("\\)")[1].substring(6,8);
	}

	public String getDate(){
		return doc.select("div[class=left]").first().text().split("\\)")[1].substring(9,11);
	}

}

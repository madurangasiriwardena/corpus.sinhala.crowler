package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;

public class SiluminaParser implements Parser{
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;

	public SiluminaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("h2").first();

		p = titleElement.parent();
		arr = url.split("/");

	}

	public String getTitle(){
		return doc.select("title").first().text();
	}

	public String getAuthor(){
		Elements AuthorElement = doc.select("p[class=byline]");
		if(AuthorElement.size()>0){
			return AuthorElement.first().text();
		}
		return "";
	}

	public String getContent(){
		Elements contents = p.select("p");
		String content="";
		for(int i=0;i<contents.size();i++){
			if(!contents.get(i).attr("class").equals("subIN"))
			content = content + contents.get(i).text();
		}
		return content;
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		return arr[3];
	}

	public String getMonth(){
		return arr[4];
	}

	public String getDate(){
		return arr[5];
	}
	
	public String getCategory() {
		return "NEWSPAPER";
	}

}

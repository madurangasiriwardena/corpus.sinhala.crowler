package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;

public class VidusaraParser implements Parser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	Elements e;
	Element bodyElement;
	Elements bodyElements;
	String[] arr;
	public VidusaraParser(String page, String url) throws Exception{
		doc = Jsoup.parse(page);
		this.url = url;
		try{
		titleElement = doc.select("font[size=5]").get(0);
		}catch(Exception e){}
		if(titleElement == null)
			try{
			titleElement = doc.select("span[class=style2]").get(0);
			}catch(Exception e){}
		if(titleElement == null)
			try{
			titleElement = doc.select("span[class=style3]").get(0);
			}catch(Exception e){}
		p = titleElement.parent();
		arr = url.split("/");

	}

	public String getTitle() throws Exception{
		String title = "";
		String[] arr = url.split("/");

		if(arr[2].equals("www.vidusara.com") && (arr[6].startsWith("news") || arr[6].startsWith("feature"))){
			title = titleElement.text();
						return title;
		}
		return "";
	}

	public String getAuthor() throws Exception{
		return "";
	}

	public String getContent() throws Exception{
		String body = "";
		return p.text();
	}

	public String getUrl() throws Exception{
		return url;
	}

	public String getYear() throws Exception{
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[3];
		}
		return "";
	}

	public String getMonth() throws Exception{
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[4];
		}
		return "";
	}

	public String getDate() throws Exception{
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[5];
		}
		return "";
	}
	
	public String getCategory() {
		return "ACADEMIC";
	}

}
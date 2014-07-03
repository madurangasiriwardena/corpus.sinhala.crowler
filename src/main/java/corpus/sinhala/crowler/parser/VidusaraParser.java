package corpus.sinhala.crowler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	Elements e;
	Element bodyElement;
	Elements bodyElements;
	String[] arr;
	public VidusaraParser implements Parser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("font[color=#336600]").first();

		p = titleElement.parent();
		e = p.getElementsByTag("font");
		bodyElement = e.get(1);
		bodyElements = bodyElement.getElementsByTag("br");
		arr = url.split("/");

	}

	public String getTitle(){
		String title = "";
		String[] arr = url.split("/");

		if(arr[2].equals("www.vidusara.com") && (arr[6].startsWith("news") || arr[6].startsWith("feature"))){
			title = titleElement.text();
						return title;
		}
		return "";
	}

	public String getAuthor(){
		return "";
	}

	public String getContent(){
		String body = "";
		return p.text();
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[3];
		}
		return "";
	}

	public String getMonth(){
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[4];
		}
		return "";
	}

	public String getDate(){
		if(arr[2].equals("www.vidusara.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("feature"))){
			return arr[5];
		}
		return "";
	}

}
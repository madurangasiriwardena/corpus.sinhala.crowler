package corpus.sinhala.crowler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DivainaParser {
	Document doc;
	String url;
	Element titleElement;
	Element p;
	Elements e;
	Element bodyElement;
	Elements bodyElements;
	String[] arr;
	public Parser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement = doc.select("font[color=#336600]").first();
		if(titleElement == null){
			titleElement = doc.select("font[color=#003300]").first();
		}
		if(titleElement == null){
			titleElement = doc.select("font[color=#006600]").first();
		}
		p = titleElement.parent();
		e = p.getElementsByTag("font");
		bodyElement = e.get(1);
		bodyElements = bodyElement.getElementsByTag("br");
		arr = url.split("/");
		
	}
	
	public String getTitle(){
		String title = "";
		String[] arr = url.split("/");
		//for (int i = 0; i < arr.length; i++) {
			//System.out.println( (arr[2].equels"www.divaina.com") + "      " + arr[6].startsWith("news"));
		//}
		if(arr[2].equals("www.divaina.com") && arr[arr.length-1].startsWith("bud") ||(arr[6].startsWith("news") || arr[6].startsWith("provin") || arr[6].startsWith("velanda")|| arr[6].startsWith("sports")|| arr[6].startsWith("forign")|| arr[6].startsWith("feature")|| arr[6].startsWith("editor")|| arr[6].startsWith("sarasavi"))){
			title = titleElement.text();
			//System.out.println(title);
			return title;
		}
		return "";
	}
	
	public String getAuthor(){
		String html;
		if(arr[2].equals("www.divaina.com") && (arr[6].startsWith("news"))){
			html = bodyElement.html();
			//System.out.println(html);
			return html.split("<br />")[2];
		}
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
		if(arr[2].equals("www.divaina.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("provin") || arr[arr.length-1].startsWith("velanda")|| arr[arr.length-1].startsWith("sports")|| arr[arr.length-1].startsWith("forign")|| arr[arr.length-1].startsWith("feature")|| arr[arr.length-1].startsWith("editor")|| arr[arr.length-1].startsWith("sarasavi"))){
			return arr[3];
		}
		return "";
	}
	
	public String getMonth(){
		if(arr[2].equals("www.divaina.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("provin") || arr[arr.length-1].startsWith("velanda")|| arr[arr.length-1].startsWith("sports")|| arr[arr.length-1].startsWith("forign")|| arr[arr.length-1].startsWith("feature")|| arr[arr.length-1].startsWith("editor")|| arr[arr.length-1].startsWith("sarasavi"))){
			return arr[4];
		}
		return "";
	}
	
	public String getDate(){
		if(arr[2].equals("www.divaina.com") && (arr[arr.length-1].startsWith("news") || arr[arr.length-1].startsWith("provin") || arr[arr.length-1].startsWith("velanda")|| arr[arr.length-1].startsWith("sports")|| arr[arr.length-1].startsWith("forign")|| arr[arr.length-1].startsWith("feature")|| arr[arr.length-1].startsWith("editor")|| arr[arr.length-1].startsWith("sarasavi"))){
			return arr[5];
		}
		return "";
	}
	
}


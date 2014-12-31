package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MahawansayaParser {
	Document doc;
	String url;
	public MahawansayaParser(String page, String url) {
		doc = Jsoup.parse(page);
		this.url = url;
	}

	public String getCategory() {
		// TODO Auto-generated method stub
		return "ACADEMIC";
	}

	public String getYear() {
		// TODO Auto-generated method stub
		return "OLD";
	}

	public String getMonth() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return url;
	}

	public String getTitle() {		
		return "මහාවංශය "  + url.split("/")[4].split("-")[1];
	}

	public String getContent() {
		Elements e= doc.select("div[class=news_item]").first().select("p");
		String text="";
		for (int i = 0; i < e.size(); i++) {
			text=text+e.get(i).text();
		}
		return text;
	}

}

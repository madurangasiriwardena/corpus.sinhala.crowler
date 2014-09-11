package corpus.sinhala.crawler;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;


public class LankadeepaParser implements Parser {
	Document doc;
	String content = "";
	String title = "";
	String author = "";
	String url = "";
	String year = "";
	String month = "";
	String date = "";	
	
	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getUrl() {
		return url;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDate() {
		return date;
	}
	
	public LankadeepaParser(String page, String url){
		String pageString = page;
		doc = Jsoup.parse(pageString);
		this.url = url;
		System.out.println(url);
		parsePage();
	}
	
	private void parsePage(){
		
		Element titleElement = doc.select("p[tabindex=1]").first();
		title = titleElement.text();
		Element root = titleElement.parent();

		Elements articleElements = root.getElementsByTag("p");
		
		Iterator<Element> itr = articleElements.iterator();
		
		if(itr.hasNext()){
			title = itr.next().text();
		}
		
		int counter = 1;
		String data = "";
		boolean dataFound = false;
		while(itr.hasNext()){
			Element e = itr.next();
			String attrStyle = e.attr("style");
			String attrClass = e.attr("class");
			if(attrStyle.equals("") && attrClass.equals("") && !e.html().equals("&nbsp;")){
				e.text().trim();
				content += (e.text()+" ");
				
				if(!dataFound){
					data = articleElements.get(counter-1).text();
					dataFound = true;
				}
			}
			
			counter++;
			
		}
		content.trim();
		
		String[] dataArr = data.split("\\|");
		author = dataArr[1];
		String[] dateArr = dataArr[0].split(" ");
		for (int i = 0; i < dateArr.length; i++) {
			System.out.println(dateArr[i]);
		}
		year = dateArr[0];
		month = dateArr[1];
		date = dateArr[3];
		
		if(month.equals("ජනවාරි")){
			month = "01";
		}else if(month.equals("පෙබරවාරි")){
			month = "02";
		}else if(month.equals("මාර්තු")){
			month = "03";
		}else if(month.equals("අපේ‍්‍රල්")){
			month = "04";
		}else if(month.equals("මැයි")){
			month = "05";
		}else if(month.equals("ජූනි")){
			month = "06";
		}else if(month.equals("ජූලි")){
			month = "07";
		}else if(month.equals("අගෝස්තු")){
			month = "08";
		}else if(month.equals("සැප්තැම්බර්")){
			month = "09";
		}else if(month.equals("ඔක්තෝබර්")){
			month = "10";
		}else if(month.equals("නොවැම්බර්")){
			month = "11";
		}else if(month.equals("දෙසැම්බර්")){
			month = "12";
		}
		
		content.trim();
		
	}

}
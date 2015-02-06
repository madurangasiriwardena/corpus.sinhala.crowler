package corpus.sinhala.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Parser;

public class LakbimaParser implements Parser{
	Document doc;
	String url;
	Element titleElement;
	Element p;
	String[] arr;
	
	public LakbimaParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;
		titleElement =  doc.select("h1[class=contentheading]").first();
		titleElement =  doc.select("h1[class=contentheading]").first();
	}
	
	public String getTitle(){
		
			
		
			return titleElement.text();
	}
	
	public String getAuthor(){
		return "";
	}
	
	public String getContent(){
		//return titleElement.parent().text();
		/*Elements contents =titleElement.parent().getElementsByTag("p");
		String content="";
		for(int i=0;i<contents.size();i++){
			if(!contents.get(i).attr("class").equals("subIN"))
			content = content + contents.get(i).text();
		}*/
		return titleElement.parent().text();
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getYear(){
		Element dateElement = doc.select("span[style=color: #ff0000;]").first();
		if (dateElement == null){
			dateElement = doc.select("span[font-size: 10pt; color: #ff0000;]").first();
		}
		if (dateElement == null){
			return "";
		}
		return dateElement.text().split(" ")[0];
	}
	
	public String getMonth(){
		Element dateElement = doc.select("span[style=color: #ff0000;]").first();
		if (dateElement == null){
			dateElement = doc.select("span[font-size: 10pt; color: #ff0000;]").first();
		}
		if (dateElement == null){
			return "";
		}
		String month = dateElement.text().split(" ")[1];
		if(month.equals("ජනවාරි")){
			return "01";
		}else if(month.equals("පෙබරවාරි")){
			return "02";
		}else if(month.equals("මාර්තු")){
			return "03";
		}else if(month.equals("අප්‍රේල්")){
			return "04";
		}else if(month.equals("මැයි")){
			return "05";
		}else if(month.equals("ජුනි")){
			
			return "06";
		}else if(month.equals("ජුලි")){
			return "07";
			//Unicode need to be changed after july
		}else if(month.equals("අගෝස්තු")){
			return "08";
		}else if(month.equals("සැප්තැම්බර්")){
			return "09";
		}else if(month.equals("ඔක්තෝබර්")){
			return "10";
		}else if(month.equals("නොවැම්බර්")){
			return "11";
		}else if(month.equals("දෙසැම්බර්")){
			return "12";
		}
		return dateElement.text().split(" ")[1];
	}
	
	public String getDate(){
		Element dateElement = doc.select("span[style=color: #ff0000;]").first();
		if (dateElement == null){
			dateElement = doc.select("span[font-size: 10pt; color: #ff0000;]").first();
		}
		if (dateElement == null){
			return "";
		}
		return dateElement.text().split(" ")[2];
	}
	
	public String getCategory() {
		return "NEWS";
	}
	
}
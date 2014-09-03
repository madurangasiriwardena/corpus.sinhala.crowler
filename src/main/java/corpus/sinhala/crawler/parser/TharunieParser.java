package corpus.sinhala.crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TharunieParser implements Parser{
	Document doc;
	String url;

	public TharunieParser(String page, String url){
		doc = Jsoup.parse(page);
		this.url = url;


	}
/*
	public String getTitle(){
		return doc.select("h2[class=itemTitle]").first().text();
	}

	public String getAuthor(){
		String[] comps = doc.select("span[class=itemAuthor]").first().text().split("ලිව්වේ");
		if(comps.length ==2){
			return comps[1];
		}else{
			comps = doc.select("span[class=itemAuthor]").first().text().split("Written by");
		}
		if(comps.length ==2){
			return comps[1];
		}
		else{
			return "";
		}
	}

	public String getContent(){
		return doc.select("div[class=itemIntroText]").first().text() + doc.select("div[class=itemFullText]").first().text();
	}

	public String getUrl(){
		return url;
	}

	public String getYear(){
		return doc.select("span[class=itemDateCreated]").first().text().split(" ")[0];
	}

	public String getMonth(){
		String month = doc.select("span[class=itemDateCreated]").first().text().split(" ")[1];
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
		}else if(month.equals("ජූනි")){
			return "06";
		}else if(month.equals("ජූලි")){
			return "07";
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
		return doc.select("span[class=itemDateCreated]").first().text().split(" ")[1];
	}

	public String getDate(){
		return doc.select("span[class=itemDateCreated]").first().text().split(" ")[2].substring(0,2);
	}
*/

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getYear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMonth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return null;
	}
}

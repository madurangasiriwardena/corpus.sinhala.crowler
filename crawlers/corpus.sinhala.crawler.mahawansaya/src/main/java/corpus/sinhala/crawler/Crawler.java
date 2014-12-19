package corpus.sinhala.crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {

	public static void main(String[] args) throws IOException{
		
		Crawler crawler = new Crawler();
		try {
			crawler.crawl(1);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void crawl(int start) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		int index=start;
		String urlString;
		XMLFileWriter writer = new XMLFileWriter();
		while(true){
			
			if(index==11){
				urlString = "http://mahamegha.lk/mahawansa/namo_buddhaya-11/";
			}else{
			urlString = "http://mahamegha.lk/mahawansa/mahawansa-" + index + "/";
			}
			
			URL url = new URL(urlString);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"cache.mrt.ac.lk", 3128));
			//HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();

			
			uc.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			try{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "UTF-8"));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}
			}catch(FileNotFoundException e){
				System.out.println(index);
				index++;
				//break;
			}

			Document doc = Jsoup.parse(String.valueOf(tmp));
			doc.setBaseUri(urlString);
			//System.out.println();
			if(this.isValid(doc)){
				writer.addDocument(doc+"", urlString);
				index++;
			}
			else{
				System.out.println(index);
				index++;
				//break;
			}
			
			
		}
	}
	
	public int get_start(){
		//get start index from DB
		return 1;
	}
	
	public void writeDB(int index){
		
	}
	
	public boolean isValid(Document doc){
		
		org.jsoup.select.Elements elements =  doc.select("div[class=news_item]");
		for(int i=0;i<elements.size();i++){
			if(elements.get(i).text().contains("මහාවංශය")){
				return true;
			}
		}
		return false;
	}
	
	
}

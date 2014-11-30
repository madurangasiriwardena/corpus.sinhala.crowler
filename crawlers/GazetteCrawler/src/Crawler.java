import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



//import corpus.sinhala.crawler.gazette.Crawler;
//import Downloader;
//import corpus.sinhala.crawler.gazette.Parser;
//import corpus.sinhala.crawler.gazette.XMLFileWriter;


public class Crawler {

	public static void main(String[] args) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		Crawler c = new Crawler();
		//Parser p = new Parser();
		Downloader d = new Downloader();
		//XMLFileWriter writer = new XMLFileWriter("/home/chamila/semester7/fyp/folders/data/");
		c.crawl(d);
	}
	
	public void crawl(Downloader d) throws ClientProtocolException, IOException{
		int count = 1;
		int year=2010;
		String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		outer:
		for(int k=0;k<12;k++){
			HttpGet post = new HttpGet("http://documents.gov.lk/gazette/"+year +"/"+ months[k] + ".htm");
			//if page notexist, break
	    	HttpClient httpclient = HttpClients.createDefault();
	    	HttpResponse response = httpclient.execute(post);
	    	HttpEntity entity = response.getEntity();
	    	
	    	if (entity != null) {
	    	    InputStream instream = entity.getContent();
	    	    
	    	    String line = null;
				StringBuffer tmp = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}

				
				Document doc = Jsoup.parse(String.valueOf(tmp));

				Elements urlList = doc.select("a");
				for(int i=0; i<urlList.size(); i++){
					System.out.println(urlList.get(i).text());
					if(!urlList.get(i).text().equals("S")){
						continue;
					}
					String tempUrl = "http://documents.gov.lk/gazette/" + year + "/" + urlList.get(i).attr("href");
					String date = urlList.get(i).parent().parent().parent().parent().parent().select("td").get(0).text();
					System.out.println(year + " " + months[k] + " " + date);
					try {
						d.Download(tempUrl,"/home/chamila/semester7/fyp/gazette/" + count + ".pdf");
						count++;
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	    	}
	    	
	    	break;
	    	
		}
	}
	
	
}

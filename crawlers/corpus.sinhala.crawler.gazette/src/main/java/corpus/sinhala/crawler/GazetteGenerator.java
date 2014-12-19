package corpus.sinhala.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.DTO;
import corpus.sinhala.crawler.infra.Generator;
import corpus.sinhala.crawler.infra.network.NetworkConnector;

public class GazetteGenerator extends Generator{
	int sYear;
	int eYear;
	int sMonth;
	int eMonth;
	int sDate;
	int eDate;

	int year;
	int month;
	int date;
	int articleId;
	String[] articleName;
	int articleNameId;

	DateTime dt;
	DateTime endDate;
	DateTime dayAfterEndDate;
	
	String[] months;
	
	boolean listEmpty;
	Queue<String> urls;

	NetworkConnector nc;
	
	boolean crawled;

	public GazetteGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate, String host, int port) {
		
		this.sYear = sYear;
		this.eYear = eYear;
		this.sMonth = sMonth;
		this.eMonth = eMonth;
		this.sDate = sDate;
		this.eDate = eDate;

		year = sYear;
		month = sMonth;
		date = sDate;
		articleId = 1;
		
		listEmpty = true;
		urls = new LinkedList<String>();
		
		months = new String[12];
		months[0] = "Jan";
		months[1] = "Feb";
		months[2] = "Mar";
		months[3] = "Apr";
		months[4] = "May";
		months[5] = "Jun";
		months[6] = "Jul";
		months[7] = "Aug";
		months[8] = "Sep";
		months[9] = "Oct";
		months[10] = "Nov";
		months[11] = "Dec";

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);
		
		if (dt.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
	        dt = dt.plusWeeks(1);
	    }
	    dt = dt.withDayOfWeek(DateTimeConstants.FRIDAY);
		year = dt.getYear();
		month = dt.getMonthOfYear();
		date = dt.getDayOfMonth();
		System.out.println("Crawling from " + dt);
		endDate = new DateTime(eYear, eMonth, eDate, 0, 0, 0, 0);
		System.out.println("To " + endDate);
		dayAfterEndDate = endDate.plusDays(1);
		
		crawled = false;

		nc = NetworkConnector.getInstance();
		try {
			nc.connect(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String baseGenerator() {
		String url = "http://documents.gov.lk/gazette/";
		return url;
	}
	
	public String listGenerator() {
		String url = "http://documents.gov.lk/gazette/" + year + "/" + months[month-1] + ".htm";
		return url;
	}

	public DTO fetchPage() throws IOException {
		if(!dt.isBefore(dayAfterEndDate) ){
			return null;
		}
		while(urls.isEmpty()){
			if(crawled){
				System.out.println("empty");
				try{
					String message = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", date);
					nc.send(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", date));
					setChanged();
				    notifyObservers(message);
				}catch(IOException e1){
					return null;
				}
				articleNameId=0;
				dt = dt.plusWeeks(1);
				year = dt.getYear();
				month = dt.getMonthOfYear();
				date = dt.getDayOfMonth();
				
				crawled = false;
				
				if(!dt.isBefore(dayAfterEndDate) ){
					try{
						nc.send("close");
						nc.close();
					}catch(IOException e1){
						return null;
					}
					
					return null;
				}
			}
			
			String urlString = listGenerator();
			HttpPost post = new HttpPost(urlString);
			
			//proxy
//			HttpHost proxy = new HttpHost("cache.mrt.ac.lk", 3128, "http");
//            RequestConfig config = RequestConfig.custom()
//                    .setProxy(proxy)
//                    .build();
//            post.setConfig(config);

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

				String base = baseGenerator();
				
				Document doc = Jsoup.parse(String.valueOf(tmp));

				Elements urlList = doc.select("a");
				for(int i=0; i<urlList.size(); i++){
					if(urlList.get(i).text().equals("S")){
						String tempUrl = urlList.get(i).attr("href");
						String dateTemp = urlList.get(i).parent().parent().parent().parent().parent().select("td").get(0).text();
//						System.out.println(dateTemp);
//						System.out.println(tempUrl);
						try{
							int dateTempInt = Integer.parseInt(dateTemp);
							if( !urls.contains(base + year + "/" + tempUrl) && dateTempInt == date){
								urls.add(base + year + "/" + tempUrl);
							}
						}catch(NumberFormatException e){
							
						}
					}
					
					
				}
				
				crawled = true;
	    	}
			
		}
		
		if(!urls.isEmpty()){
			String urlString = urls.remove();
			
			return new DTO(urlString, year, month, date);
		}else{
			return null;
		}
		
//		HttpPost post = new HttpPost(urlString);
//
//    	HttpClient httpclient = HttpClients.createDefault();
//    	HttpResponse response = httpclient.execute(post);
//    	HttpEntity entity = response.getEntity();
//
//    	if (entity != null) {
//    	    InputStream instream = entity.getContent();
//    	    
//    	    String line = null;
//			StringBuffer tmp = new StringBuffer();
//			BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
//			while ((line = in.readLine()) != null) {
//				tmp.append(line);
//			}
//			
//			String base = baseGenerator();
//			
//			Document doc = Jsoup.parse(String.valueOf(tmp));
//			doc.setBaseUri(urlString);
//			return doc;
//    	}

		

	}

}

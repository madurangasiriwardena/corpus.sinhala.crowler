package corpus.sinhala.crawler.url.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.network.NetworkConnector;

public class BuduSaranaGenerator extends Observable {
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
	
	boolean listEmpty;
	Queue<String> urls;

	NetworkConnector nc;

	public BuduSaranaGenerator(int sYear, int eYear, int sMonth, int eMonth,
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
		articleName = new String[4];
		articleName[0] = "main_news";
		articleName[1] = "main_vision";
		articleName[2] = "main_features";
		articleName[3] = "main_temple";
		articleNameId = 0;
		
		listEmpty = true;
		urls = new LinkedList<String>();

		dt = new DateTime(sYear, sMonth, 6, 0, 0, 0, 0);
		dt = dt.withDayOfWeek(DateTimeConstants.SUNDAY);
		year = dt.getYear();
		month = dt.getMonthOfYear();
		date = dt.getDayOfMonth();
		System.out.println("Crawling from " + dt);
		endDate = new DateTime(eYear, eMonth, eDate, 0, 0, 0, 0);
		System.out.println("To " + endDate);
		dayAfterEndDate = endDate.plusDays(1);

		nc = NetworkConnector.getInstance();
		try {
			nc.connect(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String baseGenerator() {
		String url = "http://www.lakehouse.lk/budusarana/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/";

		return url;
	}
	
	public String listGenerator() {
		String url = "http://www.lakehouse.lk/budusarana/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/"
				+ articleName[articleNameId]
				+ ".asp";

		return url;
	}

	public Document fetchPage() throws IOException {
		if(!dt.isBefore(dayAfterEndDate) ){
			return null;
		}
		while(urls.isEmpty()){
			if(articleNameId>=articleName.length){
				
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
			URL url = new URL(urlString);
			System.out.println("-----"+urlString);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"cache.mrt.ac.lk", 3128));
//			HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();

			try {
				uc.connect();
				String line = null;
				StringBuffer tmp = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						uc.getInputStream()));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}
				String base = baseGenerator();
				Document doc = Jsoup.parse(String.valueOf(tmp));
				urls.add(base + doc.select("a[class=fullstory]").get(0).attr("href"));
				Elements urlList = doc.select("li");
				System.out.println(urlList.size());
				for(int i=0; i<urlList.size(); i++){
					String tempUrl = urlList.get(i).select("h2").get(0).select("a").get(0).attr("href");
					if(!urls.contains(base+tempUrl)){
					urls.add(base+tempUrl);
					System.out.println(base+tempUrl );
					}
				}
			} catch (IOException e) {
			}
			articleNameId++;
			//System.out.println(urls.isEmpty());
		}
		
		String urlString = urls.remove();
		URL url = new URL(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128));
//		HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();

		try {
			uc.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "UTF-8"));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}

			Document doc = Jsoup.parse(String.valueOf(tmp));
			doc.setBaseUri(urlString);
			return doc;
			
		} catch (IOException e) {

		}
return null;
	}

}

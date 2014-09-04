package corpus.sinhala.crawler.url.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.network.NetworkConnector;

public class SiluminaGenerator extends Observable {
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

	public SiluminaGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate, String host, int port) {
		//System.out.println("aaaaaaa");
		// System.setProperty("http.proxyHost", "cache.mrt.ac.lk");
		// System.setProperty("http.proxyPort", "3128");

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
		articleName = new String[10];
		articleName[0] = "aa";
		articleName[1] = "ab";
		articleName[2] = "af";
		articleName[3] = "as";
		articleName[4] = "av";
		articleName[5] = "am";
		articleName[6] = "cr";
		articleName[7] = "ay";
		articleName[8] = "aj";
		articleName[9] = "aw";
		articleNameId = 0;
		
		listEmpty = true;
		urls = new LinkedList<String>();

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);
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
		String url = "http://www.silumina.lk/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/";

		return url;
	}
	
	public String listGenerator() {
		String url = "http://www.silumina.lk/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/_art.asp?fn="
				+ articleName[articleNameId];

		return url;
	}

	public Document fetchPage() throws IOException {
		if(!dt.isBefore(dayAfterEndDate) ){
			return null;
		}
		while(urls.isEmpty()){
			if(articleNameId>=articleName.length){
				
				try{
					nc.send(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", date));
					setChanged();
				    notifyObservers();
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
						setChanged();
					    notifyObservers();
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
//			 HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
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

				Document doc = Jsoup.parse(String.valueOf(tmp));
				
				Elements urlList = doc.select("a[class=navS]");
				for(int i=0; i<urlList.size(); i++){
					String base = baseGenerator();
					String tempUrl = urlList.get(i).attr("href");
					if(tempUrl.contains("_art.asp?fn=" + articleName[articleNameId]) && !urls.contains(base+tempUrl))
					urls.add(base+tempUrl);
				}
			} catch (IOException e) {
			}
			articleNameId++;
		}
		
		String urlString = urls.remove();
		URL url = new URL(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128));
//		 HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
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

			Document doc = Jsoup.parse(String.valueOf(tmp));
			doc.setBaseUri(urlString);
			return doc;
			
		} catch (IOException e) {

		}
return null;
	}

}

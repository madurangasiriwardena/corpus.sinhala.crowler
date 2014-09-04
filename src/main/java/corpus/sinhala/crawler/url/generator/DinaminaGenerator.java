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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.network.NetworkConnector;

public class DinaminaGenerator extends Observable {
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

	public DinaminaGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate, String host, int port) {
		System.out.println("aaaaaaa");
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
		articleName = new String[4];
		articleName[0] = "n";
		articleName[1] = "r";
		articleName[2] = "s";
		articleName[3] = "f";
//		articleName[4] = "e";
		articleNameId = 0;
		
		listEmpty = true;
		urls = new LinkedList<String>();

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);
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
		String url = "http://www.dinamina.lk/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/";

		return url;
	}
	
	public String listGenerator() {
		String url = "http://www.dinamina.lk/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/_art.asp?fn="
				+ articleName[articleNameId]
				+ Integer.toString(year).substring(2)
				+ String.format("%02d", month) + String.format("%02d", date);

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
				dt = dt.plusDays(1);
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
//				System.out.println("----------------------");
				for(int i=0; i<urlList.size(); i++){
					String base = baseGenerator();
//					System.out.println(base+urlList.get(i).attr("href"));
					String tempUrl = urlList.get(i).attr("href");
					if(tempUrl.contains("_art.asp?fn=" + articleName[articleNameId]) && !urls.contains(base+tempUrl))
					urls.add(base+tempUrl);
				}
//				System.out.println("-----------------------");
			} catch (IOException e) {
			}
			articleNameId++;
			System.out.println(urls.isEmpty());
		}
		
//		System.out.println(urls.isEmpty());
		String urlString = urls.remove();
		URL url = new URL(urlString);
//		System.out.println(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128));
//		 HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();

		try {
			uc.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
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

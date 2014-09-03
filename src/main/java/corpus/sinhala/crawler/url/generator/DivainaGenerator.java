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
import java.util.Observable;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import corpus.sinhala.crawler.network.NetworkConnector;

public class DivainaGenerator extends Observable{
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
	
	NetworkConnector nc;

	public DivainaGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate, String host, int port) {
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
		articleName = new String[7];
		articleName[0] = "news";
		articleName[1] = "feature";
		articleName[2] = "provin";
		articleName[3] = "velanda";
		articleName[4] = "cineart";
		articleName[5] = "sports";
		articleName[6] = "forign";
		articleNameId = 0;
		

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);
		System.out.println("Crawling from "+dt);
		endDate = new DateTime(eYear, eMonth, eDate, 0, 0, 0, 0);
		System.out.println("To "+endDate);
		dayAfterEndDate = endDate.plusDays(1);
		
		nc = NetworkConnector.getInstance();
		try {
			nc.connect(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generator() {
		String url = "http://www.divaina.com/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/" + articleName[articleNameId]
				+ String.format("%02d", articleId) + ".html";

		return url;
	}

	public Document fetchPage() throws IOException{
		String urlString = generator();
		URL url = new URL(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128)); // or whatever your proxy is
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
			articleId++;
			return doc;
		} catch (IOException e) {
			
			if(articleNameId<articleName.length-1){
				articleNameId++;
				articleId = 1;
				return fetchPage();
			}else{
				try{
					nc.send(year + "/" + String.format("%02d", month) + "/" + String.format("%02d", date));
					setChanged();
				    notifyObservers();
				}catch(IOException e1){
					return null;
				}
				articleNameId=0;
				dt = dt.plusDays(1);
				year = dt.getYear();
				month = dt.getMonthOfYear();
				date = dt.getDayOfMonth();
				articleId = 1;
				
				if(dt.isBefore(dayAfterEndDate) ){
					return fetchPage();
				}else{
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
			
		}

	}

}

package corpus.sinhala.crawler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import corpus.sinhala.crawler.infra.Generator;
import corpus.sinhala.crawler.infra.network.NetworkConnector;

public class LankadeepaGenerator extends Generator {
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
	boolean startDate;

	boolean listEmpty;
	Queue<String> urls;

	NetworkConnector nc;

	public LankadeepaGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate, String host, int port) {

		this.sYear = sYear;
		this.eYear = eYear;
		this.sMonth = sMonth;
		this.eMonth = eMonth;
		this.sDate = sDate;
		this.eDate = eDate;
		startDate = true;

		year = sYear;
		month = sMonth;
		date = sDate;


		urls = new LinkedList<String>();

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);

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


	public Document fetchPage() throws IOException {
		if (!dt.isBefore(dayAfterEndDate)) {
			return null;
		}
		while (urls.isEmpty()) {
			if(!startDate){
				try {
					String message = year + "-" + String.format("%02d", month)
							+ "-" + String.format("%02d", date);
					nc.send(year + "/" + String.format("%02d", month) + "/"
							+ String.format("%02d", date));
					setChanged();
					notifyObservers(message);
				} catch (IOException e1) {
					return null;
				}
				articleNameId = 0;
				dt = dt.plusDays(1);
				year = dt.getYear();
				month = dt.getMonthOfYear();
				date = dt.getDayOfMonth();

				if (!dt.isBefore(dayAfterEndDate)) {
					try {
						nc.send("close");
						nc.close();
					} catch (IOException e1) {
						return null;
					}

					return null;
				}

			}else{
				startDate = false;
			}

			HttpPost post = new HttpPost(
					"http://www.lankadeepa.lk/index.php/maincontroller/archive_container");
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			String payload = year + "-" + month + "-" + date;
			params.add(new BasicNameValuePair("date", payload));
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();

				String line = null;
				StringBuffer tmp = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						instream, "UTF-8"));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}
				Document doc = Jsoup.parse(String.valueOf(tmp));
				Elements urlList = doc.select("p[class=leftbar_news_heading]");
				for(int i=0; i<urlList.size(); i++){
					
					String tempUrl = urlList.get(i).select("a").get(0).attr("href");
					if( !urls.contains(tempUrl))
					urls.add(tempUrl);
				}
			}

			
			System.out.println(urls.isEmpty());
		}

		String urlString = urls.remove();
		URL url = new URL(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128));
		// HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
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

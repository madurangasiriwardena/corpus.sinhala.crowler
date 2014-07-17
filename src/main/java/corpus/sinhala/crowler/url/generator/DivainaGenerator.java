package corpus.sinhala.crowler.url.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DivainaGenerator {
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
	String articleName;

	DateTime dt;
	DateTime endDate;
	DateTime dayAfterEndDate;

	public DivainaGenerator(int sYear, int eYear, int sMonth, int eMonth,
			int sDate, int eDate) {
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
		articleName = "news";

		dt = new DateTime(sYear, sMonth, sDate, 0, 0, 0, 0);
		System.out.println("Crawling from "+dt);
		endDate = new DateTime(eYear, eMonth, eDate, 0, 0, 0, 0);
		System.out.println("To "+endDate);
		dayAfterEndDate = endDate.plusDays(1);
	}

	public String generator() {
		String url = "http://www.divaina.com/" + year + "/"
				+ String.format("%02d", month) + "/"
				+ String.format("%02d", date) + "/" + articleName
				+ String.format("%02d", articleId) + ".html";

		return url;
	}

	public Document fetchPage() throws IOException {
		String urlString = generator();
//		System.out.println(urlString);
		URL url = new URL(urlString);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"cache.mrt.ac.lk", 3128)); // or whatever your proxy is
		HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);

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
			articleId++;
			return doc;
		} catch (IOException e) {
			dt = dt.plusDays(1);
			year = dt.getYear();
			month = dt.getMonthOfYear();
			date = dt.getDayOfMonth();
			articleId = 1;
			
			if(dt.isBefore(dayAfterEndDate) ){
				return fetchPage();
			}else{
				return null;
			}
		}

	}

}

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

public class MawbimaGenerator extends Generator{
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

	public MawbimaGenerator(int sYear, int eYear, int sMonth, int eMonth,
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
		articleName = new String[70];
		
		//ලග්න ඵලාඵල
		articleName[0] = "105";
		articleName[1] = "106";
		articleName[2] = "107";
		articleName[3] = "108";
		articleName[4] = "109";
		articleName[5] = "110";
		articleName[6] = "111";
		articleName[7] = "112";
		articleName[8] = "113";
		articleName[9] = "114";
		articleName[10] = "115";
		articleName[11] = "116";
		
		articleName[12] = "31"; 	//Top Images
		articleName[13] = "34";		//මව්බිම දැන්
		articleName[14] = "35";		//Top Story
		articleName[15] = "38";		//හත් දෙයියනේ
		articleName[16] = "67";		//ජාත්යන්තර
		articleName[17] = "81";		//ආර්යා
		articleName[18] = "82";		//young
		articleName[19] = "83";		//mawbimaft
		articleName[20] = "84";		//ඵලාඵල
		articleName[21] = "85";		//mawbima 5
		articleName[22] = "86";		//ada mawbimen
		articleName[23] = "98";		//Political Gossip
		articleName[24] = "120";	//Methiwarabna Prathipala Esanin
		articleName[25] = "129";	//Mawbima pitarata
		articleName[26] = "54";		//Mawbima Main
		articleName[27] = "55";		//Sanakeliya
		articleName[28] = "56";		//Para dige
		articleName[29] = "57";		//satana
		articleName[30] = "58";		//jiwithayata ingrisi
		articleName[31] = "117";	//tharupathi
		articleName[32] = "40";		//alimankada
		articleName[33] = "44";		//aksharaya
		articleName[34] = "93";		//Thaprobana
		articleName[35] = "94";		//Cinegallary
		articleName[36] = "95";		//pekada
		articleName[37] = "122";	//surasa
		articleName[38] = "128";	//wesak kalapaya
		articleName[39] = "66";		//mawbima sports
		articleName[40] = "123";	//young reporters
		articleName[41] = "124";	//ape wigrahaya
		articleName[42] = "125";	//foreign news
		articleName[43] = "126";	//Interviews
		articleName[44] = "127";	//Local news
		articleName[45] = "48";		//fashion
		articleName[46] = "49";		//Movies
		articleName[47] = "50";		//Music
		articleName[48] = "51";		//Theatre
		articleName[49] = "52";		//Video
		articleName[50] = "79";		//Aththage kolaba
		articleName[51] = "80";		//Kathuwakiya
		articleName[52] = "87";		//Sagarayak meda
		articleName[53] = "88";		//Gurudina Wigrahaya
		articleName[54] = "89";		//rate patta
		articleName[55] = "91";		//kochchi karala
		articleName[56] = "92";		//kabinet 1
		articleName[57] = "96";		//rawa pratirawa
		articleName[58] = "97";		//baldiya
		articleName[59] = "99";		//කථානායකතුමනි
		articleName[60] = "100";	//රටහදන තැන
		articleName[61] = "101";	//ඇත්ත
		articleName[62] = "102";	//විකෘති
		articleName[63] = "121";	//වෙන්නේ මොකක්ද
		articleName[64] = "32";		//Cartoon
		articleName[65] = "33";		//විඩීයෝ
		articleName[66] = "75";		//මව්බිම Ft
		articleName[67] = "76";		//දිනපොත
		articleName[68] = "90";		//පළාත් සභා මැතිවරණය - 2013<
		articleName[69] = "103";	//විශේෂාංග
		
		articleNameId = 0;
		
		listEmpty = true;
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

	public String baseGenerator() {
		String url = "http://www.mawbima.lk/";

		return url;
	}
	
	public String listGenerator() {
		String url = "http://www.mawbima.lk/" +articleName[articleNameId] + "-0-" + date + "-"
				+ year + "-"
				+ String.format("%02d", month) + "-"
				+ String.format("%02d", date) + "-archive-list.html";

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
			
			HttpPost post = new HttpPost(urlString);

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

				Elements urlList = doc.select("span[class=titletextash]");
				for(int i=0; i<urlList.size(); i++){
					
					String tempUrl = urlList.get(i).select("a").first().attr("href");
					if( !urls.contains(base+tempUrl)){
						urls.add(base+tempUrl);
					}
				}
	    	}
			
			
			articleNameId++;
		}
		
		String urlString = urls.remove();
		HttpPost post = new HttpPost(urlString);

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
			doc.setBaseUri(urlString);
			return doc;
    	}

		
return null;
	}

}

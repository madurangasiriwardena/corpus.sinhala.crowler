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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String driverName;
	String url;
	String uname;
	String pwd;
	
	public Crawler() {
		driverName = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://"+"localhost"+":3306/crawler_data";
		uname = "root";
		pwd = "";
		if (conn == null)
			conn = getConnection(driverName, url, "root", "");
	}
	
	public Connection getConnection(String driver, String url, String uname,
			String pwd) {
		Connection conn = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, uname, pwd);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return conn;
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		Crawler crawler = new Crawler();
		crawler.crawl();
	}
	
	public void crawl() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//System.out.println(dateFormat.format(date));
		int counter=1;
		String startingUrl = "http://si.wikipedia.org/wiki/විශේෂ:සියළු_පිටු";
		String nextUrl=startingUrl;
		XMLFileWriter writer = new XMLFileWriter();
		while(nextUrl!=null){
			//System.out.println(nextUrl);
			URL url = new URL(nextUrl);
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
				
			}
			Document doc = Jsoup.parse(String.valueOf(tmp));
			doc.setBaseUri(nextUrl);
			
			Elements pageLinks=doc.select("ul[class=mw-allpages-chunk]").first().select("li");
			for (int i = 0; i < pageLinks.size(); i++) {
				String pageLink="http://si.wikipedia.org" + pageLinks.get(i).select("a").first().attr("href");
				//System.out.println("aaa           " + pageLink);
				if(checkDB(pageLink)){
					continue;
				}
				url = new URL(pageLink);
				uc = (HttpURLConnection) url.openConnection();
				uc.connect();
				line = null;
				tmp = new StringBuffer();
				try{
				BufferedReader in = new BufferedReader(new InputStreamReader(
						uc.getInputStream(), "UTF-8"));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}
				}catch(FileNotFoundException e){
					
				}
				Document docPage = Jsoup.parse(String.valueOf(tmp));
				docPage.setBaseUri(pageLink);
				writeDB(pageLink);
				writer.addDocument(docPage+"", pageLink);
				if(counter%100==0){
					writer.update("/home/chamila/semester7/fyp/wikipedia/" + dateFormat.format(date) + "_" + (counter/100 +1));
				}
				counter ++;
			}
			
			Elements navElements=doc.select("div[class=mw-allpages-nav]").first().select("a");
			nextUrl=null;
			
			for (int i = 0; i < navElements.size(); i++) {
				Element element=navElements.get(i);
				if(element.text().startsWith("මීළඟ පිටුව")){
					nextUrl="http://si.wikipedia.org"+ element.attr("href");
				}
			}
			
			
		}
		
		
	}
	
	boolean checkDB(String url){
		String query = "SELECT * FROM wikipedia_content WHERE url = ?";
		ResultSet rs;

		PreparedStatement stmt3;
		try {
			stmt3 = conn.prepareStatement(query);
			stmt3.setString(1, url);
			rs = stmt3.executeQuery();
			if (rs.first()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void writeDB(String url){
		String query = "INSERT INTO wikipedia_content (`url`) VALUES (?);";

		PreparedStatement stmt3;
		try {
			stmt3 = conn.prepareStatement(query);
			stmt3.setString(1, url);
			stmt3.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

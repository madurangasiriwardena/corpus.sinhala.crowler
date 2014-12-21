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

import javax.xml.stream.XMLStreamException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//import corpus.sinhala.crawler.controller.db.DataSourceException;

//import corpus.sinhala.crawler.controller.SysProperty;

public class Crawler {

	public static void main(String[] args) throws IOException{
		
		Crawler crawler = new Crawler();
		try {
			crawler.crawl(crawler.get_start());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void crawl(int start) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		int index=start;
		String urlString;
		XMLFileWriter writer = new XMLFileWriter();
		while(true){
			
			if(index==11){
				urlString = "http://mahamegha.lk/mahawansa/namo_buddhaya-11/";
			}else{
			urlString = "http://mahamegha.lk/mahawansa/mahawansa-" + index + "/";
			}
			
			URL url = new URL(urlString);
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
				this.writeDB(index-1);
				writer.update("/home/chamila/semester7/fyp/mahawansa");
				break;
			}

			Document doc = Jsoup.parse(String.valueOf(tmp));
			doc.setBaseUri(urlString);
			if(this.isValid(doc)){
				writer.addDocument(doc+"", urlString);
				index++;
			}
			else{
				this.writeDB(index-1);
				writer.update("/home/chamila/semester7/fyp/mahawansa");
				break;
			}
			
			
		}
	}
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String driverName;
	String url;
	String uname;
	String pwd;
	
	public int get_start(){
		driverName = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://"+"localhost"+":3306/crawler_data";
		uname = "root";
		pwd = "";
		connect();
		
		String query = "SELECT * FROM mahawansa_size WHERE ID = 1";
		ResultSet rs;

		PreparedStatement stmt3;
		try {
			stmt3 = conn.prepareStatement(query);
			rs = stmt3.executeQuery();
			if (rs.next()) {
				int index = rs.getInt("size");
				System.out.println("index = " + index);
				return index+1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return 1;
	}
	
	private void connect() {
		
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
	
	public void writeDB(int index){
		String query = "UPDATE mahawansa_size SET size=? WHERE id=?";

		PreparedStatement stmt3;
		try {
			stmt3 = conn.prepareStatement(query);
			stmt3.setInt(1, index);
			stmt3.setInt(2, 1);
			stmt3.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isValid(Document doc){
		
		org.jsoup.select.Elements elements =  doc.select("div[class=news_item]");
		for(int i=0;i<elements.size();i++){
			if(elements.get(i).text().contains("මහාවංශය")){
				return true;
			}
		}
		return false;
	}
	
	
}

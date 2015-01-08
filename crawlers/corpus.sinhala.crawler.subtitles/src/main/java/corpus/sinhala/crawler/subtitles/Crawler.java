package corpus.sinhala.crawler.subtitles;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Crawler {
    int counter = 0;
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
	public static void main(String[] args) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		Crawler c = new Crawler();
		Parser p = new Parser();
		Downloader d = new Downloader();
		XMLFileWriter writer = new XMLFileWriter("/home/chamila/semester7/fyp/folders/data/");
		c.crawl(d,writer,"/home/chamila/semester7/fyp/subtitles/",p);
	}
	
	public void crawl(Downloader d, XMLFileWriter writer, String SaveLoc,Parser p) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		int count = 1;
		outer:
		while(true){
			HttpGet post = new HttpGet("http://www.baiscopelk.com/category/%E0%B7%83%E0%B7%92%E0%B6%82%E0%B7%84%E0%B6%BD-%E0%B6%8B%E0%B6%B4%E0%B7%83%E0%B7%92%E0%B6%BB%E0%B7%90%E0%B7%83/page/" + count + "/");
			//if page notexist, break
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

				
				Document doc = Jsoup.parse(String.valueOf(tmp));
				if(doc.select("title").first().text().contains("Page not found")){
					break;
				}
				Elements urlList = doc.select("article[class=item-list]");
				for(int i=0; i<urlList.size(); i++){
					
					String tempUrl = urlList.get(i).select("a").first().attr("href");
					if(checkDB(tempUrl)){
						writer.update();
						break outer;
					}
					try{
						getZip(tempUrl,d,SaveLoc, writer,p);
						writeDB(tempUrl);
					}catch(Exception e){
						System.out.println(tempUrl);
					}
				}
	    	}
	    	count++;
	    	//break;
	    	
		}
		//System.out.println("break");
	}
	
	public void getZip(String url, Downloader d, String saveLoc, XMLFileWriter writer, Parser p) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		HttpGet post = new HttpGet(url);

    	HttpClient httpclient = HttpClients.createDefault();
    	HttpResponse response = httpclient.execute(post);
    	HttpEntity entity = response.getEntity();
    	
    	if (entity != null) {
    		System.out.println("True");
    	    InputStream instream = entity.getContent();
    	    
    	    String line = null;
			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}

			
			Document doc = Jsoup.parse(String.valueOf(tmp));

			Elements zipUrl = doc.select("a");
			for(int i=0;i<zipUrl.size();i++){
				if(zipUrl.get(i).text().contains("සිංහල උපසිරැසි මෙතනින් බාගන්න")){
					String link = zipUrl.get(i).attr("href");
					//d.Download(link, saveLoc );
					System.out.println(link);
					String[] arr = link.split("/");
					int len = arr.length;
					System.out.println(saveLoc + arr[len-1].split("=")[1] + "===");
					d.Download(link, saveLoc + arr[len-1].split("=")[1]);
					d.unzip(saveLoc + arr[len-1].split("=")[1], saveLoc + arr[len-1].split("=")[1].split(".zip")[0]);
					String title = arr[len-1].split("=")[1].split(".zip")[0];
					File folder = new File(saveLoc + arr[len-1].split("=")[1].split(".zip")[0]   );
					File[] listOfFiles = folder.listFiles();
					System.out.println(listOfFiles[0].toString() + "-----------");
					File innerFolder = new File(listOfFiles[0].toString());
					listOfFiles = innerFolder.listFiles();
					for (int j = 0; j < listOfFiles.length; j++) {
						if(listOfFiles[j].toString().endsWith("srt")){
							System.out.println(listOfFiles[j].toString());
							writer.addDocument(link,title, p.getText(listOfFiles[j].toString()));
							counter ++;
							System.out.println(counter);
							if(counter%100==0){
								writer.update();
								
							}
							break;
						}
					}
					System.out.println("---------------------------");
					//writer.addDocument(link, arr[len-1].split("=")[1].split(".zip")[0], "");
				}
			}
			
    	}
	}
	
	boolean checkDB(String url){
		String query = "SELECT * FROM subtitle_content WHERE url = ?";
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
		String query = "INSERT INTO subtitle_content (`url`) VALUES (?);";

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

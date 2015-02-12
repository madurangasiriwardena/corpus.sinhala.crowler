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

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Crawler {
    private int counter = 0;
    private Connection conn = null;
    private String driverName,url,uname,pwd;
    
	private final static Logger logger = Logger.getLogger(Crawler.class);
	
	public Crawler() {
		driverName = ConfigManager.getProperty(ConfigManager.MYSQL_DB_DRIVER);
		url = ConfigManager.getProperty(ConfigManager.MYSQL_DB_CONNECTION);
		uname = ConfigManager.getProperty(ConfigManager.MYSQL_DB_USER);
		pwd = ConfigManager.getProperty(ConfigManager.MYSQL_DB_PASSWORD);
		if (conn == null)
			conn = getConnection(driverName, url, uname, pwd);
	}
	
	public Connection getConnection(String driver, String url, String uname,
			String pwd) {
		Connection conn = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, uname, pwd);

		} catch (SQLException ex) {
			logger.error(ex);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} 

		return conn;
	}
	public static void main(String[] args) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		Crawler c = new Crawler();
		Parser p = new Parser();
		Downloader d = new Downloader();
		XMLFileWriter writer = new XMLFileWriter(ConfigManager.getProperty(ConfigManager.XML_SAVE_LOCATION));
		c.crawl(d,writer,ConfigManager.getProperty(ConfigManager.ZIP_SAVE_LOCATION),p);
	}
	
	public void crawl(Downloader d, XMLFileWriter writer, String SaveLoc,Parser p) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		int count = 1;
		outer:
		while(true){
			HttpGet post = new HttpGet(ConfigManager.getProperty(ConfigManager.BAISCOPE_LIST_PAGE) + count + "/");
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
						logger.error(e);
					}
				}
	    	}
	    	count++;
	    	
		}
	}
	
	public void getZip(String url, Downloader d, String saveLoc, XMLFileWriter writer, Parser p) throws ClientProtocolException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XMLStreamException{
		HttpGet post = new HttpGet(url);

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

			Elements zipUrl = doc.select("a");
			for(int i=0;i<zipUrl.size();i++){
				if(zipUrl.get(i).text().contains("සිංහල උපසිරැසි මෙතනින් බාගන්න")){
					String link = zipUrl.get(i).attr("href");
					String[] arr = link.split("/");
					int len = arr.length;
					d.Download(link, saveLoc + arr[len-1].split("=")[1]);
					d.unzip(saveLoc + arr[len-1].split("=")[1], saveLoc + arr[len-1].split("=")[1].split(".zip")[0]);
					String title = arr[len-1].split("=")[1].split(".zip")[0];
					File folder = new File(saveLoc + arr[len-1].split("=")[1].split(".zip")[0]   );
					File[] listOfFiles = folder.listFiles();
					File innerFolder = new File(listOfFiles[0].toString());
					listOfFiles = innerFolder.listFiles();
					for (int j = 0; j < listOfFiles.length; j++) {
						if(listOfFiles[j].toString().endsWith("srt")){
							writer.addDocument(link,title, p.getText(listOfFiles[j].toString()));
							counter ++;
							if(counter%5==0){
								writer.update();
							}
							break;
						}
					}
					
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
			logger.error(e);
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
			logger.error(e);
		}
	}
	
	
}

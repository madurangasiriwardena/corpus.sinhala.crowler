package corpus.sinhala.crawler.subtitles;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;


public class Downloader {
	
	public static void main(String[] args) throws FileNotFoundException{
		
				
	}
	
	
	public void unzip(String zipFile, String outputFolder) throws FileNotFoundException{
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		File dest = new File(outputFolder);
		byte[] buffer = new byte[1024];
		 
		try{
			 
	    	//create output directory is not exists
	    	File folder = dest;
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}
	 
	    	
	    	ZipEntry ze = zis.getNextEntry();
	 
	    	while(ze!=null){
	 
	    	   String fileName = ze.getName();
	           File newFile = new File(dest + File.separator + fileName);
	 
	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	 
	           if(ze.isDirectory()) 
	           {
	        	   new File(newFile.getParent()).mkdirs();
	           }
	           else
	           {
	        	FileOutputStream fos = null;
	 
	            new File(newFile.getParent()).mkdirs();
	 
	            fos = new FileOutputStream(newFile);             
	 
	            int len;
	            while ((len = zis.read(buffer)) > 0) 
	            {
	       		fos.write(buffer, 0, len);
	            }
	 
	            fos.close();   
	           }
	 
	           ze = zis.getNextEntry();
	    	}
	 
	        zis.closeEntry();
	    	zis.close();
	 
	    }catch(IOException ex){
	       ex.printStackTrace(); 
	    }
	}
	
	public void Download(String urlString, String filename) throws MalformedURLException, IOException{
		InputStream in = null;
	    FileOutputStream fout = null;
	    try {
	    	URL url = new URL(urlString);
	    	HttpGet post = new HttpGet(urlString);

	    	HttpClient httpclient = HttpClients.createDefault();
	    	HttpResponse response = httpclient.execute(post);
	    	HttpEntity entity = response.getEntity();
	    	if (entity != null) {
	        	in = entity.getContent();
				fout = new FileOutputStream(filename);

		        final byte data[] = new byte[1024];
		        int count;
		        while ((count = in.read(data, 0, 1024)) != -1) {
		        	fout.write(data, 0, count);
		        }
	        }
	        
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}
	
}

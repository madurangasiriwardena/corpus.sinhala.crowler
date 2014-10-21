package corpus.sinhala.crawler.subtitles;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {

	public static void main(String[] args){
		
	}
	
	public String getText(String filepath){
		String texts = "";
		BufferedReader br = null;
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(filepath));
			sCurrentLine = br.readLine();
			String text;
			while ( sCurrentLine!= null) {
				sCurrentLine = br.readLine();
				sCurrentLine = br.readLine();
				sCurrentLine = sCurrentLine.replaceAll("(<.*(?!<).*>)?", "");
				texts = texts + sCurrentLine + ". ";
				sCurrentLine = br.readLine();
				while(sCurrentLine!= null && sCurrentLine.length()!=0){
					
			        sCurrentLine = sCurrentLine.replaceAll("(<.*(?!<).*>)?", "");
			        texts = texts + sCurrentLine;
					sCurrentLine = br.readLine();
				}
				sCurrentLine = br.readLine();
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return texts;
	}
	
}

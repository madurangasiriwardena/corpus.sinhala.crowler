package corpus.sinhala.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.snowtide.PDF;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Document;

import corpus.sinhala.crawler.infra.Parser;

public class GazetteParser implements Parser{
	File pdf;
	int year;
	int month;
	int day;
	String text;
	String url;
//	Document doc;
//	String url;
//	Element titleElement;
//	Element p;
//	String[] arr;
	
	public static void main(String args[]) throws ClientProtocolException, IOException{
		GazetteParser gp = new GazetteParser("http://documents.gov.lk/gazette/2014/PDF/Dec/05Dec2014/II(S)2014.12.05.pdf", 2014, 12, 5);
	}
	
	public GazetteParser(String url, int year, int month, int day) throws ClientProtocolException, IOException{
//		doc = Jsoup.parse(page);
//		this.url = url;
//		titleElement = doc.select("span[class=subtitle newsdetailssubtitle").first();
//		p = titleElement.parent();
		this.year = year;
		this.month = month;
		this.day = day;
		this.url = url;
		
		download(url);
		
	}
	
	
	private void download(String urlString) throws ClientProtocolException, IOException{
		InputStream in = null;
	    FileOutputStream fout = null;
	    //System.out.println(filename);
	    try {
	    	URL url = new URL(urlString);
	    	HttpGet post = new HttpGet(urlString);
	    	HttpClient httpclient = HttpClients.createDefault();
	    	HttpResponse response = httpclient.execute(post);
	    	HttpEntity entity = response.getEntity();
	    	if (entity != null) {
	        	in = entity.getContent();
	    	    //System.out.println(filename);
	        	pdf = File.createTempFile("gazette", ".tmp");
				fout = new FileOutputStream(pdf);

		        final byte data[] = new byte[1024];
		        int count;
		        while ((count = in.read(data, 0, 1024)) != -1) {
		        	fout.write(data, 0, count);
		        }
	        }
	        
	    }finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
		text = getPDFText(pdf.getAbsolutePath());
		try{
			pdf.deleteOnExit();
		}catch(SecurityException e){
			
		}
	}
	
	public String getTitle(){
		return "";
	}
	
	public String getAuthor(){
		return "";
	}
	
	public String getContent(){
		return text;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getYear(){
		return year+"";
	}
	
	public String getMonth(){
		return month+"";
	}
	
	public String getDate(){
		return day+"";
	}
	
	public String getCategory() {
		return "GAZETTE";
	}
	
	String pdftoText(String fileName) {
		PDFParser parser;
		String parsedText = null;;
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(fileName);
		if (!file.isFile()) {
			System.err.println("File " + fileName + " does not exist.");
			return null;
		}
		try {
			parser = new PDFParser(new FileInputStream(file));
		} catch (IOException e) {
			System.err.println("Unable to open PDF Parser. " + e.getMessage());
			return null;
		}
		try {
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			System.err
					.println("An exception occured in parsing the PDF Document."
							+ e.getMessage());
		} finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return parsedText;
	}
	
	public String getPDFText (String pdfFilePath) throws IOException {
//		PDFTextStream pdfts = new PDFTextStream(pdfFilePath); 
		Document pdfts = PDF.open(pdfFilePath);
        StringBuilder str = new StringBuilder(1024);
        pdfts.pipe(new OutputTarget(str));
        pdfts.close();
        
        
        String text = str.toString();
        text = text.replace(",", "ල");
        //System.out.println(text);
        
        /*text = text.replace("/^"", "Ѫ");
        text = text.replace("/&"", "Ѧ");
        text = text.replace(/\"", "ʘ");
        text = text.replace(/@", "Ϣ");
        text = text.replace(/\(", "ƨ");
        text = text.replace(/\]", "Ɣ");
        text = text.replace(/\$", "ɤ");*/
        text = text.replace("ff;%", "ත්‍රෛ");
        text = text.replace("ffY", "ශෛ");
        text = text.replace("ffp", "චෛ");
        text = text.replace("ffc", "ජෛ");
        text = text.replace("ffl", "කෛ");
        text = text.replace("ffu", "මෛ");
        text = text.replace("ffm", "පෛ");
        text = text.replace("ffo", "දෛ");
        text = text.replace("ff;", "තෛ");
        text = text.replace("ffk", "නෛ");
        text = text.replace("ffO", "ධෛ");
        text = text.replace("ffj", "වෛ");
        text = text.replace("fm%!", "ප්‍රෞ");
        text = text.replace("fIHda", "ෂ්‍යෝ");
        text = text.replace("fPHda", "ඡ්‍යෝ");
        text = text.replace("fVHda", "ඪ්‍යෝ");
        text = text.replace("f>Hda", "ඝ්‍යෝ");
        text = text.replace("fLHda", "ඛ්‍යෝ");
        text = text.replace("f<Hda", "ළ්‍යෝ");
        text = text.replace("fMHda", "ඵ්‍යෝ");
        text = text.replace("fGHda", "ඨ්‍යෝ");
        text = text.replace("fYHda", "ශ්‍යෝ");
        text = text.replace("fÌHda", "ක්‍ෂ්‍යෝ");
        text = text.replace("fnHda", "බ්‍යෝ");
        text = text.replace("fpHda", "ච්‍යෝ");
        text = text.replace("fâHda", "ඩ්‍යෝ");
        text = text.replace("f*Hda", "ෆ්‍යෝ");
        text = text.replace("f.Hda", "ග්‍යෝ");
        text = text.replace("fcHda", "ජ්‍යෝ");
        text = text.replace("flHda", "ක්‍යෝ");
        text = text.replace("f,Hda", "ල්‍යෝ");
        text = text.replace("fuHda", "ම්‍යෝ");
        text = text.replace("fkHda", "න්‍යෝ");
        text = text.replace("fmHda", "ප්‍යෝ");
        text = text.replace("foHda", "ද්‍යෝ");
        text = text.replace("fiHda", "ස්‍යෝ");
        text = text.replace("fgHda", "ට්‍යෝ");
        text = text.replace("fjHda", "ව්‍යෝ");
        text = text.replace("f;Hda", "ත්‍යෝ");
        text = text.replace("fNHda", "භ්‍යෝ");
        text = text.replace("fOHda", "ධ්‍යෝ");
        text = text.replace("f:Hda", "ථ්‍යෝ");
        text = text.replace("fIHd", "ෂ්‍යො");
        text = text.replace("fYHd", "ශ්‍යො");
        text = text.replace("fLHd", "ඛ්‍යො");
        text = text.replace("fÌHd", "ක්‍ෂ්‍යො");
        text = text.replace("fnHd", "බ්‍යො");
        text = text.replace("fjHd", "ව්‍යො");
        text = text.replace("fvHd", "ඩ්‍යො");
        text = text.replace("f*Hd", "ෆ්‍යො");
        text = text.replace("f.Hd", "ග්‍යො");
        text = text.replace("fcHd", "ජ්‍යො");
        text = text.replace("flHd", "ක්‍යො");
        text = text.replace("fuHd", "ම්‍යො");
        text = text.replace("fmHd", "ප්‍යො");
        text = text.replace("foHd", "ද්‍යො");
        text = text.replace("fiHd", "ස්‍යො");
        text = text.replace("fgHd", "ට්‍යො");
        text = text.replace("fjHd", "ව්‍යො");
        text = text.replace("fkHd", "න්‍යො");
        text = text.replace("f;Hd", "ත්‍යො");
        text = text.replace("fNHd", "භ්‍යො");
        text = text.replace("fOHd", "ධ්‍යො");
        text = text.replace("f:Hd", "ථ්‍යො");
        text = text.replace("fIH", "ෂ්‍යෙ");
        text = text.replace("fPH", "ඡ්‍යෙ");
        text = text.replace("f<H", "ළ්‍යෙ");
        text = text.replace("fKH", "ණ්‍යෙ");
        text = text.replace("fpH", "ච්‍යෙ");
        text = text.replace("f,H", "ල්‍යෙ");
        text = text.replace("fkH", "න්‍යෙ");
        text = text.replace("fYH", "ශ්‍යෙ");
        text = text.replace("fLH", "ඛ්‍යෙ");
        text = text.replace("fÌH", "ක්‍ෂ්‍ය");
        text = text.replace("fnH", "බ්‍යෙ");
        text = text.replace("fvH", "ඩ්‍යෙ");
        text = text.replace("f*H", "ෆ්‍යෙ");
        text = text.replace("f.H", "ග්‍යෙ");
        text = text.replace("fcH", "ජ්‍යෙ");
        text = text.replace("flH", "ක්‍යෙ");
        text = text.replace("fuH", "ම්‍යෙ");
        text = text.replace("fmH", "ප්‍යෙ");
        text = text.replace("foH", "ද්‍යෙ");
        text = text.replace("fiH", "ස්‍යෙ");
        text = text.replace("fgH", "ට්‍යෙ");
        text = text.replace("fjH", "ව්‍යෙ");
        text = text.replace("f;H", "ත්‍යෙ");
        text = text.replace("fNH", "භ්‍යෙ");
        text = text.replace("fOH", "ධ්‍යෙ");
        text = text.replace("f:H", "ථ්‍යෙ");
        text = text.replace("fI%da", "ෂ්‍රෝ");
        text = text.replace("f>%da", "ඝ්‍රෝ");
        text = text.replace("fY%da", "ශ්‍රෝ");
        text = text.replace("fÌ%da", "ක්‍ෂ්‍රෝ");
        text = text.replace("fn%da", "බ්‍රෝ");
        text = text.replace("fv%da", "ඩ්‍රෝ");
        text = text.replace("f*%da", "ෆ්‍රෝ");
        text = text.replace("f.%da", "ග්‍රෝ");
        text = text.replace("fl%da", "ක්‍රෝ");
        text = text.replace("fm%da", "ප්‍රෝ");
        text = text.replace("fØda", "ද්‍රෝ");
        text = text.replace("fi%da", "ස්‍රෝ");
        text = text.replace("fg%da", "ට්‍රෝ");
        text = text.replace("f;%da", "ත්‍රෝ");
        text = text.replace("fY%d", "ශ්‍රො");
        text = text.replace("fv%d", "ඩ්‍රො");
        text = text.replace("f*%d", "ෆ්‍රො");
        text = text.replace("f.%d", "ග්‍රො");
        text = text.replace("fl%d", "ක්‍රො");
        text = text.replace("fm%d", "ප්‍රො");
        text = text.replace("fØd", "ද්‍රො");
        text = text.replace("fi%d", "ස්‍රො");
        text = text.replace("fg%d", "ට්‍රො");
        text = text.replace("f;%d", "ත්‍රො");
        text = text.replace("%a", "a%");
        text = text.replace("fYa%", "ශ්‍රේ");
        text = text.replace("fí%", "බ්‍රේ");
        text = text.replace("fâ%", "ඩ්‍රේ");
        text = text.replace("f*a%", "ෆ්‍රේ");
        text = text.replace("f.a%", "ග්‍රේ");
        text = text.replace("fla%", "ක්‍රේ");
        text = text.replace("fma%", "ප්‍රේ");
        text = text.replace("fØa", "ද්‍රේ");
        text = text.replace("fia%", "ස්‍රේ");
        text = text.replace("f;a%", "ත්‍රේ");
        text = text.replace("fè%", "ධ්‍රේ");
        text = text.replace("fI%", "ෂ්‍රෙ");
        text = text.replace("fY%", "ශ්‍රෙ");
        text = text.replace("fn%", "බ්‍රෙ");
        text = text.replace("f*%", "ෆ්‍රෙ");
        text = text.replace("f.%", "ග්‍රෙ");
        text = text.replace("fl%", "ක්‍රෙ");
        text = text.replace("fm%", "ප්‍රෙ");
        text = text.replace("fØ", "ද්‍රෙ");
        text = text.replace("fi%", "ස්‍රෙ");
        text = text.replace("f;%", "ත්‍රෙ");
        text = text.replace("fN%", "භ්‍රෙ");
        text = text.replace("fO%", "ධ්‍රෙ");
        text = text.replace("H", "්‍ය");
        text = text.replace("%", "්‍ර");
        text = text.replace("fI!", "ෂෞ");
        text = text.replace("fP!", "ඡෞ");
        text = text.replace("fY!", "ශෞ");
        text = text.replace("fn!", "බෞ");
        text = text.replace("fp!", "චෞ");
        text = text.replace("fv!", "ඩෞ");
        text = text.replace("f*!", "ෆෞ");
        text = text.replace("f.!", "ගෞ");
        text = text.replace("fc!", "ජෞ");
        text = text.replace("fl!", "කෞ");
        text = text.replace("f,!", "ලෞ");
        text = text.replace("fu!", "මෞ");
        text = text.replace("fk!", "නෞ");
        text = text.replace("fm!", "පෞ");
        text = text.replace("fo!", "දෞ");
        text = text.replace("fr!", "රෞ");
        text = text.replace("fi!", "සෞ");
        text = text.replace("fg!", "ටෞ");
        text = text.replace("f;!", "තෞ");
        text = text.replace("fN!", "භෞ");
        text = text.replace("f[!", "ඤෞ");
        text = text.replace("fIda", "ෂෝ");
        text = text.replace("fUda", "ඹෝ");
        text = text.replace("fPda", "ඡෝ");
        text = text.replace("fVda", "ඪෝ");
        text = text.replace("f>da", "ඝෝ");
        text = text.replace("fLda", "ඛෝ");
        text = text.replace("f<da", "ළෝ");
        text = text.replace("fÛda", "ඟෝ");
        text = text.replace("fKda", "ණෝ");
        text = text.replace("fMda", "ඵෝ");
        text = text.replace("fGda", "ඨෝ");
        text = text.replace("fËda", "ඬෝ");
        text = text.replace("fYda", "ශෝ");
        text = text.replace("f{da", "ඥෝ");
        text = text.replace("f|da", "ඳෝ");
        text = text.replace("fÌda", "ක්‍ෂෝ");
        text = text.replace("fnda", "බෝ");
        text = text.replace("fpda", "චෝ");
        text = text.replace("fvda", "ඩෝ");
        text = text.replace("f*da", "ෆෝ");
        text = text.replace("f.da", "ගෝ");
        text = text.replace("fyda", "හෝ");
        text = text.replace("fcda", "ජෝ");
        text = text.replace("flda", "කෝ");
        text = text.replace("f,da", "ලෝ");
        text = text.replace("fuda", "මෝ");
        text = text.replace("fkda", "නෝ");
        text = text.replace("fmda", "පෝ");
        text = text.replace("foda", "දෝ");
        text = text.replace("frda", "රෝ");
        text = text.replace("fida", "සෝ");
        text = text.replace("fgda", "ටෝ");
        text = text.replace("fjda", "වෝ");
        text = text.replace("f;da", "තෝ");
        text = text.replace("fNda", "භෝ");
        text = text.replace("fhda", "යෝ");
        text = text.replace("f[da", "ඤෝ");
        text = text.replace("fOda", "ධෝ");
        text = text.replace("f:da", "ථෝ");
        text = text.replace("fId", "ෂො");
        text = text.replace("fUd", "ඹො");
        text = text.replace("fPd", "ඡො");
        text = text.replace("fVd", "ඪො");
        text = text.replace("f>d", "ඝො");
        text = text.replace("fLd", "ඛො");
        text = text.replace("f<d", "ළො");
        text = text.replace("fÕd", "ඟො");
        text = text.replace("fKd", "ණො");
        text = text.replace("fMd", "ඵො");
        text = text.replace("fGd", "ඨො");
        text = text.replace("fËd", "ඬො");
        text = text.replace("fYd", "ශො");
        text = text.replace("f{d", "ඥො");
        text = text.replace("f|d", "ඳො");
        text = text.replace("fÌd", "ක්‍ෂො");
        text = text.replace("fnd", "බො");
        text = text.replace("fpd", "චො");
        text = text.replace("fvd", "ඩො");
        text = text.replace("f*d", "ෆො");
        text = text.replace("f.d", "ගො");
        text = text.replace("fyd", "හො");
        text = text.replace("fcd", "ජො");
        text = text.replace("fld", "කො");
        text = text.replace("f,d", "ලො");
        text = text.replace("fud", "මො");
        text = text.replace("fkd", "නො");
        text = text.replace("fmd", "පො");
        text = text.replace("fod", "දො");
        text = text.replace("frd", "රො");
        text = text.replace("fid", "සො");
        text = text.replace("fgd", "ටො");
        text = text.replace("fjd", "වො");
        text = text.replace("f;d", "තො");
        text = text.replace("fNd", "භො");
        text = text.replace("fhd", "යො");
        text = text.replace("f[d", "ඤො");
        text = text.replace("fOd", "ධො");
        text = text.replace("f:d", "ථො");
        text = text.replace("fIa", "ෂේ");
        text = text.replace("fò", "ඹේ");
        text = text.replace("fþ", "ඡේ");
        text = text.replace("f\\a", "ඪේ");
        text = text.replace("f>a", "ඝේ");
        text = text.replace("fÄ", "ඛේ");
        text = text.replace("f<a", "ළේ");
        text = text.replace("fÛa", "ඟේ");
        text = text.replace("fKa", "ණේ");
        text = text.replace("fMa", "ඵේ");
        text = text.replace("fGa", "ඨේ");
        text = text.replace("få", "ඬේ");
        text = text.replace("fYa", "ශේ");
        text = text.replace("f{a", "ඥේ");
        text = text.replace("f|a", "ඳේ");
        text = text.replace("fÌa", "ක්‍ෂේ");
        text = text.replace("fí", "බේ");
        text = text.replace("fÉ", "චේ");
        text = text.replace("fâ", "ඩේ");
        text = text.replace("f*a", "ෆේ");
        text = text.replace("f.a", "ගේ");
        text = text.replace("fya", "හේ");
        text = text.replace("fma", "පේ");
        text = text.replace("fla", "කේ");
        text = text.replace("f,a", "ලේ");
        text = text.replace("fï", "මේ");
        text = text.replace("fka", "නේ");
        text = text.replace("f–", "ජේ");
        text = text.replace("foa", "දේ");
        text = text.replace("f¾", "රේ");
        text = text.replace("fia", "සේ");
        text = text.replace("fÜ", "ටේ");
        text = text.replace("fõ", "වේ");
        text = text.replace("f;a", "තේ");
        text = text.replace("fNa", "භේ");
        text = text.replace("fha", "යේ");
        text = text.replace("f[a", "ඤේ");
        text = text.replace("fè", "ධේ");
        text = text.replace("f:a", "ථේ");
        text = text.replace("fI", "ෂෙ");
        text = text.replace("fU", "ඹෙ");
        text = text.replace("ft", "ඓ");
        text = text.replace("fP", "ඡෙ");
        text = text.replace("fV", "ඪෙ");
        text = text.replace("f>", "ඝෙ");
        text = text.replace("fn", "ඛෙ");
        text = text.replace("f<", "ළෙ");
        text = text.replace("fÛ", "ඟෙ");
        text = text.replace("fK", "ණෙ");
        text = text.replace("fM", "ඵෙ");
        text = text.replace("fG", "ඨෙ");
        text = text.replace("fË", "ඬෙ");
        text = text.replace("fY", "ශෙ");
        text = text.replace("f{", "ඥෙ");
        text = text.replace("fË", "ඳෙ");
        text = text.replace("fÌ", "ක්‍ෂෙ");
        text = text.replace("fn", "බෙ");
        text = text.replace("fp", "චෙ");
        text = text.replace("fv", "ඩෙ");
        text = text.replace("f*", "ෆෙ");
        text = text.replace("f.", "ගෙ");
        text = text.replace("fy", "හෙ");
        text = text.replace("fc", "ජෙ");
        text = text.replace("fl", "කෙ");
        text = text.replace("f,", "ලෙ");
        text = text.replace("fu", "මෙ");
        text = text.replace("fk", "නෙ");
        text = text.replace("fm", "පෙ");
        text = text.replace("fo", "දෙ");
        text = text.replace("fr", "රෙ");
        text = text.replace("fi", "සෙ");
        text = text.replace("fg", "ටෙ");
        text = text.replace("fj", "වෙ");
        text = text.replace("f;", "තෙ");
        text = text.replace("fN", "භෙ");
        text = text.replace("fh", "යෙ");
        text = text.replace("f[", "ඤෙ");
        text = text.replace("fO", "ධෙ");
        text = text.replace("f:", "ථෙ");
        text = text.replace(";=", "තු");
        text = text.replace(".=", "ගු");
        text = text.replace("=", "කු");
        text = text.replace(";+", "තූ");
        text = text.replace("+", "භූ");
        text = text.replace("=", "භු");
        text = text.replace(".+", "ගූ");
        text = text.replace("+", "කූ");
        text = text.replace("re", "රු");
        text = text.replace("rE", "රූ");
        text = text.replace("wd", "ආ");
        text = text.replace("we", "ඇ");
        text = text.replace("wE", "ඈ");
        text = text.replace("W!", "ඌ");
        text = text.replace("T!", "ඖ");
        text = text.replace("ta", "ඒ");
        text = text.replace("´", "ඕ");
        text = text.replace("¢", "ඳි");
        text = text.replace("£", "ඳී");
        text = text.replace("¥", "දූ");
        text = text.replace("§", "දී");
        text = text.replace("¨", "ලූ");
        text = text.replace("©", "ර්‍ය");
        text = text.replace("ª", "ඳූ");
        text = text.replace("¾", "ර්");
        text = text.replace("3/4", "ර්");
        //text = text.replace("3", "ර්");
        
        text = text.replace("À", "ඨි");
        text = text.replace("Á", "ඨී");
        text = text.replace("Â", "ඡී");
        text = text.replace("Ä", "ඛ්");
        text = text.replace("Å", "ඛි");
        text = text.replace("Æ", "ලු");
        text = text.replace("Ç", "ඛී");
        text = text.replace("È", "දි");
        text = text.replace("É", "ච්");
        text = text.replace("Ê", "ජ්");
        text = text.replace("Í", "රී");
        text = text.replace("Î", "ඪී");
        text = text.replace("Ð", "ඪී");
        text = text.replace("Ñ", "චි");
        text = text.replace("Ò", "ථී");
        text = text.replace("Ó", "ථී");
        text = text.replace("Ô", "ජී");
        text = text.replace("Ö", "චී");
        text = text.replace("Ù", "ඞ්");
        text = text.replace("Ú", "ඵී");
        text = text.replace("Ü", "ට්");
        text = text.replace("Ý", "ඵි");
        text = text.replace("ß", "රි");
        text = text.replace("à", "ටී");
        text = text.replace("á", "ටි");
        text = text.replace("â", "ඩ්");
        text = text.replace("ã", "ඩී");
        text = text.replace("ä", "ඩි");
        text = text.replace("å", "ඬ්");
        text = text.replace("ç", "ඬි");
        text = text.replace("è", "ධ්");
        text = text.replace("é", "ඬී");
        text = text.replace("ê", "ධි");
        text = text.replace("ë", "ධී");
        text = text.replace("ì", "බි");
        text = text.replace("í", "බ්");
        text = text.replace("î", "බී");
        text = text.replace("ï", "ම්");
        text = text.replace("ð", "ජි");
        text = text.replace("ñ", "මි");
        text = text.replace("ò", "ඹ්");
        text = text.replace("ó", "මී");
        text = text.replace("ô", "ඹි");
        text = text.replace("õ", "ව්");
        text = text.replace("ö", "ඹී");
        text = text.replace("÷", "ඳු");
        text = text.replace("Ø", "ද්‍ර");
        text = text.replace("ù", "වී");
        text = text.replace("ú", "වි");
        text = text.replace("û", "ඞ්");
        text = text.replace("ü", "ඞී");
        text = text.replace("ý", "ඡි");
        text = text.replace("þ", "ඡ්");
        text = text.replace("ÿ", "දු");
        text = text.replace("±", "දැ");
        text = text.replace("–", "ජ්");
        text = text.replace("“", "ර්‍ණ");
        
        text = text.replace("„", "ජී");
        text = text.replace("‰", "ඡි");
        text = text.replace("", "ඩි");
        text = text.replace("™", "ඤු");
        text = text.replace(".", "ග");
        text = text.replace("¿", "ළු");
        text = text.replace("I", "ෂ");
        text = text.replace("x", "ං");
        text = text.replace("#", "ඃ");
        text = text.replace("U", "ඹ");
        text = text.replace("P", "ඡ");
        text = text.replace("V", "ඪ");
        text = text.replace(">", "ඝ");
        text = text.replace("B", "ඊ");
        text = text.replace("CO", "ඣ");
        text = text.replace("L", "ඛ");
        text = text.replace("<", "ළ");
        text = text.replace("Û", "ඟ");
        text = text.replace("Ë", "ක්‍ෂ");
        text = text.replace("K", "ණ");
        text = text.replace("M", "ඵ");
        text = text.replace("G", "ඨ");
        text = text.replace("#", "ඃ");
        text = text.replace("&", ")");
        
        text = text.replace(")", "-");
        text = text.replace("*", "ෆ");
        text = text.replace(",", "ල");
        text = text.replace("\"", ",");
        text = text.replace("-", "-");
        text = text.replace("/", "රැ");
        text = text.replace(":", "ථ");
        text = text.replace("(", ":");
        text = text.replace(";", "ත");
        text = text.replace("<", "ළ");
        text = text.replace(">", "ඝ");
        text = text.replace("?", "රෑ");
        text = text.replace("B", "ඊ");
        text = text.replace("C", "ක‍");
        text = text.replace(";", "ත‍");
        text = text.replace("G", "ඨ");
        text = text.replace("H", "්‍ය");
        text = text.replace("I", "ෂ");
        text = text.replace("J", "න‍");
        text = text.replace("K", "ණ");
        text = text.replace("L", "ඛ");
        text = text.replace("M", "ඵ");
        text = text.replace("N", "භ");
        text = text.replace("O", "ධ");
        text = text.replace("P", "ඡ");
        text = text.replace("R", "ඍ");
        text = text.replace("T", "ඔ");
        text = text.replace("U", "ඹ");
        text = text.replace("V", "ඪ");
        text = text.replace("W", "උ");
        text = text.replace("Y", "ශ");
        text = text.replace("[", "ඤ");
        text = text.replace("b", "ඉ");
        text = text.replace("c", "ජ");
        text = text.replace("g", "ට");
        text = text.replace("h", "ය");
        text = text.replace("i", "ස");
        text = text.replace("j", "ව");
        text = text.replace("k", "න");
        text = text.replace("l", "ක");
        text = text.replace("m", "ප");
        text = text.replace("n", "බ");
        text = text.replace("o", "ද");
        text = text.replace("p", "ච");
        text = text.replace("r", "ර");
        text = text.replace("t", "එ");
        text = text.replace("u", "ම");
        text = text.replace("v", "ඩ");
        text = text.replace("w", "අ");
        text = text.replace("y", "හ");
        text = text.replace("Õ", "ඟ");
        text = text.replace("{", "ඥ");
        text = text.replace("|", "ඳ");
        text = text.replace("Ì", "ක්‍ෂ");
        text = text.replace("µ", "ද්‍ය");
        text = text.replace("e", "ැ");
        text = text.replace("E", "ෑ");
        text = text.replace("f", "ෙ");
        text = text.replace("q", "ු");
        text = text.replace("s", "ි");
        text = text.replace("Q", "ූ");
        text = text.replace("S", "ී");
        text = text.replace("D", "ෘ");
        text = text.replace("DD", "ෲ");
        text = text.replace("!", "ෟ");
        text = text.replace("d", "ා");
        text = text.replace("a", "්");
        text = text.replace("\'", ".");
        text = text.replace("Ѫ", "(");
        text = text.replace("Ѧ", ")");
        text = text.replace("ʘ", ",");
        text = text.replace("Ϣ", "?");
        text = text.replace("ƨ", ":");
        text = text.replace("Ɣ", "%");
        text = text.replace("ɤ", "/");
        text = text.replace("    ", " ");
        //System.out.printf("The text extracted from %s is:", pdfFilePath);
        //System.out.println(text.split("සතිපතා නිකකුත් වන ගැසට් පත්‍රයෙහි පළකිරීම  සඳහා භාරගනු ලබන දැන්වීම් පිළිබඳ")[1]);
        
        return text.split("සතිපතා නිකකුත් වන ගැසට් පත්‍රයෙහි පළකිරීම  සඳහා භාරගනු ලබන දැන්වීම් පිළිබඳ")[1];
        
    }
}
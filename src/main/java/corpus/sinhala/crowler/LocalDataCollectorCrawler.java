package corpus.sinhala.crowler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import corpus.sinhala.crowler.parser.LankadeepaParser;

public class LocalDataCollectorCrawler extends WebCrawler {

	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
			+ "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	CrawlStat myCrawlStat;
	XMLFileWriter xfw;
	String base;

	public LocalDataCollectorCrawler() throws IOException {
		myCrawlStat = new CrawlStat();
		xfw = new XMLFileWriter();

		Properties prop = new Properties();
		InputStream input;
		base = "";
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			base = prop.getProperty("base");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean shouldVisit(WebURL url) {

		String href = url.getURL().toLowerCase();
		return !filters.matcher(href).matches() && href.startsWith(base);
	}

	@Override
	public void visit(Page page) {

		System.out.println("Visited: " + page.getWebURL().getURL());
		myCrawlStat.incProcessedPages();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData parseData = (HtmlParseData) page.getParseData();
			//new LankadeepaParser(parseData.getHtml(), page.getWebURL().getURL());
			// System.out.println((new Parser(page).getTitle()));
			try {

				xfw.addDocument(parseData.getHtml(), page.getWebURL().getURL());
			} catch (IOException | XMLStreamException e) {
				e.printStackTrace();
			}

			List<WebURL> links = parseData.getOutgoingUrls();
			myCrawlStat.incTotalLinks(links.size());
			try {
				myCrawlStat.incTotalTextSize(parseData.getText().getBytes(
						"UTF-8").length);
			} catch (UnsupportedEncodingException ignored) {
				// Do nothing
			}
		}
		// We dump this crawler statistics after processing every 50 pages
		if (myCrawlStat.getTotalProcessedPages() % 50 == 0) {
			dumpMyData();
		}
	}

	// This function is called by controller to get the local data of this
	// crawler when job is finished
	@Override
	public Object getMyLocalData() {
		return myCrawlStat;
	}

	// This function is called by controller before finishing the job.
	// You can put whatever stuff you need here.
	@Override
	public void onBeforeExit() {
		if (xfw.getDocumentCounter() > 0) {
			try {
				xfw.writeToFile();
			} catch (IOException | XMLStreamException e) {
				e.printStackTrace();
			}
		}
		dumpMyData();
	}

	public void dumpMyData() {
		int id = getMyId();
		// This is just an example. Therefore I print on screen. You may
		// probably want to write in a text file.
		System.out.println("Crawler " + id + "> Processed Pages: "
				+ myCrawlStat.getTotalProcessedPages());
		System.out.println("Crawler " + id + "> Total Links Found: "
				+ myCrawlStat.getTotalLinks());
		System.out.println("Crawler " + id + "> Total Text Size: "
				+ myCrawlStat.getTotalTextSize());
	}
}

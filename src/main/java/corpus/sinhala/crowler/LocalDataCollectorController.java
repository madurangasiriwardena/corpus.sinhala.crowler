package corpus.sinhala.crowler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.Properties;

public class LocalDataCollectorController {

	String numberOfCrawlersString;
	String crawlStorageFolder;
	String maxPagesToFetch;
	String politenessDelay;
	String maxDepthOfCrawling;
	String proxyHost;
	String proxyPort;
	String seed;

	public static void main(String[] args) throws Exception {
		LocalDataCollectorController controller = new LocalDataCollectorController();
		controller.setConfiguration();
		controller.startCrawler();
	}

	public void startCrawler() throws Exception {
		// if (args.length != 2) {
		// System.out.println("Needed parameters: ");
		// System.out.println("\t rootFolder (it will contain intermediate crawl data)");
		// System.out.println("\t numberOfCralwers (number of concurrent threads)");
		// return;
		// }
		// String rootFolder = args[0];
		// int numberOfCrawlers = Integer.parseInt(args[1]);
		int numberOfCrawlers;
		if(numberOfCrawlersString != null && !numberOfCrawlersString.equals("")){
			numberOfCrawlers = Integer.parseInt(numberOfCrawlersString);
		}else{
			numberOfCrawlers = 1;
		}

		CrawlConfig config = new CrawlConfig();
		
		if(crawlStorageFolder != null && !crawlStorageFolder.equals("")){
			config.setCrawlStorageFolder(crawlStorageFolder);
		}
		if(maxPagesToFetch != null && !maxPagesToFetch.equals("")){
			config.setMaxPagesToFetch(Integer.parseInt(maxPagesToFetch));
		}
		if(politenessDelay != null && !politenessDelay.equals("")){
			config.setPolitenessDelay(Integer.parseInt(politenessDelay));
		}
		if(maxDepthOfCrawling != null && !maxDepthOfCrawling.equals("")){
			config.setMaxDepthOfCrawling(Integer.parseInt(maxDepthOfCrawling));
		}
		if(proxyHost != null && !proxyHost.equals("")){
			config.setProxyHost(proxyHost);
		}
		if(proxyPort != null && !proxyPort.equals("")){
			config.setProxyPort(Integer.parseInt(proxyPort));
		}
//		config.setCrawlStorageFolder(rootFolder);
//		config.setMaxPagesToFetch(700);
//		config.setPolitenessDelay(1000);
//		config.setMaxDepthOfCrawling(10);
//		config.setProxyHost("cache.mrt.ac.lk");
//		config.setProxyPort(3128);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		controller.addSeed("http://www.lankadeepa.lk/index.php/");
		controller.start(LocalDataCollectorCrawler.class, numberOfCrawlers);

		List<Object> crawlersLocalData = controller.getCrawlersLocalData();
		long totalLinks = 0;
		long totalTextSize = 0;
		int totalProcessedPages = 0;
		for (Object localData : crawlersLocalData) {
			CrawlStat stat = (CrawlStat) localData;
			totalLinks += stat.getTotalLinks();
			totalTextSize += stat.getTotalTextSize();
			totalProcessedPages += stat.getTotalProcessedPages();
		}
		System.out.println("Aggregated Statistics:");
		System.out.println("   Processed Pages: " + totalProcessedPages);
		System.out.println("   Total Links found: " + totalLinks);
		System.out.println("   Total Text Size: " + totalTextSize);
	}

	public void setConfiguration() throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("config.properties");
		prop.load(input);

		numberOfCrawlersString = prop.getProperty("numberOfCrawlers");
		crawlStorageFolder = prop.getProperty("crawlStorageFolder");
		maxPagesToFetch = prop.getProperty("maxPagesToFetch");
		politenessDelay = prop.getProperty("politenessDelay");
		maxDepthOfCrawling = prop.getProperty("maxDepthOfCrawling");
		proxyHost = prop.getProperty("proxyHost");
		proxyPort = prop.getProperty("proxyPort");
		seed = prop.getProperty("seed");
	}

}

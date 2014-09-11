package corpus.sinhala.crawler;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import corpus.sinhala.crawler.infra.Controller;


public class Crawler {
	public static void main(String[] args) throws IOException, XMLStreamException {
		String generator = "corpus.sinhala.crawler.WijayaGenerator";
		String parser = "corpus.sinhala.crawler.WijayaParser";

		Controller controller = new Controller();
		controller.main(args, parser, generator);
	}
}

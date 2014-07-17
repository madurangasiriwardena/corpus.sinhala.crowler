package corpus.sinhala.crowler.url.generator;

import java.io.IOException;

import org.jsoup.nodes.Document;

public class Controller {
	public static void main(String args[]) throws IOException{
		DivainaGenerator dg = new DivainaGenerator(2010, 2010, 1, 1, 1, 5);

		Document doc;
		while((doc = dg.fetchPage()) != null){
			System.out.println(doc.baseUri());
		}
	}
}

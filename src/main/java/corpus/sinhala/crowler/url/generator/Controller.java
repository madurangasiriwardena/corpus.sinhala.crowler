package corpus.sinhala.crowler.url.generator;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.jsoup.nodes.Document;

import corpus.sinhala.crowler.XMLFileWriter;

public class Controller {
	public static void main(String args[]) throws IOException, XMLStreamException{
		DivainaGenerator dg = new DivainaGenerator(2010, 2010, 1, 1, 1, 2);
		XMLFileWriter xfw = new XMLFileWriter();

		Document doc;
		while((doc = dg.fetchPage()) != null){
			System.out.println(doc.baseUri());
			try{
				xfw.addDocument(doc.html(), doc.baseUri());
			}catch(Exception e){
				
			}
			
		}
		
		if (xfw.getDocumentCounter() > 0) {
			try {
				xfw.writeToFile();
			} catch (IOException | XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
}

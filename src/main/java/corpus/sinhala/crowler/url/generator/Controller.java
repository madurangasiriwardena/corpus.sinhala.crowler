package corpus.sinhala.crowler.url.generator;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.jsoup.nodes.Document;

import corpus.sinhala.crowler.XMLFileWriter;

public class Controller {
	public static void main(String args[]) throws IOException, XMLStreamException{
		String startDate;
		String endDate;
		String host;
		int port;
		String saveLocation;
		if(args.length == 5){
			startDate = args[0];
			endDate = args[1];
			host = args[2];
			port = Integer.parseInt(args[3]);
			saveLocation = args[4];
		}else{
			return;
		}
		
		String temp1[] = startDate.split("/");
		String temp2[] = endDate.split("/");
		int sYear = Integer.parseInt(temp1[0]);
		int eYear = Integer.parseInt(temp2[0]);
		int sMonth = Integer.parseInt(temp1[1]);
		int eMonth = Integer.parseInt(temp2[1]);
		int sDate = Integer.parseInt(temp1[2]);
		int eDate = Integer.parseInt(temp2[2]);
//		DivainaGenerator dg = new DivainaGenerator(2010, 2010, 1, 1, 1, 4, "127.0.0.1", 12345);
		DivainaGenerator dg = new DivainaGenerator(sYear, eYear, sMonth, eMonth, sDate, eDate, host, port);
		XMLFileWriter xfw = new XMLFileWriter(saveLocation);
		dg.addObserver(xfw);

		Document doc;
		while((doc = dg.fetchPage()) != null){
			System.out.println(doc.baseUri());
			try{
				xfw.addDocument(doc.html(), doc.baseUri());
			}catch(Exception e){
				
			}
			
		}

	}
}

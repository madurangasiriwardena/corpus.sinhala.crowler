package corpus.sinhala.crowler;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.StAXUtils;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import corpus.sinhala.crowler.parser.LankadeepaParser;
import corpus.sinhala.crowler.parser.Parser;

import edu.uci.ics.crawler4j.crawler.Page;

public class XMLFileWriter {
	int documentCounter = 0;
	String filePrefix = "L";
	int fileCounter = 0;
	String baseFolder = "data";
	String filePath = baseFolder+"/xml";
	BufferedWriter bw;
	FileWriter fw;
	int maxDocumentCounter = 100;
//	Queue<Parser> documentQueue = new LinkedList<Parser>();
	String path;
	
	
	OMFactory factory;
	OMElement root;
	QName rootName = new QName("root");
	QName linkName = new QName("link");
	QName topicName = new QName("topic");
	QName authorName = new QName("author");
	QName contentName = new QName("content");
	QName postName = new QName("post");
	QName dateName = new QName("date");
	QName yearName = new QName("year");
	QName monthName = new QName("month");
	QName dayName = new QName("day");
	
	public XMLFileWriter() throws IOException{
		createFile();
		factory = OMAbstractFactory.getOMFactory();
		root = factory.createOMElement(rootName);

	}
	
	public void addDocument(Page page) throws IOException, XMLStreamException{
//		documentQueue.add(new Parser(page));
		Parser parser = new LankadeepaParser(page);
		
		OMElement doc = factory.createOMElement(postName);
		
		OMElement date = factory.createOMElement(dateName);
		OMElement year = factory.createOMElement(yearName);
		year.setText(parser.getYear());
		date.addChild(year);
		
		OMElement month = factory.createOMElement(monthName);
		month.setText(parser.getMonth());
		date.addChild(month);
		
		OMElement day = factory.createOMElement(dayName);
		day.setText(parser.getDate());
		date.addChild(day);
		
		doc.addChild(date);
		
		OMElement link = factory.createOMElement(linkName);
		link.setText(parser.getUrl());
		doc.addChild(link);
		
		OMElement topic = factory.createOMElement(topicName);
		topic.setText(parser.getTitle());
		doc.addChild(topic);
		
		OMElement author = factory.createOMElement(authorName);
		author.setText(parser.getAuthor());
		doc.addChild(author);
		
		OMElement content = factory.createOMElement(contentName);
		content.setText(parser.getContent());
		doc.addChild(content);
		root.addChild(doc);		
//		System.out.println(root.toString());
		
		documentCounter++;
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+documentCounter);
		if(documentCounter%maxDocumentCounter==0){
			
			writeToFile();
		}
	}
	
	
	private void writeToFile() throws IOException, XMLStreamException{
//		bw.write(root.toString());
//		bw.close();
		
		OutputStream out = new FileOutputStream(path);
		XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(out);
		writer = new IndentingXMLStreamWriter(writer);
		root.serialize(writer);
		writer.flush();
		createFile();
	}
	
	private void createFile() throws IOException{
		File dirBase = new File(baseFolder);
		dirBase.mkdir();
		File dirXml = new File(filePath);
		dirXml.mkdir();
		
		
		path = filePath+"/"+filePrefix+String.format("%04d", fileCounter)+".xml";
		 
		File file = new File(path);

		if (!file.exists()) {
			file.createNewFile();
		}
		
		fileCounter++;
	}
	
//	public static void main(String[] args) throws IOException {
//		XMLFileWriter a = new XMLFileWriter();
//	}
}

package corpus.sinhala.crowler;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

import corpus.sinhala.crowler.parser.LankadeepaParser;

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
	
	public void addDocument(Page page) throws IOException{
//		documentQueue.add(new Parser(page));
		LankadeepaParser parser = new LankadeepaParser(page);
		
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
		System.out.println(root.toString());
		
		writeToFile();
	}
	
	
	private void writeToFile() throws IOException{
		bw.write(root.toString());
		bw.close();
	}
	
	private void createFile() throws IOException{
		File dirBase = new File(baseFolder);
		dirBase.mkdir();
		File dirXml = new File(filePath);
		dirXml.mkdir();
		
		
		String path = filePath+"/"+filePrefix+String.format("%03d", fileCounter)+".xml";
		 
		File file = new File(path);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
	}
	
//	public static void main(String[] args) throws IOException {
//		XMLFileWriter a = new XMLFileWriter();
//	}
}

package corpus.sinhala.crawler.subtitles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.StAXUtils;

import javanet.staxutils.IndentingXMLStreamWriter;

public class XMLFileWriter {
	private int documentCounter;
	private String filePrefix;
	private int fileCounter;
	private String baseFolder;
	private String filePath;
	// private BufferedWriter bw;
	// private FileWriter fw;
	private int maxDocumentCounter;
	private String path;

	private OMFactory factory;
	private OMElement root;
	private QName rootName;
	private QName linkName;
	private QName topicName;
	private QName contentName;
	private QName postName;
	
	
	ArrayList<OMElement> docs;
	
	Constructor<?> cons;
	
	public XMLFileWriter() throws IOException {
		init();
		createFolder();
		factory = OMAbstractFactory.getOMFactory();
		root = factory.createOMElement(rootName);


	}

	public XMLFileWriter(String location) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		
		
		String temp[] = location.split("/");
		String createDir = "";
		if(!location.startsWith("/")){
			createDir += temp[0];
			File dir = new File(createDir);
			dir.mkdir();
		}
		
		for(int i=1; i<temp.length; i++){
			createDir += "/"+temp[i];
			File dir = new File(createDir);
			dir.mkdir();
		}
		filePath = createDir;
		
		
		init();
		factory = OMAbstractFactory.getOMFactory();
		root = factory.createOMElement(rootName);
		
	}

	private void init() {
		documentCounter = 0;
		filePrefix = "L";
		fileCounter = 0;
		maxDocumentCounter = 100;
		rootName = new QName("root");
		linkName = new QName("link");
		topicName = new QName("topic");
		contentName = new QName("content");
		postName = new QName("post");
		
		docs = new ArrayList<>();
		
		fileCounter = new File(filePath).list().length;
		System.out.println(fileCounter);
		System.out.println(filePath);
		
	}

	public int getDocumentCounter() {
		 return documentCounter;
	}

	public void addDocument(String url, String title, String text) throws IOException,
			XMLStreamException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Parser parser = (Parser) cons.newInstance(page, url);
		OMElement doc = factory.createOMElement(postName);
		
		OMElement link = factory.createOMElement(linkName);
		link.setText(url);
		doc.addChild(link);

		OMElement topic = factory.createOMElement(topicName);
		topic.setText(title);
		doc.addChild(topic);

		
		OMElement content = factory.createOMElement(contentName);
		content.setText(text);
		doc.addChild(content);
		// root.addChild(doc);

		docs.add(doc);

	}

	public void writeToFile() throws IOException, XMLStreamException {
		fileCounter++;
		path = filePath + "/" + filePrefix + String.format("%04d", fileCounter)
				+ ".xml";
		OutputStream out = new FileOutputStream(path);
		XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(out);
		writer = new IndentingXMLStreamWriter(writer);
		root.serialize(writer);
		writer.flush();
	}
	
	public void writeToFile(String fileName) throws IOException, XMLStreamException {
		path = filePath + "/" + fileName + ".xml";
		OutputStream out = new FileOutputStream(path);
		XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(out);
		writer = new IndentingXMLStreamWriter(writer);
		root.serialize(writer);
		writer.flush();
		fileCounter++;
	}
	
	public void writeToFileTemp() throws IOException, XMLStreamException {
		path = filePath + "/" + filePrefix + String.format("%04d", fileCounter)
				+ ".xml";
		OutputStream out = new FileOutputStream(path);
		XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(out);
		writer = new IndentingXMLStreamWriter(writer);
		root.serialize(writer);
		writer.flush();
	}

	private void createFolder() throws IOException {
		File dirBase = new File(baseFolder);
		dirBase.mkdir();
		File dirXml = new File(filePath);
		dirXml.mkdir();

	}

	public void update() {
		//String message = (String)arg;
		
		for (int i = 0; i < docs.size(); i++) {

			root.addChild(docs.get(i));

			documentCounter++;
			System.out
					.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
							+ documentCounter);
//			if (documentCounter % maxDocumentCounter == 0) {
//				try {
//					writeToFile();
//				} catch (IOException | XMLStreamException e) {
//				}
//				documentCounter = 0;
//				root = factory.createOMElement(rootName);
//			}
		}
		
//		if(documentCounter>0){
//			try {
//				writeToFileTemp();
//			} catch (IOException | XMLStreamException e) {
//			}
//		}
		try {
			writeToFile();
		} catch (IOException | XMLStreamException e) {
		}
		root = factory.createOMElement(rootName);
		
		docs.clear();

	}

	 public static void main(String[] args) throws IOException {
//	 XMLFileWriter a = new XMLFileWriter("/home/maduranga/bb/");
	 }
}

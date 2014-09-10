package corpus.sinhala.crawler.infra;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jsoup.nodes.Document;

public class Controller1 {
	public void main(String args[], String parserClass, String GeneratorClass) throws IOException {
		String saveLocation = "/home/maduranga/data1";
		
		try {
			ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

			
			Class<?> myClass = myClassLoader.loadClass(GeneratorClass);
			Constructor<?> cons = myClass
					.getConstructor(new Class[] { Integer.class, Integer.class,
							Integer.class, Integer.class, Integer.class,
							Integer.class, String.class, Integer.class });

			
			Generator generator = (Generator) cons.newInstance(2014, 2014, 8, 8, 9, 18,
					"127.0.0.1", 12345);
			XMLFileWriter xfw = new XMLFileWriter(saveLocation, parserClass);
			generator.addObserver(xfw);

			Document doc;
			while((doc = generator.fetchPage()) != null){
				System.out.println(doc.baseUri());
				try{
					xfw.addDocument(doc.html(), doc.baseUri());
				}catch(Exception e){
					
				}
				
			}
			
		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

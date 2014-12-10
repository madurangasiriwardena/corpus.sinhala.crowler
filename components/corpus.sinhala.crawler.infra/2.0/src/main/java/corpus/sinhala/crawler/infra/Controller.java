package corpus.sinhala.crawler.infra;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.stream.XMLStreamException;


public class Controller {
	public void main(String args[], String parserClass, String GeneratorClass) throws IOException, XMLStreamException{
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
		
		try {
			ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

			
			Class<?> myClass = myClassLoader.loadClass(GeneratorClass);
			Constructor<?> cons = myClass
					.getConstructor(new Class[] { int.class, int.class,
							int.class, int.class, int.class,
							int.class, String.class, int.class });

			
			Generator generator = (Generator) cons.newInstance(sYear, eYear, sMonth, eMonth, sDate, eDate, host, port);
			XMLFileWriter xfw = new XMLFileWriter(saveLocation, parserClass);
			generator.addObserver(xfw);

			DTO dto;
			while((dto = generator.fetchPage()) != null){
				System.out.println(dto.url);
				try{
					xfw.addDocument(dto);
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

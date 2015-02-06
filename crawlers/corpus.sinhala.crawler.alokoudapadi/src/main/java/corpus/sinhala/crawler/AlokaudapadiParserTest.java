package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class AlokaudapadiParserTest extends TestCase {

	private static AlokaudapadiParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.alokoudapadi/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new AlokaudapadiParser(content, "http://www.lakehouse.lk/alokoudapadi/2014/10/01/_art.asp?fn=a1410102");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("විෂ්ණු කතරගම ආදී දෙවිවරුන්ට පුදපූජා පැවැත්වීමෙන් ඔවුන්ගේ පිහිට සෙවීමෙන් සරණ ශීලය බිඳී බෞද්ධත්වය නැති වේ ද?", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("රේරුකානේ චන්දවිමල හිමි", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.alokoudapadi/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.lakehouse.lk/alokoudapadi/2014/10/01/_art.asp?fn=a1410102", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2014", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("10", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("01", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("ACADEMIC", parser.getCategory());
	}

}

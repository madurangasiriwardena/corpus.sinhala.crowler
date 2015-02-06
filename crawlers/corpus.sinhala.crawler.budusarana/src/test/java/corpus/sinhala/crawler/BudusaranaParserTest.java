package corpus.sinhala.crawler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class BudusaranaParserTest extends TestCase {

	private static BudusaranaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.budusarana/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new BudusaranaParser(content, "http://www.lakehouse.lk/budusarana/2014/10/15/tmp.asp?ID=vision01");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("ජීවිතාලෝකය", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("ශාස්ත්‍රපති වැලිපත පඤ්ඤානන්ද හිමි", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.budusarana/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.lakehouse.lk/budusarana/2014/10/15/tmp.asp?ID=vision01", parser.getUrl());
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
    	assertEquals("15", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("ACADEMIC", parser.getCategory());
	}

}

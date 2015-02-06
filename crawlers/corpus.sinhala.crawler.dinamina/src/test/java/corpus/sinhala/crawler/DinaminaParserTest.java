package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class DinaminaParserTest extends TestCase {

	private static DinaminaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.dinamina/src/test/resources/web/dinamina/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new DinaminaParser(content, "http://www.dinamina.lk/2011/05/26/_art.asp?fn=n1105261");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("වෘත්තීයමය ගැටලු  සාකච්ඡා මඟින් විසඳාගන්න ආචාර්යවරු එකඟ වෙති", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("ධනුෂ්ක ගොඩකුඹුර සහ සමන්මලී පි‍්‍රයශාන්ති", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.dinamina/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.dinamina.lk/2011/05/26/_art.asp?fn=n1105261", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2011", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("05", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("26", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("NEWS", parser.getCategory());
	}

}

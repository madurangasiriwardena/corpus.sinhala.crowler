package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class SiluminaParserTest extends TestCase {

	private static SiluminaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.silumina/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new SiluminaParser(content, "http://www.silumina.lk/2014/03/09/_art.asp?fn=aa1403091");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("සිළුමිණ: ප්‍රවෘත්ති - අන්තර්ජාතික පරීක්ෂණ ශ්‍රී ලංකාවට අවශ්‍ය නැහැ", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.silumina/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.silumina.lk/2014/03/09/_art.asp?fn=aa1403091", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2014", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("03", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("09", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("NEWS", parser.getCategory());
	}

}

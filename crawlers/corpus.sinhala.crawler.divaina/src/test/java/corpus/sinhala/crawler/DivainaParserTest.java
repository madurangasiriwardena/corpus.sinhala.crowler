package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class DivainaParserTest extends TestCase {

	private static DivainaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.divaina/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new DivainaParser(content, "http://www.divaina.com/2011/11/15/news01.html");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("රටපුරා රජයේ රෝහල්වල පරිපූරක වෛද්‍ය වෘත්තික 5000 ක්‌ හෙට ලෙඩ වෙති", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("ඒමන්ති මාරඹේ", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.divaina/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.divaina.com/2011/11/15/news01.html", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2011", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("11", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("15", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("NEWS", parser.getCategory());
	}

}

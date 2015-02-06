package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class VidusaraParserTest extends TestCase {

	private static VidusaraParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.vidusara/src/test/resources/web/news4.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new VidusaraParser(content, "http://www.vidusara.com/2011/04/13/news4.html");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws Exception {

		assertEquals("ආසියා ශාන්තිකර කලාපීය පරිගණක හදිසි ප්‍රහාර වැළැක්‌වීමේ පුහුණුව ඇරඹේ", parser.getTitle());
	}

    @Test
	public void testGetAuthor() throws Exception {
    	assertEquals("අමල් උඩවත්ත", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws Exception {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.vidusara/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() throws Exception {
    	assertEquals("http://www.vidusara.com/2011/04/13/news4.html", parser.getUrl());
	}

    @Test
	public void testGetYear() throws Exception {
    	assertEquals("2011", parser.getYear());
	}

    @Test
	public void testGetMonth() throws Exception {
    	assertEquals("04", parser.getMonth());
	}

    @Test
	public void testGetDate() throws Exception {
    	assertEquals("13", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("ACADEMIC", parser.getCategory());
	}

}

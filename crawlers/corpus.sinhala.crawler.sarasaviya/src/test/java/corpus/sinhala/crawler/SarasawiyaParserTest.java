package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class SarasawiyaParserTest extends TestCase {

	private static SarasawiyaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.sarasaviya/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new SarasawiyaParser(content, "http://sarasaviya.lk/2014/10/30/?fn=sa1410301");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("කැමැත්තෙන් කරන දෙයක් එපා වෙන්නේ නෑනේ...", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("සටහන – ප්‍රසාද් සමරතුංග", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.sarasaviya/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://sarasaviya.lk/2014/10/30/?fn=sa1410301", parser.getUrl());
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
    	assertEquals("30", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("CREATIVE", parser.getCategory());
	}

}

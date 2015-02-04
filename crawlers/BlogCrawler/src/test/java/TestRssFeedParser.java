/**
 * Created by dimuthuupeksha on 1/18/15.
 */

import corpus.sinhala.crawler.blog.rss.RSSFeedParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRssFeedParser {


    private JettyServer server;
    @Before
    public void before() throws Exception {
        server = new JettyServer();
        server.start();
        System.out.println("Server started");
    }

    @After
    public void after() throws Exception {
        server.stop();
    }

    @Test
    public void testRssFeedParserNotNull(){
        RSSFeedParser parser = new RSSFeedParser("http://localhost:9880/rss/sample1.txt");
        assertNotNull(parser);
        //parser.
    }
}

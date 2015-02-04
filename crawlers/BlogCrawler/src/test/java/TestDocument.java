/**
 * Created by dimuthuupeksha on 1/15/15.
 */
import corpus.sinhala.crawler.blog.controller.HathmaluwaParser;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;


public class TestDocument {
    private JettyServer server;
    @Before
    public void before() throws Exception {
        server = new JettyServer();
        server.start();
        System.out.println("Server started");
    }

    @Test
    public void testDocument() throws IOException {
        HathmaluwaParser parser = new HathmaluwaParser();
        Document doc = parser.getDoc("http://localhost:9880/html/hathmaluwa/sample1.html");
        assertNotNull(doc);
    }

    @After
    public void after() throws Exception {
        server.stop();
    }




}

/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package corpus.sinhala.crawler.blog.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import corpus.sinhala.crawler.blog.ConfigManager;
import corpus.sinhala.crawler.blog.rss.beans.Feed;
import corpus.sinhala.crawler.blog.rss.beans.FeedMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RSSFeedParser {

    final static Logger logger = Logger.getLogger(RSSFeedParser.class);

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CHANNEL = "channel";
    private static final String LANGUAGE = "language";
    private static final String COPYRIGHT = "copyright";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String ITEM = "item";
    private static final String PUB_DATE = "pubDate";
    private static final String GUID = "guid";
    private static final String ATOMID = "atom:id";
    private final URL url;
    private String link;

    public RSSFeedParser(String feedUrl) {
        link = feedUrl;
        logger.info("Feed URL : " + feedUrl);
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBlogId() {
        Feed feed = null;
        String atomId = "";
        try {
            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // Read the XML document
            int location = -1;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    //System.out.println(localPart);
                    switch (localPart) {
                        
                        case CHANNEL:
                            event = eventReader.nextEvent();
                            atomId = getCharacterData(event, eventReader);
                            //System.out.println("Atom Id "+atomId );
                            return atomId.split("-")[1];
                    }
                }
            }
        } catch (XMLStreamException e) {

            throw new RuntimeException(e);
        }

        return null;
    }
    
    
    public Feed getFeed(String blogId){  //test here
        List<FeedMessage> feeds = new ArrayList<>();
        try{
            DefaultHttpClient client = new DefaultHttpClient();
            String apiKey = ConfigManager.getProperty(ConfigManager.API_KEY);
            HttpGet get = new HttpGet("https://www.googleapis.com/blogger/v3/blogs/"+blogId+"/posts?key="+apiKey);
            get.setHeader("Accept", "*/*");
            HttpResponse response = client.execute(get);
            String json = EntityUtils.toString(response.getEntity());

            JSONObject obj = new JSONObject(json);
            JSONArray mainContent = obj.getJSONArray("items");
            for(int i=0;i<mainContent.length();i++){
                JSONObject blogPost = mainContent.getJSONObject(i);
                String link = this.link; 
                String rawTitle = blogPost.getString("title");
                String rawDes =blogPost.getString("content");
                String author=blogPost.getJSONObject("author").getString("displayName");
                String date =blogPost.getString("published");
                String id = blogPost.getString("id");
                String description=filter(rawDes);
                String title=filter(rawTitle);
                FeedMessage message = new FeedMessage();
                message.setAuthor(author);
                message.setDate(date);
                message.setDescription(description);
                message.setLink(link);
                message.setRawDes(rawDes);
                message.setRawTitle(rawTitle);
                message.setTitle(title);
                message.setId(id);
                feeds.add(message);
            }
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Feed feed= new Feed(null, null, null, null, null, null);
        feed.setEntries(feeds);
        return feed;
    }
    
    private String[] restricted = {"a", "br", "img", "div", "/", "<", ">", "nbsp", "&", "span"};

    private String filter(String s) {
        boolean open =false;
        int start =0;
        int end=0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='<'){
                start=i;
            }else if(s.charAt(i)=='>'){
                end=i;
                if(start<end){
                    
                    s=s.substring(0, start)+s.substring(end+1, s.length());
                    i=0;
                }
            }
            
            
        }
        s=s.replace("&nbsp;","");
        s=s.replace("\n", "");
        return s.trim();
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

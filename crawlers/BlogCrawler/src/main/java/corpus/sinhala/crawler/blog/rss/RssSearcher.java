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

import java.io.*;
import java.util.*;

import corpus.sinhala.crawler.blog.ConfigManager;
import corpus.sinhala.crawler.blog.controller.CacheManager;
import corpus.sinhala.crawler.blog.controller.XMLFileWriter;
import corpus.sinhala.crawler.blog.rss.beans.Feed;
import corpus.sinhala.crawler.blog.rss.beans.FeedMessage;
import corpus.sinhala.crawler.blog.rss.beans.Post;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RssSearcher extends Thread {
    final static Logger logger = Logger.getLogger(RssSearcher.class);

    private String url;
    private HashSet<String> ignoringChars;
    private final double ACCEPTANCE_RATIO;

    public RssSearcher(String url) {
        ACCEPTANCE_RATIO = 0.5;
        ignoringChars = new HashSet<String>();

        // check http://unicode-table.com/en/#control-character
        ignoringChars.add("\u0020"); // space
        ignoringChars.add("\u002C"); // ,

        ignoringChars.add("\u007b"); // {
        ignoringChars.add("\u007c"); // |
        ignoringChars.add("\u007d"); // }
        ignoringChars.add("\u007e"); // ~
        this.url = url;
        RssWebDriver driver = RssWebDriver.getInstance();
        driver.increase();

    }

    @Override
    public void run() {
        RssWebDriver driver = RssWebDriver.getInstance();
        try {
            while (true) {

                if (!driver.acquired()) {
                    if (driver.acquire()) {
                        break;
                    }
                }
                Thread.sleep(10);

            }
            driver.get(ConfigManager.getProperty(ConfigManager.FEED_BURNER_URL)); //test here
            WebElement element = driver.findElement(By.name("sourceUrl"));
            element.sendKeys(url);
            element.submit();

            List<WebElement> elements = driver.findElements(By.className("checkboxLabel")); //test here
            for (int i = 0; i < elements.size(); i++) {

                try {
                    if (elements.get(i).getText().contains("RSS")) {
                        String[] s = elements.get(i).getText().split(" ");
                        //System.out.println(s[s.length - 1]);
                        RSSFeedParser parser = new RSSFeedParser(s[s.length - 1]);
                        String blogId = parser.getBlogId();
                        //System.out.println(blogId);
                        Feed feed = parser.getFeed(blogId);  //test here
                        for (FeedMessage message : feed.getMessages()) {


                            boolean contains = false;
                            String postId = message.getId();
                            if (CacheManager.getInstance().postCache.containsKey(blogId)) {
                                if (CacheManager.getInstance().postCache.get(blogId).contains(postId)) {
                                    contains = true;
                                    //System.out.println("Contain post id " +postId);
                                } else {
                                    CacheManager.getInstance().postCache.get(blogId).add(postId);
                                    CacheManager.getInstance().serializeCache();
                                    //System.out.println("Not Contain post id " +postId);
                                }
                            } else {
                                Set<String> postIds = new HashSet<>();
                                postIds.add(postId.trim());
                                CacheManager.getInstance().postCache.put(blogId, postIds);
                                CacheManager.getInstance().serializeCache();

                            }
                            if (!contains) {
                                BufferedWriter writer = null;

                                Post post = new Post();
                                post.setLink(this.url);
                                post.setTopic(message.getTitle());
                                post.setCategory("BLOG");
                                post.setAuthor(message.getAuthor());
                                post.setContent(getAcceptedSentences(message.getDescription()));

                                String date = message.getDate();
                                date = date.split("T")[0];
                                String parts[] = date.split("-");
                                post.setYear((parts[0]));
                                post.setMonth((parts[1]));
                                post.setDay((parts[2]));

                                XMLFileWriter.getInstance().addPost(post);


                            }

                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            Thread.sleep(1000);

        } catch (Exception ex) {

            logger.error(ex);
        }

        driver.release();
        driver.decrease();
    }

    public String getAcceptedSentences(String doc) {
        String sentences[] = doc.split("[\u002E\u003F\u0021]");
        String acceptedSentences = "";
        String rejectedSentences = "";
        for (String sentence : sentences) {
            double ratio = checkString(sentence);
            //System.out.println("ratio of sentence: " + ratio);
            if (ratio >= ACCEPTANCE_RATIO) {
                acceptedSentences += sentence + ".";
            } else {
                rejectedSentences += sentence + ".";
            }
        }

        return acceptedSentences;
    }

    public double checkString(String str) {
        // sinhala unicode range is 0D80–0DFF. (from http://ucsc.cmb.ac.lk/ltrl/publications/uni_sin.pdf )
        int sinhalaLowerBound = 3456;
        int sinhalaUpperBound = 3583;
        int sinhalaCharCount = 0;
        int nonSinhalaCharCount = 0;

        for (int i = 0; i < str.length(); i++) {
            int cp = str.codePointAt(i);
            if (isIgnoringChar(str.charAt(i) + "")) {
                // ignoring chars
                //System.out.println("ignoring char: " + str.charAt(i));
                continue;
            } else if ((cp >= 0 && cp <= 31)) {
                // commands
                continue;
            } else if (cp >= 48 && cp <= 57) {
                // numbers (0 - 9)
                sinhalaCharCount++;
            } else if (cp >= 33 && cp <= 64) {
                // other symbols - do this check after checking for numbers
                continue;
            } else if (cp >= sinhalaLowerBound && cp <= sinhalaUpperBound) {
                // sinhala
                //if(debug) System.out.println("sinhala character: " + str.charAt(i));
                sinhalaCharCount++;
            } else {
                // other
                //if(debug) System.out.println("non sinhala character: " + str.charAt(i));
                nonSinhalaCharCount++;
            }
        }
        if (nonSinhalaCharCount == 0) {
            return 1;
        }

        return (1.0 * sinhalaCharCount / nonSinhalaCharCount);
    }

    boolean isIgnoringChar(String character) {
        if (ignoringChars.contains(character)) {
            return true;
        }
        return false;
    }


    private String[] splitToSentences(String article) {
        return article.split("[\u002E]");
    }
}

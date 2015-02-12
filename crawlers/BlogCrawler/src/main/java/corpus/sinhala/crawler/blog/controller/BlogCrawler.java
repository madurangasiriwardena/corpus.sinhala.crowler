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
package corpus.sinhala.crawler.blog.controller;


import java.io.BufferedWriter;
import java.io.FileWriter;

import corpus.sinhala.crawler.blog.ConfigManager;
import corpus.sinhala.crawler.blog.rss.RssWebDriver;
import org.apache.log4j.Logger;

public class BlogCrawler {

    final static Logger logger = Logger.getLogger(BlogCrawler.class);

    public void generateUrl(){
        HathmaluwaParser parser = new HathmaluwaParser();
        for (int i = 2; i < 328; i++) {
            try{
                if (RssWebDriver.getInstance().getThreads() > 10) {
                    i--;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {

                    }
                    continue;
                }
                String pageURL = ConfigManager.getProperty(ConfigManager.AGGREGATOR_URL) + i;

                logger.info("**********Crawling**********" + pageURL);
                parser.parse(pageURL);

                BufferedWriter writer = new BufferedWriter(new FileWriter("./metadata.xml", true));
                writer.write(pageURL + "\n");
                writer.flush();
                writer.close();


            }catch(Exception ex){
                logger.error(ex);
            }
        }
    }
    
    
    
    
    public static void main(String[] args) {
        BlogCrawler crawler = new BlogCrawler();
        crawler.generateUrl();
    }
}

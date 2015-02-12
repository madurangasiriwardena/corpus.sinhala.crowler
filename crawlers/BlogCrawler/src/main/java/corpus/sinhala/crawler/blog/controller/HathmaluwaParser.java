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

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import corpus.sinhala.crawler.blog.rss.RssSearcher;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HathmaluwaParser {

    final static Logger logger = Logger.getLogger(HathmaluwaParser.class);

    private HashSet<String> urls = new HashSet<>();

    public void parse(String path){
        try {
            Document doc = getDoc(path);
            Elements divs = doc.getElementsByTag("div");
            Iterator<Element> it = divs.iterator();
            
            while(it.hasNext()){
                Element div = it.next();
                if(div.attr("class").trim().equals("post-wrapper row-fluid")){
                    Element a = div.getElementsByTag("a").first();
                    String url =(a.attr("href"));
                    if(!urls.contains(url)){
                        (new RssSearcher(url)).start();
                        urls.add(url);
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    public Document getDoc(String path) throws IOException {
        Document doc = Jsoup.connect(path).get();
        return doc;
    }
}

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

import corpus.sinhala.crawler.blog.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class RssWebDriver extends HtmlUnitDriver {

    private static RssWebDriver instance = null;
    private int threads =0;
    
    public synchronized void increase(){
        threads++;
    }
    
    public synchronized void decrease(){
        threads--;
    }

    public int getThreads() {
        return threads;
    }
    
    
    
    private RssWebDriver() {
    }
    
    private boolean acquire =false;
    public synchronized boolean acquired(){
        return acquire;
    }
    
    public synchronized boolean acquire(){
        if(acquire){
            return false;
        }else{
            acquire =true;
            return true;
        }
    }
    
    public void release(){
        acquire =false;
    }

    public static RssWebDriver getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new RssWebDriver();

            instance.get(ConfigManager.getProperty(ConfigManager.FEED_BURNER_URL));
            WebElement element = instance.findElement(By.name("Email"));
            element.sendKeys(ConfigManager.getProperty(ConfigManager.GOOGLE_ID));
            element = instance.findElement(By.name("Passwd"));
            element.sendKeys(ConfigManager.getProperty(ConfigManager.GOOGLE_PASSWORD));
            element.submit();
            return instance;
        }
    }
}

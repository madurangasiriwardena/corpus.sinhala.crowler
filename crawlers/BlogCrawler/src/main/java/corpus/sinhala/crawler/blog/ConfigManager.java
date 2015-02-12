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

package corpus.sinhala.crawler.blog;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    final static Logger logger = Logger.getLogger(ConfigManager.class);

    public static final String FEED_BURNER_URL="FEED_BURNER_URL";
    public static final String GOOGLE_ID="GOOGLE_ID";
    public static final String GOOGLE_PASSWORD="GOOGLE_PASSWORD";
    public static final String API_KEY="API_KEY";
    public static final String AGGREGATOR_URL="AGGREGATOR_URL";

    public static String getProperty(String propertyName){ //reads properties from config file

        InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }
}

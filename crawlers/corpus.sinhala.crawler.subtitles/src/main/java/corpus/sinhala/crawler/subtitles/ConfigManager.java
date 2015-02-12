package corpus.sinhala.crawler.subtitles;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    public static final String MYSQL_DB_DRIVER = "MYSQL_DB_DRIVER";
    public static final String MYSQL_DB_CONNECTION = "MYSQL_DB_CONNECTION";
    public static final String MYSQL_DB_USER = "MYSQL_DB_USER";
    public static final String MYSQL_DB_PASSWORD = "MYSQL_DB_PASSWORD";
    public static final String XML_SAVE_LOCATION = "XML_SAVE_LOCATION";
    public static final String ZIP_SAVE_LOCATION="ZIP_SAVE_LOCATION";
    public static final String BAISCOPE_LIST_PAGE="BAISCOPE_LIST_PAGE";

    final static Logger logger = Logger.getLogger(ConfigManager.class);

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

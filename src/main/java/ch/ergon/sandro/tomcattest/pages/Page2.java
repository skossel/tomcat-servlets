package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Page2 implements Page {

    @Override
    public Map<String, String> getPageValues() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("values.properties")) {
            if (input == null) {
                return Collections.emptyMap();
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyMap();
        }

        Map<String, String> values = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            values.put(key, properties.getProperty(key));
        }
        return values;
    }
}

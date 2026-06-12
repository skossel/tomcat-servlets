package ch.ergon.sandro.tomcattest;

import java.util.Map;

public interface Page {

    Map<String, String> getPageValues() throws ClassNotFoundException;
}

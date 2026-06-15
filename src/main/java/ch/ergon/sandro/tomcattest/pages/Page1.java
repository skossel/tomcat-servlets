package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Page;

import java.util.Map;

public class Page1 implements Page {

    @Override
    public Map<String, String> getPageValues() {
        return Map.of(
                "LLB", "Bank LLB AG",
                "YVES", "Yves",
                "SANDRO", "Sandro",
                "apfel", "Apfel",
                "birne", "Birne"
        );
    }
}

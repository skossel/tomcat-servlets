package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Path;

@Path("/path")
public class PageClass {

    @Path("/stockprices")
    public String getFooBar() {
        return "stockprices.dso";
    }

    @Path("/page1")
    public String getPage1() {
        return "page1.dso";
    }

}

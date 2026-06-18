package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Path;

@Path("/sandro")
public class Sandro {

    @Path("/sandro1")
    public String getSandro1() {
        return "sandro1.dso";
    }

    @Path("/sandro2")
    public String getSandro2() {
        return "sandro2.dso";
    }
}

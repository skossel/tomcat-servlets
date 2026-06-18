package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Path;

@Path("/yves")
public class Yves {

    @Path("/yves1")
    public String getYves1() {
        return "yves1.dso";
    }

    @Path("/yves2")
    public String getYves2() {
        return "yves2.dso";
    }
}

package ch.ergon.sandro.tomcattest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet(name = "myWebApp", urlPatterns = {"/*"})
public class MyWebApp extends HttpServlet {

    private static final String FOO_BAR = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("Servlet MyWebApp was accessed from: " + request.getServletPath() + " with path info: " + pathInfo);

        String fileName = getFileName(pathInfo);

        String content = readFileContent(fileName);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(content);
    }

    private String getFileName(String pathInfo) {
        if (pathInfo != null && pathInfo.startsWith("/yves/")) {
            return pathInfo.substring(1) + ".yve";
        }
        String name = pathInfo.substring(1);
/*
        String reversed = new StringBuilder(name).reverse().toString();
*/
        name += ".dso";
        return name;
    }

    private String readFileContent(String fileName) throws IOException {
        String fileContent = loadFile("/" + fileName);

        Properties props = (Properties) getServletContext().getAttribute("contextProperties");
        
        String content = fileContent.replace("__FOO_BAR__", FOO_BAR);
        return replacePlaceholders(content, props);
    }

    private String loadFile(String path) throws IOException {
        try (InputStream inputStream = getServletContext().getResourceAsStream(path)) {
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        }
    }


    private String replacePlaceholders(String content, Properties props) {
        return Pattern.compile("\\$\\{([^}]+)\\}")
                .matcher(content)
                .replaceAll(match -> {
                    String val = props.getProperty(match.group(1));
                    if (val == null) {
                        return "property not found";
                    }
                    return val.replace("\"", "");
                });
    }

    private Page createPage(String className) {

        var pageClass = Class.forName(className);

        Page page = pageClass.getDeclaredConstructor().newInstance();

        page.getPageValues()
    }
}

package ch.ergon.sandro.tomcattest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet(name = "myWebApp", urlPatterns = {"/*"})
public class MyWebApp extends HttpServlet {

    private static final String FOO_BAR = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        String pageName = pathInfo.substring(1).split("/")[0];
        Page page = createPage(pageName);
        if (page == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page class not found: " + pageName);
            return;
        }

        String dsoPath = "/ch/ergon/sandro/tomcattest/pages/" + pageName + ".dso";
        String content = loadFile(dsoPath);
        Map<String, String> values = page.getPageValues();
        String finalContent = replacePlaceholders(content, values);
        if (finalContent == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page content not found: " + dsoPath);
            return;
        }
        finalContent = finalContent.replace("__FOO_BAR__", FOO_BAR);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(finalContent);
    }

    private String loadFile(String path) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                return null;
            }
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        }
    }

    private String replacePlaceholders(String content, Map<String, String> values) {
        if (content == null) {
            return null;
        }
        return Pattern.compile("\\$\\{([^}]+)\\}")
                .matcher(content)
                .replaceAll(match -> {
                    String key = match.group(1);
                    String val = values.get(key);
                    if (val == null) {
                        return "property not found";
                    }
                    return val;
                });
    }

    private Page createPage(String pageName) {
        try {
            String className = pageName.substring(0, 1).toUpperCase() + pageName.substring(1);
            String fullClassName = "ch.ergon.sandro.tomcattest.pages." + className;
            return (Page) Class
                    .forName(fullClassName)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

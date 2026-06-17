package ch.ergon.sandro.tomcattest;

import ch.ergon.sandro.tomcattest.pages.PageClass;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.stream.Collectors;

@WebServlet(name = "myWebApp", urlPatterns = {"/*"})
public class MyWebApp extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            pathInfo = "/";
        }

        try {
            if (handleRouting(pathInfo, response)) return;
        } catch (Exception e) {
            response.sendError(500, "Error: " + e.getMessage());
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No route found for " + pathInfo);
    }

    private boolean handleRouting(String path, HttpServletResponse response) throws Exception {
        if (path == null || path.length() < 2) return false;

        var pageClass = PageClass.class;
        for (var method : pageClass.getDeclaredMethods()) {
            var pathAttr = method.getAnnotation(Path.class);
            if (pathAttr != null && pathAttr.value().equals(path)) {
                var dsoFileName = (String) method.invoke(pageClass.getDeclaredConstructor().newInstance());
                var html = loadFile("/ch/ergon/sandro/tomcattest/pages/" + dsoFileName);
                if (html == null) {
                    response.sendError(404, "file not found: " + dsoFileName);
                    return true;
                }
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(html);
                return true;
            }
        }
        return false;
    }

    private String loadFile(String path) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                return null;
            }
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        }
    }

}

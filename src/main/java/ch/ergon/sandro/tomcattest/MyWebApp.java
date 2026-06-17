package ch.ergon.sandro.tomcattest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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

        for (Class<?> clazz : getClasses("ch.ergon.sandro.tomcattest.pages")) {
            for (var method : clazz.getDeclaredMethods()) {
                var pathAttr = method.getAnnotation(Path.class);
                if (pathAttr != null && pathAttr.value().equals(path)) {
                    var dsoFileName = (String) method.invoke(clazz.getDeclaredConstructor().newInstance());
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
        }
        return false;
    }

    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> files = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            files.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File file : files) {
            classes.addAll(findClasses(file, packageName));
        }
        return classes.toArray(new Class[0]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) return classes;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
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

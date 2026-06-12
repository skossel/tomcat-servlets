package ch.ergon.sandro.tomcattest;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class Context implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        Properties props = new Properties();
        try (InputStream inputStream = context.getResourceAsStream("/values.properties")) {
            if (inputStream != null) {
                props.load(inputStream);
                context.setAttribute("contextProperties", props);
                System.out.println("properties successfully loaded into ServletContext.");
            } else {
                System.err.println("values.properties not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext destroyed. Sandro");
    }
}

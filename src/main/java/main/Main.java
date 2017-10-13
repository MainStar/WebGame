package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.LoginServlet;
import servlets.RegistrationServlet;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        LoginServlet loginServlet = new LoginServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);

        context.addServlet(new ServletHolder(loginServlet), "/");
        context.addServlet(new ServletHolder(registrationServlet), "/reg");

        server.start();

        Server2 server2 = new Server2();
        server2.server2();

        server.join();
    }
}

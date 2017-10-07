package servlets;

import database.Constructor;
import database.DbManager;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    DbManager db = new DbManager();
    String login, password, passwordRepeat;

    public static Connection connection;
    public static PreparedStatement prepSt;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("username", "");

        response.getWriter().println(PageGenerator.instance().getPage("registration.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        String username = request.getParameter("username");
        response.setContentType("text/html;charset=utf-8");

        if (username.equals(null) || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        pageVariables.put("name", username.equals(null) ? "" : username);

        /***Считываем введенные данные***/
        login = request.getParameter("username");
        password = request.getParameter("password");
        passwordRepeat = request.getParameter("reepeat");

        /***Подключение к БД***/
        try {
            connection = db.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /***Заполнение таблицы***/
        if (password.equals(passwordRepeat)) {
            try {
                prepSt = connection.prepareStatement("INSERT INTO users (id, Name, Pass) VALUES (?, ?, ?)");
                prepSt.setString(1, login);
                prepSt.setString(2, password);
                prepSt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.getWriter().println(PageGenerator.instance().getPage("postregistration.html", pageVariables));
        } else {
            response.getWriter().println(PageGenerator.instance().getPage("notregistration.html", pageVariables));
        }
    }
}

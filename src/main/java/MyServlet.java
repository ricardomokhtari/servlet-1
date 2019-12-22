import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/"},loadOnStartup = 1)
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dbURL = "jdbc:postgresql://ec2-54-195-252-243.eu-west-1.compute.amazonaws.com:5432/din8m6nuaj2gb?ssl" +
               "=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {

        }
        resp.setContentType("application/json");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement s = conn.createStatement();
            String sqlStr = "select * from users;";
            ResultSet rset = s.executeQuery(sqlStr);
            while(rset.next()){
                Patient p = new Patient(rset.getString("id"),rset.getString("Latest severity score"),rset.getString("DOB"),rset.getString("Sex"),rset.getString("Last updated"));
                Gson gson = new Gson();
                String jsonString = gson.toJson(p);
                resp.getWriter().write(jsonString + "\n");
            }
            rset.close();
            s.close();
            conn.close();
        } catch (Exception e) {

        }
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        resp.setContentType("text/html");
        resp.getWriter().write("POST Received");
        System.out.println(reqBody);
    }
}

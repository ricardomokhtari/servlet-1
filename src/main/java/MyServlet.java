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

import org.json.*;

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
        resp.setHeader("Access-Control-Allow-Origin","*");
        resp.setContentType("application/json");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, "hdzsfwizwsoxxz", "b62525ba2575aa2a67faab4ab24f80234d850305522338e2bb16dd02f474db8e");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String path = req.getServletPath();

        switch(path){
            case "/":
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
                break;
            default:
                break;
        }

    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();

        String dbURL = "jdbc:postgresql://ec2-54-195-252-243.eu-west-1.compute.amazonaws.com:5432/din8m6nuaj2gb?ssl" +
                "=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {

        }
        resp.setContentType("application/json");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, "hdzsfwizwsoxxz", "b62525ba2575aa2a67faab4ab24f80234d850305522338e2bb16dd02f474db8e");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch(path){
            case "/newpatient":
                String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject obj = new JSONObject(reqBody);
                String firstname = obj.getString("firstname");
                String middlename = obj.getString("middlename");
                String surname = obj.getString("surname");
                String sex = obj.getString("sex");
                String ethnicity = obj.getString("ethnicity");
                String dob = obj.getString("dob");

                String sqlCmd = "(\'"+firstname+"\',\'"+middlename+"\',\'"+surname+"\',\'"+sex+"\',\'"+ethnicity+"\',\'"+dob+"\')";
                System.out.println(sqlCmd);

                try {
                    Statement s = conn.createStatement();
                    String sqlStr = "INSERT INTO patient (firstname, middlename, surname, sex, ethnicity, dob) values "+sqlCmd;
                    s.execute(sqlStr);
                    s.close();
                    conn.close();
                } catch (Exception e) {
                    System.out.println("FAILED");
                }
                break;
            case "/viewpage":
                try {
                    String reqBody2=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    Statement s = conn.createStatement();
                    String sqlStr = "select * from " + reqBody2 + ";";
                    System.out.println(sqlStr);
                    ResultSet rset = s.executeQuery(sqlStr);
                    resp.setHeader("Access-Control-Allow-Origin","*");
                    resp.setContentType("application/json");
                    while (rset.next()) {
                        Record r = new Record(rset.getString("id"), rset.getString("date"), rset.getString("imageage"), rset.getString("erythemascore"), rset.getString("edemascore"),rset.getString("exclorationscore"),rset.getString("lichenificationscore"),rset.getString("areascore"),rset.getString("totalscore"),rset.getString("comments"));
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(r);
                        resp.getWriter().write(jsonString + "\n");
                    }
                    rset.close();
                    s.close();
                    conn.close();
                } catch (Exception e){
                    System.out.println("FAILED");
                }
                break;
            default:
                break;
        }
    }
}

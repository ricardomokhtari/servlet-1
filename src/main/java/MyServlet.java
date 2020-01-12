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
    // function that handles GET requests from frontend
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // database URL
        String dbURL = "jdbc:postgresql://ec2-54-195-252-243.eu-west-1.compute.amazonaws.com:5432/din8m6nuaj2gb?ssl" +
               "=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        try {
            // try to connect to postgres driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {

        }
        // header is required to send cross-origin requests
        resp.setHeader("Access-Control-Allow-Origin","*");
        // set the response data type to JSON
        resp.setContentType("application/json");
        // initialise the connection
        Connection conn = null;
        try {
            // attempt connection
            conn = DriverManager.getConnection(dbURL, "hdzsfwizwsoxxz", "b62525ba2575aa2a67faab4ab24f80234d850305522338e2bb16dd02f474db8e");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // use getServletPath to extract where the request came from
        String path = req.getServletPath();

        // perform switch with the path
        switch(path){

            // if request came from homepage
            case "/home":
                try {
                    Statement s = conn.createStatement();
                    // select all entries in users table
                    String sqlStr = "select * from users;";
                    ResultSet rset = s.executeQuery(sqlStr);
                    while(rset.next()){
                        // create a patient object to send data as JSON
                        Patient p = new Patient(rset.getString("id"),rset.getString("Latest severity score"),rset.getString("DOB"),rset.getString("Sex"),rset.getString("Last updated"));
                        Gson gson = new Gson();
                        // convert patient to JSON
                        String jsonString = gson.toJson(p);
                        // send
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

    // function for handling POST requests from frontend
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // extract where request came from
        String path = req.getServletPath();

        // database URL
        String dbURL = "jdbc:postgresql://ec2-54-195-252-243.eu-west-1.compute.amazonaws.com:5432/din8m6nuaj2gb?ssl" +
                "=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        try {
            // connect to postgres driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {

        }
        // set response data type to JSON
        resp.setContentType("application/json");
        // intialise the connection
        Connection conn = null;
        try {
            // attempt connection to database
            conn = DriverManager.getConnection(dbURL, "hdzsfwizwsoxxz", "b62525ba2575aa2a67faab4ab24f80234d850305522338e2bb16dd02f474db8e");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // perform switch with path
        switch(path){

            // if request comes from new patient page
            case "/newpatient":
                // read in the input from frontend
                String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject obj = new JSONObject(reqBody);
                // extract fields
                String firstname = obj.getString("firstname");
                String middlename = obj.getString("middlename");
                String surname = obj.getString("surname");
                String sex = obj.getString("sex");
                String ethnicity = obj.getString("ethnicity");
                String dob = obj.getString("dob");

                // define SQL command with fields entered
                String sqlCmd = "(\'"+firstname+"\',\'"+middlename+"\',\'"+surname+"\',\'"+sex+"\',\'"+ethnicity+"\',\'"+dob+"\')";
                System.out.println(sqlCmd);

                try {
                    Statement s = conn.createStatement();
                    // insert new patient into patient table
                    String sqlStr = "INSERT INTO patient (firstname, middlename, surname, sex, ethnicity, dob) values "+sqlCmd;
                    s.execute(sqlStr);
                    s.close();
                    conn.close();
                } catch (Exception e) {
                    System.out.println("FAILED");
                }
                break;

            // if request comes from view page
            case "/viewpage":
                try {
                    // read in info sent from frontend
                    String reqBody2=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    Statement s = conn.createStatement();
                    // define SQL statement
                    String sqlStr = "select * from " + reqBody2 + ";";
                    System.out.println(sqlStr);
                    // execute SQL statement
                    ResultSet rset = s.executeQuery(sqlStr);
                    // setting header necessary for cross-origin requests
                    resp.setHeader("Access-Control-Allow-Origin","*");
                    resp.setContentType("application/json");
                    while (rset.next()) {
                        // create a record object for sending data to frontend
                        Record r = new Record(rset.getString("id"), rset.getString("date"), rset.getString("imageage"), rset.getString("erythemascore"), rset.getString("edemascore"),rset.getString("exclorationscore"),rset.getString("lichenificationscore"),rset.getString("areascore"),rset.getString("totalscore"),rset.getString("comments"));
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(r);
                        // send
                        resp.getWriter().write(jsonString + "\n");
                    }
                    rset.close();
                    s.close();
                    conn.close();
                } catch (Exception e){
                    System.out.println("FAILED");
                }
                break;
            case "/upload":
                try{
                    // read in info sent from frontend
                    String reqBody3 = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    JSONObject obj1 = new JSONObject(reqBody3);
                    // extract fields
                    String date = obj1.getString("date");
                    String region = obj1.getString("region");
                    String erythema = obj1.getString("erythema");
                    String edema = obj1.getString("edema");
                    String excoriation = obj1.getString("excoriation");
                    String lichenification = obj1.getString("lichenification");
                    String areaScore = obj1.getString("areaScore");
                    String totalScore = obj1.getString("totalScore");
                    Statement s = conn.createStatement();
                    // define SQL statement
                    String sqlStr = "INSERT INTO" + region + " (\'"+date+"\',"+erythema+","+edema+","+","+excoriation+","+lichenification+","+areaScore+","+totalScore+")";
                    System.out.println(sqlStr);
                    // execute SQL statement
                    ResultSet rset = s.executeQuery(sqlStr);
                    // setting header necessary for cross-origin requests
                    resp.setHeader("Access-Control-Allow-Origin","*");
                    resp.setContentType("application/json");
                    resp.getWriter().write(sqlStr);
                    rset.close();
                    s.close();
                    conn.close();
                } catch (Exception e){
                    System.out.println("FAILED");
                }
                break;
            case "/":
                break;
            case "/create":
                break;
            default:
                break;
        }
    }
}

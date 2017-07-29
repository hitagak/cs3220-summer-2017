package edu.csula.cs3220.examples.jdbc;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/jdbc/guestbook")
public class GuestBookServlet extends HttpServlet {

    public void init () throws ServletException {
        // load driver
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch( ClassNotFoundException e ) {
            throw new ServletException( e );
        }
    }


	public void doGet( HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Connection c = null;
        try {
            String url = "jdbc:mysql://localhost/cs3220_test";
            String username = "root";
            String password = "";

            // CS3 server example
            // String url = "jdbc:mysql://localhost/cs3220xstu25";
            // String username = "cs3220xstu25";
            // String password = "password";

            c = DriverManager.getConnection( url, username, password );
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM guestbook" );
            List<GuestBookEntry> list = new ArrayList<>();

            while( rs.next() ) {
                list.add(new GuestBookEntry(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("message")
                ));
            }
            request.setAttribute("list", list);

            c.close();
        } catch( SQLException e ) {
            throw new ServletException( e );
        } finally {
            try
            {
                if( c != null ) c.close();
            }
            catch( SQLException e )
            {
                throw new ServletException( e );
            }
        }

        request.getRequestDispatcher("/WEB-INF/jdbc/guestbook.jsp")
        .forward(request, response);
	}
}

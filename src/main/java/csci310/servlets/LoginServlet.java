package csci310.servlets;

import com.google.gson.Gson;
import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.JsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String psw = req.getParameter("psw");
        String dbResponse = DatabaseManager.shared().verifyUser(new User(username, psw));
        if (dbResponse != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            User user =  JsonHelper.shared().fromJson(dbResponse, User.class);
            Response response = new Response(true,null,user);
            resp.getWriter().println(JsonHelper.shared().toJson(response));

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            Response response = new Response<>(false, "Either username or password is wrong.");
            resp.getWriter().println(JsonHelper.shared().toJson(response));
        }
    }
}

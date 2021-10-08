package csci310.servlets;

import com.google.gson.Gson;
import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.JsonHelper;
import csci310.utilities.UserDatabaseUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String psw = req.getParameter("psw");
        String dbResponse = UserDatabaseUtil.verifyUser(new User(username, psw));
        if (dbResponse != null) {
            User user =  JsonHelper.shared().fromJson(dbResponse, User.class);
            Response response = new Response(true,null,user);
            resp.getWriter().println(JsonHelper.shared().toJson(response));

        } else {
            Response response = new Response<>(false, "Either username or password is wrong.");
            resp.getWriter().println(JsonHelper.shared().toJson(response));
        }
    }
}

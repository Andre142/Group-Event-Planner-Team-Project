package csci310.servlets;

import com.google.gson.Gson;
import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;

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
        Gson gson = new Gson();
        if (DatabaseManager.shared().verifyUser(new User(username, psw))) {
            resp.setStatus(HttpServletResponse.SC_OK);
            Response response = new Response(true);
            resp.getWriter().println(gson.toJson(response));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            Response response = new Response<>(false, "Either username or password is wrong.");
            resp.getWriter().println(gson.toJson(response));
        }
    }
}

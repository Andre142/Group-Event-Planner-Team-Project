package csci310.servlets;

import com.google.gson.Gson;
import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        User user = gson.fromJson(req.getReader(), User.class);
        resp.setContentType("application/json");

        if (DatabaseManager.shared().checkUserExists(user.getUsername())) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            Response response = new Response(false, "The username exists.");
            resp.getWriter().println(gson.toJson(response));
            return;
        }

        DatabaseManager.shared().insertUser(user);
        resp.setStatus(HttpServletResponse.SC_OK);
        Response response = new Response(true);
        resp.getWriter().println(gson.toJson(response));
    }
}

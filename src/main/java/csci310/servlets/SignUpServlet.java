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
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = JsonHelper.shared().fromJson(req.getReader(), User.class);
        resp.setContentType("application/json");

        if (DatabaseManager.shared().checkUserExists(user.getUsername())) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            Response response = new Response(false, "The username has been associated with an account.");
            resp.getWriter().println(JsonHelper.shared().toJson(response));
            return;
        }

        user.setUuid(UUID.randomUUID().toString());
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        user.setPsw(null);
        Response response = new Response(true,null,user);
        resp.getWriter().println(JsonHelper.shared().toJson(response));
    }
}

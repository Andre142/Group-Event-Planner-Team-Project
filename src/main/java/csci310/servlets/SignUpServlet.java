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
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = JsonHelper.shared().fromJson(req.getReader(), User.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        if (UserDatabaseUtil.checkUserExists(user.getUsername())) {
            Response response = new Response(false, "The username has been associated with an account.");
            resp.getWriter().println(JsonHelper.shared().toJson(response));
            return;
        }

        user.setUuid(UUID.randomUUID().toString());
        UserDatabaseUtil.insertUser(user);

        user.setPsw(null);
        Response response = new Response(true,null,user);
        resp.getWriter().println(JsonHelper.shared().toJson(response));
    }
}

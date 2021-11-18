package csci310.servlets;

import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperMethods;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = HelperMethods.shared().fromJson(req.getReader(), User.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        if (DatabaseManager.object().checkUserExists(user.getUsername()) != null) {
            Response response = new Response(false, "The username has been associated with an account.");
            resp.getWriter().println(HelperMethods.shared().toJson(response));
            return;
        }

        user.setUuid(UUID.randomUUID().toString());
        DatabaseManager.object().insertUser(user);

        user.setPsw(null);
        Response response = new Response(true,null,user);
        resp.getWriter().println(HelperMethods.shared().toJson(response));
    }
}

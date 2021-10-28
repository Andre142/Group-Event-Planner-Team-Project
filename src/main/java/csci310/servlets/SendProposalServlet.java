//package csci310.servlets;
//
//import csci310.models.Proposal;
//import csci310.models.Response;
//import csci310.utilities.DatabaseManager;
//import csci310.utilities.JsonHelper;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class SendProposalServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Proposal proposal = JsonHelper.shared().fromJson(req.getReader(),Proposal.class);
//        resp.setStatus(HttpServletResponse.SC_OK);
//        resp.setContentType("application/json");
//
//         insert to SentProposals
//
//         insert to ReceivedProposals for every receiver
//
//         link a list of events to the proposal made
//
//        ArrayList<String> dbResponse = DatabaseManager.shared().searchUsers(usernameSubstring);
//        if (dbResponse != null) {
//            Response response = new Response(true,null,dbResponse);
//            resp.getWriter().println(JsonHelper.shared().toJson(response));
//
//        } else {
//            Response response = new Response<>(false, "No results returned for this query");
//            resp.getWriter().println(JsonHelper.shared().toJson(response));
//        }
//    }
//}

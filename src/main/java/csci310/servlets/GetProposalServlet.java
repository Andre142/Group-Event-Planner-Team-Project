package csci310.servlets;

import csci310.models.Event;
import csci310.models.Proposal;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.JsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class GetProposalServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        ArrayList<Proposal> proposals = new ArrayList<>();
        Boolean isSent = req.getParameter("type").equals("sent");
        String username = req.getParameter("username");
        ArrayList<String> proposalIDs = DatabaseManager.shared().getProposalIDs(username,isSent);

        for (String proposalID: proposalIDs) {
            ArrayList<String> receiverUsernames = DatabaseManager.shared().getReceiverUsernamesInProposal(proposalID);
            ArrayList<Event> events = DatabaseManager.shared().getEventsInProposal(proposalID);
            if (isSent) {
                proposals.add(new Proposal(username,receiverUsernames,events));
            } else {
                String senderUsername = DatabaseManager.shared().getSenderUsernameInProposal(proposalID);
                proposals.add(new Proposal(senderUsername,receiverUsernames,events));
            }
        }

        Response response = new Response(true,null,proposals);
        resp.getWriter().println(JsonHelper.shared().toJson(response));
    }
}

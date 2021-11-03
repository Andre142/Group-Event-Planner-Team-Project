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

public class SendProposalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Proposal proposal = JsonHelper.shared().fromJson(req.getReader(),Proposal.class);
        proposal.generateIDForProposalAndEvents();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

//      insert to SentProposals
        DatabaseManager.shared().insertSentProposal(proposal.getProposalID(), proposal.getProposalTitle(), proposal.getSenderUsername());
//      insert to ReceivedProposals for every receiver
        for (String username: proposal.getReceiverUsernames())
            DatabaseManager.shared().insertReceivedProposal(username, proposal.getProposalID());
//      link a list of events to the proposal made
        for (Event event: proposal.getEvents())
            DatabaseManager.shared().insertEvent(event, proposal.getProposalID());

        Response response = new Response(true);
        resp.getWriter().println(JsonHelper.shared().toJson(response));
    }
}

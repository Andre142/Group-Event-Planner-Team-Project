package csci310.servlets;

import csci310.utilities.BlockedListDatabase;
import jdk.nashorn.internal.ir.Block;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserDatabaseServlet")
public class BlockedListServlet extends HttpServlet {
    private static final long serialVersionUID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("type");
        String blocker = request.getParameter("blocker");
        String blockee = null;
        try
        {
            blockee = request.getParameter("blockee");
        }
        catch (Exception e)
        {
            System.out.println(e.getClass());
        }

        response.setContentType("text/plain");
        PrintWriter printWriter = response.getWriter();

        try
        {
            try
            {
                if (request.getParameter("throw").equalsIgnoreCase("true"))
                {
                    throw new SQLException();
                }
            }
            catch (NullPointerException e)
            {

            }

            if (type.equalsIgnoreCase("getBlocked"))
            {
                printWriter.print(String.valueOf(BlockedListDatabase.IsBlocking(blocker, blockee)));
            }
            else if (type.equalsIgnoreCase("removeBlock"))
            {
                BlockedListDatabase.DeleteBlock(blocker, blockee);
            }
            else if (type.equalsIgnoreCase("addBlock"))
            {
                BlockedListDatabase.AddBlock(blocker, blockee);
            }
            else if (type.equalsIgnoreCase("getBlockList"))
            {
                ArrayList<String> blockedList = BlockedListDatabase.GetBlockedList(blocker);
                for (int i = 0; i < blockedList.size(); i++)
                {
                    printWriter.print(blockedList.get(i));
                    if (i != blockedList.size() - 1)
                    {
                        printWriter.print(",");
                    }
                }
            }
            else
            {
                printWriter.print("invalid type");
            }

        }
        catch (SQLException e)
        {
            printWriter.print("exception occurred");
        }

        printWriter.flush();
    }
}

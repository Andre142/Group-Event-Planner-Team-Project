package csci310.utilities;

import java.sql.*;
import java.util.ArrayList;

public class BlockedListDatabase {
    public static void CreateDatabase() throws SQLException
    {

    }

    public static void DeleteDatabase() throws SQLException
    {

    }

    public static void AddBlock(String blockerUsername, String blockedUsername)  throws SQLException
    {

    }
    public static void DeleteBlock(String blockerUsername, String blockedUsername) throws SQLException
    {

    }

    public static ArrayList<String> GetBlockedList(String blockerUsername)  throws SQLException
    {
        return new ArrayList<String>();
    }

    public static boolean IsBlocking(String blockerUsername, String blockedUsername) throws SQLException
    {
        return false;
    }
}
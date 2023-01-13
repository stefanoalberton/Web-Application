<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title> Join request Managing </title>
    </head>
    <body>
        <h1>Send a Join Request</h1>
        <form method="POST">
                <label for="joinRequestContent">Content of Join Request:</label>
                    <input name="joinRequestContent" type="text"/><br/>
                <button type="submit">Send a join request</button><br/>
        </form>

        <h1>Send a Join Request</h1>
        <form method="POST">
             <label for="joinRequestGroupID">Group ID of group to join:</label>
                <input name="joinRequestGroupID" type="text"/><br/>
             <label for="joinRequestUserID">User ID of the user that wants to enters our group:</label>
                <input name="joinRequestUserID" type="text"/><br/>
             <button type="submit">Accept a join request</button><br/>
        </form>

        <h1>Delete a Join Request</h1>
                <form method="POST">
                     <label for="joinRequestGroupID">Group ID of group :</label>
                        <input name="joinRequestGroupID" type="text"/><br/>
                     <label for="joinRequestUserID">User ID of the user that wants to enters our group</label>
                        <input name="joinRequestUserID" type="text"/><br/>
                     <button type="submit">Delete join request</button><br/>
                </form>
    </body>
</html>
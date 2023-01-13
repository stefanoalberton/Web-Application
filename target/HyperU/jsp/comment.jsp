<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title> Idea Comments </title>
    </head>
    <body>
        <h1> Idea Comments </h1>
        <hr/>
        <!-- display the message -->
        <c:import url="/jsp/show-message.jsp"/>
        <!-- display the chat of the group -->
        <c:if test='${not empty ideaComments}'>
            <c:forEach var="c" items="${ideaComments}">
                <c:out value="[[[ ${c.ID} ]]] , "/>
                <c:out value="[ ${c.user.username} ] , "/>
                <c:out value="[ ${c.sentTime} ] :"/>
                <c:out value=" ${c.text} "/>
                <br>
            </c:forEach>
        </c:if>

        <br>

        <h1>Post a comment </h1>
            <form method="POST" >
                <label for="commentText">Comment:</label>
                <input name="commentText" type="text"/><br/>
                <button type="submit">Send</button><br/>
            </form>

        <h1>Delete a Comment</h1>
            <form method="POST" action="delete">
                <label for="commentID"> ID of comment :</label>
                <input name="commentID" type="text"/><br/>
                <button type="submit">Delete comment</button><br/>
            </form>

        <h1>Modify a comment</h1>
            <form method="POST" action="modify">
                <label for="modifiedCommentID">ID of comment :</label>
                <input name="modifiedCommentID" type="text"/><br/>
                <label for="modifiedCommentText"> new Comment:</label>
                <input name="modifiedCommentText" type="text"/><br/>
                <button type="submit">Modify comment</button><br/>
            </form>
    </body>
</html>
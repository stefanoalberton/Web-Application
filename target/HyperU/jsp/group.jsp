<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title> Group Chat </title>
    </head>
    <body>
        <h1> Group Chat </h1>
        <hr/>
        <!-- display the message -->
        <c:import url="/jsp/show-message.jsp"/>
        <!-- display the chat of the group -->
        <c:if test='${not empty groupMessages}'>
            <c:forEach var="m" items="${groupMessages}">
                <c:out value="[ ${m.user.username} ] , "/></td>
                <c:out value="[ ${m.sentTime} ] :"/>
                <c:out value=" ${m.content} "/>
                <c:out value=" ${m.file} "/>
                <br>
            </c:forEach>
        </c:if>

        <br>

        <h1>Send a message</h1>
            <form method="POST" >
        <label for="messageContent">Message:</label>
            <input name="messageContent" type="text"/><br/>
        <button type="submit">Submit</button><br/>
    </body>
</html>
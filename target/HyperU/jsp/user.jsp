<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>User</title>
    </head>
    <body>
        <c:choose>
            <c:when test='${user != null}'>
                <h1><c:out value="${user.profile.name} ${user.profile.surname}" /></h1>
                <hr/>

                <p><strong>Username: </strong><c:out value="${user.username}" /></p>
                <p><strong>Email: </strong><c:out value="${user.email}" /></p>
                <p><strong>Role: </strong><c:out value="${user.role}" /></p>
                <p><strong>Banned: </strong><c:out value="${user.banned}" /></p>
                <p></p>
                <p><strong>BirthDate: </strong><c:out value="${user.profile.birthDate}" /></p>
                <p><strong>Gender: </strong><c:out value="${user.profile.gender}" /></p>

                <h4>Biography</h4>
                <p>
                    <c:out value="${user.profile.biography}" />
                </p>
                <p>
                    <strong>Topics: </strong>
                    <c:if test='${not empty user.profile.topics}'>
                        <c:forEach var="topic" items="${user.profile.topics}">
                            <span><c:out value="${topic.name}" />, </span>
                        </c:forEach>
                    </c:if>
                </p>
                <p>
                    <strong>Skills: </strong>
                    <c:if test='${not empty user.profile.skills}'>
                        <c:forEach var="skill" items="${user.profile.skills}">
                            <span><c:out value="${skill.name}" />, </span>
                        </c:forEach>
                    </c:if>
                </p>
                <img src="../image/profile/<c:out value="${user.ID}" />" alt="<c:out value="${user.username}" />" style="max-width: 500px; width:100%" />

                <form method="GET" action="logout">
                    <p><input type="submit" value="Logout"></p>
                </form>
            </c:when>
            <c:otherwise>
                <h1>Login</h1>
                <hr />
                <form method="POST" action="login">
                    <p><input name="username" type="text" placeholder="Username or Email" /></p>
                    <p><input name="password" type="password" placeholder="Password"/></p>
                    <p><button type="submit">Login</button><br/></p>
                </form>
            </c:otherwise>
        </c:choose>
    </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Manage idea</title>
    </head>
    <body>
        <c:choose>
            <c:when test='${idea != null}'>
                <h1><c:out value="${idea.title}" /></h1>
                <hr/>
                <h4>Description</h4>
                <p>
                    <c:out value="${idea.description}" />
                </p>
                <p>
                    <strong>Topics: </strong>
                    <c:if test='${not empty idea.topics}'>
                        <c:forEach var="topic" items="${idea.topics}">
                            <span><c:out value="${topic.name}" />, </span>
                        </c:forEach>
                    </c:if>
                </p>
                <p>
                    <strong>Skills: </strong>
                    <c:if test='${not empty idea.skills}'>
                        <c:forEach var="skill" items="${idea.skills}">
                            <span><c:out value="${skill.name}" />, </span>
                        </c:forEach>
                    </c:if>
                </p>
                <img src="../image/idea/<c:out value="${idea.ID}" />" alt="<c:out value="${idea.title}" />" style="max-width: 500px; width:100%" />

                <h1>Edit</h1>
                <hr/>

                <form enctype="multipart/form-data" method="POST">
                    <input type="hidden" name="operation" value="PUT" />
                    <p><input type="text" name="title" placeholder="Title" value="<c:out value="${idea.title}" />"></p>
                    <p><textarea name="description" placeholder="Description"><c:out value="${idea.description}" /></textarea></p>
                    <p>
                        <select name="topics" multiple>
                            <c:if test='${not empty topics}'>
                                <c:forEach var="topic" items="${topics}">
                                    <option value="<c:out value="${topic.ID}" />"><c:out value="${topic.name}" /></option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </p>
                    <p>
                        <select name="skills" multiple>
                            <c:if test='${not empty skills}'>
                                <c:forEach var="skill" items="${skills}">
                                    <option value="<c:out value="${skill.ID}" />"><c:out value="${skill.name}" /></option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </p>
                    <p>
                        <input type="file" name="image" accept="image/*">
                    </p>
                    <p><input type="submit" value="Send"></p>
                </form>

                <h1>Delete</h1>
                <hr/>

                <form method="POST">
                    <input type="hidden" name="operation" value="DELETE" />
                    <p><input type="submit" value="Delete"></p>
                </form>

                <form method="POST">
                    <input type="hidden" name="operation" value="LIKE" />
                    <p><input type="submit" value="Like"></p>
                </form>
            </c:when>
            <c:otherwise>
                <h1>Create a new Idea</h1>
                <hr />
                <form enctype="multipart/form-data" method="POST">
                    <p><input type="text" name="title" placeholder="Title"></p>
                    <p><textarea name="description" placeholder="Description"></textarea></p>
                    <p>
                        <select name="topics" multiple>
                            <c:if test='${not empty topics}'>
                                <c:forEach var="topic" items="${topics}">
                                    <option value="<c:out value="${topic.ID}" />"><c:out value="${topic.name}" /></option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </p>
                    <p>
                        <select name="skills" multiple>
                            <c:if test='${not empty skills}'>
                                <c:forEach var="skill" items="${skills}">
                                    <option value="<c:out value="${skill.ID}" />"><c:out value="${skill.name}" /></option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </p>
                    <p><input type="file" name="image" accept="image/*"></p>
                    <p><input type="submit" value="Send"></p>
                </form>
            </c:otherwise>
        </c:choose>
    </body>
</html>
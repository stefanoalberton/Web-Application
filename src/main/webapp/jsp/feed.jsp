<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <%@include file="./components/navbar.jsp" %>

                    <div class="row page m-0 p-0">
                        <%@include file="./components/sidebar_teams.jsp" %>

                            <!-- Central column to display ideas -->
                            <div
                                class="content col-sm-12 col-xs-12 col-md-12 col-lg-6 d-flex flex-column align-items-center">
                                <div class="d-grid gap-5 justify-content-center w-100" id="ideas"></div>
                            </div>

                        <%@include file="./components/sidebar_requests.jsp" %>
                    </div>

                    <%@include file="./components/join_team_modal.jsp" %>

                    <%@include file="./components/footer.jsp" %>

                        <script src="<c:out value="${baseUri}" />/js/components/comment.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/components/idea.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/feed.js"></script>
            </body>
        </html>
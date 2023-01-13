<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <a id="activePage" data-page="requests" class="d-none"></a>
                <%@include file="./components/navbar.jsp" %>

                    <div class="row page m-0 p-0 d-flex justify-content-center">
                        <div
                            class="content col-sm-12 col-xs-12 col-md-12 col-lg-6 d-flex flex-column align-items-center requestsTeams px-0">
                            <div class="mb-3">
                                <h2>Requests</h2>
                            </div>
                            <div class="justify-content-center list-group w-100" id="requests"></div>
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>
                        <script src="<c:out value="${baseUri}" />/js/components/request.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/requests.js"></script>
            </body>

        </html>
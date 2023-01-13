<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div id="splashScreen"><img src="<c:out value="${baseUri}" />/media/logos/logo.svg" alt="HyperU" /></div>

        <side class="col col-md-3 col-lg-3 side d-none d-lg-block">
            <div class="sidebar-item-right">
                <div class="sticky py-4 requestsTeams">
                    <h5 class="mx-4 mb-4">Requests</h5>
                    <div class="list-group" id="sideRequests"></div>
                </div>
            </div>
        </side>
        <script src="<c:out value="${baseUri}" />/js/components/request.js"></script>
        <script src="<c:out value="${baseUri}" />/js/sidebar_requests.js"></script>
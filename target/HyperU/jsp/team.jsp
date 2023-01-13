<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <a id="activePage" data-page="teams" class="d-none"></a>
                <%@include file="./components/navbar.jsp" %>

                    <div class="row page m-0 p-0">
                        <%@include file="./components/sidebar_teams.jsp" %>

                            <div
                                class="content col-sm-12 col-xs-12 col-md-12 col-lg-6 d-flex flex-column align-items-center">
                                <div class="d-flex justify-content-center w-100" id="chat">
                                    <div class="card border-0 bg-transparent chat d-none">
                                        <div class="card-header bg-transparent border-0" id="chatHeader"></div>
                                        <div class="card-body msg-container bg-transparent border-0 p-0 text-dark d-grid gap-2"
                                            id="chatContent"></div>
                                        <div class="card-footer bg-transparent border-0">
                                            <div class="input-group has-icons">
                                                <textarea class="form-control py-3 shadow rounded-2" id="chatMessage"
                                                    aria-label="New message"
                                                    placeholder="Type your message..."></textarea>
                                                <i class="feather icon-paperclip h3 m-1 me-5 attach"
                                                    id="chatAttach"></i>
                                                <i class="feather icon-send text-primary h3 m-1 send" id="chatSend"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%@include file="./components/sidebar_requests.jsp" %>
                    </div>

                    <div class="modal fade" id="modalInfoTeam" tabindex="-1" role="dialog"
                        aria-labelledby="modalInfoTeamTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-scrollable">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="modalInfoTeamTitle"></h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>
                                <div class="modal-body"></div>
                            </div>
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>

                        <script src="https://cdn.jsdelivr.net/npm/autosize@4.0.3/dist/autosize.min.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/components/message.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/chat.js"></script>
            </body>

        </html>
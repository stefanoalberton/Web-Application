<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!-- Send Request Modal -->
        <div class="modal fade" id="sendRequestModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="sendRequestModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content" id="sendRequestModalContent">
                    <div class="modal-header">
                        <h4 id="sendRequestModalLabel" class="modal-title">Ask join a team</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <!-- Form to send a join request -->
                    <form id="sendRequestForm" action="#" method="POST">
                        <div class="modal-body">
                            <div class="my-3">
                                <label for="sendRequestTeams" class="form-label">Select a team</label>
                                <select id="sendRequestTeams" class="form-select" name="teams"></select>
                            </div>
                            <div class="my-3">
                                <label for="sendRequestMessage" class="form-label">Add a message</label>
                                <input type="text" name="message" class="form-control" id="sendRequestMessage" value=""
                                    placeholder="Add a message, if you want">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary"
                                data-bs-dismiss="modal">Close</button>
                            <button id="send" type="submit" class="btn btn-primary">Send</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="<c:out value="${baseUri}" />/js/join_request.js"></script>
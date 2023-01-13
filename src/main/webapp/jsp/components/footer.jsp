<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!-- Container to display notification -->
        <div class="toast-container" id="notifications"></div>

        <div class="modal fade" id="installPWAModal" tabindex="-1" aria-labelledby="installPWAModalLabel" aria-modal="true"
            role="dialog" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content text-center">
                    <div class="modal-body">
                        <h5 id="installPWAModalLabel">You know?</h5>
                        <p class="mt-4">You can install our webapp to your device, click the button below!</p>
                        <div class="row mt-4">
                            <div class="col-4"><button class="btn btn-secondary btn-lg w-100" data-bs-dismiss="modal" id="notInstallPWAButton">No</button></div>
                            <div class="col-8"><button class="btn btn-primary btn-lg w-100" id="installPWAButton">Install</button></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8"
            crossorigin="anonymous"></script>
        <script src="<c:out value="${baseUri}" />/js/footer.js"></script>
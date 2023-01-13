<!-- Manage Request Modal -->
<div class="modal fade" id="manageRequestModal" data-bs-backdrop="static" data-bs-keyboard="false"
    tabindex="-1" aria-labelledby="manageRequestModalLabel" aria-hidden="true" data-teamid=""
    data-userid="">
    <div class="modal-dialog">
        <div class="modal-content" id="manageRequestModalContent">
            <div class="modal-header">
                <h4 class="modal-title" id="manageRequestModalLabel">
                    <a href="" id="requestUsername" class="text-decoration-none"></a> asks:
                </h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                    aria-label="Close"></button>
            </div>

            <div class="modal-body">
                <div id="requestMessage"></div>
            </div>

            <div class="modal-footer">
                <button id="decline" type="button"
                    class="btn btn-outline-secondary">Decline</button>
                <button id="accept" type="button" class="btn btn-primary">Accept</button>
            </div>
        </div>
    </div>
</div>
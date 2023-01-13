function loadRequestsSidebar() {
  var spinner = loadingSpinner.clone();
  $("#sideRequests").append(spinner);

  $.ajax({
    url: contextPath + "user/me/requests",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#sideRequests #loadingSpinner").remove();
    },
    success: function (data) {
      var requests = data.data;

      if (requests.length <= 0) {
        $("#sideRequests").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-bell h1"></i>
                                        <p class="text-center">Seems you have<br />no requests</p>
                                    </div>`);
      }

      requests.forEach((request) => {
        requestElement = createSidebarRequest(request);
        $("#sideRequests").append(requestElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadRequestsSidebar();

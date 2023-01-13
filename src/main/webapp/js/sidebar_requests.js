//Function to display join request on the sidebar
function loadRequestsSidebar() {
  const spinner = loadingSpinner.clone();
  $("#sideRequests").append(spinner);

  //AJax call to REST API
  $.ajax({
    url: contextPath + "user/me/requests",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#sideRequests #loadingSpinner").remove();
    },
    success: function (data) {
      const requests = data.data;

      //display a message if user has no request
      if (requests.length <= 0) {
        $("#sideRequests").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-bell h1"></i>
                                        <p class="text-center">Seems you have<br />no requests</p>
                                    </div>`);
      }

      //Create a request element in the sidebar for each request
      requests.forEach((request) => {
        var requestElement = createSidebarRequest(request);
        $("#sideRequests").append(requestElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadRequestsSidebar();

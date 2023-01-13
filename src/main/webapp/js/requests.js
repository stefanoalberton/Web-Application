//Function to display requests on the mobile version
function loadRequests() {
  const spinner = loadingSpinner.clone();
  $("#requests").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "user/me/requests",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#requests #loadingSpinner").remove();
    },
    success: function (data) {
      const requests = data.data;

      //Display a message if user has no request      
      if (requests.length <= 0) {
        $("#requests").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-bell h1"></i>
                                        <p class="text-center">Seems you have<br />no requests</p>
                                    </div>`);
      }

      //Create a request element for each request
      requests.forEach((request) => {
        var requestElement = createRequest(request);
        $("#requests").append(requestElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//load the sideBar
loadRequests();

// Send a join request
function sendRequest(e) {
  e.preventDefault();

  // Retrieve teamID
  const teamID = $("#sendRequestTeams").val();

  // Retrieve message
  const requestMessage = $("#sendRequestMessage").val();

  //Prepare JSON object
  const requestObject = {
    message: requestMessage,
  };

  //Ajax call to RESTAPI
  $.ajax({
    url: contextPath + "team/" + teamID + "/request",
    method: "POST",
    data: JSON.stringify(requestObject),
    beforeSend: beforeSend,
    complete: function () {
      $("#sendRequestModal").modal("hide");
    },
    success: function (data) {
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Link the submit button with the function
$("#sendRequestForm").submit(sendRequest);

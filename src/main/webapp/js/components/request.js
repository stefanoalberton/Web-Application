//Function to create request to be displayed into the sidebar
function createSidebarRequest(request) {
  //Retrieve all the information that have to be displayed
  const team = request.team;
  const idea = team.idea;

  const user = request.user;

  const username = user.username;
  const userID = user.id;
  const userImage = contextPath + user.profilePictureUrl;
  const ideaTitle = idea.title;
  const teamID = team.id;
  const requestMessage = request.message != "" ? request.message : "No message for you 😢";

  //Create an element for the request to be displayed in the sidebar
  var sidebarElement =
    $(`<a href="#" data-teamid="${teamID}" data-userid="${userID}" class="list-group-item list-group-item-action px-4 py-2 ps-5" data-bs-toggle="modal" data-bs-target="#manageRequestModal">
          <div class="d-flex align-items-center justify-content-end">
            <div>
              <span class="req-username d-block">${username}</span>
              <span class="d-block subtitle">for ${ideaTitle}</span>
              <div class="req-message d-none">${requestMessage}</div>
            </div>
            <img src="${userImage}" class="rounded-circle ms-2" alt="${username}">
          </div>
        </a>`);

  // Add the teamID and userID to the manageRequestmodal
  sidebarElement.click(setRequestModal);

  return sidebarElement;
}

//Function to create a request element for the mobile version
function createRequest(request) {
  //Retrieve all the information that have to been displayed
  const team = request.team;
  const idea = team.idea;

  const user = request.user;

  const username = user.username;
  const userID = user.id;
  const userImage = contextPath + user.profilePictureUrl;
  const ideaTitle = idea.title;
  const teamID = team.id;
  const requestMessage = request.message != "" ? request.message : "No message for you 😢";

  //Create an element for a request
  var sidebarElement = $(`<a href="#" data-teamid="${teamID}" data-userid="${userID}" class="list-group-item list-group-item-action w-100" data-bs-toggle="modal" data-bs-target="#manageRequestModal">
          <div class="d-flex align-items-center">
            <img src="${userImage}" class="rounded-circle me-2" alt="${username}">
            <div>
              <span class="req-username d-block">${username} </span>
              <span class="d-block subtitle">for ${ideaTitle}</span>
              <div class="req-message d-none">${requestMessage}</div>
            </div>
          </div>
        </a>`);

  // Add the teamID and userID to the manageRequestmodal
  sidebarElement.click(setRequestModal);

  return sidebarElement;
}

//Function to create the modal to manage a request
function setRequestModal(e) {
  e.preventDefault();

  //Retrieve info needed
  const teamID = $(this).data("teamid");
  const userID = $(this).data("userid");
  const requestMessage = $(this).find(".req-message").html();
  const username = $(this).find(".req-username").text();

  //Pre-compiled modal
  $("#manageRequestModal").data("teamid", teamID);
  $("#manageRequestModal").data("userid", userID);
  $("#manageRequestModal #requestMessage").html(requestMessage);
  $("#manageRequestModal #requestUsername").text(username);
  $("#manageRequestModal #requestUsername").attr("href", baseUri + "u/" + username);
}

//Function to accept request
function acceptRequest(e) {
  e.preventDefault();

  // Retrieve the clicked ID
  const teamID = $("#manageRequestModal").data("teamid");
  const userID = $("#manageRequestModal").data("userid");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "team/" + teamID + "/request/" + userID,
    method: "PUT",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      $("#manageRequestModal").modal("hide");
    },
    success: function (data) {
      $("#sideRequests, #requests")
        .find("[data-userid=" + userID + "][data-teamid=" + teamID + "]")
        .remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to decline a request
function declineRequest(e) {
  e.preventDefault();

  // Retrieve the clicked ID
  const teamID = $("#manageRequestModal").data("teamid");
  const userID = $("#manageRequestModal").data("userid");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "team/" + teamID + "/request/" + userID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      $("#manageRequestModal").modal("hide");
    },
    success: function (data) {
      $("#sideRequests, #requests")
        .find("[data-userid=" + userID + "][data-teamid=" + teamID + "]")
        .remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

///LInk functions to modals
$("#manageRequestModal #accept").click(acceptRequest);
$("#manageRequestModal #decline").click(declineRequest);

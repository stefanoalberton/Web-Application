async function fillNavbar() {
  await checkLogin();

  //Retrieve the logged user
  loggedUser = getLoggedUser();

  //Fill the elements with the info of the logged user
  $("#userImageMenu, #userImageNavbar").attr("alt", loggedUser.sub);
  $("#userImageMenu, #userImageNavbar").attr("src", contextPath + "image/profile/" + loggedUser.id);
  $("#userMenu, #userNavbar").text(loggedUser.sub);

  //If common user it doesn't have te possibilty to access moderator page
  if (loggedUser.role != "Common User") {
    $("#manageDropdownItem").removeClass("d-none");
  }
}

//Function to set active page
function setActivePage() {
  var activePage = "home";
  if ($("#activePage").length > 0) {
    activePage = $("#activePage").data("page");
  }
  $("#page-" + activePage).addClass("active");
}

//Logout if button is clicked
$("#logoutBtn").click(function (e) {
  e.preventDefault();

  //Ajax call to Rest API
  $.ajax({
    url: contextPath + "user/logout",
    method: "GET",
    success: function () {
      sessionStorage.removeItem("jwt");
      deleteCookie("token");
      location.href = baseUri + "login";
    },
  });
});


var allSkillsAvailable = null;
// Get skills to use in add/remove idea
function getAllSkills() {
  //Ajax call to REST API
  $.ajax({
    url: contextPath + "skill",
    method: "GET",
    beforeSend: beforeSend,
    success: function (data) {
      allSkillsAvailable = data.data;
    },
    error: function (data) {
      showError(data);
    },
  });
}

var allTopicsAvailable = null;
// Get topics to use in add/remove idea
function getAllTopics() {
  //Ajax call to REST API
  $.ajax({
    url: contextPath + "topic",
    method: "GET",
    beforeSend: beforeSend,
    success: function (data) {
      allTopicsAvailable = data.data;
    },
    error: function (data) {
      showError(data);
    },
  });
}

// Get skills and topics
getAllSkills();
getAllTopics();

// Fill navbar
fillNavbar();
setActivePage();

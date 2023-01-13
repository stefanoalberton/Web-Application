async function fillNavbar() {
  await checkLogin();

  var loggedUser = parseJwt(sessionStorage.getItem("jwt"));

  $("#userImageMenu, #userImageNavbar").attr("alt", loggedUser.sub);
  $("#userImageMenu, #userImageNavbar").attr("src", contextPath + "image/profile/" + loggedUser.id);
  $("#userMenu, #userNavbar").text(loggedUser.sub);

  if (loggedUser.role != "Common User") {
    $("#manageDropdownItem").removeClass("d-none");
  }
}

function setActivePage() {
  var activePage = "home";
  if ($("#activePage").length > 0) {
    activePage = $("#activePage").data("page");
  }
  $("#page-" + activePage).addClass("active");
}

$("#logoutBtn").click(function () {
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

fillNavbar();
setActivePage();

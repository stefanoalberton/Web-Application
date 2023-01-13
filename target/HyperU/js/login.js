checkLogin(true);

$("#login-form").submit(function (e) {
  e.preventDefault();

  $.ajax({
    url: contextPath + "user/login",
    method: "POST",
    data: $(this).serialize(),
    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    success: function (data) {
      var userData = data.data;
      var userToken = userData.jwtoken;
      sessionStorage.setItem("jwt", userToken);
      setCookie("token", userData.user.id + "." + userData.token);
      location.reload();
    },
    error: function (data) {
      showError(data);
    },
  });
});

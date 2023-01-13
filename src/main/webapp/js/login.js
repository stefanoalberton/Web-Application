//Check if already logged in
checkLogin(true);

//Manage form Validation
$("#inputPassword, #inputUsername").change(function () {
  $("#passwordValidation").text("Please enter the password.");
  $("#inputPassword")[0].setCustomValidity("");
  $("#inputUsername")[0].setCustomValidity("");
  $("#usernameValidation").removeClass("d-none");
});

//Form validation
$("#login-form").submit(function (e) {
  e.preventDefault();

  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  if (!isInvalid) {
    //Ajax call to REST API
    $.ajax({
      url: contextPath + "user/login",
      method: "POST",
      data: $(this).serialize(),
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      success: function (data) {
        //Save useful info about the logged user
        const userData = data.data;
        const userToken = userData.jwtoken;
        sessionStorage.setItem("jwt", userToken);
        setCookie("token", userData.user.id + "." + userData.token);
        location.reload();
      },
      error: function (data) {
        //Display error message and customized form validation based on back-end validation
        if (data.responseJSON.message.errorCode == -202) {
          $("#passwordValidation").text(data.responseJSON.message.message);
          $("#usernameValidation").addClass("d-none");
          $("#inputPassword")[0].setCustomValidity(false);
          $("#inputUsername")[0].setCustomValidity(false);
        } else {
          showError(data);
        }
      },
    });
  }
});

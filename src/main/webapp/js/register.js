//Function to do form validation on the password field
function validatePasswordCheck() {
  //Check if the psw and its check are equal
  if ($("#inputPassword").val() != $("#inputPasswordCheck").val()) {
    $("#passwordCheckValidation").text("Passwords must be equals.");
    $("#inputPasswordCheck")[0].setCustomValidity(false);
  } else {
    $("#inputPasswordCheck")[0].setCustomValidity("");
    $("#passwordCheckValidation").text("Please reenter the password.");
  }
}

//Function to do form validation on the age of the 
function validateBirthdate() {
  const age = calculateAge(new Date($("#inputBirthdate").val()));

  if (age < 16) {
    $("#birthdateValidation").text("You must be at least 16.");
    $("#inputBirthdate")[0].setCustomValidity(false);
  } else if (age > 120) {
    $("#birthdateValidation").text("Seriously? " + age + " years?");
    $("#inputBirthdate")[0].setCustomValidity(false);
  } else {
    $("#inputBirthdate")[0].setCustomValidity("");
    $("#birthdateValidation").text("Please enter the birthdate.");
  }
}

$("#inputPasswordCheck").change(validatePasswordCheck);
$("#inputBirthdate").change(validateBirthdate);
$("#inputUsername").change(function () {
  $("#inputUsername")[0].setCustomValidity("");
});

//Function to submit the registration
$("#register-form").submit(function (e) {
  e.preventDefault();

  //Custom form Validation
  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  $("#usernameValidation").text("Please enter a valid username.");

  validatePasswordCheck();
  validateBirthdate();

  $(this).addClass("was-validated");

  //IF valid retrieve info from the form
  if (!isInvalid) {
    const username = $("#inputUsername").val();
    const email = $("#inputEmail").val();
    const profileName = $("#inputName").val();
    const surname = $("#inputSurname").val();
    const birthDate = $("#inputBirthdate").val();
    const gender = $("#inputGender").val();
    const biography = $("#inputBiography").val();
    const password = $("#inputPassword").val();
    const passwordCheck = $("#inputPasswordCheck").val();

    //Prpare JSON Object
    const profileObject = {
      username: username,
      email: email,
      profile: {
        name: profileName,
        surname: surname,
        birthDate: birthDate,
        gender: gender,
        biography: biography,
        skills: [],
        topics: [],
      },
      password: password,
      passwordCheck: passwordCheck,
    };

    //AJax call to rest API
    $.ajax({
      url: contextPath + "user/register",
      method: "POST",
      data: JSON.stringify(profileObject),
      contentType: "application/json; charset=UTF-8",
      success: function () {
        $("#register").addClass("d-none");
        $("#success").removeClass("d-none");

        // Logout from current user
        $.ajax({
          url: contextPath + "user/logout",
          method: "GET",
          success: function () {
            sessionStorage.removeItem("jwt");
            deleteCookie("token");
          },
        });
      },
      error: function (data) {
        if (data.responseJSON.message.errorCode == -211) {
          $("#usernameValidation").text(data.responseJSON.message.message);
          $("#inputUsername")[0].setCustomValidity(false);
        } else {
          showError(data);
        }
      },
    });
  }
});

autosize($("#inputBiography"));

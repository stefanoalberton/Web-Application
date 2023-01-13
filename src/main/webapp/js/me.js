//Function to format date for the date picker
function formatOnlyDateSelector(date) {
  date = new Date(date);
  return date.getFullYear() + "-" + (date.getMonth() + 1).toString().padStart(2, "0") + "-" + date.getDate().toString().padStart(2, "0");
}

//Function to create a modal to edit the profile
function getProfileModal(user) {
  profile = user.profile;

  //Retrieve info of the user
  const username = user.username;
  const email = user.email;
  const profileName = profile.name;
  const surname = profile.surname;
  const birthDate = profile.birthDate;
  const biography = profile.biography;
  const gender = profile.gender;

  //Per-compiled modal
  $("#profileModal #name").val(profileName);
  $("#profileModal #surname").val(surname);
  $("#profileModal #username").val(username);
  $("#profileModal #email").val(email);
  $("#profileModal #birthdate").val(formatOnlyDateSelector(birthDate));
  $("#profileModal #biography").val(biography);
  $("#profileModal #gender").val(gender);

  autosize($("#profileModal #biography"));
}

$("#profileModal").on("shown.bs.modal", function () {
  autosize.update($("#profileModal #biography"));
});

// Fill modal for adding skills
function getAddSkillsModal(user) {
  const userSkills = user.profile.skills;

  //Ajax call to REST API
  $("#addSkillProfile").empty();

  if (allSkillsAvailable != null) {
    //Display only the skills not owned by the user
    const usefulSkills = allSkillsAvailable.filter(isNotIn(userSkills));

    usefulSkills.forEach((skill) => {
      const skillName = skill.name;
      const skillID = skill.id;
      $("#addSkillProfile").append($(`<option value="${skillID}">${skillName}</option>`));
    });
  } else {
    setTimeout(() => {
      getAddSkillsModal(user);
    }, 200);
  }
}

// Fill modal for deleting skills
function getUserSkillModal(user) {
  $("#deleteSkillProfile").empty();

  //Display only skills owned by the user
  user.profile.skills.forEach((skill) => {
    const skillName = skill.name;
    const skillID = skill.id;
    $("#deleteSkillProfile").append($(`<option value="${skillID}">${skillName}</option>`));
  });
}

// Fill modal for adding topics
function getAddTopicsModal(user) {
  const userTopics = user.profile.topics;

  //Ajax call to REST API
  $("#addTopicProfile").empty();

  if (allTopicsAvailable != null) {
    //Display only the topics not followed by the user
    const usefulTopics = allTopicsAvailable.filter(isNotIn(userTopics));

    usefulTopics.forEach((topic) => {
      const topicName = topic.name;
      const topicID = topic.id;
      $("#addTopicProfile").append($(`<option value="${topicID}">${topicName}</option>`));
    });
  } else {
    setTimeout(() => {
      getAddTopicsModal(user);
    }, 200);
  }
}

// Fill modal for deleting topics
function getUserTopicsModal(user) {
  $("#deleteTopicProfile").empty();
  //Display only topics followed by a user
  user.profile.topics.forEach((topic) => {
    const topicName = topic.name;
    const topicID = topic.id;
    $("#deleteTopicProfile").append($(`<option value="${topicID}">${topicName}</option>`));
  });
}

//Validate birth date to see if user is at least 16 years old
function validateBirthdate() {
  const age = calculateAge(new Date($("#birthdate").val()));

  if (age < 16) {
    $("#birthdateValidation").text("You must be at least 16.");
    $("#birthdate")[0].setCustomValidity(false);
  } else if (age > 120) {
    $("#birthdateValidation").text("Seriously? " + age + " years?");
    $("#birthdate")[0].setCustomValidity(false);
  } else {
    $("#birthdate")[0].setCustomValidity("");
    $("#birthdateValidation").text("Please enter the birthdate.");
  }
}

//Validate passoword check if the two enterd passwords are equal
function validatePasswordCheck() {
  if ($("#password").val() != $("#passwordCheck").val()) {
    $("#passwordCheckValidation").text("Passwords must be equals.");
    $("#passwordCheck")[0].setCustomValidity(false);
  } else {
    $("#passwordCheck")[0].setCustomValidity("");
    $("#passwordCheckValidation").text("Please reenter the password.");
  }
}

//Validation
$("#passwordCheck").change(validatePasswordCheck);
$("#birthdate").change(validateBirthdate);
$("#username").change(function () {
  $("#username")[0].setCustomValidity("");
});

//Function to validate edit of the profile
function editProfile(e) {
  e.preventDefault();

  //Custom validation
  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  $("#usernameValidation").text("Please enter a valid username.");

  validateBirthdate();

  $(this).addClass("was-validated");

  //IF valid retrieve info from the form
  if (!isInvalid) {
    const username = $("#profileModal #username").val();
    const email = $("#profileModal #email").val();
    const profileName = $("#profileModal #name").val();
    const surname = $("#profileModal #surname").val();
    const birthDate = $("#profileModal #birthdate").val();
    const gender = $("#profileModal #gender").val();
    const biography = $("#profileModal #biography").val();

    //Prepare JSON Object
    const profileObject = {
      username: username,
      email: email,
      profile: {
        name: profileName,
        surname: surname,
        birthDate: birthDate,
        gender: gender,
        biography: biography,
      },
    };

    //Ajax call to REST API
    const currentModal = $(this).parents(".modal");
    $.ajax({
      url: contextPath + "user/me",
      method: "PUT",
      data: JSON.stringify(profileObject),
      contentType: "application/json; charset=UTF-8",
      beforeSend: beforeSend,
      success: function (data) {
        currentModal.modal("hide");
        $("#editProfileForm").removeClass("was-validated");
        showSuccess(data);
        getProfile(false);
      },
      error: function (data) {
        if (data.responseJSON.message.errorCode == -211) {
          $("#usernameValidation").text(data.responseJSON.message.message);
          $("#username")[0].setCustomValidity(false);
        } else {
          currentModal.modal("hide");
          showError(data);
        }
      },
    });
  }
}

//Function to change password
function changePassword(e) {
  e.preventDefault();

  //Custom validation
  const isInvalid = !$(this)[0].checkValidity();

  validatePasswordCheck();

  $(this).addClass("was-validated");

  //IF valid Ajax call to REST API
  if (!isInvalid) {
    const currentModal = $(this).parents(".modal");
    $.ajax({
      url: contextPath + "user/me/password",
      method: "POST",
      data: $(this).serialize(),
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      beforeSend: beforeSend,
      complete: function () {
        currentModal.modal("hide");
      },
      success: function (data) {
        $("#changePasswordForm").removeClass("was-validated");
        showSuccess(data);
      },
      error: function (data) {
        showError(data);
      },
    });
  }
}

//Function to add a skill
function addSkillProfile(e) {
  e.preventDefault();

  //Retrieve info from form
  const skillID = $("#addSkillProfile").val();
  const level = $("#addSkillProfileLevel").val();

  //Ajax call to rest API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "user/me/skill/" + skillID + "?" + $.param({ level: level }),
    method: "POST",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      showSuccess(data);
      getProfile(false);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to delete a skill
function deleteSkillProfile(e) {
  e.preventDefault();
  //Retrieve skillID
  const skillID = $("#deleteSkillProfile").val();

  //Ajax call to REST API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "user/me/skill/" + skillID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      showSuccess(data);
      getProfile(false);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to add a topic
function addTopicProfile(e) {
  e.preventDefault();
  //Retrieve topicID
  const topicID = $("#addTopicProfile").val();

  //Ajax call to REST API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "user/me/topic/" + topicID,
    method: "POST",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      showSuccess(data);
      getProfile(false);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to delete topic
function deleteTopicProfile(e) {
  e.preventDefault();
  //Retrieve topicID
  const topicID = $("#deleteTopicProfile").val();

  //Ajax call to REST API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "user/me/topic/" + topicID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      showSuccess(data);
      getProfile(false);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to change profile picture
function changeProfilePicture() {
  //Retrieve files
  var formData = new FormData();
  const files = $(this)[0].files;

  // Check file selected or not
  if (files.length > 0) {
    formData.append("file", files[0]);

    //Ajax call to REST API
    $.ajax({
      url: contextPath + "user/me/image",
      type: "PUT",
      data: formData,
      contentType: false,
      processData: false,
      beforeSend: beforeSend,
      success: function (data) {
        $("#profilePictureImage img").attr("src", $("#profilePictureImage img").attr("src") + "?t=1");
        showSuccess(data);
      },
      error: function (data) {
        showError(data);
      },
    });
  }
}

//Links all the form to their correspective functions
$("#editProfileForm").submit(editProfile);
$("#changePasswordForm").submit(changePassword);
$("#addSkillForm").submit(addSkillProfile);
$("#deleteSkillForm").submit(deleteSkillProfile);
$("#addTopicForm").submit(addTopicProfile);
$("#deleteTopicForm").submit(deleteTopicProfile);

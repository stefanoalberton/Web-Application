//Function to display the page
function getModeratorPage() {
  //If role is administrator show also card for managed moderators
  if (loggedUser.role == "Common User") {
    location.href = baseUri + "feed";
  } else {
    if (loggedUser.role == "Administrator") {
      getModerators();
    } else {
      $("#moderatorsSection").remove();
    }
    //Create all the cards for the mdoerators
    getBannedUsers();
    getSkills();
    getTopics();
  }

  //Link forms to functions
  $("#addModeratorForm").submit(addModerator);
  $("#deleteModeratorForm").submit(deleteModerator);
  $("#banUserForm").submit(banUser);
  $("#unbanUserForm").submit(unbanUser);
  $("#addSkillForm").submit(addSkill);
  $("#editSkillForm").submit(editSkill);
  $("#deleteSkillForm").submit(deleteSkill);
  $("#addTopicForm").submit(addTopic);
  $("#editTopicForm").submit(editTopic);
  $("#deleteTopicForm").submit(deleteTopic);
}

//Function to get a list of all the moderators
function getModerators() {
  const spinner = loadingSpinner.clone();
  $("#moderatorsSection").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#moderatorsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#moderatorsSection table").removeClass("d-none");

      data = data.data;

      //Create and fill Moderator Table
      $("#moderatorTable").empty();
      data.forEach((moderator) => {
        var moderatorRowEl = createModeratorUserRow(moderator);
        $("#moderatorTable").append(moderatorRowEl);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Get a list of all the banned users
function getBannedUsers() {
  const spinner = loadingSpinner.clone();
  $("#bannedUsersSection").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/ban",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#bannedUsersSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#bannedUsersSection table").removeClass("d-none");

      data = data.data;

      //Create and fill the table with banned users
      $("#bannedTable").empty();
      data.forEach((user) => {
        var userRowEl = createBannedUserRow(user);

        $("#bannedTable").append(userRowEl);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to get a list of all the avaible skills
function getSkills() {
  const spinner = loadingSpinner.clone();
  $("#skillsSection").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "skill",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#skillsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#skillsSection table").removeClass("d-none");

      data = data.data;

      //Create and fill the table with all the skills
      $("#skillsTable").empty();
      data.forEach((skill) => {
        var skillRowEl = createSkillRow(skill);

        $("#skillsTable").append(skillRowEl);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to get a list of all the avaible topics
function getTopics() {
  const spinner = loadingSpinner.clone();
  $("#topicsSection").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "topic",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#topicsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#topicsSection table").removeClass("d-none");

      data = data.data;

      //Create and fill the table with all the topics
      $("#topicsTable").empty();
      data.forEach((topic) => {
        var topicRowEl = createTopicRow(topic);

        $("#topicsTable").append(topicRowEl);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to promote a user to moderator
function addModerator(e) {
  e.preventDefault();

  //Retrieve username of the user
  const username = $(this).find("#username").val();

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "admin/moderator/" + username,
    method: "POST",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#addModeratorModal").modal("hide");
      showSuccess(data);
      getModerators();
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to downgrade a moderator
function deleteModerator(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  const userID = $(this).data("id");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "admin/moderator/" + userID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#deleteModeratorModal").modal("hide");
      showSuccess(data);
      getModerators();
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to ban a user
function banUser(e) {
  e.preventDefault();

  //Retrieve the username of the user that has to be banned
  const username = $(this).find("#username").val();

  //Ajac call to REST API
  $.ajax({
    url: contextPath + "moderator/ban/" + username,
    method: "PUT",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#banUserModal").modal("hide");
      showSuccess(data);
      getBannedUsers();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to readmite a banned user
function unbanUser(e) {
  e.preventDefault();

  //Retrieve the clicked userID
  const userID = $(this).data("id");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/unban/" + userID,
    method: "PUT",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#unbanUserModal").modal("hide");
      showSuccess(data);
      getBannedUsers();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to add a new skill
function addSkill(e) {
  e.preventDefault();

  //Retrieve info from the form
  const skillName = $(this).find("#name").val();
  const description = $(this).find("#description").val();

  //Prepare JSON Object
  const dataObject = {
    name: skillName,
    description: description,
  };

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/skill",
    method: "POST",
    data: JSON.stringify(dataObject),
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#addSkillModal").modal("hide");
      showSuccess(data);
      getSkills();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to edit a skill
function editSkill(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  const skillID = $(this).data("id");

  //Retireve info from form
  const skillName = $(this).find("#name").val();
  const description = $(this).find("#description").val();

  //prepare JSON object
  const dataObject = {
    name: skillName,
    description: description,
  };

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/skill/" + skillID,
    method: "PUT",
    data: JSON.stringify(dataObject),
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#editSkillModal").modal("hide");
      showSuccess(data);
      getSkills();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to delete a skill
function deleteSkill(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  const skillID = $(this).data("id");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/skill/" + skillID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#deleteSkillModal").modal("hide");
      showSuccess(data);
      getSkills();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to add topic to the list of avaible topics
function addTopic(e) {
  e.preventDefault();
  //Retrieve data from form
  const topicName = $(this).find("#name").val();
  const description = $(this).find("#description").val();

  //Prepare JSON Object
  const dataObject = {
    name: topicName,
    description: description,
  };

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/topic",
    method: "POST",
    data: JSON.stringify(dataObject),
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#addTopicModal").modal("hide");
      showSuccess(data);
      getTopics();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to edit a topics
function editTopic(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  const topicID = $(this).data("id");

  //Retrieve info from the form
  const topicName = $(this).find("#name").val();
  const description = $(this).find("#description").val();

  //Prepare JSON Object
  const dataObject = {
    name: topicName,
    description: description,
  };

  //AJax call to REST API
  $.ajax({
    url: contextPath + "moderator/topic/" + topicID,
    method: "PUT",
    data: JSON.stringify(dataObject),
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#editTopicModal").modal("hide");
      showSuccess(data);
      getTopics();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Function to delete a topic
function deleteTopic(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  const topicID = $(this).data("id");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "moderator/topic/" + topicID,
    method: "DELETE",
    contentType: "application/json; charset=UTF-8",
    beforeSend: beforeSend,
    success: function (data) {
      $("#deleteTopicModal").modal("hide");
      showSuccess(data);
      getTopics();
    },
    error: function (data) {
      showError(data);
    },
    statusCode: {
      304: function () {
        createToast("No changes.", "warning");
      },
    },
  });
}

//Create the page
getModeratorPage();

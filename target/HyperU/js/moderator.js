var loggedUser = parseJwt(sessionStorage.getItem("jwt"));

function getModeratorPage() {
  if (loggedUser.role == "Common User") {
    location.href = baseUri + "feed";
  } else {
    if (loggedUser.role == "Administrator") {
      getModerators();
    } else {
      $("#moderatorsSection").remove();
    }
    getBannedUsers();
    getSkills();
    getTopics();
  }

  //Link forms
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

function getModerators() {
  var spinner = loadingSpinner.clone();
  $("#moderatorsSection").append(spinner);

  $.ajax({
    url: contextPath + "moderator",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#moderatorsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#moderatorsSection table").removeClass("d-none");

      var data = data.data;

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

function getBannedUsers() {
  var spinner = loadingSpinner.clone();
  $("#bannedUsersSection").append(spinner);

  $.ajax({
    url: contextPath + "moderator/ban",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#bannedUsersSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#bannedUsersSection table").removeClass("d-none");

      var data = data.data;

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

function getSkills() {
  var spinner = loadingSpinner.clone();
  $("#skillsSection").append(spinner);

  $.ajax({
    url: contextPath + "skill",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#skillsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#skillsSection table").removeClass("d-none");

      var data = data.data;

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

function getTopics() {
  var spinner = loadingSpinner.clone();
  $("#topicsSection").append(spinner);

  $.ajax({
    url: contextPath + "topic",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#topicsSection #loadingSpinner").remove();
    },
    success: function (data) {
      $("#topicsSection table").removeClass("d-none");

      var data = data.data;

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

function addModerator(e) {
  e.preventDefault();

  username = $(this).find("#username").val();

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

function deleteModerator(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  userID = $(this).data("id");

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

function banUser(e) {
  e.preventDefault();

  username = $(this).find("#username").val();

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

function unbanUser(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  userID = $(this).data("id");

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

function addSkill(e) {
  e.preventDefault();

  skillName = $(this).find("#name").val();
  description = $(this).find("#description").val();

  dataObject = {
    name: skillName,
    description: description,
  };

  $.ajax({
    url: contextPath + "moderator/skill",
    method: "POST",
    dataType: "json",
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

function editSkill(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  skillID = $(this).data("id");

  skillName = $(this).find("#name").val();
  description = $(this).find("#description").val();

  dataObject = {
    name: skillName,
    description: description,
  };

  $.ajax({
    url: contextPath + "moderator/skill/" + skillID,
    method: "PUT",
    dataType: "json",
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

function deleteSkill(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  skillID = $(this).data("id");

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

function addTopic(e) {
  e.preventDefault();
  topicName = $(this).find("#name").val();
  description = $(this).find("#description").val();

  dataObject = {
    name: topicName,
    description: description,
  };

  $.ajax({
    url: contextPath + "moderator/topic",
    method: "POST",
    dataType: "json",
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

function editTopic(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  topicID = $(this).data("id");

  topicName = $(this).find("#name").val();
  description = $(this).find("#description").val();

  dataObject = {
    name: topicName,
    description: description,
  };

  $.ajax({
    url: contextPath + "moderator/topic/" + topicID,
    method: "PUT",
    dataType: "json",
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

function deleteTopic(e) {
  e.preventDefault();

  //Retrieve the clicked ID
  topicID = $(this).data("id");

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

getModeratorPage();

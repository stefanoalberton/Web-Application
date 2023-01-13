var imgToUpload = null;

//Function to add the image to an Idea
function insertIdeaImage(ideaID, ideaData) {
  //Retrieve files
  const formData = new FormData();

  // Check file selected or not
  if (imgToUpload != null) {
    formData.append("file", imgToUpload);

    //Ajax call to REST API
    $.ajax({
      url: contextPath + "idea/" + ideaID + "/image",
      type: "PUT",
      beforeSend: beforeSend,
      data: formData,
      contentType: false,
      processData: false,
      complete: function () {
        $("#createIdeaModal").modal("hide");
      },
      success: function (data) {
        showSuccess(ideaData);
        showSuccess(data);
      },
      error: function () {
        showError(data);
      },
    });
  } else {
    $("#createIdeaModal").modal("hide");
    showSuccess(ideaData);
  }
}

//function to retrieve the list of all avaibile skills
function getCreateIdeaSkills() {
  //AJax call to REST API
  $.ajax({
    url: contextPath + "skill",
    method: "GET",
    beforeSend: beforeSend,
    success: function (data) {
      const skills = data.data;
      var dropdown = $("#postIdeaSkills");
      skills.forEach((skill) => {
        dropdown.append($("<option />").val(skill.id).text(skill.name));
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to retrieve the list of all avaible topics
function getCreateIdeaTopics() {
  $.ajax({
    url: contextPath + "topic",
    method: "GET",
    beforeSend: beforeSend,
    success: function (data) {
      var topics = data.data;
      dropdown = $("#postIdeaTopics");
      topics.forEach((topic) => {
        dropdown.append($("<option />").val(topic.id).text(topic.name));
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

getCreateIdeaSkills();
getCreateIdeaTopics();

$("#fileInputImage").change(function () {
  if (this.files && this.files[0]) {
    var reader = new FileReader();
    reader.onload = imageIsLoaded;
    reader.readAsDataURL(this.files[0]);
    imgToUpload = this.files[0];
  }
});

function imageIsLoaded(e) {
  const picture = '<img src="' + e.target.result + '" class="idea-image rounded w-100" alt="Upload Image" />';
  $("#ideaImage").empty().append(picture);
}

//Function to do form validation when a new idea is created
$("#postIdeaForm").submit(function (e) {
  e.preventDefault();

  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  //If valid retrieve data from form
  if (!isInvalid) {
    const name = $("#ideaName").val();
    const description = $("#ideaDescription").val();
    var skills = [];
    var topics = [];

    $("#postIdeaSkills")
      .val()
      .forEach(function (skillID) {
        skills.push({ id: skillID });
      });

    $("#postIdeaTopics")
      .val()
      .forEach(function (topicID) {
        topics.push({ id: topicID });
      });

    //Prepare JSON Object
    const ideaObj = {
      title: name,
      description: description,
      skills: skills,
      topics: topics,
    };

    //After validation create the idea
    //Ajax call to REST API
    $.ajax({
      url: contextPath + "idea",
      method: "POST",
      beforeSend: beforeSend,
      data: JSON.stringify(ideaObj),
      contentType: "application/json; charset=UTF-8",
      success: function (data) {
        $("#postIdeaForm").removeClass("was-validated");
        const insertedID = data.data;

        // When idea is created, update the image
        insertIdeaImage(insertedID, data);
      },
      error: function (data) {
        $("#createIdeaModal").modal("hide");
        showError(data);
      },
    });
  }
});

//Function to delete an Idea
$("#deleteIdeaForm").submit(function (e) {
  e.preventDefault();

  //Retrieve ID of the idea
  const ideaID = $(this).data("id");

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "idea/" + ideaID,
    method: "DELETE",
    beforeSend: beforeSend,
    complete: function () {
      $("#deleteIdeaModal").modal("hide");
    },
    success: function (data) {
      $(".card-idea[data-id=" + ideaID + "]").remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
});

function updateIdea(ideaID, ideaObj) {
  $.ajax({
    url: contextPath + "idea/" + ideaID,
    method: "PUT",
    beforeSend: beforeSend,
    data: JSON.stringify(ideaObj),
    contentType: "application/json; charset=UTF-8",
    complete: function () {
      $("#editIdeaModal").modal("hide");
    },
    success: function (data) {
      $("#editIdeaForm").removeClass("was-validated");
      $(".card-idea[data-id=" + ideaID + "]").data("title", ideaObj.title);
      $(".card-idea[data-id=" + ideaID + "]")
        .find(".card-title")
        .text(ideaObj.title);
      $(".card-idea[data-id=" + ideaID + "]").data("description", ideaObj.description);
      $(".card-idea[data-id=" + ideaID + "]")
        .find(".card-description")
        .html(processDescription(ideaObj.description));
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to add a skill
function addSkillIdea(e) {
  e.preventDefault();

  //Retrieve info from form
  const skillID = $("#addSkillIdea").val();
  const skillName = $("#addSkillIdea :selected").text();
  const skillUrl = baseUri + "skill/" + skillID;
  const ideaID = $(this).data("id");
  var ideaCard = $(".card-idea[data-id=" + ideaID + "]");

  //Ajax call to rest API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/skill/" + skillID,
    method: "POST",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      var ideaSkills = ideaCard.data("skills");
      ideaSkills.push({
        id: skillID,
        name: skillName,
      });
      ideaCard.data("skills", ideaSkills);
      ideaCard.find(".idea-skills").append(`<a data-id="${skillID}" class="idea-skill me-1" href="${skillUrl}"><span class="badge rounded-pill bg-secondary">${skillName}</span>`);
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to delete a skill
function deleteSkillIdea(e) {
  e.preventDefault();
  //Retrieve skillID
  const skillID = $("#removeSkillIdea").val();
  const ideaID = $(this).data("id");
  var ideaCard = $(".card-idea[data-id=" + ideaID + "]");

  //Ajax call to REST API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/skill/" + skillID,
    method: "DELETE",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      var ideaSkills = ideaCard.data("skills");
      ideaSkills = ideaSkills.filter(function (skill) {
        return skill.id != skillID;
      });
      ideaCard.data("skills", ideaSkills);
      ideaCard.find(".idea-skills .idea-skill[data-id=" + skillID + "]").remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to add a topic
function addTopicIdea(e) {
  e.preventDefault();

  //Retrieve info from form
  const topicID = $("#addTopicIdea").val();
  const topicName = $("#addTopicIdea :selected").text();
  const topicUrl = baseUri + "topic/" + topicID;
  const hashtagTopic = toHashtag(topicName);
  const ideaID = $(this).data("id");
  var ideaCard = $(".card-idea[data-id=" + ideaID + "]");

  //Ajax call to rest API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/topic/" + topicID,
    method: "POST",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      var ideaTopics = ideaCard.data("topics");
      ideaTopics.push({
        id: topicID,
        name: topicName,
      });
      ideaCard.data("topics", ideaTopics);
      ideaCard.find(".idea-topics").append(`<a data-id="${topicID}" class="idea-topic link-success text-decoration-none me-1" href="${topicUrl}"><strong>#${hashtagTopic}</strong></a>`);
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to delete topic
function deleteTopicIdea(e) {
  e.preventDefault();
  //Retrieve topicID
  const topicID = $("#removeTopicIdea").val();
  const ideaID = $(this).data("id");
  var ideaCard = $(".card-idea[data-id=" + ideaID + "]");

  //Ajax call to REST API
  const currentModal = $(this).parents(".modal");
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/topic/" + topicID,
    method: "DELETE",
    beforeSend: beforeSend,
    complete: function () {
      currentModal.modal("hide");
    },
    success: function (data) {
      var ideaTopics = ideaCard.data("topics");
      ideaTopics = ideaTopics.filter(function (topic) {
        return topic.id != topicID;
      });
      ideaCard.data("topics", ideaTopics);
      ideaCard.find(".idea-topics .idea-topic[data-id=" + topicID + "]").remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

// Autosize elements
autosize($("#ideaDescription"));
autosize($("#ideaEditDescription"));

$("#editIdeaModal").on("shown.bs.modal", function () {
  autosize.update($("#ideaEditDescription"));
});

// Edit idea
$("#editIdeaForm").submit(function (e) {
  e.preventDefault();

  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  //If valid retrieve data from form
  if (!isInvalid) {
    const ideaID = $(this).data("id");
    const name = $("#ideaEditTitle").val();
    const description = $("#ideaEditDescription").val();

    //Prepare JSON Object
    const ideaObj = {
      title: name,
      description: description,
    };

    //After validation create the idea
    updateIdea(ideaID, ideaObj);
  }
});

// Manage skills and topics of idea
$("#addSkillIdeaForm").submit(addSkillIdea);
$("#deleteSkillIdeaForm").submit(deleteSkillIdea);
$("#addTopicIdeaForm").submit(addTopicIdea);
$("#deleteTopicIdeaForm").submit(deleteTopicIdea);

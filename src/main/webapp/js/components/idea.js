//Function to create a card of one idea
function createIdeaCard(idea) {
  //Retrieve all the info that have to been displayed
  const ideaID = idea.id;
  const username = idea.user.username;
  const userImage = contextPath + idea.user.profilePictureUrl;
  const title = idea.title;
  const rawDescription = idea.description;
  const description = processDescription(rawDescription);
  const postedTime = formatDateTime(idea.postedTime);
  const ideaImage = contextPath + idea.imageUrl;
  const numLikes = idea.numLikes;
  const ideaLiked = idea.liked ? "true" : "false";
  const ideaLikedClass = idea.liked ? "liked" : "";

  const profileUrl = baseUri + "u/" + username;

  //Prepare a list of al the topics related to the idea
  var topics = $("<div></div>");
  idea.topics.forEach((topic) => {
    const hashtagTopic = toHashtag(topic.name);
    const topicUrl = baseUri + "topic/" + topic.id;
    topics.append($(`<a data-id="${topic.id}" class="idea-topic link-success text-decoration-none me-1" href="${topicUrl}"><strong>#${hashtagTopic}</strong></a>`));
  });

  //Prepare a list of al the skills necessary for the idea
  var skills = $("<div></div>");
  idea.skills.forEach((skill) => {
    const skillName = skill.name;
    const skillUrl = baseUri + "skill/" + skill.id;
    skills.append($(`<a data-id="${skill.id}" class="idea-skill me-1" href="${skillUrl}"><span class="badge rounded-pill bg-secondary">${skillName}</span>`));
  });

  // Check if the user is the owner of the idea
  const isOwner = idea.user.id == loggedUser.id;

  //Display edit/delete buttons only if logged user is the creator of the idea
  var editDelButtons = $("<div></div>");
  if (isOwner) {
    editDelButtons.append(`<a class="edit-idea me-2 text-decoration-none" href="#"><i class="feather icon-edit-2"></i></a>
    <a class="delete-idea text-decoration-none" href="#"><i class="feather icon-trash"></i></a>`);
  }

  // Display edit buttons (skill/topics/image) only if it is the prorfile of the logged user
  const manageSkills = isOwner
    ? `<a href="#" class="add-skill me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#addSkillModalIdea"><i class="feather icon-plus"></i></a>
      <a href="#" class="remove-skill me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#deleteSkillModalIdea"><i class="feather icon-trash"></i></a>`
    : "";
  const skillsTitle = isOwner ? `<div class="text-primary mt-3 d-flex align-items-center"><strong class="me-2">Skills</strong>${manageSkills}</div>` : "";

  const manageTopics = isOwner
    ? `<a href="#" class="add-topic me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#addTopicModalIdea"><i class="feather icon-plus"></i></a>
      <a href="#" class="remove-topic me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#deleteTopicModalIdea"><i class="feather icon-trash"></i></a>`
    : "";
  const topicsTitle = isOwner ? `<div class="text-primary mt-3 d-flex align-items-center"><strong class="me-2">Topics</strong>${manageTopics}</div>` : "";

  const imageSelectorClass = isOwner ? "image-selector" : "";
  const imageSelector = isOwner
    ? `<div class="image-hover-editor d-flex align-items-center justify-content-center rounded large-image-selector">
            <i class="h2 m-0 feather icon-edit-2 text-white"></i>
        </div>
        <input type="file" accept="image/jpeg, image/png" class="idea-image-upload d-none" />`
    : "";

  //Define Idea Element
  var ideaElement = $(
    `<div class="card shadow-lg card-idea" data-id="${ideaID}">
      <div class="card-body">
          <div class="col d-flex justify-content-start align-items-center small">
              <img src="${userImage}" alt="${username}" class="me-2 rounded-circle profile-image">
              <a href="${profileUrl}" class="text-dark text-decoration-none"><strong class="card-text">${username}</strong></a>
              <strong class="mx-2 text-muted">•</strong>
              <strong class="card-text text-muted flex-fill">${postedTime}</strong>
              ${editDelButtons.html()}
          </div>
          <div class="mt-3">
              <h3 class="card-title">${title}</h3>
              <div class="card-description text-justified mb-4">${description}</div>

              ${topicsTitle}
              <div class="idea-topics font-monospace h6 my-2 lh-lg">${topics.html()}</div>

              ${skillsTitle}
              <div class="idea-skills h6 my-2 lh-lg">${skills.html()}</div>
          </div>
          <div class="mt-4 ${imageSelectorClass}">
              <img src="${ideaImage}" class="idea-image rounded" alt="${title}">
              ${imageSelector}
          </div>
      </div>

      <div class="card-footer">
        <div class="d-flex align-items-center">
            <div class="flex-fill">
                <a href="#" class="like-idea mx-2 text-dark text-decoration-none d-inline-flex align-items-center" data-liked="${ideaLiked}">
                    <i class="feather icon-heart h4 my-0 me-2 ${ideaLikedClass}"></i>
                    <strong>${numLikes}</strong>
                </a>
                <a href="#" class="comment-idea mx-2 text-dark text-decoration-none">
                    <i class="feather icon-message-circle h4"></i>
                    <span class="d-none">Comments</span>
                </a>
                <a href="${baseUri + "idea/" + ideaID}" class="share-idea mx-2 text-dark text-decoration-none">
                    <i class="feather icon-share-2 h4"></i>
                    <span class="d-none">Share</span>
                </a>
            </div>

            <button class="btn btn-primary join-team" type="button" data-bs-toggle="modal" data-bs-target="#sendRequestModal">
                <i class="feather icon-users me-2"></i>
                <span>Join Team</span>
            </button>
        </div>
        <div class="comments-container my-4 pt-4 border-top d-none">
          <form class="form-send" action="#" method="POST">
            <div class="input-group">
              <input required type="text" class="commentText form-control" placeholder="Write a comment" aria-label="Write a comment" aria-describedby="btn-send-${ideaID}" name="commentText">
              <button class="btn btn-outline-primary" type="submit" id="btn-send-${ideaID}"><i class="h5 feather icon-send"></i></button>
            </div>
          </form>
          <div class="comments d-grid gap-2 mt-4 d-none"></div>
        </div>
      </div>
    </div>`
  );

  // Set title, raw description, skills and description as data
  ideaElement.data("title", title);
  ideaElement.data("description", rawDescription);
  ideaElement.data("skills", idea.skills);
  ideaElement.data("topics", idea.topics);

  //Links function to click events
  ideaElement.find(".like-idea").click(likeImage);

  ideaElement.find(".form-send").submit(sendComment);

  ideaElement.find(".comment-idea").click(openComments);

  ideaElement.find(".share-idea").click(shareIdea);

  ideaElement.find(".delete-idea").click(function (e) {
    e.preventDefault();

    $("#deleteIdeaForm").data("id", $(this).parents(".card-idea").data("id"));
    $("#deleteIdeaModal").modal("show");
  });

  ideaElement.find(".edit-idea").click(function (e) {
    e.preventDefault();

    const ideaCard = $(this).parents(".card-idea");

    $("#editIdeaForm").data("id", ideaCard.data("id"));
    $("#ideaEditTitle").val(ideaCard.data("title"));
    $("#ideaEditDescription").val(ideaCard.data("description"));

    $("#editIdeaModal").modal("show");
  });

  ideaElement.find(".idea-image").click(function () {
    $(this).toggleClass("expanded");
  });

  ideaElement.find(".image-hover-editor").click(function () {
    $(this).parents(".card-idea").find(".idea-image-upload").click();
  });

  ideaElement.find(".idea-image-upload").change(editIdeaImage);

  // Edit topics and skills
  ideaElement.find(".add-skill").click(fillAddSkillModal);
  ideaElement.find(".remove-skill").click(fillRemoveSkillModal);
  ideaElement.find(".add-topic").click(fillAddTopicModal);
  ideaElement.find(".remove-topic").click(fillRemoveTopicModal);

  // Check if team accept new members and display "Send request" button
  const availableTeams = idea.teams.filter((team) => team.acceptRequests);
  const canJoinTeam = idea.user.id != loggedUser.id && availableTeams.length > 0;

  if (canJoinTeam) {
    ideaElement.find(".join-team").data("teams", availableTeams);
    ideaElement.find(".join-team").click(loadTeamsModal);
  } else {
    ideaElement.find(".join-team").remove();
  }

  return ideaElement;
}

// Function to share a link of the idea
function shareIdea(e) {
  e.preventDefault();

  copyToClipboard($(this).attr("href"));
  createToast("Link copied to clipboard", "success");
}

// Function to display the comments under an idea
function openComments(e) {
  e.preventDefault();

  const currIdeaElement = $(this).parents(".card");
  const commentContainer = currIdeaElement.find(".comments-container");

  if (commentContainer.hasClass("d-none")) {
    commentContainer.removeClass("d-none");
    commentContainer.find(".comments").empty();
    loadComments(currIdeaElement);
  } else {
    commentContainer.addClass("d-none");
  }
}

// Function to like an Idea
function likeImage(e) {
  e.preventDefault();

  const likeBtnElement = $(this);
  const currIdeaElement = $(this).parents(".card");
  const ideaID = currIdeaElement.data("id");

  var currIdeaLiked = likeBtnElement.data("liked");

  const method = currIdeaLiked ? "DELETE" : "POST";
  const updaterConstant = currIdeaLiked ? -1 : 1;

  const likeImageIcon = likeBtnElement.find("i");
  var likeCounter = likeBtnElement.find("strong");

  // Ajax call to REST API
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/like",
    method: method,
    beforeSend: beforeSend,
    success: function (data) {
      currIdeaLiked = !currIdeaLiked;

      // Display full heart and update the counter
      likeBtnElement.data("liked", currIdeaLiked);
      likeImageIcon.toggleClass("liked");
      likeCounter.text(parseInt(likeCounter.text()) + updaterConstant);
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

// FUnction to load the comments of an idea
function loadComments(ideaElement, page = 0) {
  var commentContainer = ideaElement.find(".comments");
  commentContainer.removeClass("d-none");

  const spinner = loadingSpinner.clone();
  commentContainer.append(spinner);

  // Ajax call to rest API
  $.ajax({
    url: contextPath + "idea/" + ideaElement.data("id") + "/comment",
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      commentContainer.find("#loadingSpinner").remove();
    },
    success: function (data) {
      var comments = data.data;

      if (comments.length <= 0 && page <= 0) {
        commentContainer.addClass("d-none");
      }

      // Create a comment element for each comment
      comments.forEach((comment) => {
        var commentElement = createComment(comment);
        commentContainer.append(commentElement);
      });

      // Click to display more comments
      loadMoreCommentsElement = $(`<a href="#" data-page="${page}" class="text-decoration-none"><strong>Load more comments</strong></a>`);
      loadMoreCommentsElement.click(function (e) {
        e.preventDefault();
        $(this).remove();
        loadComments(ideaElement, $(this).data("page") + 1);
      });

      if (comments.length > 0) {
        commentContainer.append(loadMoreCommentsElement);
      }
    },
    error: function (data) {
      showError(data);
    },
  });
}

// Function to send a new comment
function sendComment(e) {
  e.preventDefault();

  // Retrieve ideaID
  const ideaElement = $(this).parents(".card");
  const commentContainer = ideaElement.find(".comments");
  const ideaID = ideaElement.data("id");

  // Retrieve commentText
  const text = ideaElement.find("[name=commentText]").val();

  // Prepare JSON Object
  var commentObj = {
    text: text,
    sentTime: new Date(),
    user: {
      id: loggedUser.id,
      username: loggedUser.sub,
      profilePictureUrl: "image/profile/" + loggedUser.id,
    },
  };

  // Ajax CAll to REST API
  $.ajax({
    url: contextPath + "idea/" + ideaID + "/comment",
    method: "POST",
    data: JSON.stringify(commentObj),
    beforeSend: beforeSend,
    success: function (data) {
      commentObj.id = data.data;

      ideaElement.find("[name=commentText]").val("");
      var commentElement = createComment(commentObj);
      commentContainer.removeClass("d-none");
      commentContainer.prepend(commentElement);
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

// Add the teams to the SendRequestModal
function loadTeamsModal(e) {
  e.preventDefault();

  const teams = $(this).data("teams");

  $("#sendRequestTeams").empty();
  teams.forEach((team) => {
    $("#sendRequestTeams").append(`<option value="${team.id}">${team.name}</option>`);
  });
}

// Change the image of the idea
function editIdeaImage() {
  // Retrieve ideaID
  const ideaElement = $(this).parents(".card-idea");
  const ideaID = ideaElement.data("id");

  //Retrieve files
  var formData = new FormData();
  const files = $(this)[0].files;

  // Check file selected or not
  if (files.length > 0) {
    formData.append("file", files[0]);

    // Ajax call to REST API
    $.ajax({
      url: contextPath + "idea/" + ideaID + "/image",
      type: "PUT",
      data: formData,
      contentType: false,
      processData: false,
      beforeSend: beforeSend,
      success: function (data) {
        ideaElement.find(".idea-image").attr("src", ideaElement.find(".idea-image").attr("src") + "?t=1");
        showSuccess(data);
      },
      error: function (data) {
        showError(data);
      },
    });
  }
}

// Fill modal of add skills
function fillAddSkillModal() {
  // Set ideaID to the form
  const ideaCard = $(this).parents(".card-idea");
  const ideaID = ideaCard.data("id");
  $("#addSkillIdeaForm").data("id", ideaID);

  // Retrieve ideaID
  const ideaElement = $(this).parents(".card");
  const ideaSkills = ideaElement.data("skills");

  if (allSkillsAvailable != null) {
    $("#addSkillIdea").empty();
    // Display only the skills not owned by the user
    const usefulSkills = allSkillsAvailable.filter(isNotIn(ideaSkills));

    usefulSkills.forEach((skill) => {
      const skillName = skill.name;
      const skillID = skill.id;
      $("#addSkillIdea").append($(`<option value="${skillID}">${skillName}</option>`));
    });
  } else {
    createToast("Wait until skills are loaded", "warning");
  }
}

// Fill modal of remove skills
function fillRemoveSkillModal() {
  // Set ideaID to the form
  const ideaCard = $(this).parents(".card-idea");
  const ideaID = ideaCard.data("id");
  $("#deleteSkillIdeaForm").data("id", ideaID);
  // Retrieve ideaID
  const ideaElement = $(this).parents(".card");
  const ideaSkills = ideaElement.data("skills");

  $("#removeSkillIdea").empty();
  ideaSkills.forEach((skill) => {
    const skillName = skill.name;
    const skillID = skill.id;
    $("#removeSkillIdea").append($(`<option value="${skillID}">${skillName}</option>`));
  });
}

// Fill modal of add topics
function fillAddTopicModal() {
  // Set ideaID to the form
  const ideaCard = $(this).parents(".card-idea");
  const ideaID = ideaCard.data("id");
  $("#addTopicIdeaForm").data("id", ideaID);
  // Retrieve ideaID
  const ideaElement = $(this).parents(".card");
  const ideaTopics = ideaElement.data("topics");

  if (allTopicsAvailable != null) {
    $("#addTopicIdea").empty();
    // Display only the skills not owned by the user
    const usefulTopics = allTopicsAvailable.filter(isNotIn(ideaTopics));

    usefulTopics.forEach((topic) => {
      const topicName = topic.name;
      const topicID = topic.id;
      $("#addTopicIdea").append($(`<option value="${topicID}">${topicName}</option>`));
    });
  } else {
    createToast("Wait until topics are loaded", "warning");
  }
}

// Fill modal of remove topics
function fillRemoveTopicModal() {
  // Set ideaID to the form
  const ideaCard = $(this).parents(".card-idea");
  const ideaID = ideaCard.data("id");
  $("#deleteTopicIdeaForm").data("id", ideaID);
  // Retrieve ideaID
  const ideaElement = $(this).parents(".card");
  const ideaTopics = ideaElement.data("topics");

  $("#removeTopicIdea").empty();
  ideaTopics.forEach((topic) => {
    const topicName = topic.name;
    const topicID = topic.id;
    $("#removeTopicIdea").append($(`<option value="${topicID}">${topicName}</option>`));
  });
}

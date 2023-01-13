function createIdeaCard(idea) {
  ideaID = idea.id;
  username = idea.user.username;
  userImage = contextPath + idea.user.profilePictureUrl;
  title = idea.title;
  description = idea.description;
  postedTime = formatDateTime(idea.postedTime);
  ideaImage = contextPath + idea.imageUrl;
  numLikes = idea.numLikes;
  ideaLiked = idea.liked ? "true" : "false";
  ideaLikedClass = idea.liked ? "liked" : "";

  profileUrl = baseUri + "u/" + username;

  topics = $("<div></div>");
  idea.topics.forEach((topic) => {
    var hashtagTopic = toHashtag(topic.name);
    var topicUrl = baseUri + "topic/" + topic.id;
    topics.append($(`<a class="link-success text-decoration-none me-1" href="${topicUrl}"><strong>#${hashtagTopic}</strong></a>`));
  });

  skills = $("<div></div>");
  idea.skills.forEach((skill) => {
    var skillName = skill.name;
    var skillUrl = baseUri + "skill/" + skill.id;
    skills.append($(`<a class="me-1" href="${skillUrl}"><span class="badge rounded-pill bg-secondary">${skillName}</span>`));
  });

  ideaElement = $(
    `<div class="card shadow-lg card-idea" data-id="${ideaID}">
      <div class="card-body">
          <div class="col d-flex justify-content-start align-items-center small">
              <img src="${userImage}" alt="${username}" class="me-2 rounded-circle profile-image">
              <a href="${profileUrl}" class="text-dark text-decoration-none"><strong class="card-text">${username}</strong></a>
              <strong class="mx-2 text-muted">•</strong>
              <strong class="card-text text-muted">${postedTime}</strong>
          </div>
          <div class="mt-3">
              <h3 class="card-title">${title}</h3>
              <p>${description}</p>

              <div class="font-monospace h6 my-2 mt-4">${topics.html()}</div>

              <div class="h6 my-2">${skills.html()}</div>
          </div>
          <div class="mt-4">
              <img src="${ideaImage}" class="idea-image rounded" alt="${title}">
          </div>
      </div>

      <div class="card-footer d-flex align-items-center">
          <div class="flex-fill">
              <a href="#" class="like-image mx-2 text-dark text-decoration-none d-inline-flex align-items-center" data-liked="${ideaLiked}">
                  <i class="feather icon-heart h4 my-0 me-2 ${ideaLikedClass}"></i>
                  <strong>${numLikes}</strong>
              </a>
              <a href="#" class="mx-2 text-dark text-decoration-none">
                  <i class="feather icon-message-circle h4"></i>
                  <span class="d-none">Comments</span>
              </a>
          </div>

          <button class="btn btn-primary" type="button">
              <i class="feather icon-users me-2"></i>
              <span>Join Team</span>
          </button>
      </div>
    </div>`
  );

  ideaElement.find(".like-image").click(function (e) {
    e.preventDefault();

    likeBtnElement = $(this);

    var currIdeaLiked = likeBtnElement.data("liked");

    var method = currIdeaLiked ? "DELETE" : "POST";
    var updaterConstant = currIdeaLiked ? -1 : 1;

    var likeImageIcon = likeBtnElement.find("i");
    var likeCounter = likeBtnElement.find("strong");

    $.ajax({
      url: contextPath + "idea/" + ideaID + "/like",
      method: method,
      beforeSend: beforeSend,
      success: function () {
        currIdeaLiked = !currIdeaLiked;

        likeBtnElement.data("liked", currIdeaLiked);
        likeImageIcon.toggleClass("liked");
        likeCounter.text(parseInt(likeCounter.text()) + updaterConstant);
      },
      error: function (data) {},
    });
  });

  ideaElement.find(".idea-image").click(function () {
    $(this).toggleClass("expanded");
  });

  return ideaElement;
}

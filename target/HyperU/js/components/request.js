function createSidebarRequest(request) {
  var team = request.team;
  var idea = team.idea;

  var user = request["user"];

  var username = user.username;
  var userImage = contextPath + user.profilePictureUrl;
  var ideaTitle = idea.title;

  var sidebarElement = $(`<a href="#" class="list-group-item list-group-item-action px-4 py-2 ps-5">
          <div class="d-flex align-items-center">
            <div>
              <span class="d-block">${username} </span>
              <span class="d-block subtitle">for ${ideaTitle}</span>
            </div>
            <img src="${userImage}" class="rounded-circle ms-2" alt="${username}">
          </div>
        </a>`);

  return sidebarElement;
}

function createRequest(request) {
  var team = request.team;
  var idea = team.idea;

  var user = request.user;

  var username = user.username;
  var userImage = contextPath + user.profilePictureUrl;
  var ideaTitle = idea.title;

  var sidebarElement = $(`<a href="#" class="list-group-item list-group-item-action w-100">
          <div class="d-flex align-items-center">
            <img src="${userImage}" class="rounded-circle me-2" alt="${username}">
            <div>
              <span class="d-block">${username} </span>
              <span class="d-block subtitle">for ${ideaTitle}</span>
            </div>
          </div>
        </a>`);

  return sidebarElement;
}

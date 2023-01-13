function createSidebarTeam(team) {
  var teamName = team.name;
  var imageUrl = contextPath + team.imageUrl;
  var teamUrl = baseUri + "team/" + team.id;

  var teamElement = $(`<a href="${teamUrl}" class="list-group-item list-group-item-action px-4 py-2 pe-5">
                            <div class="d-flex align-items-center">
                                <img src="${imageUrl}" class="rounded-circle me-2" alt="${teamName}">
                                <span class="m-0">${teamName}</span>
                            </div>
                        </a>`);

  return teamElement;
}

function createTeam(team) {
  var teamName = team.name;
  var imageUrl = contextPath + team.imageUrl;
  var teamUrl = baseUri + "team/" + team.id;

  var teamElement = $(`<a href="${teamUrl}" class="list-group-item list-group-item-action w-100">
                            <div class="d-flex align-items-center">
                                <img src="${imageUrl}" class="rounded-circle me-2" alt="${teamName}">
                                <span class="m-0">${teamName}</span>
                            </div>
                        </a>`);

  return teamElement;
}

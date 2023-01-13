//Function to create a team element to display in the sidebar
function createSidebarTeam(team) {
  //Retrieve information needed
  const teamName = team.name;
  const imageUrl = contextPath + team.imageUrl;
  const teamUrl = baseUri + "team/" + team.id;

  //Create the element
  var teamElement = $(`<a href="${teamUrl}" class="list-group-item list-group-item-action px-4 py-2 pe-5">
                            <div class="d-flex align-items-center">
                                <img src="${imageUrl}" class="rounded-circle me-2" alt="${teamName}">
                                <span class="m-0">${teamName}</span>
                            </div>
                        </a>`);

  return teamElement;
}

//Function to create a team element to display in mobile version
function createTeam(team) {
  //Retrieve information needed
  const teamName = team.name;
  const imageUrl = contextPath + team.imageUrl;
  const teamUrl = baseUri + "team/" + team.id;

  //Create the element
  var teamElement = $(`<a href="${teamUrl}" class="list-group-item list-group-item-action w-100">
                            <div class="d-flex align-items-center">
                                <img src="${imageUrl}" class="rounded-circle me-2" alt="${teamName}">
                                <span class="m-0">${teamName}</span>
                            </div>
                        </a>`);

  return teamElement;
}

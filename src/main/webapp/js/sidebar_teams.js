//Function to display teams on the sidebar
function loadTeamSidebar() {
  const spinner = loadingSpinner.clone();
  $("#sideTeams").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "user/me/teams",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#sideTeams #loadingSpinner").remove();
    },
    success: function (data) {
      const teams = data.data;

      //Display a message if the user is not member of any team
      if (teams.length <= 0) {
        $("#sideTeams").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-users h1"></i>
                                        <p class="text-center">Seems you aren't<br />in any team</p>
                                    </div>`);
      }

      //Create a team element to display in the sidebar for each teams
      teams.forEach((team) => {
        var teamElement = createSidebarTeam(team);
        $("#sideTeams").append(teamElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadTeamSidebar();

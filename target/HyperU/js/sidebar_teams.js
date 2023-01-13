function loadTeamSidebar() {
  var spinner = loadingSpinner.clone();
  $("#sideTeams").append(spinner);

  $.ajax({
    url: contextPath + "user/me/teams",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#sideTeams #loadingSpinner").remove();
    },
    success: function (data) {
      var teams = data.data;

      if (teams.length <= 0) {
        $("#sideTeams").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-users h1"></i>
                                        <p class="text-center">Seems you aren't<br />in any team</p>
                                    </div>`);
      }

      teams.forEach((team) => {
        teamElement = createSidebarTeam(team);
        $("#sideTeams").append(teamElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadTeamSidebar();

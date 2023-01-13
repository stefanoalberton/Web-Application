function loadTeam() {
  var spinner = loadingSpinner.clone();
  $("#teams").append(spinner);

  $.ajax({
    url: contextPath + "user/me/teams",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#teams #loadingSpinner").remove();
    },
    success: function (data) {
      var teams = data.data;

      if (teams.length <= 0) {
        $("#teams").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-users h1"></i>
                                        <p class="text-center">Seems you aren't<br />in any team</p>
                                    </div>`);
      }

      teams.forEach((team) => {
        teamElement = createTeam(team);
        $("#teams").append(teamElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadTeam();

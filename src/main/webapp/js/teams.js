//Function to display teams in the mobile version
function loadTeam() {
  const spinner = loadingSpinner.clone();
  $("#teams").append(spinner);

  //AJac call to REST API
  $.ajax({
    url: contextPath + "user/me/teams",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#teams #loadingSpinner").remove();
    },
    success: function (data) {
      const teams = data.data;

      //Display a message if user is not a member of any team
      if (teams.length <= 0) {
        $("#teams").append(`<div class="d-flex flex-column align-items-center justify-content-center px-4 my-3">
                                        <i class="feather icon-users h1"></i>
                                        <p class="text-center">Seems you aren't<br />in any team</p>
                                    </div>`);
      }

      //Create a team element to display for each team
      teams.forEach((team) => {
        var teamElement = createTeam(team);
        $("#teams").append(teamElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

loadTeam();

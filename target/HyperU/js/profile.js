var usernamePage = getUrlTokens("u")[0];

function getIdeas(userID) {
  var spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  $.ajax({
    url: contextPath + "profile/" + userID + "/ideas",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      var ideas = data.data;

      if (ideas.length <= 0) {
        $("#ideas").append(`<div class="d-block text-center w-100">
                              <h4 class="mt-5">The user never posted an idea</h4>
                            </div>`);
      }

      ideas.forEach((idea) => {
        ideaElement = createIdeaCard(idea);

        $("#ideas").append(ideaElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

function getProfile() {
  var spinner = loadingSpinner.clone();
  $("#profile").append(spinner);

  $.ajax({
    url: contextPath + "profile/" + usernamePage,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#profile #loadingSpinner").remove();
    },
    success: function (data) {
      var user = data.data;
      var userID = user.id;

      profileElement = createProfileCard(user);
      $("#profile").append(profileElement);

      getIdeas(userID);
    },
    error: function (data) {
      if (data.status != 404) {
        showError(data);
      } else {
        $("#profile").append(`<div class="d-block text-center w-100">
                                <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="No user found">
                                <h2 class="mt-5">No user found</h2>
                                <h4>The user doesn't exists</h4>
                              </div>`);
      }
    },
  });
}

getProfile();

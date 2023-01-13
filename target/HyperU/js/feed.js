function getFeed() {
  var spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  $.ajax({
    url: contextPath + "user/me/feed",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      var ideas = data.data;

      if (ideas.length <= 0) {
        $("#ideas").append(`<div class="d-block text-center w-100">
                              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="No ideas to list">
                              <h2 class="mt-5">No ideas to list</h2>
                              <h4>Try starting adding topics to your profile</h4>
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

getFeed();

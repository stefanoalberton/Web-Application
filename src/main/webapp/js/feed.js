//Define useful variables
var page = 0;
var noMoreIdeas = false;

//Function to get the personalized feed for the logged user
async function getFeed() {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  //Ajax call to REST API
  await $.ajax({
    url: contextPath + "user/me/feed",
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const ideas = data.data;

      //If no ideas are present...
      if (ideas.length <= 0) {
        noMoreIdeas = true;
        if (page <= 0) {
          $("#ideas").append(createAstroError("No ideas to list", "Try starting adding topics to your profile"));
        } else {
          if ($("#noMoreElements").length <= 0 && $(".card-idea").length > 0) {
            $("#ideas").append(`<div id="noMoreElements" class="d-block text-center w-100">
                              <h4>You reached the end 🖖</h4>
                            </div>`);
          }
        }
      }

      //Create card for each idea
      ideas.forEach((idea) => {
        var ideaElement = createIdeaCard(idea);
        $("#ideas").append(ideaElement);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

getFeed();

//Manage the scroll of the feed
$(document).scroll(async function () {
  if (!noMoreIdeas && window.innerHeight + window.scrollY + 10 >= document.body.scrollHeight) {
    page++;
    await getFeed();
  }
});

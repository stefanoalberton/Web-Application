//retrieve the IdeaID from the URL
const ideaID = getUrlTokens("idea")[0];

//FUnction to retrieve info and display them about a specific idea
function getIdea() {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  //ajax call to REST API
  $.ajax({
    url: contextPath + "idea/" + ideaID,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const idea = data.data;

      //Create only one idea card
      ideaElement = createIdeaCard(idea);
      $("#ideas").append(ideaElement);

      $(".comment-idea").click();
    },
    error: function (data) {
      //Display error message
      if (data.status == 404) {
        $("#ideas").append(createAstroError("Idea not exists", "Why are you here???"));
      } else {
        showError(data);
      }
    },
  });
}

getIdea();

//Define useful variables
var page = 0;
var noMoreIdeas = false;

//Function to display all the ideas related to specific topic
async function getTopicIdeas() {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  //Retrieve the topicID from the URL
  const topicID = getUrlTokens("topic")[0];

  //AJax call to REST API
  await $.ajax({
    url: contextPath + "topic/" + topicID,
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const resData = data.data;
      const ideas = resData.ideas;

      //Create an element to display name and description of the topic
      const topic = resData.topic;
      var topicElement = $(`<div class="text-center">
              <h2>${topic.name}</h2>
              <div>${topic.description}</div>
            </div>`);

      if (page <= 0) {
        $("#topic").append(topicElement);
      }

      //Display a message if no ideas are present
      if (ideas.length <= 0) {
        noMoreIdeas = true;
        if (page <= 0) {
          $("#ideas").append(createAstroError("No ideas to list", "Start posting ideas with this topic"));
        } else {
          if ($("#noMoreElements").length <= 0 && $(".card-idea").length > 0) {
            $("#ideas").append(`<div id="noMoreElements" class="d-block text-center w-100">
                              <h4>You reached the end 🖖</h4>
                            </div>`);
          }
        }
      } else {
        //Create the ideas
        ideas.forEach((idea) => {
          var ideaElement = createIdeaCard(idea);

          $("#ideas").append(ideaElement);
        });
      }
    },
    error: function (data) {
      if (data.status != 404) {
        showError(data);
      } else {
        $("#ideas").append(createAstroError("Topic doesn't exists", "Why are you here???"));
      }
    },
  });
}

//Create the page
getTopicIdeas();

//Manage the scrolling of the page
$(document).scroll(async function () {
  if (!noMoreIdeas && window.innerHeight + window.scrollY + 10 >= document.body.scrollHeight) {
    page++;
    await getTopicIdeas();
  }
});

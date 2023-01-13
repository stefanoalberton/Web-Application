function getTopicIdeas() {
  var spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  var topicID = getUrlTokens("topic")[0];
  $.ajax({
    url: contextPath + "topic/" + topicID,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      var resData = data.data;
      var ideas = resData.ideas;

      var topic = resData.topic;
      topicElement = $(`<div class="text-center">
              <h2>${topic.name}</h2>
              <div>${topic.description}</div>
            </div>`);

      $("#topic").append(topicElement);

      if (ideas.length <= 0) {
        $("#ideas").append(`<div class="d-block text-center w-100">
                              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="No ideas to list">
                              <h2 class="mt-5">No ideas to list</h2>
                              <h4>Start posting ideas with this topic</h4>
                            </div>`);
      } else {
        ideas.forEach((idea) => {
          ideaElement = createIdeaCard(idea);

          $("#ideas").append(ideaElement);
        });
      }
    },
    error: function (data) {
      if (data.status != 404) {
        showError(data);
      } else {
        $("#ideas").append(`<div class="d-block text-center w-100">
                              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="Topic not exists">
                              <h2 class="mt-5">Topic doesn't exists</h2>
                              <h4>Why are you here???</h4>
                            </div>`);
      }
    },
  });
}

getTopicIdeas();

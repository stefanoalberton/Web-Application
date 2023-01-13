function getSkillIdeas() {
  var spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  var skillID = getUrlTokens("skill")[0];
  $.ajax({
    url: contextPath + "skill/" + skillID,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      var resData = data.data;
      var ideas = resData.ideas;

      var skill = resData.skill;
      skillElement = $(`<div class="text-center">
              <h2>${skill.name}</h2>
              <div>${skill.description}</div>
            </div>`);

      $("#skill").append(skillElement);

      if (ideas.length <= 0) {
        $("#ideas").append(`<div class="d-block text-center w-100">
                              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="No ideas to list">
                              <h2 class="mt-5">No ideas to list</h2>
                              <h4>Start posting ideas requiring this skill</h4>
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
                              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="Skill not exists">
                              <h2 class="mt-5">Skill doesn't exists</h2>
                              <h4>Why are you here???</h4>
                            </div>`);
      }
    },
  });
}

getSkillIdeas();

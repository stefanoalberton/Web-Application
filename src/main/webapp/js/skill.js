//Define useful variables
var page = 0;
var noMoreIdeas = false;

//Function to display all the ideas related to specific skill
async function getSkillIdeas() {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  //Retrieve skilID from URL
  const skillID = getUrlTokens("skill")[0];

  //Ajax call to REST API
  await $.ajax({
    url: contextPath + "skill/" + skillID,
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const  resData = data.data;
      const  ideas = resData.ideas;

      //Create an element with name and description of the skill
      const skill = resData.skill;
      var skillElement = $(`<div class="text-center">
              <h2>${skill.name}</h2>
              <div>${skill.description}</div>
            </div>`);

      if (page <= 0) {
        $("#skill").append(skillElement);
      }

      //If no ideas are related to that skill
      if (ideas.length <= 0) {
        noMoreIdeas = true;
        if (page <= 0) {
          $("#ideas").append(createAstroError("No ideas to list", "Start posting ideas requiring this skill"));
        } else {
          if ($("#noMoreElements").length <= 0 && $(".card-idea").length > 0) {
            $("#ideas").append(`<div id="noMoreElements" class="d-block text-center w-100">
                              <h4>You reached the end 🖖</h4>
                            </div>`);
          }
        }
      } else {
        //Create all the cards of the ideas
        ideas.forEach((idea) => {
          var ideaElement = createIdeaCard(idea);

          $("#ideas").append(ideaElement);
        });
      }
    },
    error: function (data) {
      //Display Errors
      if (data.status != 404) {
        showError(data);
      } else {
        $("#ideas").append(createAstroError("Skill doesn't exists", "Why are you here???"));
      }
    },
  });
}

//create the page
getSkillIdeas();

//Manage the scroll of the page
$(document).scroll(async function () {
  if (!noMoreIdeas && window.innerHeight + window.scrollY + 10 >= document.body.scrollHeight) {
    page++;
    await getSkillIdeas();
  }
});

//Define userful variables
var usernamePage = getUrlTokens("u")[0];
var isMe = false;

var page = 0;
var noMoreIdeas = false;

var userID;

//check if correspond to logged user
if (location.href.indexOf("/me") >= 0) {
  usernamePage = loggedUser.id;
  isMe = true;
}

//Function to get ideas created by a specific user
async function getIdeas() {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  //Ajax call to REST API
  await $.ajax({
    url: contextPath + "profile/" + userID + "/ideas",
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const ideas = data.data;

      //No ideas to display
      if (ideas.length <= 0) {
        noMoreIdeas = true;
        const noMoreText = page <= 0 ? "No ideas here 😢" : "You reached the end 🖖";
        if ($("#noMoreElements").length <= 0) {
          $("#ideas").append(`<div id="noMoreElements" class="d-block text-center w-100">
                              <h4>${noMoreText}</h4>
                            </div>`);
        }
      }

      //Create a card for each idea
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

//Get info of the user
function getProfile(withIdeas = true) {
  const spinner = loadingSpinner.clone();
  $("#profile").empty();
  $("#profile").append(spinner);

  //Ajax call to rest API
  $.ajax({
    url: contextPath + "profile/" + usernamePage,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#profile #loadingSpinner").remove();
    },
    success: function (data) {
      const user = data.data;
      userID = user.id;

      //Create the profile card
      profileElement = createProfileCard(user, isMe);
      $("#profile").append(profileElement);

      if (withIdeas) getIdeas();

      //IF correspond to logged user, pre-compile the modals
      if (isMe) {
        getProfileModal(user);
        getAddSkillsModal(user);
        getUserSkillModal(user);
        getAddTopicsModal(user);
        getUserTopicsModal(user);

        //manage update of profile picture
        $("#profilePictureImage .image-hover-editor").click(function () {
          $("#profilePictureUpload").click();
        });

        $("#profilePictureUpload").change(changeProfilePicture);

        //Function to logout pressin the logout button
        $("#logoutBtnProfile").click(function (e) {
          e.preventDefault();

          $.ajax({
            url: contextPath + "user/logout",
            method: "GET",
            success: function () {
              sessionStorage.removeItem("jwt");
              deleteCookie("token");
              location.href = baseUri + "login";
            },
          });
        });
      }
    },
    error: function (data) {
      if (data.status != 404) {
        showError(data);
      } else {
        $("#profile").append(createAstroError("User doesn't exists", "Why are you here???"));
      }
    },
  });
}

//Create the page
getProfile();

//manage scrolling of the page
$(document).scroll(async function () {
  if (!noMoreIdeas && window.innerHeight + window.scrollY + 10 >= document.body.scrollHeight) {
    page++;
    await getIdeas();
  }
});

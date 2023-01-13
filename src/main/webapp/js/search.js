function getParameterByName(name, url = window.location.href) {
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return "";
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

var page = 0;
var noMoreIdeas = false;
var noMoreUsers = false;

function getSearchUsers(page = 0) {
  const spinner = loadingSpinner.clone();
  $("#users").append(spinner);

  $.ajax({
    url: contextPath + "search/users",
    data: $.param({ page: page, q: getParameterByName("q") }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#users #loadingSpinner").remove();
    },
    success: function (data) {
      const users = data.data;

      if (users.length <= 0) {
        noMoreUsers = true;
        if (page <= 0) {
          $("#users").append(`<div class="d-block text-center w-100">
                              <h4>No users found 😢</h4>
                            </div>`);
        } else {
          if ($("#noMoreElementsUsers").length <= 0 && $(".card-user").length > 0) {
            $("#users").append(`<div id="noMoreElementsUsers" class="d-block text-center w-100">
                              <h4>You reached the end 🖖</h4>
                            </div>`);
          }
        }
      }

      users.forEach((user) => {
        var userElement = createSearchUserCard(user);
        $("#users").append(userElement);
      });

      $("#loadMoreUsers").remove();
      if (users.length > 0) {
        $("#users").append(`<div class="d-block text-center w-100">
                                <h4><a href="#" id="loadMoreUsers" data-page="${page}" class="text-decoration-none"><strong>Search more users</strong></a></h4>
                            </div>`);
        $("#loadMoreUsers").click(function (e) {
          e.preventDefault();
          getSearchUsers($(this).data("page") + 1);
        });
      }
    },
    error: function (data) {
      showError(data);
    },
  });
}

async function getSearchIdeas(page = 0) {
  const spinner = loadingSpinner.clone();
  $("#ideas").append(spinner);

  await $.ajax({
    url: contextPath + "search/ideas",
    data: $.param({ page: page, q: getParameterByName("q") }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#ideas #loadingSpinner").remove();
    },
    success: function (data) {
      const ideas = data.data;

      if (ideas.length <= 0) {
        noMoreIdeas = true;
        if (page <= 0) {
          $("#ideas").append(`<div class="d-block text-center w-100">
                              <h4>No ideas found 😢</h4>
                            </div>`);
        } else {
          if ($("#noMoreElements").length <= 0 && $(".card-idea").length > 0) {
            $("#ideas").append(`<div id="noMoreElements" class="d-block text-center w-100">
                              <h4>You reached the end 🖖</h4>
                            </div>`);
          }
        }
      }

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

getSearchUsers();
getSearchIdeas();

$(document).scroll(async function () {
  if (!noMoreIdeas && window.innerHeight + window.scrollY + 10 >= document.body.scrollHeight) {
    page++;
    await getSearchIdeas(page);
  }
});

$("[name=q]").val(getParameterByName("q"));

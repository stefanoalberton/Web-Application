var loggedUser = parseJwt(sessionStorage.getItem("jwt"));
var loggedUserID = loggedUser.id;
var currentMessageList = []; //contains the current messages of the chat
var updatedMessageList = []; //contains the updated messages taken of the chat
var teamID = getUrlTokens("team")[0];
var page = 0;
var noMoreMessages = false;

async function getChatContent() {
  var spinner = loadingSpinner.clone();
  $("#chatContent").prepend(spinner);

  await $.ajax({
    url: contextPath + "team/" + teamID + "/message",
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#chatContent #loadingSpinner").remove();
    },
    success: function (data) {
      var messages = data.data;

      if (messages.length > 0) {
        var lastDate = onlyDate(messages[0].sentTime);
        var prevMessage = null;

        messages.forEach((message, i) => {
          prevMessage = i < messages.length - 1 ? messages[i + 1] : null;
          var lastMessage = i > 0 ? messages[i - 1] : null;

          var isContinue = prevMessage != null && prevMessage.user.id == message.user.id && onlyDate(prevMessage.sentTime).getTime() == onlyDate(message.sentTime).getTime();
          var wasContinue = lastMessage != null && lastMessage.user.id == message.user.id && onlyDate(lastMessage.sentTime).getTime() == onlyDate(message.sentTime).getTime();

          var messageElement;
          if (message.user.id == loggedUserID) {
            messageElement = createMessageFromMe(message, page <= 0);
          } else {
            messageElement = createMessageFromOther(message);
          }

          if (isContinue) {
            messageElement.addClass("pt-0");
            messageElement.find(".image-cont .profile-image").remove();
            messageElement.find(".username").remove();
          }

          if (wasContinue) {
            messageElement.addClass("pb-0");
          }

          if (lastDate.getTime() != onlyDate(message.sentTime).getTime()) {
            $("#chatContent").prepend(`<div class="date-badge h5 d-flex flex-row justify-content-center my-2">
                <span class="badge rounded-pill bg-secondary">${formatDate(lastDate)}</span>
                </div>`);
            lastDate = onlyDate(message.sentTime);
          }

          $("#chatContent").prepend(messageElement);
        });

        lastDate = onlyDate(messages[messages.length - 1].sentTime);
        $("#chatContent").prepend(`<div class="h5 d-flex flex-row justify-content-center">
                                    <span class="badge rounded-pill bg-secondary">${formatDate(lastDate)}</span>
                                </div>`);
      } else {
        noMoreMessages = true;
      }

      if (page <= 0) {
        currentMessageList = messages.slice(0);
        updatedMessageList = messages.slice(0);

        $("#chatContent").scrollTop($("#chatContent .msg:last-child").position().top);

        $("#chatContent").scroll(async function () {
          if (!noMoreMessages && $(this).scrollTop() < 10) {
            page++;
            await getChatContent();
          }
        });

        setTimeout(listenForChanges, 1000);
      }
    },
    error: function (data) {
      showError(data);
    },
  });
}

function getChat() {
  var spinner = loadingSpinner.clone();
  $("#chat").append(spinner);

  $.ajax({
    url: contextPath + "team/" + teamID,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#chat #loadingSpinner").remove();
    },
    success: function (data) {
      var team = data.data;

      $("#chat .chat").removeClass("d-none");

      teamName = team.name;
      description = team.idea.description;
      imageUrl = contextPath + team.imageUrl;

      teamElement = $(
        `<div class="d-flex" data-id="${team.id}">
                    <img src="${imageUrl}" class="d-inline-block align-top rounded-circle" width="50" height="50" alt="${teamName}">
                    <div class="idea-description text-light ms-3">
                        <h2 class="card-title"><strong>${teamName}</strong></h2>
                        <a class="fs-6 team-description lh-sm text-white" href="#" data-toggle="modal" data-target="#modalInfoTeam">View info</a>
                    </div>
                </div>`
      );

      teamElement.find("[data-toggle=modal]").click(function (e) {
        e.preventDefault();

        var modal = new bootstrap.Modal($($(this).data("target")).get(0));
        modal.show();
      });

      $("#modalInfoTeam .modal-title").text(teamName);
      $("#modalInfoTeam .modal-body").html(processMessage(description));

      $("#modalInfoTeam .modal-body").append(
        $(`<a class="h6 mt-4 d-flex align-items-center text-decoration-none" href="${baseUri + "idea/" + team.idea.id}">
        <strong><i class="feather icon-external-link me-2"></i>Go to the idea</strong>
      </a>`)
      );

      $("#chatHeader").append(teamElement);

      getChatContent();
    },
    error: function (data) {
      if (data.status != 404) {
        showError(data);
      } else {
        $("#chat").append(`<div class="d-block text-center w-100">
                                <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="No user found">
                                <h2 class="mt-5">No team found</h2>
                                <h4>The team doesn't exists</h4>
                            </div>`);
      }
    },
  });
}

function listenForChanges() {
  $.ajax({
    url: contextPath + "team/" + teamID + "/message",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      setTimeout(listenForChanges, 1000);
    },
    success: function (data) {
      var messages = data.data;

      updatedMessageList = messages.slice(0);

      var changes = findChanges(currentMessageList, updatedMessageList);
      processChanges(changes);

      currentMessageList = updatedMessageList.slice(0);
    },
  });
}

function findChanges(currentMessages, newMessages) {
  function isNotInWithSameContent(otherArray) {
    return function (current) {
      return (
        otherArray.filter(function (other) {
          return other.id == current.id && other.content == current.content;
        }).length == 0
      );
    };
  }

  function isNotIn(otherArray) {
    return function (current) {
      return (
        otherArray.filter(function (other) {
          return other.id == current.id;
        }).length == 0
      );
    };
  }

  function isUpdated(otherArray) {
    return function (current) {
      return (
        otherArray.filter(function (other) {
          return other.id == current.id;
        }).length == 1
      );
    };
  }

  let updated = newMessages.filter(isNotInWithSameContent(currentMessages)).filter(isUpdated(currentMessages.filter(isNotInWithSameContent(newMessages))));
  let inserted = newMessages.filter(isNotIn(currentMessages));
  let deleted = currentMessages.filter(isNotIn(newMessages));

  return {
    update: updated,
    insert: inserted,
    delete: deleted,
  };
}

function processChanges(changes) {
  function processInsert(messages) {
    if (messages.length > 0) {
      var messageElement;
      messages.forEach((message) => {
        var lastMessage = currentMessageList[0];

        if (new Date(message.sentTime).getTime() > new Date(lastMessage.sentTime).getTime()) {
          var isContinue = lastMessage.user.id == message.user.id;
          if (onlyDate(lastMessage.sentTime).getTime() != onlyDate(message.sentTime).getTime()) {
            $("#chatContent").append(`<div class="date-badge h5 d-flex flex-row justify-content-center my-2">
                  <span class="badge rounded-pill bg-secondary">${formatDate(message.sentTime)}</span>
                </div>`);
            isContinue = false;
          }

          if (message.user.id == loggedUserID) {
            messageElement = createMessageFromMe(message);
          } else {
            messageElement = createMessageFromOther(message);
          }

          if (isContinue) {
            messageElement.addClass("pt-0");
            messageElement.find(".image-cont .profile-image").remove();
            messageElement.find(".username").remove();

            $(".msg[data-id=" + lastMessage.id + "]").addClass("pb-0");
          }

          $("#chatContent").append(messageElement);
        }
      });
      $("#chatContent").animate(
        {
          scrollTop: $("#chatContent").prop("scrollHeight"),
        },
        200
      );
    }
  }

  function processUpdate(messages) {
    if (messages.length > 0) {
      messages.forEach((updatedMessage) => {
        updatedMessageID = updatedMessage.id;
        $(".msg[data-id=" + updatedMessageID + "] .message .msg-content").html(processMessage(updatedMessage.content));
      });
    }
  }

  function processDelete(messages) {
    if (messages.length > 0) {
      messages.forEach((deletedMessage) => {
        deletedMessageID = deletedMessage.id;

        $(".msg[data-id=" + deletedMessageID + "]").remove();

        if ($("#chatContent > div").last().hasClass("date-badge")) {
          $("#chatContent > div").last().remove();
        }
      });
    }
  }

  processInsert(changes.insert);
  processUpdate(changes.update);
  processDelete(changes.delete);
}

$("#chatSend").click(function () {
  var chatMessage = $("#chatMessage").val();
  sendMessage(chatMessage);
  $("#chatMessage").val("");
});

$("#chatAttach").click(function () {
  var chatMessage = $("#chatMessage").val();
  alert(chatMessage);
});

$("#chatMessage").keyup(function (e) {
  if (e.keyCode == 13 && !e.shiftKey) {
    var chatMessage = $(this).val();
    sendMessage(chatMessage);
    $(this).val("");

    autosize.update($(this));
  }
});

getChat();

autosize($("#chatMessage"));

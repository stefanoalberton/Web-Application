//Retrievieng useful info and define variables
const loggedUserID = loggedUser.id;
var currentMessageList = []; //contains the current messages of the chat
var updatedMessageList = []; //contains the updated messages taken of the chat
const teamID = getUrlTokens("team")[0];
var page = 0;
var noMoreMessages = false;
var teamOwner = false;

//Function to get the list of all the messages in the chat
async function getChatContent() {
  const spinner = loadingSpinner.clone();
  $("#chatContent").prepend(spinner);

  //Ajax call to REST API
  await $.ajax({
    url: contextPath + "team/" + teamID + "/message",
    data: $.param({ page: page }),
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#chatContent #loadingSpinner").remove();
    },
    success: function (data) {
      //Retrieve messages from JSON object
      const messages = data.data;

      if (messages.length > 0) {
        var lastDate = onlyDate(messages[0].sentTime);
        var prevMessage = null;

        messages.forEach((message, i) => {
          //Retreive previous and last message
          prevMessage = i < messages.length - 1 ? messages[i + 1] : null;
          var lastMessage = i > 0 ? messages[i - 1] : null;

          //Variables to not display username and profile picture when messages were sent from same user
          var isContinue =
            prevMessage != null &&
            prevMessage != undefined &&
            message != null &&
            message != undefined &&
            prevMessage.user != undefined &&
            message.user != undefined &&
            prevMessage.user.id == message.user.id &&
            onlyDate(prevMessage.sentTime).getTime() == onlyDate(message.sentTime).getTime();
          var wasContinue =
            lastMessage != null &&
            lastMessage != undefined &&
            message != null &&
            message != undefined &&
            prevMessage.user != undefined &&
            message.user != undefined &&
            lastMessage.user.id == message.user.id &&
            onlyDate(lastMessage.sentTime).getTime() == onlyDate(message.sentTime).getTime();

          //Create message element (different style if message was sent from the logged user)
          var messageElement;
          if (message.user.id == loggedUserID) {
            messageElement = createMessageFromMe(message, page <= 0);
          } else {
            messageElement = createMessageFromOther(message);
          }

          //Do not display username and profile picture when messages were sent from same user
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
        $("#chatContent").prepend(`<div class="date-badge h5 d-flex flex-row justify-content-center">
                                    <span class="badge rounded-pill bg-secondary">${formatDate(lastDate)}</span>
                                </div>`);
      } else {
        noMoreMessages = true;
      }

      //Loads older messages when scroll to the high part of the page
      if (page <= 0) {
        currentMessageList = messages.slice(0);
        updatedMessageList = messages.slice(0);

        if ($("#chatContent .msg").length > 0) {
          $("#chatContent").scrollTop($("#chatContent .msg:last-child").position().top);
        }

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
      //Display error messages
      if (data.status != 405) {
        showError(data);
      } else {
        $("#chatHeader").remove();
        $("#chat .chat").remove();
        $("#chat").append(createAstroError("You shoudn't be here", "This page contains secrets 🤫."));
      }
    },
  });
}

//Function to get information about the team that have to be displayed in the chat page
function getChat() {
  const spinner = loadingSpinner.clone();
  $("#chat").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "team/" + teamID,
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#chat #loadingSpinner").remove();
    },
    success: function (data) {
      const team = data.data;

      $("#chat .chat").removeClass("d-none");

      const teamName = team.name;
      const description = team.description;
      const imageUrl = contextPath + team.imageUrl;

      if (team.idea.user.id == loggedUser.id) {
        teamOwner = true;
      }

      ///Add button to edit team name and description (editable only if the user is the owner of the team)
      var teamEdit = "";
      if (teamOwner) {
        teamEdit = `<a class="edit-idea ms-2 text-decoration-none text-white" href="#" data-bs-toggle="modal" data-bs-target="#editTeamModal"><i class="feather icon-edit-2"></i></a>`;
      }

      ///Add image of the team (editable if the user is the owner of the team)
      var teamImage = `<img src="${imageUrl}" class="d-inline-block align-top rounded-circle" width="75" height="75" alt="${teamName}">`;
      if (teamOwner) {
        teamImage = `<div class="me-3">
                        <div id="changeTeamImage" class="image-selector">
                          <img src="${imageUrl}" class="rounded-circle" width="75" height="75" alt="${teamName}">
                          <div class="image-hover-editor d-flex align-items-center justify-content-center">
                            <i class="h4 m-0 feather icon-edit-2 text-white"></i>
                          </div>
                       </div>
                       <input type="file" accept="image/jpeg, image/png" id="teamImageUpload" class="d-none">
                      </div>`;
      }

      //Add name and description of the team
      var teamElement = $(`<div class="d-flex" data-id="${team.id}">
                          ${teamImage}
                          <div class="idea-description text-light ms-3">
                              <h2 class="card-title"><strong>${teamName}</strong></h2>
                              <a class="fs-6 team-description lh-sm text-white" href="#" data-bs-toggle="modal" data-bs-target="#modalInfoTeam">View info</a>
                              ${teamEdit}
                          </div>
                      </div>`);

      //Pre-compiled modals
      $("#modalInfoTeam .modal-title").text(teamName);
      $("#modalInfoTeam .modal-body .description").html(processDescription(description));
      $("#modalInfoTeam .modal-body").append(`<div><h6 class="text-primary mt-4"><strong>Members</strong></h6><div id="teamMembers"></div></div>`);
      getMembers();

      $("#modalInfoTeam .modal-body").append(
        $(`<a class="h6 mt-4 d-flex align-items-center text-decoration-none" href="${baseUri + "idea/" + team.idea.id}">
        <strong><i class="feather icon-external-link me-2"></i>Go to the idea</strong>
      </a>`)
      );

      $("#inputTeamName").val(teamName);
      $("#inputTeamDescription").val(description);
      $("#acceptRequests").prop("checked", team.acceptRequests);

      $("#chatHeader").append(teamElement);

      $("#changeTeamImage").click(function () {
        $("#teamImageUpload").click();
      });

      $("#teamImageUpload").change(changeTeamImage);

      getChatContent();
    },
    error: function (data) {
      //Display error message
      if (data.status != 404) {
        showError(data);
      } else {
        $("#chat").append(createAstroError("Team doesn't exists", "Why are you here???"));
      }
    },
  });
}

//Function to get the list of the members of a team
function getMembers() {
  $("#teamMembers").empty();

  const spinner = loadingSpinner.clone();
  $("#teamMembers").append(spinner);

  //Ajax call to REST API
  $.ajax({
    url: contextPath + "team/" + teamID + "/member",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      $("#teamMembers #loadingSpinner").remove();
    },
    success: function (data) {
      //Retrieve members from JSON Object
      const members = data.data;

      members.forEach((member) => {
        //Retrieve info about a member
        const memberUsername = member.username;
        const memberProfilePictureUrl = contextPath + member.profilePictureUrl;
        const memberProfileUrl = baseUri + "u/" + memberUsername;

        //Add the possibility to delete members if the logged user is the owner of the team
        const deleteMember = member.id != loggedUser.id && teamOwner ? `<a class="delete text-decoration-none" href="#"><i class="feather icon-trash"></i></a>` : "";

        //Display info about the members of a team
        $("#teamMembers").append(`<div class="member my-3 d-flex justify-content-start align-items-center" data-id="${member.id}">
              <img src="${memberProfilePictureUrl}" class="me-3 rounded-circle d-block profile-image" alt="${memberUsername}">
              <a href="${memberProfileUrl}" class="text-dark text-decoration-none flex-fill"><strong class="h5 m-0 username me-1">${memberUsername}</strong></a>
              ${deleteMember}
            </div>`);

        $("#teamMembers .delete").click(removeMember);
      });
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Function to remove a member from a team
function removeMember(e) {
  e.preventDefault();

  //Retrieve ID from the clicked button
  const memberElement = $(this).parents(".member");
  const memberID = memberElement.data("id");

  //Ajax call to Rest API
  $.ajax({
    url: contextPath + "team/" + teamID + "/member/" + memberID,
    method: "DELETE",
    beforeSend: beforeSend,
    success: function (data) {
      $("#teamMembers .member[data-id=" + memberID + "]").remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

//Funcion to listern for new messages in the chat
function listenForChanges() {
  $.ajax({
    url: contextPath + "team/" + teamID + "/message",
    method: "GET",
    beforeSend: beforeSend,
    complete: function () {
      setTimeout(listenForChanges, 1000);
    },
    success: function (data) {
      const messages = data.data;

      updatedMessageList = messages.slice(0);

      var changes = findChanges(currentMessageList, updatedMessageList);
      processChanges(changes);

      currentMessageList = updatedMessageList.slice(0);
    },
  });
}

//Function to check if new messages has arrived
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

  //check if message was updated
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

//Function to process the messages that were edited (same as getChatContent() )
function processChanges(changes) {
  function processInsert(messages) {
    if (messages.length > 0) {
      messages.forEach((message) => {
        var lastMessage = currentMessageList.length > 0 ? currentMessageList[0] : null;

        if (lastMessage == null || new Date(message.sentTime).getTime() > new Date(lastMessage.sentTime).getTime()) {
          var isContinue = lastMessage != null && lastMessage.user.id == message.user.id;
          if (lastMessage == null || onlyDate(lastMessage.sentTime).getTime() != onlyDate(message.sentTime).getTime()) {
            $("#chatContent").append(`<div class="date-badge h5 d-flex flex-row justify-content-center my-2">
                  <span class="badge rounded-pill bg-secondary">${formatDate(message.sentTime)}</span>
                </div>`);
            isContinue = false;
          }

          var messageElement;
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

  //Process updated messages
  function processUpdate(messages) {
    if (messages.length > 0) {
      messages.forEach((updatedMessage) => {
        updatedMessageID = updatedMessage.id;
        $(".msg[data-id=" + updatedMessageID + "] .message .msg-content").html(processMessage(updatedMessage.content));
      });
    }
  }

  //Process deleted messages
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

//Function to send a new message
function sendMessage(chatMessage) {
  if (chatMessage.trim().length > 0) {
    //Ajax call to REST API
    $.ajax({
      url: contextPath + "team/" + teamID + "/message",
      method: "POST",
      beforeSend: beforeSend,
      data: JSON.stringify({
        content: chatMessage,
      }),
      contentType: "application/json; charset=UTF-8",
      error: function (data) {
        showError(data);
      },
      success: function (data) {
        showSuccess(data);
      },
    });
  } else {
    createToast("Text is missing.", "danger");
  }
}

//Function to send file in the chat
function sendFile() {
  var formData = new FormData();
  const files = $(this)[0].files;

  // Check file selected or not
  if (files.length > 0) {
    formData.append("file", files[0]);
    //Ajax call to REST API
    $.ajax({
      url: contextPath + "team/" + teamID + "/message",
      type: "POST",
      beforeSend: beforeSend,
      data: formData,
      contentType: false,
      processData: false,
      success: function (data) {
        showSuccess(data);
      },
      error: function (data) {
        showError(data);
      },
    });
  }
}

//Function to change the image of the team
function changeTeamImage() {
  var formData = new FormData();
  const files = $(this)[0].files;

  // Check file selected or not
  if (files.length > 0) {
    formData.append("file", files[0]);

    //Ajax call to REST API
    $.ajax({
      url: contextPath + "team/" + teamID + "/image",
      type: "PUT",
      beforeSend: beforeSend,
      data: formData,
      contentType: false,
      processData: false,
      success: function (data) {
        $("#changeTeamImage img").attr("src", $("#changeTeamImage img").attr("src") + "?t=1");
        showSuccess(data);
      },
      error: function (data) {
        showError(data);
      },
    });
  }
}

//LInk functions to buttons and icons
$("#chatSend").click(function () {
  const chatMessage = $("#chatMessage").val();
  sendMessage(chatMessage);
  $("#chatMessage").val("");
});

$("#chatAttach").click(function () {
  $("#fileSend").click();
});

$("#fileSend").change(sendFile);

$("#chatMessage").keyup(function (e) {
  if (e.keyCode == 13 && !e.shiftKey) {
    const chatMessage = $(this).val();
    sendMessage(chatMessage);
    $(this).val("");

    autosize.update($(this));
  }
});

//Function for the editing of the information of a team
$("#editTeamForm").submit(function (e) {
  e.preventDefault();

  //Form validation
  const isInvalid = !$(this)[0].checkValidity();

  $(this).addClass("was-validated");

  if (!isInvalid) {
    const teamObject = {
      name: $("#inputTeamName").val(),
      description: $("#inputTeamDescription").val(),
      acceptRequests: $("#acceptRequests").prop("checked"),
    };

    //Ajax call to REST API
    $.ajax({
      url: contextPath + "team/" + teamID,
      method: "PUT",
      beforeSend: beforeSend,
      data: JSON.stringify(teamObject),
      contentType: "application/json; charset=UTF-8",
      complete: function () {
        $("#editTeamModal").modal("hide");
      },
      error: function (data) {
        showError(data);
      },
      success: function (data) {
        showSuccess(data);

        //Update the modals and remove previous validation
        $("#editTeamForm").removeClass("was-validated");

        $(".card-title").text(teamObject.name);
        $("#modalInfoTeam .modal-title").text(teamObject.name);
        $("#modalInfoTeam .modal-body .description").html(processDescription(teamObject.description));
      },
    });
  }
});

//Create the chat
getChat();

//Autosize various elements of the page
autosize($("#chatMessage"));
autosize($("#inputTeamDescription"));

$("#editTeamModal").on("shown.bs.modal", function () {
  autosize.update($("#inputTeamDescription"));
});

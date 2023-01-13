function createMessageFromOther(message) {
  messageID = message.id;
  username = message.user.username;
  userImage = contextPath + message.user.profilePictureUrl;
  content = processMessage(message.content);
  postedTime = formatTime(message.sentTime);

  messageElement = $(
    `<div class="d-flex flex-row justify-content-start px-2 py-1 msg" data-id="${messageID}">
        <div class="image-cont profile-image me-3">
            <img src="${userImage}" class="rounded-circle profile-image" alt="${username}">
        </div>
        <div class="bg-msg-other p-3 rounded-2 w-75 message">
            <div class="d-flex msg-header">
                <h6 class="username"><strong>${username}</strong></h6>
            </div>
            
            <p class="msg-content">${content}</p>
            <div class="msg-footer d-flex align-items-center">
              <small class="d-flex justify-content-end mt-2 text-muted"><strong>${postedTime}</strong></small>
            </div>
        </div>
    </div>`
  );

  return messageElement;
}

function createMessageFromMe(message, canEditOrDelete = true) {
  messageID = message.id;
  username = message.user.username;
  userImage = contextPath + message.user.profilePictureUrl;
  content = processMessage(message.content);
  postedTime = formatTime(message.sentTime);

  editControls = `<i class="feather icon-edit-2 ms-auto h6 m-0 btn-edit"></i><i class="feather icon-trash ms-2 h6 m-0 text-primary btn-delete"></i>`;

  messageElement = $(
    `<div class="d-flex flex-row justify-content-end px-2 py-1 msg" data-id="${messageID}">
        <div class="bg-light p-3 rounded-2 w-75 message">
            <div class="d-flex msg-header d-none">
                <h6 class="username"><strong>${username}</strong></h6>
            </div>
            
            <p class="msg-content">${content}</p>

            <div class="msg-footer d-flex align-items-center mt-2">
              <small class="d-flex justify-content-start text-muted"><strong>${postedTime}</strong></small>
              ${canEditOrDelete ? editControls : ""}
            </div>
        </div>
        <!--<div class="ms-3 image-cont profile-image">
            <img src="${userImage}" class="rounded-circle profile-image" alt="${username}">
        </div>-->
    </div>`
  );

  messageElement.find(".btn-delete").click(function () {
    var messageID = $(this).parents(".msg").data("id");
    deleteMessage(messageID);
  });

  messageElement.find(".btn-edit").click(function () {
    var messageParent = $(this).parents(".msg");
    var messageID = messageParent.data("id");

    messageParent.find(".msg-footer > i").hide();

    var messageContent = messageParent.find(".message .msg-content").html();
    var editMessageElement = $(`<div class="edit">
                                  <textarea class="form-control mt-2 mb-2 rounded-2 msg-content-edit" aria-label="New message" rows="2">${deprocessMessage(messageContent)}</textarea>
                                  <div class="d-flex justify-content-end">
                                      <button type="button" class="btn btn-sm btn-primary btn-edit-save"><i class="feather icon-save me-2"></i>Save</button>
                                  </div>
                                </div>`);

    editMessageElement.find(".btn-edit-save").click(function () {
      var chatMessage = $(this).parents(".edit").find(".msg-content-edit").val();
      updateMessage(messageID, chatMessage, $(this));
    });

    editMessageElement.find(".msg-content-edit").keyup(function (e) {
      if (e.keyCode == 13 && !e.shiftKey) {
        var chatMessage = $(this).val();
        updateMessage(messageID, chatMessage, $(this));
      }
    });

    editMessageElement.insertAfter(messageParent.find(".msg-header"));

    autosize(messageParent.find(".edit .msg-content-edit"));
    autosize.update(messageParent.find(".edit .msg-content-edit"));

    messageParent.find(".message .msg-content").hide();
  });

  return messageElement;
}

function updateMessage(messageID, chatMessage, messageElement) {
  if (chatMessage.trim().length > 0) {
    $.ajax({
      url: contextPath + "message/" + messageID,
      method: "PUT",
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

        messageElement.parents(".msg").find(".msg-footer > i").show();
        messageElement.parents(".msg").find(".message .msg-content").html(processMessage(chatMessage));
        messageElement.parents(".msg").find(".message .msg-content").show();
        messageElement.parents(".edit").remove();
      },
    });
  } else {
    createToast("Text is missing.", "danger");
  }
}

function deleteMessage(messageID) {
  $.ajax({
    url: contextPath + "message/" + messageID,
    method: "DELETE",
    beforeSend: beforeSend,
    contentType: "application/json; charset=UTF-8",
    error: function (data) {
      showError(data);
    },
    success: function (data) {
      showSuccess(data);
    },
  });
}

function sendMessage(chatMessage) {
  if (chatMessage.trim().length > 0) {
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

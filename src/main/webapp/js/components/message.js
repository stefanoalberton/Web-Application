//function to display size of a file
function bytesToSize(bytes) {
  const sizes = ["Bytes", "KB", "MB", "GB", "TB"];
  if (bytes == 0) return "0 Byte";
  const i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
  return Math.round(bytes / Math.pow(1024, i), 2) + " " + sizes[i];
}

//Create message element for message sent from other people
function createMessageFromOther(message) {
  //Retrieve all the info that have to be displayed
  const messageID = message.id;
  const username = message.user.username;
  const userImage = contextPath + message.user.profilePictureUrl;
  const content = message.content !== undefined ? processMessage(message.content) : "";
  const file = message.fileInfo !== undefined ? message.fileInfo : null;
  const fileUrl = message.fileUrl !== undefined ? contextPath + message.fileUrl : null;
  const postedTime = formatTime(message.sentTime);

  //Check if message contains a file
  const fileContent =
    file != null
      ? `<div class="d-flex align-items-center">
            <a href="${fileUrl}" class="text-decoration-none"><i class="h3 feather icon-download me-3"></i></a>
            <div class="d-flex flex-column">
                <strong>${file.fileName}</strong>
                <small class="text-muted"><strong>${bytesToSize(file.fileSize)}</strong></small>
            </div>
        </div>`
      : "";

  //Create message Element
  var messageElement = $(
    `<div class="d-flex flex-row justify-content-start px-2 py-1 msg" data-id="${messageID}">
        <div class="image-cont profile-image me-3">
            <img src="${userImage}" class="rounded-circle profile-image" alt="${username}">
        </div>
        <div class="bg-msg-other p-3 rounded-2 w-75 message">
            <div class="d-flex msg-header">
                <h6 class="username"><strong>${username}</strong></h6>
            </div>
            
            ${fileContent}
            <p class="msg-content">${content}</p>

            <div class="msg-footer d-flex align-items-center">
              <small class="d-flex justify-content-end mt-2 text-muted"><strong>${postedTime}</strong></small>
            </div>
        </div>
    </div>`
  );

  return messageElement;
}

//FUnction to create message sent from the logged user
function createMessageFromMe(message, canEditOrDelete = true) {
  //Retrieve al the information that have to be displayed
  const messageID = message.id;
  const username = message.user.username;
  const userImage = contextPath + message.user.profilePictureUrl;
  const content = message.content !== undefined ? processMessage(message.content) : "";
  const file = message.fileInfo !== undefined ? message.fileInfo : null;
  const fileUrl = message.fileUrl !== undefined ? contextPath + message.fileUrl : null;
  const postedTime = formatTime(message.sentTime);

  //Check if contains a file
  var fileContent =
    file != null
      ? `<div class="d-flex align-items-center">
            <a href="${fileUrl}" class="text-decoration-none"><i class="h3 feather icon-download me-3"></i></a>
            <div class="d-flex flex-column">
                <strong>${file.fileName}</strong>
                <small class="text-muted"><strong>${bytesToSize(file.fileSize)}</strong></small>
            </div>
        </div>`
      : "";

  //DIsplay buttons to edit/delete a message
  var editControls;
  if (file != null) {
    editControls = `<i class="feather icon-trash ms-auto h6 m-0 text-primary btn-delete"></i>`;
  } else {
    editControls = `<i class="feather icon-edit-2 ms-auto h6 m-0 btn-edit"></i><i class="feather icon-trash ms-2 h6 m-0 text-primary btn-delete"></i>`;
  }

  //Create message elemnt
  var messageElement = $(
    `<div class="d-flex flex-row justify-content-end px-2 py-1 msg" data-id="${messageID}">
        <div class="bg-light p-3 rounded-2 w-75 message">
            <div class="d-flex msg-header d-none">
                <h6 class="username"><strong>${username}</strong></h6>
            </div>
            
            ${fileContent}
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

  //Link functions to clicks event
  messageElement.find(".btn-delete").click(function () {
    const messageID = $(this).parents(".msg").data("id");
    deleteMessage(messageID);
  });

  messageElement.find(".btn-edit").click(function () {
    const messageParent = $(this).parents(".msg");
    const messageID = messageParent.data("id");

    messageParent.find(".msg-footer > i").hide();

    //Display an input form for edit the message
    const messageContent = messageParent.find(".message .msg-content").html();
    var editMessageElement = $(`<div class="edit">
                                  <textarea class="form-control mt-2 mb-2 rounded-2 msg-content-edit" aria-label="New message" rows="2">${deprocessMessage(messageContent)}</textarea>
                                  <div class="d-flex justify-content-end">
                                      <button type="button" class="btn btn-sm btn-primary btn-edit-save"><i class="feather icon-save me-2"></i>Save</button>
                                  </div>
                                </div>`);

    //Save the edited message
    editMessageElement.find(".btn-edit-save").click(function () {
      const chatMessage = $(this).parents(".edit").find(".msg-content-edit").val();
      updateMessage(messageID, chatMessage, $(this));
    });

    editMessageElement.find(".msg-content-edit").keyup(function (e) {
      if (e.keyCode == 13 && !e.shiftKey) {
        const chatMessage = $(this).val();
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

//Function to update a message
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

//Function to delete a message
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

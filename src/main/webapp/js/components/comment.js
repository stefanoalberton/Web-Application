// Create a single comment
function createComment(comment) {
  // Retrieve info of the comment
  const commentID = comment.id;
  const text = comment.text;
  const sentTime = formatDateTime(comment.sentTime);

  // Retrieve info of the user who posted the comment
  const user = comment.user;
  const username = user.username;
  const userID = user.id;
  const userProfilePictureUrl = contextPath + user.profilePictureUrl;
  const profileUrl = baseUri + "u/" + username;

  // Check if current user is owner of the comment
  const loggedUserID = loggedUser.id;
  var buttons = $("<div></div>");

  // If logged user display buttons to edit and delete commment
  if (userID == loggedUserID) {
    buttons.append(`<a class="edit me-2 text-decoration-none" href="#"><i class="feather icon-edit-2"></i></a>
    <a class="delete text-decoration-none" href="#"><i class="feather icon-trash"></i></a>`);
  }

  // Create element for comment
  var commentElement = $(`<div class="my-2 d-flex justify-content-start align-items-center comment" data-id="${commentID}">
        <img src="${userProfilePictureUrl}" class="me-3 rounded-circle d-block profile-image" alt="${username}">
        <div class="w-100">
            <a href="${profileUrl}" class="username me-1 text-dark text-decoration-none"><strong>${username}</strong></a>
            <span class="commentText">${text}</span>
            <div class="text-muted small">
              <strong>${sentTime}</strong>
              <span class="ms-2"> 
                  ${buttons.html()}              
              </span>
            </div>
        </div>
      </div>`);

  // Link function to click events
  // Delete comment
  commentElement.find(".delete").click(deleteComment);
  // Edit comment
  commentElement.find(".edit").click(editComment);

  return commentElement;
}

// Function to delete a comment
function deleteComment(e) {
  e.preventDefault();

  // Retrieve commentID
  const commentElement = $(this).parents(".comment");
  const commentID = commentElement.data("id");

  // Ajax call to REST API
  $.ajax({
    url: contextPath + "comment/" + commentID,
    method: "DELETE",
    beforeSend: beforeSend,
    success: function (data) {
      commentElement.remove();
      showSuccess(data);
    },
    error: function (data) {
      showError(data);
    },
  });
}

// Function to edit a comment
function editComment(e) {
  e.preventDefault();

  // Retrieve commentID
  const commentElement = $(this).parents(".comment");
  const commentID = commentElement.data("id");

  // Retrieve commentText
  const commentText = commentElement.find(".commentText").text();

  commentElement.find(".commentText").hide();

  // Display input field to edit the comment
  var editCommentElement = $(`<form class="form-save" action="#" method="POST">
            <div class="input-group my-2">
                <input required type="text" class="comment form-control" value="${commentText}" placeholder="Edit ccomment" aria-label="Edit comment" aria-describedby="btn-edit-${commentID}" name="comment">
                <button class="btn btn-outline-primary" type="submit" id="btn-edit-${commentID}"><i class="h5 feather icon-save"></i></button>
            </div>
        </form>`);

  editCommentElement.submit(saveComment);
  editCommentElement.insertAfter(commentElement.find(".username"));
}

// Function to save an edited comment
function saveComment(e) {
  e.preventDefault();

  // Retrieve commentID
  const formElement = $(this);
  const commentElement = $(this).parents(".comment");
  const commentID = commentElement.data("id");

  // Retrieve comment text
  const commentText = commentElement.find("[name=comment]").val();

  // Prepare JSON Object
  const newCommentObj = {
    text: commentText,
  };

  // Ajax call to REST API
  $.ajax({
    url: contextPath + "comment/" + commentID,
    method: "PUT",
    data: JSON.stringify(newCommentObj),
    beforeSend: beforeSend,
    success: function (data) {
      formElement.remove();
      commentElement.find(".commentText").text(commentText);
      commentElement.find(".commentText").show();
      showSuccess(data);
    },
    error: function (data) {
      commentElement.find(".commentText").show();
      showError(data);
    },
  });
}

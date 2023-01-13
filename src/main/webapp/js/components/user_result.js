//Function to create a user card to be displayed after a search
function createSearchUserCard(user) {
  //Retrieve the information needed
  const userID = user.id;
  const username = user.username;

  const userImageUrl = contextPath + user.profilePictureUrl;

  //Create the card
  var userElement = $(`<a href="${baseUri + "u/" + username}" class="card shadow-lg card-user text-decoration-none">
                        <div class="card-body p-2">
                            <div class="col d-flex justify-content-start align-items-center">
                                <div id="profilePictureImage" class="me-3">
                                  <img src="${userImageUrl}" alt="${username}" class="rounded-circle" width="50" height="50">
                                </div>
                                <div>
                                    <h4 class="card-title mb-0"><strong>${username}</strong></h4>
                                </div>
                            </div>
                        </div>
                    </a>`);

  return userElement;
}

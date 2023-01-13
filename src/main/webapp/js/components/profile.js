//function to process biography
function processBiography(biography) {
  return marked(biography.trim(), { breaks: true });
}

//Function to create card of the profile of a user
function createProfileCard(user, isMe) {
  //Retrieve info that have to be displayed
  const userID = user.id;
  const profile = user.profile;
  const username = user.username;

  const userImageUrl = contextPath + user.profilePictureUrl;

  const completeName = profile.name + " " + profile.surname;
  const birthDate = formatDateText(profile.birthDate);
  const biography = profile.biography.trim() != "" ? processBiography(profile.biography) : "<span class='text-muted'>No biography</span>";

  //Display a list of the topics followed by a user
  var topics = $("<div><span class='text-muted mt-2'>No interests</span></div>");
  if (profile.topics.length > 0) {
    topics.empty();
    topics.addClass("font-monospace h6 mt-1 lh-lg");
    profile.topics.forEach((topic) => {
      const hashtagTopic = toHashtag(topic.name);
      const topicUrl = baseUri + "topic/" + topic.id;
      topics.append($(`<a class="link-success text-decoration-none me-1" href="${topicUrl}"><strong>#${hashtagTopic}</strong></a>`));
    });
  }

  //Display a list of the skills owned by a user
  var skills = $("<div><span class='text-muted mt-2'>No skills</span></div>");
  if (profile.skills.length > 0) {
    skills.empty();
    skills.addClass("mt-1 h6 lh-lg");
    profile.skills.forEach((skill) => {
      const skillName = skill.name;
      const skillUrl = baseUri + "skill/" + skill.id;
      skills.append(
        $(`<a class="me-1" href="${skillUrl}" data-bs-toggle="tooltip" data-bs-placement="top" title="Level: ${skill.level}"><span class="badge rounded-pill bg-secondary">${skillName}</span>`)
      );
    });
  }

  //Display edit buttons (skill/topics/image/profile) only if it is the prorfile of the logged user
  const editButton = isMe
    ? `<button type="button" class="btn btn-sm btn-outline-primary mt-3" data-bs-toggle="modal" data-bs-target="#profileModal"><i class="feather icon-edit-2 me-2"></i>Edit profile</button>
        <button type="button" class="btn btn-sm btn-outline-primary mt-3" data-bs-toggle="modal" data-bs-target="#passwordModal"><i class="feather icon-lock me-2"></i>Change password</button>
        <button type="button" class="btn btn-sm btn-outline-primary mt-3" id="logoutBtnProfile"><i class="feather icon-log-out me-2"></i>Logout</button>`
    : "";

  const manageSkills = isMe
    ? `<a href="#" class="me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#addSkillModal"><i class="feather icon-plus"></i></a>
      <a href="#" class="me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#deleteSkillModal"><i class="feather icon-trash"></i></a>`
    : "";

  const manageTopics = isMe
    ? `<a href="#" class="me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#addTopicModal"><i class="feather icon-plus"></i></a>
      <a href="#" class="me-1 badge rounded-pill bg-primary text-white text-decoration-none" data-bs-toggle="modal" data-bs-target="#deleteTopicModal"><i class="feather icon-trash"></i></a>`
    : "";

  const imageSelectorClass = isMe ? "image-selector" : "";
  const imageSelector = isMe
    ? `<div class="image-hover-editor d-flex align-items-center justify-content-center">
            <i class="h2 m-0 feather icon-edit-2 text-white"></i>
        </div>
        <input type="file" accept="image/jpeg, image/png" id="profilePictureUpload" class="d-none" />`
    : "";

  const genderString = profile.gender == "MALE" ? "Male" : profile.gender == "FEMALE" ? "Female" : "Not declared";

  //Create profile elemnt
  var profileElement = $(`<div class="card shadow-lg card-profile" data-id="${userID}">
                        <div class="card-body">
                            <!-- Name and image -->
                            <div class="col d-flex justify-content-start align-items-center">
                                <div id="profilePictureImage" class="${imageSelectorClass} me-3">
                                  <img src="${userImageUrl}" alt="${username}" class="rounded-circle" width="100" height="100">
                                  ${imageSelector}
                                </div>

                                <div>
                                    <h2 class="card-title mb-0"><strong>${completeName}</strong></h2>
                                    <div class="text-muted"><strong>@${username}</strong></div>
                                    ${editButton}
                                </div>
                            </div>

                            <!-- Biography, skill and topics-->
                            <div class="mt-3">
                                <div class="text-primary mt-3"><strong>About me</strong></div>

                                <div class="mt-2 text-justified">${biography}</div>

                                <div class="mt-2 col d-flex justify-content-start align-items-center">
                                    <i class="me-2 feather icon-calendar"></i><strong>${birthDate}</strong>
                                </div>

                                <div class="mt-2 col d-flex justify-content-start align-items-center">
                                    <i class="me-2 feather icon-user"></i><strong>${genderString}</strong>
                                </div>
                                
                                <div class="text-primary mt-3 d-flex align-items-center"><strong class="me-2">Skills</strong>${manageSkills}</div>
                                ${skills[0].outerHTML}
                                
                                <div class="text-primary mt-3 d-flex align-items-center"><strong class="me-2">Interests</strong>${manageTopics}</div>
                                ${topics[0].outerHTML}
                            </div>
                        </div>
                    </div>`);

  return profileElement;
}

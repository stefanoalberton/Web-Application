function createProfileCard(user) {
  userID = user.id;
  profile = user.profile;
  username = user.username;

  userImageUrl = contextPath + user.profilePictureUrl;

  completeName = profile.name + " " + profile.surname;
  birthDate = formatDateText(profile.birthDate);
  biography = profile.biography.trim() != "" ? profile.biography : "<span class='text-muted'>No biography</span>";

  topics = $("<div><span class='text-muted'>No interests</span></div>");
  if (profile.topics.length > 0) {
    topics.empty();
    topics.addClass("font-monospace h6");
    profile.topics.forEach((topic) => {
      var hashtagTopic = toHashtag(topic.name);
      var topicUrl = baseUri + "topic/" + topic.id;
      topics.append($(`<a class="link-success text-decoration-none me-1" href="${topicUrl}"><strong>#${hashtagTopic}</strong></a>`));
    });
  }

  skills = $("<div><span class='text-muted'>No skills</span></div>");
  if (profile.skills.length > 0) {
    skills.empty();
    skills.addClass("my-2 h6");
    profile.skills.forEach((skill) => {
      var skillName = skill.name;
      var skillUrl = baseUri + "skill/" + skill.id;
      skills.append($(`<a class="me-1" href="${skillUrl}"><span class="badge rounded-pill bg-secondary">${skillName}</span>`));
    });
  }

  profileElement = $(`<div class="card shadow-lg card-idea" data-id="${userID}">
    <div class="card-body">
        <!-- Nome  e immagine -->
        <div class="col d-flex justify-content-start align-items-center">
            <img src="${userImageUrl}" alt="user" class="me-3 rounded-circle" width="100" height="100">

            <div>
                <h2 class="card-title mb-0"><strong>${completeName}</strong></h2>
                <div class="text-muted"><strong>@${username}</strong></div>
            </div>
        </div>

        <!-- Biography, skill and topics-->
        <div class="mt-3">
            <div class="text-primary mt-2"><strong>About me</strong></div>

            <div class="mt-2 col d-flex justify-content-start align-items-start">
                <i class="me-2 feather icon-calendar"></i>${birthDate}
            </div>

            <div class="mt-2 col d-flex justify-content-start align-items-start">
                <i class="me-2 feather icon-align-left"></i>${biography}
            </div>
            
            <div class="text-primary mt-2"><strong>Skills</strong></div>
            ${skills[0].outerHTML}

            <div class="text-primary mt-2"><strong>Interests</strong></div>
            ${topics[0].outerHTML}
        </div>
    </div>
</div>`);

  return profileElement;
}

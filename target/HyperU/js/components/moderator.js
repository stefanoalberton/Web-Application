function createModeratorUserRow(moderator) {
  var moderatorRowEl = $(`<tr data-id="${moderator.id}">
                            <th scope="row" class="min">
                                <div class="btn-group" role="group" aria-label="Actions">
                                    <a id="delete" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#deleteModeratorModal"><i class="feather icon-trash"></i></a>
                                </div>
                            </th>
                            <td>${moderator.username}</td>
                            <td>${moderator.email}</td>
                        </tr>`);

  moderatorRowEl.find("#delete").click(function () {
    id = $(this).parents("tr").data("id");
    $("#deleteModeratorForm").data("id", id);
  });

  return moderatorRowEl;
}

function createBannedUserRow(user) {
  var userRowEl = $(`<tr data-id="${user.id}">
                        <th scope="row" class="min">
                            <div class="btn-group" role="group" aria-label="Actions">
                                <a id="delete" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#unbanUserModal"><i class="feather icon-trash"></i></a>
                            </div>
                        </th>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                    </tr>`);

  userRowEl.find("#delete").click(function () {
    id = $(this).parents("tr").data("id");
    $("#unbanUserForm").data("id", id);
  });

  return userRowEl;
}

function createSkillRow(skill) {
  var skillRowEl = $(`<tr data-id="${skill.id}">
            <th scope="row" class="min">
                <div class="btn-group" role="group" aria-label="Actions">
                    <a id="delete" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#deleteSkillModal"><i class="feather icon-trash"></i></a>
                    <a id="edit" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#editSkillModal"><i class="feather icon-edit-2"></i></a>
                </div>
            </th>
            <td id="name">${skill.name}</td>
            <td id="description">${skill.description}</td>
          </tr>`);

  //Add the skillID to the delete modal
  skillRowEl.find("#delete").click(function () {
    id = $(this).parents("tr").data("id");
    $("#deleteSkillForm").data("id", id);
  });

  //Add the skillID to the edit modal
  skillRowEl.find("#edit").click(function () {
    id = $(this).parents("tr").data("id");
    $("#editSkillForm").data("id", id);

    //Pre compiled input box
    skillName = $(this).parents("tr").find("#name").text();
    description = $(this).parents("tr").find("#description").text();

    $("#editSkillModal #name").val(skillName);
    $("#editSkillModal #description").val(description);
  });

  return skillRowEl;
}

function createTopicRow(topic) {
  var topicRowEl = $(`<tr data-id="${topic.id}">
            <th scope="row" class="min">
                <div class="btn-group" role="group" aria-label="Actions">
                    <a id="delete" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#deleteTopicModal"><i class="feather icon-trash"></i></a>
                    <a id="edit" href=# class="btn text-primary" data-bs-toggle="modal" data-bs-target="#editTopicModal"><i class="feather icon-edit-2"></i></a>
                </div>
            </th>
            <td id="name">${topic.name}</td>
            <td id="description">${topic.description}</td>
          </tr>`);

  //Add the topicID to the delete modal
  topicRowEl.find("#delete").click(function () {
    id = $(this).parents("tr").data("id");
    $("#deleteTopicForm").data("id", id);
  });

  //Add the topicID to the edit modal
  topicRowEl.find("#edit").click(function () {
    id = $(this).parents("tr").data("id");
    $("#editTopicForm").data("id", id);

    //Pre compiled input box
    topicName = $(this).parents("tr").find("#name").text();
    description = $(this).parents("tr").find("#description").text();

    $("#editTopicModal #name").val(topicName);
    $("#editTopicModal #description").val(description);
  });

  return topicRowEl;
}

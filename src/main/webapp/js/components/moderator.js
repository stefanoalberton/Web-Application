//Function to create rows for the table containing the list of moderators
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

  //link the function to downgrade moderator
  moderatorRowEl.find("#delete").click(function () {
    id = $(this).parents("tr").data("id");
    $("#deleteModeratorForm").data("id", id);
  });

  return moderatorRowEl;
}

//Function to create rows for the table containg the list of banned users
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

  //Link the function to readmit a banned user
  userRowEl.find("#delete").click(function () {
    const id = $(this).parents("tr").data("id");
    $("#unbanUserForm").data("id", id);
  });

  return userRowEl;
}

//Function to create rows for the table containg the list of all skill
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
    const id = $(this).parents("tr").data("id");
    $("#deleteSkillForm").data("id", id);
  });

  //Add the skillID to the edit modal
  skillRowEl.find("#edit").click(function () {
    const id = $(this).parents("tr").data("id");
    $("#editSkillForm").data("id", id);

    //Pre compiled input box
    const skillName = $(this).parents("tr").find("#name").text();
    const description = $(this).parents("tr").find("#description").text();

    $("#editSkillModal #name").val(skillName);
    $("#editSkillModal #description").val(description);
  });

  return skillRowEl;
}

//Function to create rows for the table containg the list of all topics
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
    const id = $(this).parents("tr").data("id");
    $("#deleteTopicForm").data("id", id);
  });

  //Add the topicID to the edit modal
  topicRowEl.find("#edit").click(function () {
    const id = $(this).parents("tr").data("id");
    $("#editTopicForm").data("id", id);

    //Pre compiled input box
    const topicName = $(this).parents("tr").find("#name").text();
    const description = $(this).parents("tr").find("#description").text();

    $("#editTopicModal #name").val(topicName);
    $("#editTopicModal #description").val(description);
  });

  return topicRowEl;
}

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>


            <body>
                <%@include file="./components/navbar.jsp" %>

                    <div class="row page m-0 p-0">
                        <%@include file="./components/sidebar_teams.jsp" %>

                            <div
                                class="content col-sm-12 col-xs-12 col-md-12 col-lg-6 d-flex flex-column align-items-center">

                                <!-- Moderators -->
                                <div class="w-100 db-5" id="moderatorsSection">
                                    <div class="col d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Moderators</h3>

                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                            data-bs-target="#addModeratorModal">
                                            <i class="feather icon-user-plus me-2"></i>Add moderator
                                        </button>
                                    </div>

                                    <div class="mt-3 table-responsive">
                                        <table class="table table-light align-middle d-none">
                                            <thead>
                                                <tr>
                                                    <th scope="col"></th>
                                                    <th scope="col">Username</th>
                                                    <th scope="col">Email</th>
                                                </tr>
                                            </thead>
                                            <tbody id="moderatorTable"></tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- Delete Moderator Modal -->
                                <div class="modal fade" id="deleteModeratorModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteModeratorModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content" id="deleteModeratorModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="deleteModeratorModalLabel">
                                                    Dowgnrade moderator
                                                </h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="deleteModeratorForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <span>Do you really want to downgrade this moderator?</span>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">No</button>
                                                    <button type="submit" class="btn btn-primary">Yes</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Add Moderator Modal -->
                                <div class="modal fade" id="addModeratorModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="addModeratorModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="addModeratorModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="addModeratorModalLabel">
                                                    Promote to moderator
                                                </h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="addModeratorForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="username" class="form-label">Username</label>
                                                        <input type="text" name="username" class="form-control"
                                                            id="username" placeholder="Username" required>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Promote</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Banned users -->
                                <div class="w-100 my-5" id="bannedUsersSection">
                                    <div class="col d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Banned</h3>

                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                            data-bs-target="#banUserModal">
                                            <i class="feather icon-slash me-2"></i>Ban User
                                        </button>
                                    </div>

                                    <div class="mt-3 table-responsive">
                                        <table class="table table-light align-middle d-none">
                                            <thead>
                                                <tr>
                                                    <th scope="col"></th>
                                                    <th scope="col">Username</th>
                                                    <th scope="col">Email</th>
                                                </tr>
                                            </thead>
                                            <tbody id="bannedTable"></tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- Ban user Modal -->
                                <div class="modal fade" id="banUserModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="banUserModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="banUserModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="banUserModalLabel">Ban user</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="banUserForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="username" class="form-label">Username</label>
                                                        <input name="username" type="text" class="form-control"
                                                            id="username" required placeholder="Username">
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Ban</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Unban user Modal -->
                                <div class="modal fade" id="unbanUserModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="unbanUserModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content" id="unbanUserModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="unbanUserModalLabel">Unban user</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="unbanUserForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <span>Do you really want to unban this user?</span>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">No</button>
                                                    <button type="submit" class="btn btn-primary">Yes</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Skills -->
                                <div class="w-100 my-5" id="skillsSection">
                                    <div class="col d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Skills</h3>

                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                            data-bs-target="#addSkillModal">
                                            <i class="feather icon-plus me-2"></i>Add skill
                                        </button>
                                    </div>

                                    <div class="mt-3 table-responsive">
                                        <table class="table table-light align-middle d-none">
                                            <thead>
                                                <tr>
                                                    <th scope="col"></th>
                                                    <th scope="col">Name</th>
                                                    <th scope="col">Description</th>
                                                </tr>
                                            </thead>
                                            <tbody id="skillsTable"></tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- Add Skill Modal -->
                                <div class="modal fade" id="addSkillModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="addSkillModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="addSkillModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="addSkillModalLabel">Add skill</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="addSkillForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="name" class="form-label">Name</label>
                                                        <input type="text" class="form-control" id="name" name="name"
                                                            placeholder="Name" required>
                                                    </div>

                                                    <div class="my-3">
                                                        <label for="description" class="form-label">Description</label>
                                                        <input type="text" class="form-control" id="description"
                                                            name="description" placeholder="Description" required>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Add</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Edit Skill Modal -->
                                <div class="modal fade" id="editSkillModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="editSkillModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="editSkillModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="editSkillModalLabel">Edit skill</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="editSkillForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="name" class="form-label">Name</label>
                                                        <input type="text" class="form-control" id="name" name="name"
                                                            placeholder="Name" required>
                                                    </div>
                                                    <div class="my-3">
                                                        <label for="description" class="form-label">Description</label>
                                                        <input type="text" class="form-control" id="description"
                                                            name="description" placeholder="Description" required>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Save</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Delete Skill Modal -->
                                <div class="modal fade" id="deleteSkillModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteSkillModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content" id="deleteSkillModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="deleteSkillModalLabel">Delete skill</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="deleteSkillForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <span>Do you really want to delete this skill?</span>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">No</button>
                                                    <button type="submit" class="btn btn-primary">Yes</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Topics -->
                                <div class="w-100 mt-5" id="topicsSection">
                                    <div class="col d-flex justify-content-between align-items-center">
                                        <h3 class="card-title">Topics</h3>

                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                            data-bs-target="#addTopicModal">
                                            <i class="feather icon-plus me-2"></i>Add topic
                                        </button>
                                    </div>

                                    <div class="mt-3 table-responsive">
                                        <table class="table table-light align-middle d-none">
                                            <thead>
                                                <tr>
                                                    <th scope="col"></th>
                                                    <th scope="col">Name</th>
                                                    <th scope="col">Description</th>
                                                </tr>
                                            </thead>
                                            <tbody id="topicsTable"></tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- Add Topic Modal -->
                                <div class="modal fade" id="addTopicModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="addTopicModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="addTopicModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="addTopicModalLabel">Add topic</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="addTopicForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="name" class="form-label">Name</label>
                                                        <input type="text" class="form-control" id="name" name="name"
                                                            placeholder="Name" required>
                                                    </div>

                                                    <div class="my-3">
                                                        <label for="description" class="form-label">Description</label>
                                                        <input type="text" class="form-control" id="description"
                                                            name="description" placeholder="Description" required>
                                                    </div>

                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Add</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <!-- Edit Topic Modal -->
                                <div class="modal fade" id="editTopicModal" data-bs-backdrop="static"
                                    data-bs-keyboard="false" tabindex="-1" aria-labelledby="editTopicModalLabel"
                                    aria-hidden="true">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content" id="editTopicModalContent">
                                            <div class="modal-header">
                                                <h4 class="modal-title" id="editTopicModalLabel">Edit topic</h4>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                            </div>

                                            <form id="editTopicForm" action="#" method="POST">
                                                <div class="modal-body">
                                                    <div class="my-3">
                                                        <label for="name" class="form-label">Name</label>
                                                        <input type="text" class="form-control" id="name" name="name"
                                                            placeholder="Name" required>
                                                    </div>

                                                    <div class="my-3">
                                                        <label for="description" class="form-label">Description</label>
                                                        <input type="text" class="form-control" id="description"
                                                            name="description" placeholder="Description" required>
                                                    </div>
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-outline-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Save</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Delete Topic Modal -->
                            <div class="modal fade" id="deleteTopicModal" data-bs-backdrop="static"
                                data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteTopicModalLabel"
                                aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content" id="deleteTopicModalContent">
                                        <div class="modal-header">
                                            <h4 class="modal-title" id="deleteTopicModalLabel">Delete topic</h4>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                        </div>

                                        <form id="deleteTopicForm" action="#" method="POST">
                                            <div class="modal-body">
                                                <div class="my-3">
                                                    <span>Do you really want to delete this topic?</span>
                                                </div>
                                            </div>

                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-outline-secondary"
                                                    data-bs-dismiss="modal">No</button>
                                                <button type="submit" class="btn btn-primary">Yes</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <%@include file="./components/sidebar_requests.jsp" %>
                    </div>

                    <%@include file="./components/footer.jsp" %>

                        <script src="<c:out value="${baseUri}" />/js/components/moderator.js"></script>
                        <script src="<c:out value="${baseUri}" />/js/moderator.js"></script>
            </body>

        </html>
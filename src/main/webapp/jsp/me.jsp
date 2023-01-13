<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <a id="activePage" data-page="user" class="d-none"></a>
                <%@include file="./components/navbar.jsp" %>

                    <div class="row page m-0 p-0">
                        <%@include file="./components/sidebar_teams.jsp" %>

                            <div
                                class="content col-sm-12 col-xs-12 col-md-12 col-lg-6 d-flex flex-column align-items-center">
                                <div class="mb-5 d-flex justify-content-center w-100" id="profile"></div>
                                <div class="mt-5 d-grid gap-5 justify-content-center w-100" id="ideas"></div>
                            </div>

                            <%@include file="./components/sidebar_requests.jsp" %>
                    </div>

                    <!-- Edit Profile Modal -->
                    <div class="modal fade" id="profileModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="profileModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content" id="profileModalContent">
                                <div class="modal-header">
                                    <h4 class="modal-title" id="profileModalLabel">Edit profile</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>

                                <!-- Form to edit profile -->
                                <form id="editProfileForm" action="#" method="PUT" class="needs-validation" novalidate>
                                    <div class="modal-body">
                                        <div class="my-3">
                                            <label for="name" class="form-label">Name</label>
                                            <input name="name" type="text" class="form-control" id="name" required>
                                            <div class="invalid-feedback">
                                                Please enter the name.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="surname" class="form-label">Surname</label>
                                            <input name="surname" type="text" class="form-control" id="surname"
                                                required>
                                            <div class="invalid-feedback">
                                                Please enter the surname.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="username" class="form-label">Username</label>
                                            <input name="username" type="text" class="form-control" id="username"
                                                required pattern="[a-zA-Z0-9._-]+">
                                            <div class="invalid-feedback" id="usernameValidation">
                                                Please enter a valid username.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="email" class="form-label">Email</label>
                                            <input name="email" type="email" class="form-control" id="email" required>
                                            <div class="invalid-feedback">
                                                Please enter the email.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="birthdate" class="form-label">Birthdate</label>
                                            <input name="birthdate" type="date" class="form-control" id="birthdate"
                                                required>
                                            <div class="invalid-feedback">
                                                Please enter the password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="gender" class="form-label">Gender</label>
                                            <select name="gender" id="gender" class="form-select" aria-label="Gender">
                                                <option value="MALE">Male</option>
                                                <option value="FEMALE">Female</option>
                                                <option value="NOT_DECLARED">Not declared</option>
                                            </select>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="biography" class="form-label">Biography</label>
                                            <textarea name="biography" class="form-control" id="biography"></textarea>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
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

                    <!-- Modatl to change password -->
                    <div class="modal fade" id="passwordModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content" id="passwordModalContent">
                                <div class="modal-header">
                                    <h4 class="modal-title" id="passwordModalLabel">Change password</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>

                                <form id="changePasswordForm" action="#" method="PUT" class="needs-validation" novalidate>
                                    <div class="modal-body">
                                        <div class="my-3">
                                            <label for="oldPassword" class="form-label">Old password</label>
                                            <input name="old_password" type="password" class="form-control"
                                                id="oldPassword" required>
                                            <div class="invalid-feedback" id="oldPasswordValidation">
                                                Please the old password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="password" class="form-label">New password</label>
                                            <input name="password" type="password" class="form-control" id="password" required>
                                            <div class="invalid-feedback" id="passwordValidation">
                                                Please enter the password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>

                                        <div class="my-3">
                                            <label for="passwordCheck" class="form-label">Repeat new password</label>
                                            <input name="password_check" type="password" class="form-control"
                                                id="passwordCheck" required>
                                            <div class="invalid-feedback" id="passwordCheckValidation">
                                                Please reenter the password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
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

                    <!-- Add Skill Modal -->
                    <div class="modal fade" id="addSkillModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="addSkillModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content" id="addSkillModalContent">
                                <div class="modal-header">
                                    <h4 class="modal-title" id="addSkillModalLabel">Add skill</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>

                                <form id="addSkillForm" action="#" method="POST">
                                    <div class="modal-body">
                                        <div class="my-3">
                                            <label for="addSkillProfile" class="form-label">Select skill</label>
                                            <select id="addSkillProfile" class="form-select" aria-label="Select skill">
                                            </select>
                                        </div>
                                        <div class="my-3">
                                            <label for="addSkillProfileLevel" class="form-label">Select level</label>
                                            <select id="addSkillProfileLevel" class="form-select" aria-label="Select level">
                                                <option value="1">1</option>
                                                <option value="2">2</option>
                                                <option value="3">3</option>
                                                <option value="4">4</option>
                                                <option value="5">5</option>
                                            </select>
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

                    <!-- Delete Skill Modal -->
                    <div class="modal fade" id="deleteSkillModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="deleteSkillModalLabel" aria-hidden="true">
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
                                            <label for="deleteSkillProfile" class="form-label">Select skill</label>
                                            <select id="deleteSkillProfile" class="form-select" aria-label="Select skill"></select>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-outline-secondary"
                                            data-bs-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-primary">Delete</button>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Add Topics Modal -->
                    <div class="modal fade" id="addTopicModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="addTopicModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content" id="addTopicModalContent">
                                <div class="modal-header">
                                    <h4 class="modal-title" id="addTopicModalLabel">Add topic</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>

                                <form id="addTopicForm" action="#" method="PUT">
                                    <div class="modal-body">
                                        <div class="my-3">
                                            <label for="addTopicProfile" class="form-label">Select topic</label>
                                            <select id="addTopicProfile" class="form-select" aria-label="Select topic"></select>
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

                    <!-- Delete Topic Modal -->
                    <div class="modal fade" id="deleteTopicModal" data-bs-backdrop="static" data-bs-keyboard="false"
                        tabindex="-1" aria-labelledby="deleteTopicModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content" id="deleteTopicModalContent">
                                <div class="modal-header">
                                    <h4 class="modal-title" id="deleteTopicModalLabel">Delete topic</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>

                                <form id="deleteTopicForm" action="#" method="PUT">
                                    <div class="modal-body">
                                        <div class="my-3">
                                            <label for="deleteTopicProfile" class="form-label">Select topic</label>
                                            <select id="deleteTopicProfile" class="form-select" aria-label="Select topic">
                                            </select>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-outline-secondary"
                                            data-bs-dismiss="modal">No</button>
                                        <button type="submit" class="btn btn-primary">Delete</button>
                                    </div>

                                </form>

                            </div>
                        </div>
                    </div>

                    <%@include file="./components/join_team_modal.jsp" %>

                        <%@include file="./components/footer.jsp" %>

                            <script src="<c:out value=" ${baseUri}" />/js/components/comment.js"></script>
                            <script src="<c:out value=" ${baseUri}" />/js/components/idea.js"></script>
                            <script src="<c:out value=" ${baseUri}" />/js/components/profile.js"></script>
                            <script src="<c:out value=" ${baseUri}" />/js/me.js"></script>
                            <script src="<c:out value=" ${baseUri}" />/js/profile.js"></script>
            </body>

        </html>
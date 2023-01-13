<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div id="splashScreen"><img src="<c:out value="${baseUri}" />/media/logos/logo.svg" alt="HyperU" /></div>

        <header>
            <nav class="navbar navbar-dark bg-dark navbar-expand fixed-top mb-5 px-4 d-flex align-items-center row-lg">
                <a class="col-lg-3 navbar-brand d-flex align-items-center me-3" href="<c:out value="${baseUri}" />">
                <img src="<c:out value="${baseUri}" />/img/logo.svg" class="d-inline-block align-top" alt="HyperU">
                <h1 class="mx-2 my-0 d-none d-sm-inline-block"><strong>HyperU</strong></h1>
                </a>

                <div class="col-lg-6 d-flex align-items-center justify-content-center flex-fill">
                    <a class="d-none d-lg-inline-block btn btn-light p-2 shadow"
                    href="<c:out value="${baseUri}" />/feed">
                    <i class="feather icon-home h4 m-0 px-1"></i>
                    <span class="d-none">Home</span>
                    </a>
                    <form class="search-form form-inline mx-0 mx-lg-3 w-50" action="<c:out value="${baseUri}" />/search" method="GET">
                        <div class="has-search">
                            <i class="feather icon-search h4 m-0"></i>
                            <input type="text" name="q" class="form-control py-3 shadow" placeholder="Search">
                        </div>
                    </form>
                    <a class="btn-create-post d-none d-lg-inline-block btn btn-light p-2 shadow" href="#"
                        data-bs-toggle="modal" data-bs-target="#createIdeaModal">
                        <i class="feather icon-edit h4 px-1 m-0"></i>
                        <span class="d-none">New Idea</span>
                    </a>
                </div>

                <div class="col-lg-3 d-none d-lg-flex justify-content-end dropdown">
                    <button class="btn dropdown-toggle d-flex align-items-center" type="button" id="userMenuDropdown"
                        data-bs-toggle="dropdown" aria-expanded="false">
                        <img src="" width="40" height="40" class="me-2 rounded-circle" alt="" id="userImageMenu">
                        <strong class="h5 m-0 mx-2 d-none d-md-inline" id="userMenu"></strong>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end py-3 shadow profile-menu"
                        aria-labelledby="userMenuDropdown">
                        <li>
                            <a href="<c:out value="${baseUri}" />/me" class="dropdown-item">
                            <i class="feather icon-user me-2"></i>
                            <strong>My profile</strong>
                            </a>
                        </li>
                        <li class="d-none" id="manageDropdownItem">
                            <a href="<c:out value="${baseUri}" />/manage" class="dropdown-item">
                            <i class="feather icon-settings me-2"></i>
                            <strong>Manage</strong>
                            </a>
                        </li>
                        <li>
                            <a href="#" id="logoutBtn" class="dropdown-item text-primary">
                                <i class="feather icon-log-out me-2"></i>
                                <strong>Logout</strong>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>

        <div class="fixed-bottom my-3 d-flex d-lg-none align-items-center justify-content-center">
            <nav class="nav bg-white rounded navbar px-3 py-2 border border-dark border-3 shadow">
                <a id="page-home" class="nav-link px-3 d-inline-flex align-items-center" href="<c:out value="
                    ${baseUri}" />/feed">
                <i class="feather icon-home h4 m-0"></i>
                <span class="d-none">Home</span>
                </a>
                <a id="page-teams" class="nav-link px-3 d-inline-flex align-items-center" href="<c:out value="
                    ${baseUri}" />/teams">
                <i class="feather icon-message-square h4 m-0"></i>
                <span class="d-none">Teams</span>
                </a>
                <a class="btn-create-post nav-link px-3 d-inline-flex align-items-center" href="#"
                    data-bs-toggle="modal" data-bs-target="#createIdeaModal">
                    <i class="feather icon-edit h4 m-0"></i>
                    <span class="d-none">New Idea</span>
                </a>
                <a id="page-requests" class="nav-link px-3 d-inline-flex align-items-center" href="<c:out value="
                    ${baseUri}" />/requests">
                <i class="feather icon-bell h4 m-0"></i>
                <span class="d-none">Requests</span>
                </a>
                <a id="page-user" class="nav-link px-3 d-inline-flex align-items-center" href="<c:out value="
                    ${baseUri}" />/me">
                <img src="" width="25" height="25" class="rounded-circle" alt="" id="userImageNavbar">
                <span class="d-none" id="userNavbar"></span>
                </a>
            </nav>
        </div>

        <!-- Create Idea Modal -->
        <div class="modal fade" id="createIdeaModal" tabindex="-1" aria-labelledby="createIdeaModalLabel"
            data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createIdeaModalLabel">Do you have a new idea? Post it!</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="#" method="POST" id="postIdeaForm" class="needs-validation" novalidate>
                        <div class="modal-body">
                            <div>
                                <label for="ideaName" class="form-label">Title</label>
                                <input id="ideaName" class="form-control form-control-lg" type="text" placeholder="Title"
                                    aria-label="Title" required>
                                <div class="invalid-feedback">
                                    Please enter a title.
                                </div>
                                <div class="valid-feedback">
                                    Perfect!
                                </div>
                            </div>

                            <div class="mt-3">
                                <label for="ideaDescription" class="form-label">Description</label>
                                <textarea id="ideaDescription" class="form-control" placeholder="Description"
                                    aria-label="Description" required></textarea>
                                <div class="invalid-feedback">
                                    Please enter a description.
                                </div>
                                <div class="valid-feedback">
                                    Perfect!
                                </div>
                            </div>

                            <div class="mt-3">
                                <label for="postIdeaSkills" class="form-label">Skills</label>
                                <select class="form-select" multiple aria-label="multiple select"
                                    id="postIdeaSkills"></select>
                            </div>

                            <div class="mt-3">
                                <label for="postIdeaTopics"
                                    class="form-label">Topics</label>
                                <select class="form-select" multiple aria-label="multiple select"
                                    id="postIdeaTopics"></select>
                            </div>

                            <div class="mt-3">
                                <label for="fileInputImage" class="form-label">Image (optional)</label>
                                <input class="form-control" type="file" id="fileInputImage" accept="image/png, image/jpeg">
                            </div>
                            <div class="mt-1 pt-1" id="ideaImage"></div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary"
                                data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Post</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Add Skill Modal -->
        <div class="modal fade" id="addSkillModalIdea" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="addSkillModalIdeaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" id="addSkillModalIdeaContent">
                    <div class="modal-header">
                        <h4 class="modal-title" id="addSkillModalIdeaLabel">Add skill</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="addSkillIdeaForm" action="#" method="POST">
                        <div class="modal-body">
                            <div class="my-3">
                                <label for="addSkillIdea" class="form-label">Select skill</label>
                                <select id="addSkillIdea" class="form-select" aria-label="Select skill">
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
        <div class="modal fade" id="deleteSkillModalIdea" data-bs-backdrop="static" data-bs-keyboard="false"
            tabindex="-1" aria-labelledby="deleteSkillModalIdeaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" id="deleteSkillModalIdeaContent">
                    <div class="modal-header">
                        <h4 class="modal-title" id="deleteSkillModalIdeaLabel">Delete skill</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="deleteSkillIdeaForm" action="#" method="POST">
                        <div class="modal-body">
                            <div class="my-3">
                                <label for="removeSkillIdea" class="form-label">Select skill</label>
                                <select id="removeSkillIdea" class="form-select" aria-label="Select skill"></select>
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
        <div class="modal fade" id="addTopicModalIdea" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="addTopicModalIdeaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" id="addTopicModalIdeaContent">
                    <div class="modal-header">
                        <h4 class="modal-title" id="addTopicModalIdeaLabel">Add topic</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="addTopicIdeaForm" action="#" method="PUT">
                        <div class="modal-body">
                            <div class="my-3">
                                <label for="addTopicIdea" class="form-label">Select topic</label>
                                <select id="addTopicIdea" class="form-select" aria-label="Select topic"></select>
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
        <div class="modal fade" id="deleteTopicModalIdea" data-bs-backdrop="static" data-bs-keyboard="false"
            tabindex="-1" aria-labelledby="deleteTopicModalIdeaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" id="deleteTopicModalIdeaContent">
                    <div class="modal-header">
                        <h4 class="modal-title" id="deleteTopicModalIdeaLabel">Delete topic</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="deleteTopicIdeaForm" action="#" method="PUT">
                        <div class="modal-body">
                            <div class="my-3">
                                <label for="removeTopicIdea" class="form-label">Select topic</label>
                                <select id="removeTopicIdea" class="form-select" aria-label="Select topic"></select>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">No</button>
                            <button type="submit" class="btn btn-primary">Delete</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>

        <!-- Delete Idea Modal -->
        <div class="modal fade" id="deleteIdeaModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="deleteIdeaModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" id="deleteIdeaModalContent">
                    <div class="modal-header">
                        <h4 class="modal-title" id="deleteIdeaModalLabel">Delete idea</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
        
                    <form id="deleteIdeaForm" action="#" method="POST">
                        <div class="modal-body">
                            <div class="my-3">
                                <span>Do you really want to delete this idea?</span>
                            </div>
                        </div>
        
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">No</button>
                            <button type="submit" class="btn btn-primary">Yes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Edit Idea Modal -->
        <div class="modal fade" id="editIdeaModal" tabindex="-1" aria-labelledby="editIdeaModalLabel"
            data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editIdeaModalLabel">Do you have a new idea? Post it!</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="#" method="POST" id="editIdeaForm" class="needs-validation" novalidate>
                        <div class="modal-body">
                            <div>
                                <label for="ideaEditTitle" class="form-label">Title</label>
                                <input id="ideaEditTitle" class="form-control form-control-lg" type="text" placeholder="Title"
                                    aria-label="Title" required>
                                <div class="invalid-feedback">
                                    Please enter a title.
                                </div>
                                <div class="valid-feedback">
                                    Perfect!
                                </div>
                            </div>

                            <div class="mt-3">
                                <label for="ideaEditDescription" class="form-label">Description</label>
                                <textarea id="ideaEditDescription" class="form-control" placeholder="Description"
                                    aria-label="Description" required></textarea>
                                <div class="invalid-feedback">
                                    Please enter a description.
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

        <script src="<c:out value="${baseUri}" />/js/navbar.js"></script>
        <script src="<c:out value="${baseUri}" />/js/manage_idea.js"></script>
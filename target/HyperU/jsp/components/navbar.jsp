<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <header>
            <nav class="navbar navbar-dark bg-dark navbar-expand fixed-top mb-5 px-4 d-flex align-items-center row-lg">
                <a class="col-lg-3 navbar-brand d-flex align-items-center me-3" href="<c:out value=" ${baseUri}" />">
                <img src="<c:out value=" ${baseUri}" />/img/logo.svg" class="d-inline-block align-top" alt="HyperU">
                <h1 class="mx-2 my-0 d-none d-sm-inline-block"><strong>HyperU</strong></h1>
                </a>

                <div class="col-lg-6 d-flex align-items-center justify-content-center flex-fill">
                    <a class="d-none d-lg-inline-block btn btn-light p-2 shadow" href="<c:out value="
                        ${baseUri}" />/feed">
                    <i class="feather icon-home h4 m-0 px-1"></i>
                    <span class="d-none">Home</span>
                    </a>
                    <form class="search-form form-inline mx-0 mx-lg-3 w-50">
                        <div class="has-search">
                            <i class="feather icon-search h4 m-0"></i>
                            <input type="text" class="form-control py-3 shadow" placeholder="Search">
                        </div>
                    </form>
                    <a class="btn-create-post d-none d-lg-inline-block btn btn-light p-2 shadow" href="#">
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
                            <a href="<c:out value=" ${baseUri}" />/me" class="dropdown-item">
                            <i class="feather icon-user me-2"></i>
                            <strong>My profile</strong>
                            </a>
                        </li>
                        <li class="d-none" id="manageDropdownItem">
                            <a href="<c:out value=" ${baseUri}" />/manage" class="dropdown-item">
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
                <a class="btn-create-post nav-link px-3 d-inline-flex align-items-center" href="#">
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

        <script src="<c:out value=" ${baseUri}" />/js/navbar.js"></script>
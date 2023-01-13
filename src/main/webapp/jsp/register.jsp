<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <%@include file="./components/navbar_void_dark.jsp" %>

                    <div class="row page m-0 p-0">
                        <div
                            class="col-12 col-md-7 col-xl-5 bg-white min-vh-100 d-flex flex-column pb-5 p-header-void px-3 px-sm-5">
                            <div class="flex-fill d-flex align-items-center justify-content-center mt-5">

                                <!-- Form for Registration -->
                                <form action="#" method="POST" id="register-form"
                                    class="w-100 px-3 px-sm-5 mw-25 needs-validation" novalidate>
                                    <h2 class="mb-4"><strong>Sign up</strong></h2>
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label for="inputName" class="form-label">Name</label>
                                            <input name="name" type="text" class="form-control" id="inputName"
                                                placeholder="Name" required>
                                            <div class="invalid-feedback">
                                                Please enter the name.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputSurname" class="form-label">Surname</label>
                                            <input name="surname" type="text" class="form-control" id="inputSurname"
                                                placeholder="Surname" required>
                                            <div class="invalid-feedback">
                                                Please enter the surname.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputUsername" class="form-label">Username</label>
                                            <input name="username" type="text" class="form-control" id="inputUsername"
                                                placeholder="Username" required pattern="[a-zA-Z0-9._-]+">
                                            <div class="invalid-feedback" id="usernameValidation">
                                                Please enter a valid username.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputEmail" class="form-label">Email</label>
                                            <input name="email" type="email" class="form-control" id="inputEmail"
                                                placeholder="Email" required>
                                            <div class="invalid-feedback">
                                                Please enter the email.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputPassword" class="form-label">Password</label>
                                            <input name="password" type="password" class="form-control"
                                                id="inputPassword" placeholder="Password" required>
                                            <div class="invalid-feedback">
                                                Please enter the password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputPasswordCheck" class="form-label">Retype password</label>
                                            <input name="passwordCheck" type="password" class="form-control"
                                                id="inputPasswordCheck" placeholder="Retype password" required>
                                            <div class="invalid-feedback" id="passwordCheckValidation">
                                                Please reenter the password.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputBirthdate" class="form-label">Birthdate</label>
                                            <input name="birthdate" type="date" class="form-control" id="inputBirthdate"
                                                placeholder="Birthdate" required>
                                            <div class="invalid-feedback" id="birthdateValidation">
                                                Please enter the birthdate.
                                            </div>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="inputGender" class="form-label">Gender</label>
                                            <select id="inputGender" class="form-select">
                                                <option value="MALE">Male</option>
                                                <option value="FEMALE">Female</option>
                                                <option value="NOT_DECLARED" selected>Not declared</option>
                                            </select>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <label for="inputBiography" class="form-label">Something about you</label>
                                            <textarea name="biography" class="form-control" id="inputBiography"
                                                placeholder="Biography"></textarea>
                                            <div class="valid-feedback">
                                                Perfect!
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" id="privacyCheck"
                                                    required>
                                                <label class="form-check-label" for="privacyCheck">
                                                    I've read and accepted the
                                                    <a href="<c:out value="${baseUri}" />/privacy">Privacy Policy</a>
                                                </label>
                                                <div class="invalid-feedback">
                                                    You must agree the terms.
                                                </div>
                                                <div class="valid-feedback">
                                                    Perfect!
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12 d-flex align-items-center justify-content-between">
                                            <a href="<c:out value="${baseUri}" />/login" class="text-muted
                                            text-decoration-none">
                                            <strong>Login</strong>
                                            </a>
                                            <button type="submit" id="register" class="btn btn-primary btn-lg">
                                                <i class="feather icon-user me-3"></i><span>Sign up</span>
                                            </button>
                                            <a class="btn btn-success btn-lg d-none" id="success" href="<c:out value="
                                                ${baseUri}" />/login">
                                            <i class="feather icon-award me-3"></i>
                                            <span>Fantastic! Go and login!</span>
                                            </a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="row row-cols-auto mt-5">
                                <a href="<c:out value="${baseUri}" />/contacts" class="col text-muted
                                text-decoration-none">Contacts</a>
                                <span class="text-muted col">•</span>
                                <a href="<c:out value="${baseUri}" />/privacy" class="col text-muted
                                text-decoration-none">Privacy</a>
                                <a href="<c:out value="${baseUri}" />/api/easterquokka.jpg" class="col text-white
                                text-decoration-none">Quokka</a>
                            </div>
                        </div>
                        <div class="col-md-5 col-xl-7 d-none d-md-block p-0 m-0 bg-image-container">
                            <img src="<c:out value="${baseUri}" />/media/background.svg" class="background-image">
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>

                        <script src="<c:out value="${baseUri}" />/js/register.js"></script>
            </body>

        </html>
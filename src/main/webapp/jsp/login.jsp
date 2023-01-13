<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <div id="splashScreen"><img src="<c:out value="${baseUri}" />/media/logos/logo.svg" alt="HyperU" />
                </div>

                <%@include file="./components/navbar_void_dark.jsp" %>

                    <div class="row page m-0 p-0">
                        <div
                            class="col-12 col-md-7 col-xl-4 bg-white min-vh-100 d-flex flex-column pb-5 p-header-void px-3 px-sm-5">
                            <div class="flex-fill d-flex align-items-center justify-content-center">

                                <!-- Form for login operations -->
                                <form action="#" method="POST" id="login-form"
                                    class="w-100 px-3 px-sm-5 mw-25 needs-validation" novalidate>
                                    <h2 class="mb-4"><strong>Login</strong></h2>
                                    <div class="input-group mb-3 has-validation">
                                        <span class="input-group-text" id="usernameIcon"><i
                                                class="feather icon-user"></i></span>
                                        <input type="text" class="form-control" name="username" id="inputUsername"
                                            placeholder="Username" aria-label="Username" aria-describedby="usernameIcon"
                                            required>
                                        <div class="invalid-feedback" id="usernameValidation">
                                            Please enter the username.
                                        </div>
                                    </div>
                                    <div class="input-group mb-3 has-validation">
                                        <span class="input-group-text" id="passwordIcon"><i
                                                class="feather icon-lock"></i></span>
                                        <input type="password" class="form-control" name="password"
                                            placeholder="Password" aria-label="Password" aria-describedby="passwordIcon"
                                            id="inputPassword" required>
                                        <div class="invalid-feedback" id="passwordValidation">
                                            Please enter the password.
                                        </div>
                                    </div>
                                    <input type="hidden" name="token" value="true">
                                    <div class="d-flex align-items-center justify-content-between">
                                        <a href="<c:out value="${baseUri}" />/register"
                                        class="text-muted text-decoration-none"><strong>Sign up</strong></a><button
                                            type="submit" id="login" class="btn btn-primary btn-lg"><i
                                                class="feather icon-log-in me-3"></i><span>Login</span></button>
                                    </div>
                                </form>
                            </div>

                            <!-- Links to contacts and privacy policies -->
                            <div class="row row-cols-auto mt-5">
                                <a href="<c:out value="${baseUri}" />/contacts" class="col text-muted
                                text-decoration-none">Contacts</a>
                                <span class="text-muted col">•</span>
                                <a href="<c:out value="${baseUri}" />/privacy" class="col text-muted
                                text-decoration-none">Privacy</a>
                            </div>
                        </div>
                        <div class="col-md-5 col-xl-8 d-none d-md-block p-0 m-0 bg-image-container">
                            <img src="<c:out value="${baseUri}" />/media/background.svg" class="background-image">
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>

                        <script src="<c:out value="${baseUri}" />/js/login.js"></script>
            </body>

        </html>
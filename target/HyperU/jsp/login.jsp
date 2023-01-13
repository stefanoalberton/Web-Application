<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>
        
            <body>
                <%@include file="./components/navbar_void_dark.jsp" %>

                    <div class="row page m-0 p-0">
                        <div
                            class="col-12 col-md-6 col-lg-4 bg-white min-vh-100 d-flex flex-column pb-5 p-header-void px-3 px-sm-5">
                            <div class="flex-fill d-flex align-items-center justify-content-center">
                                <form action="#" method="POST" id="login-form" class="w-100 px-3 px-sm-5 mw-25">
                                    <h2 class="mb-4"><strong>Login</strong></h2>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text" id="usernameIcon"><i
                                                class="feather icon-user"></i></span>
                                        <input type="text" class="form-control" name="username" placeholder="Username"
                                            aria-label="Username" aria-describedby="usernameIcon">
                                    </div>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text" id="passwordIcon"><i
                                                class="feather icon-lock"></i></span>
                                        <input type="password" class="form-control" name="password"
                                            placeholder="Password" aria-label="Password"
                                            aria-describedby="passwordIcon">
                                    </div>
                                    <input type="hidden" name="token" value="true">
                                    <div class="d-flex align-items-center justify-content-between">
                                        <a href="./register"
                                            class="text-muted text-decoration-none"><strong>Register</strong></a><button
                                            type="submit" id="login" class="btn btn-primary btn-lg"><i
                                                class="feather icon-log-in me-3"></i><span>Login</span></button>
                                    </div>
                                </form>
                            </div>
                            <div class="row row-cols-auto">
                                <a href="./contacts" class="col text-muted text-decoration-none">Contacts</a>
                                <span class="text-muted col">•</span>
                                <a href="./privacy" class="col text-muted text-decoration-none">Privacy</a>
                            </div>
                        </div>
                        <div class="col-md-6 col-lg-8 d-none d-md-block p-0 m-0"><img src="<c:out value="${baseUri}" />/media/background.svg"
                                class="background-image">
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>

                        <script src="<c:out value="${baseUri}" />/js/login.js"></script>
            </body>

        </html>
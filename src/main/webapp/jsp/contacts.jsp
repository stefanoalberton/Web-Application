<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <%@include file="./components/navbar_void.jsp" %>

                    <!-- Contacts -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>Contact us</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    You can contact us at the email info@hyperu.space.
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="mailto:info@hyperu.space">
                                Email<i class="feather icon-at-sign ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/uranus.svg" class="landing-image"
                                alt="Uranus">
                            </div>
                        </div>
                    </div>

                    <!-- Documentation -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>Official API</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    You can read the documentation of our official APIs by clicking the button below
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="<c:out value="${baseUri}" />/api/documentation.html">
                                Official documentation<i class="feather icon-chevrons-right ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/astro.svg" class="landing-image"
                                alt="Astronaut">
                            </div>
                        </div>
                    </div>

                    <%@include file="./components/footer.jsp" %>
            </body>

        </html>
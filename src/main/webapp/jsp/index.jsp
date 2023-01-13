<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!doctype html>
        <html lang="en">

        <%@include file="./components/header.jsp" %>

            <body>
                <%@include file="./components/navbar_void.jsp" %>

                    <!-- What is HyperU -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>HyperU</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    Post your idea online and find new talented people who will become
                                    your new coworkers of your dream project!
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="<c:out value="${baseUri}" />/feed">
                                Start inventing<i class="feather icon-chevrons-right ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/uranus.svg" class="landing-image"
                                alt="Uranus">
                            </div>
                        </div>
                    </div>

                    <!-- Why it is called HyperU -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>Why?</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    "Idea" is a complex word. It inspires the creativity and genius of
                                    human being, which led
                                    him to the extraordinary inventions of which we became the protagonist. For Plato,
                                    the Greek
                                    philosopher, ideas were located in a heaven where they are collected together,
                                    perfect and immutable:
                                    the HyperUranion.
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="<c:out value="${baseUri}" />/contacts">
                                Contact us<i class="feather icon-chevrons-right ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/plato.svg" class="landing-image" alt="Plato">
                            </div>
                        </div>
                    </div>

                    <!-- UniPD -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>Born in UniPD</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    A star is born under the sign of University of Padua. And this is HyperU.
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="https://dei.unipd.it">
                                University of Padua<i class="feather icon-chevrons-right ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/unipd.svg" class="landing-image" alt="UniPD">
                            </div>
                        </div>
                    </div>

                    <!-- Privacy Policy -->
                    <div class="p-header-void min-vh-100 container">
                        <div class="d-md-flex align-items-center justify-content-around">
                            <div class="mega-title-container my-5">
                                <h1 class="mega-title"><strong>We care about your privacy</strong></h1>
                                <p class="h5 mt-4 text-muted-light">
                                    Read our Privacy Policy and see how we use your information.
                                </p>
                                <a class="btn btn-primary btn-lg mt-4" href="<c:out value="${baseUri}" />/privacy">
                                Privacy Policy<i class="feather icon-chevrons-right ms-3"></i>
                                </a>
                            </div>
                            <div class="text-center">
                                <img src="<c:out value="${baseUri}" />/media/gdpr.svg" class="landing-image" alt="GDPR">
                            </div>
                        </div>
                    </div>

                    <!-- Reviews -->
                    <!--
                    <div class="p-header-void min-vh-100 container">
                        <div class="mega-title-container my-5">
                            <h1 class="mega-title"><strong>They say about us</strong></h1>
                        </div>
                        <div class="mt-5">
                            <div class="row my-5 d-flex align-items-center">
                                <div class="col-md-7">
                                    <h4>Marcel De Santa, Spain</h4>
                                    <h2 class="h1">With HyperU I started a new life!</h2>
                                    <p class="my-4 lead text-muted-light">I was looking for something different in my
                                        life. I was a mechanic but now with HyperU I
                                        joined in a french group called "STR". We are going to built a new fast car that
                                        will be sold in
                                        2022!
                                    </p>
                                </div>
                                <div class="col-md-5 text-center">
                                    <img src="./media/users/marcel.png" class="rounded-circle" width="300" height="300"
                                        alt="Marcel">
                                </div>
                            </div>

                            <hr />

                            <div class="row my-5 d-flex align-items-center">
                                <div class="col-md-7 order-md-2">
                                    <h4>Paolo Cracco, Italy</h4>
                                    <h2 class="h1">A new way to eat</h2>
                                    <p class="my-4 lead text-muted-light">
                                        I wrote my idea on HyperU, about a new type of restaurant, and I
                                        found in few hours new
                                        colleagues that join me!</p>
                                </div>
                                <div class="col-md-5 order-md-1 text-center">
                                    <img src="./media/users/paolo.png" class="rounded-circle" width="300" height="300"
                                        alt="Paolo">
                                </div>
                            </div>

                            <hr />

                            <div class="row my-5 d-flex align-items-center">
                                <div class="col-md-7">
                                    <h4>John Tattler, England</h4>
                                    <h2 class="h1">Useful for work in groups</h2>
                                    <p class="my-4 lead text-muted-light">
                                        I had an idea about a new videogame for mobile phones, but I
                                        needed some computer engineers to develop it. I found three
                                        of them on HyperU, they were just graduated from university. So 3 months later,
                                        my new company, Digital Places, was
                                        born.
                                    </p>
                                </div>
                                <div class="col-md-5 text-center">
                                    <img src="./media/users/john.png" class="rounded-circle" width="300" height="300"
                                        alt="John">
                                </div>
                            </div>
                        </div>
                    </div>
                    -->
                    <%@include file="./components/footer.jsp" %>
            </body>

        </html>
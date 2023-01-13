<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <header>
            <nav class="navbar navbar-dark bg-dark navbar-expand fixed-top mb-5 px-4 d-flex align-items-center">
                <a class="navbar-brand d-flex align-items-center me-3 flex-fill" href="<c:out value="${baseUri}" />">
                    <img src="<c:out value="${baseUri}" />/media/logos/logo.svg" class="d-inline-block align-top" alt="HyperU">
                    <h1 class="mx-2 my-0"><strong>HyperU</strong></h1>
                </a>
                <div>
                    <a href="<c:out value="${baseUri}" />/login" class="d-inline-flex align-items-center text-warning text-decoration-none me-4"><i class="feather icon-log-in me-2"></i><strong>Login</strong></a>
                    <a href="<c:out value="${baseUri}" />/register" class="d-inline-flex align-items-center text-white text-decoration-none"><i class="feather icon-user me-2"></i><strong>Sign up</strong></a>
                </div>
            </nav>
        </header>
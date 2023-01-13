<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <c:set var="baseUri" value="https://hyperu.space" />

        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
            <meta name="author" content="HyperGroup">

            <title>HyperU</title>

            <!-- Favicons -->
            <link rel="apple-touch-icon" href="<c:out value="${baseUri}" />/media/favicons/apple-touch-icon.png">
            <link rel="icon" href="<c:out value="${baseUri}" />/media/favicons/favicon-32x32.png" sizes="32x32"
                type="image/png">
            <link rel="icon" href="<c:out value="${baseUri}" />/media/favicons/favicon-16x16.png" sizes="16x16"
                type="image/png">
            <link rel="manifest" href="<c:out value="${baseUri}" />/media/favicons/manifest.webmanifest">
            <link rel="mask-icon" href="<c:out value="${baseUri}" />/media/favicons/safari-pinned-tab.svg" color="#202040">
            <link rel="icon" href="<c:out value="${baseUri}" />/media/favicons/favicon.ico">
            <meta name="msapplication-config" content="<c:out value="${baseUri}" />/media/favicons/browserconfig.xml">
            <meta name="theme-color" content="#202040">

            <meta name="description"
                content="A webapp in which users can share project ideas and create teams based on required skills to realize the project.">

            <!-- Twitter -->
            <meta name="twitter:card" content="summary_large_image">
            <meta name="twitter:creator" content="HyperGroup">
            <meta name="twitter:title" content="HyperU">
            <meta name="twitter:description"
                content="A webapp in which users can share project ideas and create teams based on required skills to realize the project.">
            <meta name="twitter:image" content="<c:out value="${baseUri}" />/media/favicons/social-card.png">

            <!-- Facebook -->
            <meta property="og:url" content="<c:out value="${baseUri}" />">
            <meta property="og:title" content="HyperU">
            <meta property="og:description"
                content="A webapp in which users can share project ideas and create teams based on required skills to realize the project.">
            <meta property="og:type" content="website">
            <meta property="og:image" content="<c:out value="${baseUri}" />/media/favicons/social-card.png">
            <meta property="og:image:secure_url" content="<c:out value="${baseUri}" />/media/favicons/social-card.png">
            <meta property="og:image:type" content="image/png">
            <meta property="og:image:width" content="1200">
            <meta property="og:image:height" content="630">

            <!-- Feather Icons -->
            <link rel="stylesheet" type="text/css" href="https://at-ui.github.io/feather-font/css/iconfont.css"
                crossorigin="anonymous">

            <!-- Bootstrap and Custom style -->
            <link href="<c:out value="${baseUri}" />/css/style.css" rel="stylesheet">

            <!-- JQuery -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
                integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
                crossorigin="anonymous"></script>

            <!-- Main functions -->
            <script src="<c:out value="${baseUri}" />/js/utils.js"></script>

            <!-- Autosize textareas -->
            <script src="https://cdn.jsdelivr.net/npm/autosize@4.0.3/dist/autosize.min.js"></script>
        </head>
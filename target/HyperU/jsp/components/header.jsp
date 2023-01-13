<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="baseUri" value="//hyperu.space" />

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>HyperU</title>
    <link rel="icon" href="<c:out value="${baseUri}" />/media/logos/favicon.png" type="image/png" />
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" type="text/css" href="https://at-ui.github.io/feather-font/css/iconfont.css" crossorigin="anonymous">

    <link href="<c:out value="${baseUri}" />/css/style.css" rel="stylesheet">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
        integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
        crossorigin="anonymous"></script>

    <script src="<c:out value="${baseUri}" />/js/utils.js"></script>
</head>
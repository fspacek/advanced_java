<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="css/bootstrap.min.css" rel="stylesheet"></link>
    <link rel="stylesheet" href="css/bootstrap-theme.min.css"></link>
    <script src="js/bootstrap.min.js"></script>
    <style>
        .top-login {
            margin-top: 25%;
        }
    </style>
</head>

<body>
<div class="container">
    <%@include file="partials/header.jsp" %>
    <div class="top-login">
        <div class="row">
            <h3 class="col-md-4 col-md-offset-4">Login</h3>
        </div>
        <form method="POST">
            <div class="row">
                <div class="col-md-5">
                    <% Object failed = request.getAttribute("failed"); %>
                    <c:if test="${ failed != null }">
                    <span class="label label-danger">
                        Wrong login or password.
                    </span>
                    </c:if>
                </div>
            </div>
            <div class="row">
                <label class="col-md-1 col-md-offset-3">Username: </label>
                <input class="col-md-4"
                       type="text"
                       name="login" value="jozef@novak.cz"></input>
            </div>
            <div class="row">
                <label class="col-md-1 col-md-offset-3">Password: </label>
                <input class="col-md-4"
                       type="password"
                       name="password"></input>
            </div>
            <div class="row">
                <input
                        class="btn btn-primary col-md-5 col-md-offset-3"
                        type="submit" value="Login"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>

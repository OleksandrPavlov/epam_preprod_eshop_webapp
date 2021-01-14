<%@ page pageEncoding="UTF-8" contentType="text/html charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "ex" uri = "/WEB-INF/custom.tld"%>
<%@ taglib prefix = "login" tagdir = "/WEB-INF/tags"%>

<%@ taglib prefix = "l" tagdir = "/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

      <link rel="shortcut icon" href="/static/img/icons8-food-cart-48.png" type="image/png">
    <script src="https://kit.fontawesome.com/99570d6d36.js" crossorigin="anonymous"></script>
    <link href="${pageContext.request.contextPath}/static/css/custom.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/static/css/cart.css" rel="stylesheet"/>
    <title>TShop</title>
    <script>var ctx = "${pageContext.request.contextPath}";</script>
    <style>
        @font-face{
        font-family: awesome;
        src: url(${pageContext.request.contextPath}/static/fonts/20051.ttf);
    </style>
</head>
<body>
    <header>
        <div>
            <a class="navbar-brand company-title" href="${pageContext.request.contextPath}/home">EShop</a>
        </div>
       <div></div>
        <l:locale/>
        <login:login/>
    </header>

    <!-- HERE WILL BE INNER PAGE-->
    <jsp:include page="${requestScope.currentPage}"/>
<footer>
   <!-- TO DO FOOTER-->
</footer>
<!--Custom scripts-->

<script src="${pageContext.request.contextPath}/static/js/sendingPreferences.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script src="${pageContext.request.contextPath}/static/js/order.js"></script>
<script src="${pageContext.request.contextPath}/static/js/locale.js"></script>

</body>
</html>
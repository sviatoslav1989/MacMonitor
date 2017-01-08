<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cross table</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<div class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>
    <div class="well">
    The file must be in the following format:(device ip;network interface;cross;socket)<br>
    "10.10.20.100";"Fa0/2";"6/11/13";"12/58"<br>
    "10.10.20.100";"Fa0/4";"6/11/14";"12/59"<br>
    "10.10.20.100";"Fa0/6";"6/11/15";"12/63"<br>
    </div>

    <form:form method="POST" modelAttribute="filecsv" enctype="multipart/form-data">
        File to upload: <form:input type="file" path="file" id="file" class="form-control input-sm"/>
        <input type="submit" value="Upload"> Press here to upload the file!
    </form:form>
</div>
</body>

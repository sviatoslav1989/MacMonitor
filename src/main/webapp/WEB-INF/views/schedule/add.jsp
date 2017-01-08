<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title> Add schedule time Form</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<div class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well lead">New schedule record</div>
    <form:form method="POST"  class="form-horizontal">


        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Time(hour:minute)</label>
                    <input type="number" min="0" max="23" name= "hour" value="${hour}" class="form-control input-sm"/>:
                    <input type="number" min ="0" max="59" name = "minute" value="${minute}" class="form-control input-sm"/>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="form-actions floatRight">
                <input type="submit" value="Save" class="btn btn-primary btn-sm"/>
            </div>
        </div>
    </form:form>

</div>
</body>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Edit Form</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<div class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well lead">User Edit</div>
    <form:form method="POST"  commandName="user" class="form-horizontal">

        <form:input type="hidden" path="password"  class="form-control input-sm"/>
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Login name </label>
                <div class="col-md-7">
                    <form:input type="text" path="login"  class="form-control input-sm"/>
                </div>
            </div>
        </div>



        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="userRoles">Roles </label>
                <div class="col-md-7">
                    <form:select path="userRoles" items="${roles}" multiple="true" itemValue="type" itemLabel="type" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Description</label>
                <div class="col-md-7">
                    <form:input type="text" path="description" class="form-control input-sm"/>
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

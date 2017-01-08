<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cross Edit Form</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<div class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well lead">Edit cross record</div>
    <form:form method="POST" commandName="cross" class="form-horizontal">

        <form:input type="hidden" path="id"/>
        <input type="hidden" name="credId" value="${credId}"/>
        <form:input type="hidden" path="iface"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Cross</label>
                <div class="col-md-7">
                    <form:input type="text" path="cross" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Socket</label>
                <div class="col-md-7">
                    <form:input type="text" path="socket" class="form-control input-sm"/>
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

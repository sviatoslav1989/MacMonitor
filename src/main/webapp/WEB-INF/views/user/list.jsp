<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User List</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<script>
    $(document).ready(function()
            {
                $("#userTable").tablesorter();
            }
    );
</script>
<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well">
        <a href="<c:url value='/user/add' />">Add New User</a>
    </div>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of Users </span></div>

        <table class="table table-hover tablesorter-blue" id = "userTable">
            <thead>
            <tr>

                <th>Login name</th>
                <th>Description</th>
                <th>Roles</th>
                <th class="sorter-false" width="100"></th>
                <th class="sorter-false" width="100"></th>
                <th class="sorter-false" width="100"></th>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user" varStatus="loop">
                <tr>
                    <td>${user.login}</td>
                    <td>${user.description}</td>
                    <td>
                        <c:forEach items="${user.userRoles}" var="role" varStatus="loop">
                            ${role.type}<br>
                        </c:forEach>
                    </td>
                    <td><a href="<c:url value='/user/changepassword-${user.id}' />" class="btn btn-success">change password</a></td>
                    <td><a href="<c:url value='/user/edit-${user.id}' />" class="btn btn-success ">edit</a></td>
                    <td><a href="<c:url value='/user/delete-${user.id}' />" class="btn btn-danger ">delete</a></td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
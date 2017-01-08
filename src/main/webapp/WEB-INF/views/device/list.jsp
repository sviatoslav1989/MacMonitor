<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Device List</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<script>

    $(document).ready(function()
            {
                $("#deviceTable").tablesorter({
                    headers: {

                        1: { sorter: 'myIpAddress' }
                    }
                });
            }
    );
</script>
<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>
    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
    <div class="well">
        <a href="<c:url value='/device/add' />">Add New Device</a>
    </div>
    </sec:authorize>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of Devices </span></div>

        <table class="table table-hover tablesorter-blue" id = "deviceTable">
            <thead>
            <tr>
                <th>№</th>
                <th>ip address</th>
                <th>credential type</th>
                <th>Description</th>
                <th class="sorter-false" width="100"></th>
                <th class="sorter-false" width="100"></th>
                <th class="sorter-false" width="100"></th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <th class="sorter-false" width="100"></th>
                     <th class="sorter-false" width="100"></th>
                     <th class="sorter-false" width="100"></th>
                </sec:authorize>


            </tr>
            </thead>
            <tbody>
            <c:forEach items="${credentials}" var="credential" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${credential.ipAddress}</td>
                    <td>${credential.type}</td>
                    <td>${credential.description}</td>
                    <td><a href="<c:url value='/device/arp-${credential.credId}' />" class="btn btn-success">Arp Table</a></td>
                    <td><a href="<c:url value='/device/mac-${credential.credId}' />" class="btn btn-success ">Mac Table</a></td>
                    <td><a href="<c:url value='/device/cross-${credential.credId}' />" class="btn btn-success ">Cross Table</a></td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                        <td><a href="<c:url value='/device/edit-${credential.credId}' />" class="btn btn-success ">edit</a></td>
                        <td><a href="<c:url value='/device/copy-${credential.credId}' />" class="btn btn-success ">copy</a></td>
                        <td><a href="<c:url value='/device/delete-${credential.credId}' />" class="btn btn-danger" onclick ="return confirm('Are you shured?')">delete</a></td>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
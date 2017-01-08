<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Socket table</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>

<script>

    $(document).ready(function()
            {
                $("#crossTable").tablesorter({
                    headers: {

                        1: { sorter: 'myIpAddress' },
                        2: { sorter: 'ifName' }

                    }
                });
            }
    );
</script>

<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well">
        <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
             <a href="<c:url value='/cross/add' />">Add New Cross</a>
             <br>
                <a href="<c:url value='/cross/upload' />">Upload csv</a>
            <br>
        </sec:authorize>

        <a href="<c:url value='/cross/download' />">Download  csv</a>
    </div>

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Cross Table</span></div>

        <table class="table table-hover tablesorter-blue" id = "crossTable">
            <thead>
            <tr>
                <th>№</th>
                <th>Ip address</th>
                <th>iface</th>
                <th>cross</th>
                <th>socket</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <th class="sorter-false" width="100"></th>
                    <th class="sorter-false" width="100"></th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${crosses}" var="cross" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${cross.credential.ipAddress}</td>
                    <td>${cross.iface}</td>
                    <td>${cross.cross}</td>
                    <td>${cross.socket}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <td><a href="<c:url value='/cross/edit-${cross.id}' />" class="btn btn-success custom-width">edit</a></td>
                    <td><a href="<c:url value='/cross/delete-${cross.id}' />" class="btn btn-danger custom-width">delete</a></td>
                    </sec:authorize>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
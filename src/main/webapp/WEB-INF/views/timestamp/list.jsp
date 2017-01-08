<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Polls' timestamps</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<script>

    $(document).ready(function()
            {
                $("#timestampTable").tablesorter({
                    headers: {


                        1: { sorter: 'customDateWithSeconds' }

                    }
                });
            }
    );
</script>
<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of polls' timestamps </span></div>

        <table class="table table-hover tablesorter-blue" id="timestampTable">
            <thead>
            <tr>
                <th>№</th>
                <th>Timestamp</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <th width="100" class="sorter-false"></th>
                </sec:authorize>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${timeEntries}" var="timeEntry" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${timeEntry.dateWithSeconds}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <td>
                        <a href="<c:url value='/timestamp/delete-${timeEntry.id}' />" class="btn btn-danger" onclick ="return confirm('Are you shured?')">delete</a>
                    </td>
                    </sec:authorize>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Polls' schedule</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<script>
    $(document).ready(function()
            {
                $("#scheduleTable").tablesorter();
            }
    );
</script>
<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
    <div class="well">
        <a href="<c:url value='/schedule/add' />">Add new schedule time</a>
    </div>
    </sec:authorize>

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Schedule time</span></div>

        <table class="table table-hover tablesorter-blue" id="scheduleTable">
            <thead>
            <tr>
                <th>№</th>
                <th>time</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                <th class="sorter-false" width="100"></th>
                </sec:authorize>


            </tr>
            </thead>
            <tbody>
            <c:forEach items="${scheduleEntries}" var="scheduleEntry" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${scheduleEntry}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <td>
                        <a href="<c:url value='/schedule/delete-${scheduleEntry.id}' />" class="btn btn-danger">delete</a>
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
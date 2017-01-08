<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Result table</title>
    <%@include file="../scriptandcss.jsp" %>

</head>

<body>

<script>

    $(document).ready(function()
            {
                $("#resultTable").tablesorter({
                    headers: {

                         3: { sorter: 'myIpAddress' },
                        4: { sorter: 'myIpAddress' },
                        5: { sorter: 'ifName' },
                        8: { sorter: 'customDate' }

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
        <div class="panel-heading">
            <span class="lead">Result Table</span><br>
            <a href="<c:url value='/result/poll' />">Poll devices</a><br>
            <a href="<c:url value='/result/download' />">Download  csv</a>
        </div>

        <table class="table table-hover tablesorter-blue" id="resultTable">
            <thead>
            <tr>
                <th>№</th>
                <th>mac</th>
                <th>vlan</th>
                <th>ip</th>
                <th>device ip</th>
                <th>if name</th>
                <th>cross</th>
                <th>socket</th>
                <th>date</th>
                 <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                <th class="sorter-false" width="100"></th>
                 </sec:authorize>


            </tr>
            </thead>
            <tbody>
            <c:forEach items="${resultEntries}" var="resultEntry" varStatus="loop">
                    <td>${loop.index+1}</td>
                    <td>${resultEntry.mvidEntry.macAddress}</td>
                    <td>${resultEntry.mvidEntry.vlan}</td>
                    <td>${resultEntry.mvidEntry.ipAddress}</td>
                    <td>${resultEntry.credential.ipAddress}</td>
                    <td>${resultEntry.crossEntry.iface}</td>
                    <td>${resultEntry.crossEntry.cross}</td>
                    <td>${resultEntry.crossEntry.socket}</td>
                    <td>${resultEntry.timeEntry}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('EDITOR')">
                    <td><a href="<c:url value='/result/delete-${resultEntry.id}' />" class="btn btn-danger" onclick ="return confirm('Are you shured?')">delete</a></td>
        </sec:authorize>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

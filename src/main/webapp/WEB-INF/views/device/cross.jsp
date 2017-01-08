<<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                $("#crossTable").tablesorter({
                    headers: {

                        1: { sorter: 'ifName' }

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
        <div class="panel-heading"><span class="lead">Cross table for device with ip address ${credential.ipAddress} </span></div>

        <table class="table table-hover tablesorter-blue" id = "crossTable">
            <thead>
            <tr>
                <th>№</th>
                <th>iface</th>
                <th>cross</th>
                <th>socket</th>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${crossEntries}" var="crossEntry" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${crossEntry.iface}</td>
                    <td>${crossEntry.cross}</td>
                    <td>${crossEntry.socket}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

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
                $("#arpTable").tablesorter({
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

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Arp table of device with ip address ${arpTable.device.credential.ipAddress} </span></div>

        <table class="table table-hover tablesorter-blue" id = "arpTable">
            <thead>
            <tr>
                <th>№</th>
                <th>ip address</th>
                <th>mac address</th>
                <th>vlan</th>


            </tr>
            </thead>
            <tbody>
            <c:forEach items="${arpTable.arpEntries}" var="arpEntry" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${arpEntry.ipAddress}</td>
                    <td>${arpEntry.macAddress}</td>
                    <td>${arpEntry.vlan}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
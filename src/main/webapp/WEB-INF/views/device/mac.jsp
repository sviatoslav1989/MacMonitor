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
                $("#macTable").tablesorter({
                    headers: {

                        3: { sorter: 'ifName' }

                    }
                });
            }
    );

    function handleTable(){
        var trunkCheckBox = document.getElementById("trunkCheckBox");
        var macTable = document.getElementById("macTable");
        for (var i = 0, row; row = macTable.rows[i]; i++) {
            //iterate through rows
            //rows would be accessed using the "row" variable assigned in the for loop
           var cell = row.cells[4];
            if(cell.innerHTML=="TRUNK" && trunkCheckBox.checked == false){
                row.style.display = 'none';
            }
            else {
                row.style.display = '';
            }
        }
    }
    //trunkCheckBox.addEventListener("change",handleTable);

</script>
<div  class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Mac table of device with ip address ${macTable.device.credential.ipAddress} </span>
            <input type="checkbox" id="trunkCheckBox" checked onchange="handleTable()">Show records where port trunk status is trunk
        </div>

        <table class="table table-hover tablesorter-blue" id = "macTable">
            <thead>
            <tr>
                <th>№</th>
                <th>mac address</th>
                <th>vlan</th>
                <th>iface</th>
                <th>port trunk status</th>


            </tr>
            </thead>
            <tbody>
            <c:forEach items="${macTable.macEntries}" var="macEntry" varStatus="loop">
                <tr>
                    <td>${loop.index+1}</td>
                    <td>${macEntry.macAddress}</td>
                    <td>${macEntry.vlan}</td>
                    <td>${macEntry.iface}</td>
                    <td>${macEntry.portTrunkStatus}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
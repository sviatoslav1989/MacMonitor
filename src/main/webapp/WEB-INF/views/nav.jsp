<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<ul class="nav nav-pills">

    <sec:authorize access="hasRole('ADMIN')">
        <li role="presentation"><a href="../user/list">Users</a></li>
    </sec:authorize>
    <li role="presentation"><a href="../account/main">My account</a></li>
    <li role="presentation"><a href="../device/list">Devices</a></li>
    <li role="presentation"><a href="../cross/list">Crosses</a></li>
    <li role="presentation"><a href="../result/list">Result table</a></li>
    <li role="presentation"><a href="../timestamp/list">Polls' timestamps</a></li>
    <li role="presentation"><a href="../schedule/list">Polls' schedule</a></li>
</ul>

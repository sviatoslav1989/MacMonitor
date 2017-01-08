<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Device Registration Form</title>
    <%@include file="../scriptandcss.jsp" %>
</head>

<body>
<div class="generic-container">
    <%@include file="../authheader.jsp" %>
    <%@include file="../nav.jsp" %>

    <div class="well lead">Device Edit</div>
    <form:form method="POST"  commandName="credential" class="form-horizontal">


        <form:input type="hidden" path="credId"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Ip address</label>
                <div class="col-md-7">
                    <form:input type="text" path="ipAddress"  class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Port</label>
                <div class="col-md-7">
                    <form:input type="text" path="port" value="161" class="form-control input-sm"/>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp Version</label>
                <div class="col-md-7">
                    <form:select path="snmpVersion" items="${snmpVersion}" multiple="false" class="form-control input-sm" />
                </div>
            </div>
        </div>


        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp Community</label>
                <div class="col-md-7">
                    <form:input type="text" path="snmpCommunity" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp Security Name</label>
                <div class="col-md-7">
                    <form:input type="text" path="snmpV3SecurityName" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp security level</label>
                <div class="col-md-7">
                    <form:select path="snmpV3SecurityLevel" items="${snmpV3SecurityLevel}" multiple="false" class="form-control input-sm" />
                </div>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp auth protocol</label>
                <div class="col-md-7">
                    <form:select path="snmpV3AuthProtocol" items="${snmpV3AuthProtocol}" multiple="false" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Snmp priv protocol</label>
                <div class="col-md-7">
                    <form:select path="SnmpV3PrivProtocol" items="${snmpV3PrivProtocol}" multiple="false" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Auth key</label>
                <div class="col-md-7">
                    <form:input type="text" path="authKey" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Priv key</label>
                <div class="col-md-7">
                    <form:input type="text" path="privKey" class="form-control input-sm"/>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Description</label>
                <div class="col-md-7">
                    <form:input type="text" path="description" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <input type="submit" value="Save" class="btn btn-primary btn-sm"/>
            </div>
        </div>
    </form:form>

</div>
</body>
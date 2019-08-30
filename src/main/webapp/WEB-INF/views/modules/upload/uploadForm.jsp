<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>文件的上传和下载</title>
    <style>
        img{
            width: 100px;
            height: 100px;
        }
    </style>
</head>
<body>
<form name="serForm" action="${ctx}/upload/upload/fileUpload" method="post"  enctype="multipart/form-data">
    <h1>采用流的方式上传文件,支持单文件、多文件</h1>
    <input type="file" name="file" multiple>
    <input type="submit" value="upload"/>
    <h1>回显的图片</h1>
    <c:forEach items="${listUrl3}" var="list">
        <img alt="无法加载" src="/upload/${list}"/><br>
    </c:forEach>
    <hr style="height:3px;border:none;border-top:3px double red;" />
</form>
<form name="Form2" action="${ctx}/upload/upload/springMvcUpload" method="post"  enctype="multipart/form-data">
    <h1>使用spring mvc提供的类的方法上传文件，支持单文件、多文件</h1>
    <input type="file" name="file" multiple>
    <input type="submit" value="upload"/>
    <h1>回显的图片</h1>
    <c:forEach items="${listUrl}" var="list">
        <img alt="无法加载" src="/upload/${list}"/><br>
    </c:forEach>
</form>
<hr style="height:3px;border:none;border-top:3px double red;" />

<form action="${ctx}/upload/upload/springUpload" method="post" enctype="multipart/form-data">
    <h1>使用spring提供的类上传文件，支持单文件、多文件</h1>
    <input type="file" name="file" multiple>
    <input type="submit" value="upload">
    <h1>回显的图片</h1>
    <c:forEach items="${listUrl2}" var="list">
        <img alt="无法加载" src="/upload/${list}"/><br>
    </c:forEach>
</form>
<hr style="height:3px;border:none;border-top:3px double red;" />

<form action="${ctx}/upload/upload/download" method="post">
    <h1>使用spring提供的类下载文件</h1>
    <input value="向后端传下载文件所在的路径" disabled style="width: 300px">
    <input type="submit" value="download">
</form>
<hr style="height:3px;border:none;border-top:3px double red;" />

<form action="${ctx}/upload/upload/download2" method="post">
    <h1>使用二进制流的形式下载文件</h1>
    <input value="向后端传下载的文件名和文件所在的路径" disabled style="width: 300px">
    <input type="submit" value="download">
</form>
<hr style="height:3px;border:none;border-top:3px double red;" />
</body>
</html>

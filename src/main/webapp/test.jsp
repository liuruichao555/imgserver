<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>upload</title>
</head>
<body>
    <form action="/upload/img" method="post" enctype="multipart/form-data">
        <input type="file" name="uploadFile">
        <input type="submit" value="提交">
    </form>
</body>
</html>

<html>
<head>
    <title>${title!"我是默认值"}</title>
</head>
<body>
    <label>学号:</label>${student.id}<br>
    <label>姓名:</label>${student.name}<br>
    <label>住址:</label>${student.address}<br>

    学生列表：
    <table border="1">
        <#list students as s>
            <#if s_index % 2 == 0>
                <tr style="color: red;">
            <#else>
                <tr>
            </#if>
                <td>${s_index+1}</td>
                <td>${s.id}</td>
                <td>${s.name}</td>
                <td>${s.address}</td>
            </tr>
        </#list>
    </table>
    <br>
    <#--属性不为空-->
    <#if curdate ??>
        当前日期：${curdate?string("yyyy/MM/dd HH:mm:ss")}
    <#--属性为空-->
    <#else>
        curdate为null
    </#if>
</body>
</html>
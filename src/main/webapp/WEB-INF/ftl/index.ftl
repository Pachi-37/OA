<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OA系统</title>
    <link rel="stylesheet" href="../../resources/layui/css/layui.css">
</head>

<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo" style="font-size:18px">OA系统</div>

        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:void(0)">
                    <span class="layui-icon layui-icon-user" style="font-size: 20px">
                    </span>
                    ${current_employee.name} | ${current_department.departmentName} | ${current_employee.title}
                </a>
            </li>
            <li class="layui-nav-item"><a href="/logout">注销</a></li>
        </ul>
    </div>


    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree">
                <#list node_list as node>
                    <#if node.nodeType == "1">
                        <!-- 父节点 -->
                        <li class="layui-nav-item layui-nav-itemed">
                            <a href="javascript:void(0)">${node.nodeName}</a>
                            <dl class="layui-nav-child module" data-node-id="1"></dl>
                        </li>
                    </#if>
                    <#if node.nodeType == "2">
                        <!-- 子结点 -->
                        <dd class="function" data-parent-id="1">
                            <a href="javascript;void(0)" target="ifmMain">${node.nodeName}</a>
                        </dd>
                    </#if>
                </#list>
            </ul>
        </div>

        <div class="layui-body" style="overflow-y: hidden">
            <iframe name="ifmMain" style="border: 0px;width: 100%;height: 100%"></iframe>
        </div>
        <div class="layui-footer">
            Copyright
        </div>
    </div>
</div>

<script src="/resources/layui/layui.js"></script>
<script>
    layui.$(".function").each(function () {
        var func = layui.$(this);
        var parentId = func.data("parent-id");
        layui.$("dl[data-node-id=" + parentId + "]").append(func);
    })

    layui.element.render('nav');
</script>

</body>
</html>
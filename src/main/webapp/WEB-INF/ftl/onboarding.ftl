<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>员工入职</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        .ns-container {
            position: absolute;
            width: 500px;
            height: 450px;
            top: 150px;
            left: 50%;
            margin-left: -250px;
            padding: 20px;
            box-sizing: border-box;
            border: 1px solid #cccccc;
        }
    </style>
</head>
<body>

<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h2>员工入职</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>

<div class="ns-container">
    <h1 style="text-align: center;margin-bottom: 20px">员工入职信息</h1>
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">姓名：</label>
            <div class="layui-input-block">
                <input type="text" name="name" required lay-verify="required" placeholder="员工姓名" autocomplete="off"
                       class="layui-input">
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">账号：</label>
            <div class="layui-input-block">
                <input type="text" name="user_name" required lay-verify="required" placeholder="账号" autocomplete="off"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">职位：</label>
            <div class="layui-input-block">
                <input type="text" name="title" required lay-verify="required" placeholder="职位" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">员工级别：</label>
            <div class="layui-input-block">
                <select name="level" lay-verify="required">
                    <#list 1..current_employee.level as level>
                        <option value="${level}">${level}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">密码框</label>
            <div class="layui-input-inline">
                <input type="password" value="test" name="password" required lay-verify="required" placeholder="请输入密码"
                       autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux">默认密码 test</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门</label>
            <div class="layui-input-block">
                <select name="department" lay-verify="required">
                    <option value=""></option>
                    <#if current_employee.level == 8>
                        <#list all_departments as department>
                            <option value="${department.departmentId!0}">${department.departmentName!部门不存在}</option>
                        </#list>
                    <#elseif current_employee.level == 7>
                        <option ${current_department.departmentId} selected>${current_department.departmentName}</option>
                    </#if>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" type="button" lay-submit lay-filter="sub">立即提交</button>
            </div>
        </div>
    </form>
</div>

</body>

<script src="/resources/layui/layui.js" charset="utf-8"></script>
<script src="/resources/sweetalert2.all.min.js"></script>
<script>
    var layForm = layui.form;
    var $ = layui.$;

    layForm.on('submit(sub)', function (data) {
        console.info("向服务器提交的表单数据", data.field);
        $.post("/onboarding/create", data.field, function (json) {
            console.info("服务器返回数据", json);
            if (json.code == "0") {
                /*SweetAlert2确定对话框*/
                swal({
                    type: 'success',
                    html: "<h2>入职手续办理成功</h2>",
                    confirmButtonText: "确定"
                }).then(function (result) {
                    window.location.href = "/forward/notice";
                });
            } else {
                swal({
                    type: 'warning',
                    html: "<h2>" + json.message + "</h2>",
                    confirmButtonText: "确定"
                });
            }
        }, "json");
        return false;
    });
</script>

</html>
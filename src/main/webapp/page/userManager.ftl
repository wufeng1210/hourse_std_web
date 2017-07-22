<html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>租房管理系统</title>
<link rel="stylesheet" type="text/css"
      href="../page/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
      href="../page/jquery-easyui-1.5.1/themes/icon.css">
<script type="text/javascript" src="../page/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript"
        src="../page/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript"
        src="../page/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    function doSearch(){
        var userName = $("#s_userName").val() =="" ? -1: $("#s_userName").val();
        var mobile = $("#s_mobile").val() =="" ? -1: $("#s_mobile").val();
        $("#dia").datagrid('load',{
            userName:userName,
            mobile:mobile
        });
    }
    //刷新
    function doReload(){
        $('#dia').datagrid('load',{
        });
    }
    function openAddDialog(){
        $("#dlg").dialog("open").dialog("setTitle","添加用户");
        url="/user/saveOrUpdate.do?userId=-1";
    }
    function openModifyDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要编辑的数据！');
            return;
        }
        var row=selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle","修改用户信息");
        $("#fm").form("load",row);
        $("#User").attr("readonly","readonly");
        //alert(row.Userid);
        url="/user/saveOrUpdate.do?userId="+row.userId;
    }
    //保存用户
    function doSave(){

        $("#fm").form("submit",{
            url:url,
            onSubmit:function(){

                return $(this).form("validate");
            },
            success:function(result){
                if(result.errorMsg){
                    $.messager.alert('系统提示',"<font color=red>"+result.errorMsg+"</font>");
                }else{
                    $.messager.alert('系统提示','保存成功！');
                    closeAddDialog();
                    $("#dia").datagrid("reload");
                }
            }
        });
    }
    function closeAddDialog(){
        $("#dlg").dialog("close");
        $("#fm").form('clear');
    }
    //打开role选择角色
    function openRoleChooseDialog(){
        $("#dlg2").dialog("open").dialog("setTitle","选择角色");
    }
    //查找
    function searchRole(){
        $('#dg2').datagrid('load',{
            s_roleName:$("#s_roleName").val(),

        });
    }
    //关闭图框
    function closeRoleDialog(){
        $("#s_roleName").val("");
        $('#dg2').datagrid('load',{
            s_roleName:""
        });
        $("#dlg2").dialog("close");
    }
    //选择数据
    function chooseRole(){
        var selectedRows=$("#dg2").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一个角色！');
            return;
        }
        var row=selectedRows[0];
        $("#roleId").val(row.roleId);
        $("#roleName").val(row.roleName);
        closeRoleDialog();
    }
    function doDelete(){
//        $.post("/deleteUser.do?user_id=testid",{},function(result){
//            if(result.role=="超级管理员"||result.role=="管理员"||result.role=="操作员"){
                var selectedRows=$("#dia").datagrid('getSelections');
                if(selectedRows.length==0){
                    $.messager.alert('系统提示','请选择要删除的数据');
                    return;
                }
                var strIds=[];
                for(var i=0;i<selectedRows.length;i++){
                    strIds.push(selectedRows[i].userId);
                }
                var ids=strIds.join(",");
                $.messager.confirm("系统提示","你确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
                    if(r){
                        $.post("/user/delete.do",{delIds:ids},function(result){
                            if(result.success){

                                $.messager.alert('系统提示',"您已成功删除<font color=red>"+result.delNums+"</font>条数据！");
                                $("#dia").datagrid("reload");
                            }else{
                                $.messager.alert('系统提示',result.errorMsg);
                            }
                        },"json");
                    }
                });
//            }else{
//                //alert(result.success);
//                $.messager.alert('系统提示',"对不起，您没删除的权限！！");
//            }
//        }
//                ,"json");


    }
    function exportExcel(){
        window.open('/user/fileExport.do');
    }
</script>
</head>
<body style="margin:1px;">
<table id="dia" class="easyui-datagrid" title="用户表" style="width:1150px;height:470px" toolbar="#tb"
       url="/user/list.do" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:false">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th   data-options="field:'userId',width:150" >用户编号</th>
        <th   data-options="field:'userName',width:150" >用户名</th>
        <th   data-options="field:'userPassWord',width:150" >密码</th>
        <th   data-options="field:'roleName',width:150" >用户角色</th>
        <th   data-options="field:'mobile',width:150" >手机号</th>
        <th   data-options="field:'NAME',width:150" >用户姓名</th>
        <th   data-options="field:'qq',width:150" >qq</th>
        <th   data-options="field:'wechat',width:150" >微信</th>
        <th   data-options="field:'nickName',width:150" >用户昵称</th>
    </tr>
    </thead>
</table>

<div id="tb">
    <a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-export" plain="true" >导出全部数据</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openModifyDialog()">修改 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="doDelete()">删除 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="doReload()">刷新 </a>
<div>
    &nbsp;用户名：<input type="text" name="s_userName" id="s_userName" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>
    &nbsp;手机号：<input type="text" name="s_mobile" id="s_mobile" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>

    <a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>
<div id="dlg" class="easyui-dialog" style="width:600px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table>
            <tr>
                <td>用户名:</td>
                <td><input type="text" id="userName" name="userName" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input type="text" id="userPassWord" name="userPassWord" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td>角色名称：</td>
                <td><input type="hidden" id="roleId" name="roleId" /><input type="text"  id="roleName" name="roleName"  readonly="readonly" class="easyui-validatebox" required="true"/></td>
                <td colspan="2"><a href="javascript:openRoleChooseDialog()"  class="easyui-linkbutton" >选择角色</a></td>
            </tr>
            <tr>
                <td>手机号:</td>
                <td><input type="text" id="mobile" name="mobile" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>姓名:</td>
                <td><input type="text" id="NAME" name="NAME" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>qq:</td>
                <td><input type="text" id="qq" name="qq" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>微信:</td>
                <td><input type="text" id="wechat" name="wechat" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>昵称:</td>
                <td><input type="text" id="nickName" name="nickName" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>允许查看用户ids(如:1,2,):</td>
                <td><input type="text" id="allowIds" name="allowIds" class="easyui-validatebox"/></td>
                <td>0:允许全部</td>
            </tr>
        </table>
    </form>

</div>
<div id="dlg-buttons">
    <a href="javascript:doSave()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg2" class="easyui-dialog"  iconCls="icon-search" style="width:500px;height:480px;padding:10px 20px"
     closed="true" buttons="#dlg2-buttons">
    <div style="height:40px;" align="center">
        角色名称：<input type="text" id="s_roleName" name="s_roleName"  onkeydown="if(event.keyCode==13) searchRole()" />
        <a href="javascript:searchRole()"  class="easyui-linkbutton"  iconCls="icon-search"  plain="true">搜索</a>
    </div>
    <div style="height:350px;">
        <table id="dg2" title="查询结果" class="easyui-datagrid"  fitColumn="true"
               pagination="true" rownumbers="true" url="/role/list.do?roleId=-1"  singleSelect="true" fit="true" >
            <thead>
            <tr>
                <th field="roleId"  width="50"  align="center">编号</th>
                <th field="roleName"  width="150"  align="center">角色名称</th>
                <th field="roleDescription"  width="210"  align="center">备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div id="dlg2-buttons">
    <a href="javascript:chooseRole()"  class="easyui-linkbutton"  iconCls="icon-ok"  >确定</a>
    <a href="javascript:closeRoleDialog()"  class="easyui-linkbutton"  iconCls="icon-cancel"  >关闭</a>
</div>
</body>
</html>
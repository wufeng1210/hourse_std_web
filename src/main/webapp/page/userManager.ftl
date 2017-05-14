<html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>租房管理系统</title>
<link rel="stylesheet" type="text/css"
      href="page/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
      href="page/jquery-easyui-1.5.1/themes/icon.css">
<script type="text/javascript" src="page/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript"
        src="page/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript"
        src="page/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    function searchUser(){
        $("#dia").datagrid('load',{
            userName:$("#userName").val()
        });
    }
    function openAddDialog(){
        $("#dlg").dialog("open").dialog("setTitle","添加用户");
        url="/saveOrUpdateUser.do?userId=-1";
    }
    function openUserModifyDialog(){
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
        url="/saveOrUpdateUser.do?userId="+row.userId;
    }
    //保存产品
    function saveUser(){

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
                    closeUserAddDialog();
                    $("#dia").datagrid("reload");
                }
            }
        });
    }
    function closeUserAddDialog(){
        $("#dlg").dialog("close");
        $("#fm").form('clear');
    }
    function deleteUser(){
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
                        $.post("/deleteUser.do",{delIds:ids},function(result){
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

</script>
</head>
<body style="margin:1px;">
<table id="dia" class="easyui-datagrid" title="用户表" style="width:1150px;height:470px" toolbar="#tb"
       url="/userList.do" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:true">
    <tr>
        <th   data-options="field:'userId',width:150" >用户编号</th>
        <th   data-options="field:'userName',width:150" >用户姓名</th>
        <th   data-options="field:'userPassWord',width:150" >密码</th>
    </tr>
    </thead>
</table>

<div id="tb">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openUserModifyDialog()">修改 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUser()">删除 </a>
<div>
    &nbsp;用户名：<input type="text" name="userName" id="userName" style="width:100px" onkeydown="if(event.keyCode==13) searchUser()"/>

    <a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>
<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table>
            <tr>
                <td>用户名:</td>
                <td><input type="text" id="userName" name="userName" class="easyui-validatebox" required="true"/></td>
            <tr>
            </tr>
                <td>密码:</td>
                <td><input type="text" id="userPassWord" name="userPassWord" class="easyui-validatebox" required="true"/></td>
            </tr>

        </table>
    </form>

</div>
<div id="dlg-buttons">
    <a href="javascript:saveUser()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeUserAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<#--<div id="dlg2" class="easyui-dialog" style="width:500px;height:150px;padding:10px 20px"-->
     <#--closed="true" buttons="#dlg2-buttons">-->
    <#--<form id="uploadForm" method="post" enctype="multipart/form-data">-->
        <#--<table>-->
            <#--<tr>-->
                <#--<td>上传文件</td>-->
                <#--<td><input type="file" name="storeUploadFile"/>上传文件</td>-->
            <#--</tr>-->
        <#--</table>-->
    <#--</form>-->
<#--</div>-->
<#--<div id="dlg2-buttons">-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="uploadFile()">上传文件</a>-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>-->
<#--</div>-->
</body>
</html>
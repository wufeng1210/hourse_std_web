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
        $("#dia").datagrid('load',{
            roleId:-1,
            roleName:$("#roleName").val()
        });
    }
    function openAddDialog(){
        $("#dlg").dialog("open").dialog("setTitle","添加角色");
        url="/role/saveOrUpdate.do?roleId=-1";
    }
    function openModifyDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要编辑的数据！');
            return;
        }
        var row=selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle","修改角色信息");
        $("#fm").form("load",row);
        $("#User").attr("readonly","readonly");
        //alert(row.roleId);
        url="/role/saveOrUpdate.do?roleId="+row.roleId;
    }
    //保存
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
                    strIds.push(selectedRows[i].roleId);
                }
                var ids=strIds.join(",");
                $.messager.confirm("系统提示","你确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
                    if(r){
                        $.post("/role/delete.do",{delIds:ids},function(result){
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

    function openAuthDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要授权的角色！');
            return;
        }
        var row=selectedRows[0];
        roleId=row.roleId;

        $("#dlg2").dialog("open").dialog("setTitle","角色授权");
        url="/auth/getAuth.do?roleId="+roleId;;

        $("#tree").tree({
            lines:true,
            url:url,
            checkbox:true,
            cascadeCheck:false,

            onLoadSuccess:function(){
                $("#tree").tree('expandAll');
            },
            onCheck:function(node,checked){
                if(checked){
                    checkNode($('#tree').tree('getParent',node.target));
                }
            }
        });
    }

    function checkNode(node){
        if(!node){
            return;
        }else{
            checkNode($('#tree').tree('getParent',node.target));
            $('#tree').tree('check',node.target);
        }
    }

    function closeAuthDialog(){
        $("#dlg2").dialog("close");
    }

    function saveAuth(){
        var nodes=$('#tree').tree('getChecked');
        var authArrIds=[];
        for(var i=0;i<nodes.length;i++){
            authArrIds.push(nodes[i].id);
        }
        var authIds=authArrIds.join(",");
        $.post("/auth?action=auth",{authIds:authIds,roleId:roleId},function(result){
            if(result.success){
                $.messager.alert('系统提示','授权成功！');
                closeAuthDialog();
            }else{
                $.messager.alert('系统提示',result.errorMsg);
            }
        },"json");
    }
</script>
</head>
<body style="margin:1px;">
<table id="dia" class="easyui-datagrid" title="角色表" style="width:1150px;height:470px" toolbar="#tb"
       url="/role/list.do?roleId=-1" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th   data-options="field:'roleId',width:150" >角色编号</th>
        <th   data-options="field:'roleName',width:150" >角色名称</th>
        <th   data-options="field:'roleDescription',width:150" >备注</th>
        <#--<th   data-options="field:'userPassWord',width:150" >密码</th>-->
    </tr>
    </thead>
</table>

<div id="tb">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openModifyDialog()">修改 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="doDelete()">删除 </a>
    <a href="javascript:openAuthDialog()" class="easyui-linkbutton" iconCls="icon-roleManage" plain="true">角色授权</a>
<div>
    &nbsp;角色名：<input type="text" name="roleName" id="roleName" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>

    <a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>
<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table>
            <tr>
                <td>角色名:</td>
                <td><input type="text" id="roleName" name="roleName" class="easyui-validatebox" required="true"/></td>
            </tr>
            <#--</tr>-->
                <#--<td>描述:</td>-->
                <#--<td><input type="text" id="userPassWord" name="userPassWord" class="easyui-validatebox" required="true"/></td>-->
            <#--</tr>-->

        </table>
    </form>

</div>
<div id="dlg-buttons">
    <a href="javascript:doSave()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg2" class="easyui-dialog" style="width: 300px;height: 450px;padding: 10px 20px"
     closed="true" buttons="#dlg2-buttons">
    <ul id="tree" class="easyui-tree"></ul>
</div>

<div id="dlg2-buttons">
    <a href="javascript:saveAuth()" class="easyui-linkbutton" iconCls="icon-ok" >授权</a>
    <a href="javascript:closeAuthDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
</div>
</body>
</html>
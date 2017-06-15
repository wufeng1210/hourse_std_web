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
            activityId:-1,
            activityTitle:$("#activityTitle").val()
        });
    }
    function openAddDialog(){
        $("#dlg").dialog("open").dialog("setTitle","添加活动");
        url="/activity/saveOrUpdate.do?activityId=-1";
    }
    function openModifyDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要编辑的数据！');
            return;
        }
        var row=selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle","修改活动信息");
        $("#fm").form("load",row);
        $("#activity").attr("readonly","readonly");
        //alert(row.activityid);
        url="/activity/saveOrUpdate.do?activityId="+row.activityId;
    }
    //保存活动
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
//        $.post("/deleteactivity.do?activity_id=testid",{},function(result){
//            if(result.role=="超级管理员"||result.role=="管理员"||result.role=="操作员"){
                var selectedRows=$("#dia").datagrid('getSelections');
                if(selectedRows.length==0){
                    $.messager.alert('系统提示','请选择要删除的数据');
                    return;
                }
                var strIds=[];
                for(var i=0;i<selectedRows.length;i++){
                    strIds.push(selectedRows[i].activityId);
                }
                var ids=strIds.join(",");
                $.messager.confirm("系统提示","你确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
                    if(r){
                        $.post("/activity/delete.do",{delIds:ids},function(result){
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
<table id="dia" class="easyui-datagrid" title="活动表" style="width:1150px;height:470px" toolbar="#tb"
       url="/activity/list.do?activityId=-1" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th   data-options="field:'activityId',width:150" >活动编号</th>
        <th   data-options="field:'activityTitle',width:150" >活动标题</th>
        <th   data-options="field:'activityImagePath',width:150" >活动图片路径</th>
        <th   data-options="field:'activityImageUrl',width:150" >活动图片地址</th>
    </tr>
    </thead>
</table>

<div id="tb">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openModifyDialog()">修改 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="doDelete()">删除 </a>
<div>
    &nbsp;活动标题：<input type="text" name="activityTitle" id="activityTitle" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>

    <a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>
<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table>
            <tr>
                <td>活动标题:</td>
                <td><input type="text" id="activityTitle" name="activityTitle" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td>活动图片路径:</td>
                <td><input type="text" id="activityImagePath" name="activityImagePath" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td>活动图片地址:</td>
                <td><input type="text" id="activityImageUrl" name="activityImageUrl" class="easyui-validatebox" required="true"/></td>
            </tr>
        </table>
    </form>

</div>
<div id="dlg-buttons">
    <a href="javascript:doSave()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>
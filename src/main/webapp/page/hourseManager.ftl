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
            hourseId:-1,
            userId:$("#userId").val()
        });
    }
    function openAddDialog(){
        $("#dlg").dialog("open").dialog("setTitle","添加房屋");
        url="/hourse/saveOrUpdate.do?hourseId=-1";
    }
    function openModifyDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要编辑的数据！');
            return;
        }
        var row=selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle","修改房屋信息");
        $("#fm").form("load",row);
        $("#User").attr("readonly","readonly");
        //alert(row.hourseId);
        url="/hourse/saveOrUpdate.do?hourseId="+row.hourseId;
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
//            if(result.hourse=="超级管理员"||result.hourse=="管理员"||result.hourse=="操作员"){
                var selectedRows=$("#dia").datagrid('getSelections');
                if(selectedRows.length==0){
                    $.messager.alert('系统提示','请选择要删除的数据');
                    return;
                }
                var strIds=[];
                for(var i=0;i<selectedRows.length;i++){
                    strIds.push(selectedRows[i].hourseId);
                }
                var ids=strIds.join(",");
                $.messager.confirm("系统提示","你确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
                    if(r){
                        $.post("/hourse/delete.do",{delIds:ids},function(result){
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
<table id="dia" class="easyui-datagrid" title="房屋表" style="width:1150px;height:470px" toolbar="#tb"
       url="/hourse/list.do?hourseId=-1" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:false">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th   data-options="field:'hourseId',width:150" >房屋编号</th>
        <th   data-options="field:'userId',width:150" >用户编号</th>
        <th   data-options="field:'userName',width:150" >用户名称</th>
        <th   data-options="field:'province',width:150" >省份</th>
        <th   data-options="field:'city',width:150" >城市</th>
        <th   data-options="field:'area',width:150" >区域</th>
        <th   data-options="field:'residentialQuarters',width:150" >房屋所在小区</th>
        <th   data-options="field:'roomNum',width:150" >房间数量</th>
        <th   data-options="field:'toiletNum',width:150" >卫生间数量</th>
        <th   data-options="field:'hallNum',width:150" >大厅数量</th>

        <th   data-options="field:'kitchenNum',width:150" >厨房数量</th>
        <th   data-options="field:'monthly',width:150" >月租（元）</th>
        <th   data-options="field:'packingingLot',width:150" >是否有车位</th>
        <th   data-options="field:'rentingWay',width:150" >租房方式</th>
        <th   data-options="field:'brokerMobile',width:150" >经纪人手机号</th>
        <th   data-options="field:'brokerCode',width:150" >经纪人编号</th>
        <th   data-options="field:'brokerName',width:150" >经纪人姓名</th>
        <th   data-options="field:'areaCovered',width:150" >占比面积（平方）</th>
        <th   data-options="field:'refrigerator',width:150" >是否有冰箱</th>
        <th   data-options="field:'heater',width:150" >是否有热水器</th>
        <th   data-options="field:'bed',width:150" >是否有床</th>
        <th   data-options="field:'desk',width:150" >是否有桌子</th>
        <th   data-options="field:'airConditioner',width:150" >是否有空调</th>
        <th   data-options="field:'cabinet',width:150" >是否有柜子</th>

        <th   data-options="field:'statusStr',width:150" >状态</th>
    </tr>
    </thead>
</table>

<div id="tb">
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openModifyDialog()">修改 </a>-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="doDelete()">删除 </a>-->
<div>
    &nbsp;用户编号：<input type="text" name="userId" id="userId" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>

    <a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>
<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table>
            <tr>
                <td>房屋名:</td>
                <td><input type="text" id="hourseName" name="hourseName" class="easyui-validatebox" required="true"/></td>
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
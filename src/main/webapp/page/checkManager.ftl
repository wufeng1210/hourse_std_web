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
        url="/check/saveOrUpdate.do?hourseId=-1";
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
        url="/check/saveOrUpdate.do?hourseId="+row.hourseId;
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
        $("#dlgCheck").dialog("close");
        $("#fmCheck").form('clear');
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
                        $.post("/check/delete.do",{delIds:ids},function(result){
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

    function openCheckDialog(){
        var selectedRows=$("#dia").datagrid('getSelections');
        if(selectedRows.length!=1){
            $.messager.alert('系统提示','请选择一条要编辑的数据！');
            return;
        }
        var row=selectedRows[0];
        $("#dlgCheck").dialog("open").dialog("setTitle","审核房屋信息");
        $("#fmCheck").form("load",row);
        $("#dlgCheck input").attr("readonly","readonly");
        //alert(row.hourseId);
        url="/check/saveOrUpdate.do?hourseId="+row.hourseId;
    }
    function doCheckSuccess(){
        url+="&state=1";
        $("#fmCheck").form("submit",{
            url:url,
            onSubmit:function(){

                return $(this).form("validate");
            },
            success:function(result){
                if(result.errorMsg){
                    $.messager.alert('系统提示',"<font color=red>"+result.errorMsg+"</font>");
                }else{
                    $.messager.alert('系统提示','审核通过！');
                    closeAddDialog();
                    $("#dia").datagrid("reload");
                }
            }
        });
    }
    function doCheckFail(){
        var brokerCode=$("#fmCheck").find("#brokerCode").val();
        var brokerName=$("#fmCheck").find("#brokerName").val();
        var brokerMobile=$("#fmCheck").find("#brokerMobile").val();
        url+="&state=3";
        $("#fmCheck").form("submit",{
            url:url,
            onSubmit:function(){

                return $(this).form("validate");
            },
            success:function(result){
                if(result.errorMsg){
                    $.messager.alert('系统提示',"<font color=red>"+result.errorMsg+"</font>");
                }else{
                    $.messager.alert('系统提示','审核打回！请联系!<br>'+brokerName+'('+brokerCode+')--'+brokerMobile);
                    closeAddDialog();
                    $("#dia").datagrid("reload");
                }
            }
        });
    }

</script>
</head>
<body style="margin:1px;">
<table id="dia" class="easyui-datagrid" title="审核表" style="width:1150px;height:470px" toolbar="#tb"
       url="/check/list.do?hourseId=-1" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
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
        <th   data-options="field:'packingingLotStr',width:150" >是否有车位</th>
        <th   data-options="field:'rentingWay',width:150" >租房方式</th>
        <th   data-options="field:'brokerMobile',width:150" >经纪人手机号</th>
        <th   data-options="field:'brokerCode',width:150" >经纪人编号</th>
        <th   data-options="field:'brokerName',width:150" >经纪人姓名</th>
        <th   data-options="field:'areaCovered',width:150" >占比面积（平方）</th>
        <th   data-options="field:'squarePrice',width:150" >价格（平方）</th>
        <th   data-options="field:'furniture',width:150" >家具</th>
        <th   data-options="field:'near',width:150" >周边</th>
        <th   data-options="field:'traffic',width:150" >交通</th>
        <th   data-options="field:'description',width:150" >描述</th>

        <th   data-options="field:'statusStr',width:150" >状态</th>
    </tr>
    </thead>
</table>

<div id="tb">
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openModifyDialog()">修改 </a>-->
    <#--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="doDelete()">删除 </a>-->
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openCheckDialog()">审核 </a>
<div>
    &nbsp;用户编号：<input type="text" name="userId" id="userId" style="width:100px" onkeydown="if(event.keyCode==13) doSearch()"/>

    <a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>
</div>

<div id="dlgCheck" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"
     closed="true" buttons="#dlgCheck-buttons">
    <form id="fmCheck" method="post">
        <table>
            <tr>
                <td>用户编号:</td>
                <td><input type="text" id="userId" name="userId"  required="true"/></td>
            </tr>
            <tr>
                <td>用户名称:</td>
                <td><input type="text" id="userName" name="userName"  required="true"/></td>
            </tr>
            <tr>
                <td>省份:</td>
                <td><input type="text" id="province" name="province"  required="true"/></td>
            </tr>
            <tr>
                <td>城市:</td>
                <td><input type="text" id="city" name="city"  required="true"/></td>
            </tr>
            <tr>
                <td>区域:</td>
                <td><input type="text" id="area" name="area"  required="true"/></td>
            </tr>
            <tr>
                <td>房屋所在小区:</td>
                <td><input type="text" id="residentialQuarters" name="residentialQuarters"  required="true"/></td>
            </tr>
            <tr>
                <td>房间数量:</td>
                <td><input type="text" id="roomNum" name="roomNum"  required="true"/></td>
            </tr>
            <tr>
                <td>卫生间数量:</td>
                <td><input type="text" id="toiletNum" name="toiletNum"  required="true"/></td>
            </tr>
            <tr>
                <td>大厅数量:</td>
                <td><input type="text" id="hallNum" name="hallNum"  required="true"/></td>
            </tr>

            <tr>
                <td>厨房数量:</td>
                <td><input type="text" id="kitchenNum" name="kitchenNum"  required="true"/></td>
            </tr>
            <tr>
                <td>月租（元）:</td>
                <td><input type="text" id="monthly" name="monthly"  required="true"/></td>
            </tr>
            <tr>
                <td>是否有车位:</td>
                <td><input type="text" id="packingingLotStr" name="packingingLotStr"  required="true"/></td>
            </tr>
            <tr>
                <td>租房方式:</td>
                <td><input type="text" id="rentingWay" name="rentingWay"  required="true"/></td>
            </tr>
            <tr>
                <td>经纪人手机号:</td>
                <td><input type="text" id="brokerMobile" name="brokerMobile"  required="true"/></td>
            </tr>
            <tr>
                <td>经纪人编号:</td>
                <td><input type="text" id="brokerCode" name="brokerCode"  required="true"/></td>
            </tr>

            <tr>
                <td>经纪人姓名:</td>
                <td><input type="text" id="brokerName" name="brokerName"  required="true"/></td>
            </tr>
            <tr>
                <td>占比面积（平方）:</td>
                <td><input type="text" id="areaCovered" name="areaCovered"  required="true"/></td>
            </tr>
            <tr>
                <td>价格（平方）:</td>
                <td><input type="text" id="squarePrice" name="squarePrice"  required="true"/></td>
            </tr>
            <tr>
                <td>家具:</td>
                <td><input type="text" id="furniture" name="furniture"  required="true"/></td>
            </tr>
            <tr>
                <td>周边:</td>
                <td><input type="text" id="near" name="near"  required="true"/></td>
            </tr>
            <tr>
                <td>交通:</td>
                <td><input type="text" id="traffic" name="traffic"  required="true"/></td>
            </tr>
            <tr>
                <td>描述:</td>
                <td><input type="text" id="description" name="description"  required="true"/></td>
            </tr>

        </table>
    </form>

</div>
<div id="dlgCheck-buttons">
    <a href="javascript:doCheckSuccess()" class="easyui-linkbutton" iconCls="icon-ok">审核通过</a>
    <a href="javascript:doCheckFail()" class="easyui-linkbutton" iconCls="icon-cancel">审核打回</a>
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
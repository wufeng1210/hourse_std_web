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
    <#--$(function() {-->
    <#--$("#tree").tree({-->

    <#--line : tree,-->
    <#--url : 'getAuth.do?roleId=${userInfo.roleId}',-->
    <#--//state:"closed",-->
    <#--onLoadSuccess : function() {-->
    <#--$("#tree").tree('collapseAll');-->
    <#--},-->
    <#--onClick : function(node) {-->
    <#--if (node.iconCls == "icon-exit") {-->
    <#--logout();-->
    <#--} else if (node.iconCls == "icon-modifyPassword") {-->
    <#--openPasswordModifyDialog();-->
    <#--}-->
    <#--else if (node.attributes.authPath) {-->
    <#--//alert("yes");-->
    <#--openTab(node);-->
    <#--}-->

    <#--}-->

    <#--});-->
    <#--/*  $("#tabs").tabs({-->
    <#--Onselect:function(nodetext){-->
    <#--alert("yes");-->
    <#--$.post("city?action=list", {-->
    <#--nodetext : nodetext-->
    <#--}, function(result) {-->
    <#--if (result.success) {-->

    <#--} else {-->
    <#--$.messager.alert('系统提示', result.errorMsg);-->
    <#--}-->
    <#--}, "json");-->
    <#--}-->
    <#--}); */-->
    <#--$("#tabs").click(function(){-->
    <#--var title = $('.tabs-selected').text();-->
    <#--//	alert(title);-->
    <#--$.post("city?action=list", {-->
    <#--nodetext : title-->
    <#--}, function(result) {-->
    <#--if (result.success) {-->

    <#--} else {-->
    <#--$.messager.alert('系统提示', result.errorMsg);-->
    <#--}-->
    <#--}, "json");-->
    <#--});-->
    <#--function logout() {-->
    <#--$.messager.confirm('系统提示', '您确定要退出系统吗？', function(r) {-->
    <#--if (r) {-->
    <#--window.location.href = 'user?action=logout';-->
    <#--}-->
    <#--});-->
    <#--}-->
    <#--function openPasswordModifyDialog() {-->
    <#--url = "user?action=modifyPassword";-->
    <#--$("#dlg").dialog("open").dialog("setTitle", "修改密码");-->
    <#--}-->


    <#--function openTab(node) {-->
    <#--var nodetext = node.text;-->
    <#--//alert(node.text);-->
    <#--if ($("#tabs").tabs("exists", nodetext)) {-->
    <#--$("#tabs").tabs("select", nodetext);-->

    <#--} else {-->
    <#--$.post("city?action=list", {-->
    <#--nodetext : node.text-->
    <#--}, function(result) {-->
    <#--if (result.success) {-->

    <#--} else {-->
    <#--$.messager.alert('系统提示', result.errorMsg);-->
    <#--}-->
    <#--}, "json");-->
    <#--var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src="-->
    <#--+ node.attributes.authPath + "></iframe>";-->

    <#--$("#tabs").tabs("add", {-->
    <#--title : nodetext,-->
    <#--iconCls : node.iconCls,-->
    <#--closable : true,-->
    <#--content : content-->
    <#--});-->
    <#--}-->
    <#--}-->

    <#--});-->

    function modifyPassword() {
        $("#fm").form("submit", {
            url : url,
            onSubmit : function() {
                var oldPassword = $("#oldPassword").val();
                var newPassword = $("#newPassword").val();
                var newPassword2 = $("#newPassword2").val();
                if (!$(this).form("validate")) {
                    return false;
                }

                if (newPassword != newPassword2) {
                    $.messager.alert('系统提示', '确认密码输入错误');
                    return false;
                }
                //alert("yes");
                return true;
            },
            success : function(result) {
                var result = eval('(' + result + ')');

                if (result.errorMsg) {
                    alert("yes");
                    $.messager.alert('系统提示', result.errorMsg);
                    return;
                } else {
                    $.messager.alert('系统提示', '密码修改成功，下一次登录生效！');
                    closePasswordModifyDialog();
                }
            }
        });
    }
    function closePasswordModifyDialog() {
        $("#dlg").dialog("close");
        $("#oldPassword").val("");
        $("#newPassword").val("");
        $("#newPassword2").val("");
    }
    function closeAuthDialog(){
        $("#dlg2").dialog("close");
    }
    function treetab() {
        $("#dlg2").dialog("open").dialog("setTitle", "选择条件");
        $("#trees").tree({
            line : tree,
            url : 'auth?action=menu&parentId=-1',
            //state:"closed",
            onLoadSuccess : function() {
                $("#trees").tree('expandAll');
            },
            onClick : function(node) {
                selectedTab(node);
            }
        });
    }
    function selectedTab(node){

        var nodetext = node.text;
        //alert(node.text);
        if ($("#tabs").tabs("exists", nodetext)) {
            //alert("yes");
            $("#tabs").tabs("select", nodetext);
        } else {
            $.post("city?action=list", {
                nodetext : node.text
            }, function(result) {
                if (result.success) {
                    $("#tree").tree('collapseAll');
                } else {
                    $.messager.alert('系统提示', result.errorMsg);
                }
            }, "json");
            var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src="
                    + node.attributes.authPath + "></iframe>";
            //alert(content);
            //alert('auth?a='+nodetext+'');
            //alert(node.attributes.authPath);

        }
    }

</script>
</head>
<body style="margin:1px;">
<table id="dia" class="easyui-datagrid" title="用户表" style="width:1150px;height:470px" toolbar="#tb"
       url="/userList.do" data-options="pageSize:100,pageList:[100,200,300,400,500],pagination:true,rownumbers:true,singleSelect:true,showFooter:true,fitColumns:false"
       fit="true" idField="id">
    <thead data-options="frozen:true">
    <tr>
        <th   data-options="field:'userId',width:150" >userId</th>
        <th   data-options="field:'userName',width:150" >userName</th>
    <#--        <th   data-options="field:'saledate',width:100" >销售日期</th>
            <th   data-options="field:'salemonth',width:50">销售月</th>
            <th   data-options="field:'pdmodel',width:120">产品型号</th>
            <th   data-options="field:'series',width:60">产品系列</th>
            <th   data-options="field:'actualsale',width:60">实际销量</th>
            <th   data-options="field:'price',width:80">统一零售价</th>
            <th   data-options="field:'unifyprice',width:80">销售价格</th>
            <th   data-options="field:'difprice',width:80">优惠价格</th>
            <th   data-options="field:'discount',width:80">折扣</th>-->

    </tr>
    </thead>
</table>

<div id="tb">
    <a href="javascript:uploadActivityFile()" class="easyui-linkbutton" iconCls="icon-import" plain="true">导入Excel表</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddDialog()">添加 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openActivityModifyDialog()">修改 </a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteActivity()">删除 </a>
    <div>这里是搜索条件</div>
<#--<div>
    &nbsp;指定店名称：<input type="text" name="s_storename" id="s_storename" style="width:100px" onkeydown="if(event.keyCode==13) searchActivity()"/>
    终端经理：<input type="text" name="s_managername" id="s_managername" style="width:100px" onkeydown="if(event.keyCode==13) searchActivity()"/>
    系统类型：<input type="text" name="s_systemtype" id="s_systemtype" style="width:100px" onkeydown="if(event.keyCode==13) searchActivity()"/>

    <a href="javascript:searchActivity()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
</div>-->
</div>
<#--<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px"-->
<#--closed="true" buttons="#dlg-buttons">-->
<#--<form id="fm" method="post">-->
<#--<table>-->
<#--<tr>-->
<#--<td>userName:</td>-->
<#--<td><input type="text" id="userName" name="userName" class="easyui-validatebox" required="true"/></td>-->
<#--&lt;#&ndash;                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
<#--<td>城市:</td>-->
<#--<td><input type="text" id="city" name="city" class="easyui-validatebox" required="true"/></td>&ndash;&gt;-->
<#--</tr>-->
<#--&lt;#&ndash;<tr>-->
<#--<td>区域:</td>-->
<#--<td><input type="text" id="area" name="area" class="easyui-validatebox" required="true"/></td>-->
<#--<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
<#--<td>终端经理:</td>-->
<#--<td><input type="text" id="managername" name="managername" class="easyui-validatebox" required="true"/></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td>营业代表:</td>-->
<#--<td><input type="text" id="salename" name="salename" class="easyui-validatebox" required="true"/></td>-->
<#--<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
<#--<td>系统类型:</td>-->
<#--<td><input type="text" id="systemtype" name="systemtype" class="easyui-validatebox" required="true"/></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td>专柜类型:</td>-->
<#--<td><input type="text" id="counterstype" name="counterstype" class="easyui-validatebox" required="true"/></td>-->
<#--<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
<#--<td>门店系列:</td>-->
<#--<td><input type="text" id="storeseries" name="storeseries" class="easyui-validatebox" required="true"/></td>-->

<#--</tr>-->
<#--<tr>-->
<#--<td>开店时间:</td>-->
<#--<td><input type="text" id="startime" name="startime" class="easyui-validatebox" required="true"/></td>-->
<#--<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
<#--<td>闭店时间:</td>-->
<#--<td><input type="text" id="stoptime" name="stoptime" class="easyui-validatebox" required="true"/></td>-->

<#--</tr>-->
<#--<tr>-->
<#--<td>门店状态:</td>-->
<#--<td><select class="easyui-combobox" id="storestate" name="storestate" style="width:134px">-->
<#--<option value="正常">正常</option>-->
<#--<option value="2014年新开门店">2014年新开门店</option>-->
<#--<option value="已撤店">已撤店</option>-->
<#--</select></td>-->
<#--</tr>&ndash;&gt;-->
<#--</table>-->
<#--</form>-->

<#--</div>-->
<div id="dlg-buttons">
    <a href="javascript:saveActivity()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeActivityAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg2" class="easyui-dialog" style="width:500px;height:150px;padding:10px 20px"
     closed="true" buttons="#dlg2-buttons">
    <form id="uploadForm" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td>上传文件</td>
                <td><input type="file" name="storeUploadFile"/>上传文件</td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg2-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="uploadFile()">上传文件</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>
</div>
</body>
</html>
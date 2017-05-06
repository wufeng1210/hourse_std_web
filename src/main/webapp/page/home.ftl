<html>
<head>
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
        $(function() {
            $("#tree").tree({

                line : tree,
                url : 'getAuth.do?roleId=${userInfo.roleId}',
                //state:"closed",
                onLoadSuccess : function() {
                    $("#tree").tree('collapseAll');
                },
                onClick : function(node) {
                    if (node.iconCls == "icon-exit") {
                        logout();
                    } else if (node.iconCls == "icon-modifyPassword") {
                        openPasswordModifyDialog();
                    }
                    else if (node.attributes.authPath) {
                        //alert("yes");
                        openTab(node);
                    }

                }

            });
            /*  $("#tabs").tabs({
                Onselect:function(nodetext){
                    alert("yes");
                    $.post("city?action=list", {
                        nodetext : nodetext
                    }, function(result) {
                        if (result.success) {

                        } else {
                            $.messager.alert('系统提示', result.errorMsg);
                        }
                    }, "json");
                }
            }); */
            $("#tabs").click(function(){
                var title = $('.tabs-selected').text();
                //	alert(title);
                $.post("city?action=list", {
                    nodetext : title
                }, function(result) {
                    if (result.success) {

                    } else {
                        $.messager.alert('系统提示', result.errorMsg);
                    }
                }, "json");
            });
            function logout() {
                $.messager.confirm('系统提示', '您确定要退出系统吗？', function(r) {
                    if (r) {
                        window.location.href = 'user?action=logout';
                    }
                });
            }
            function openPasswordModifyDialog() {
                url = "user?action=modifyPassword";
                $("#dlg").dialog("open").dialog("setTitle", "修改密码");
            }


            function openTab(node) {
                var nodetext = node.text;
                //alert(node.text);
                if ($("#tabs").tabs("exists", nodetext)) {
                    $("#tabs").tabs("select", nodetext);

                } else {
                    $.post("city?action=list", {
                        nodetext : node.text
                    }, function(result) {
                        if (result.success) {

                        } else {
                            $.messager.alert('系统提示', result.errorMsg);
                        }
                    }, "json");
                    var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src="
                            + node.attributes.authPath + "></iframe>";

                    $("#tabs").tabs("add", {
                        title : nodetext,
                        iconCls : node.iconCls,
                        closable : true,
                        content : content
                    });
                }
            }

        });

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
<body class="easyui-layout">

<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="首页" data-options="iconCls:'icon-home'">
            <div align="center" style="">
                <font color="blue" size="10">欢迎使用</font><br>
            </div>
        </div>
    </div>
</div>
<div region="west" style="width: 160px; padding: 5px;" title="导航菜单"
     split="true">
    <ul id="tree" class="easyui-tree"></ul>
</div>
<div region="south" style="height: 25px; padding: 5px;" align="center">
    欢迎:${userInfo.userName}</div>
<div id="dlg" class="easyui-dialog"
     style="width: 400px; height: 220px; padding: 10px 20px" closed="true"
     buttons="#dlg-buttons" data-options="iconCls:'icon-modifyPassword'">
    <form id="fm" method="post">
        <table cellspacing="4px;">
            <tr>
                <td>用户名：</td>
                <td><input type="hidden" name="userId" id="userId"
                           /><input type="text" name="useName"
                                                                   readonly="readonly"
                                                                   style="width: 200px; background-color: #dddbdb;" /></td>
            </tr>
            <tr>
                <td>原密码：</td>
                <td><input type="password" class="easyui-validatebox"
                           name="oldPassword" id="oldPassword"
                           style="width: 200px; background-color: #dddbdb;" required="true" /></td>
            </tr>
            <tr>
                <td>新密码：</td>
                <td><input type="password" class="easyui-validatebox"
                           name="newPassword" id="newPassword"
                           style="width: 200px; background-color: #dddbdb;" required="true" /></td>
            </tr>
            <tr>
                <td>确认新密码：</td>
                <td><input type="password" class="easyui-validatebox"
                           name="newPassword2" id="newPassword2"
                           style="width: 200px; background-color: #dddbdb;" required="true" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:modifyPassword()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a>
    <a href="javascript:closePasswordModifyDialog()"
       class="easyui-linkbutton" iconCls="icon-ok">关闭</a>
</div>
<div id="dlg2" class="easyui-dialog"
     style="width: 300px; height:450px; padding: 10px 20px" closed="true"
     buttons="#dlg2-buttons">
    <ul id="trees" class="easyui-tree"></ul>
</div>
<div id="dlg2-buttons">

    <a href="javascript:closeAuthDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
</div>
</body>
</html>
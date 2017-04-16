
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>

    <meta charset="utf-8">
    <title>租房管理系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- CSS -->
    <link rel="stylesheet" href="page/assets/css/reset.css">
    <link rel="stylesheet" href="page/assets/css/supersized.css">
    <link rel="stylesheet" href="page/assets/css/style.css">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="page/assets/js/html5.js"></script>
    <![endif]-->

</head>

<body>

<div class="page-container">
    <h1>租房管理系统</h1>
    <form action="/login.do" method="post">
        <input type="text" name="userName" class="username" placeholder="请输入您的用户名">
        <input type="password" name="userPassWord" class="password" placeholder="请输入您的用户密码">
        <input type="Captcha" class="Captcha" name="Captcha" placeholder="请输入验证码">
        <button type="submit" class="submit_button">登录</button>
        <div class="error"><span>+</span></div>
    </form>
    <input type="hidden" value="${securityName}">
</div>

<!-- Javascript-->
<script src="page/assets/js/jquery-1.8.2.min.js" ></script>
<script src="page/assets/js/supersized.3.2.7.min.js" ></script>
<script src="page/assets/js/supersized-init.js" ></script>
<script src="page/assets/js/scripts.js" ></script>

</body>

</html>
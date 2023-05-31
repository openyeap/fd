<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
	<title>${I18n.site_name}</title>
</head>
<body class="hold-transition login-page">
   <div class="login-box">
    <!-- /.login-logo -->
    <div class="card card-outline card-primary">
      <div class="card-header text-center">
        <a href="/" class="h1"><b>F</b>DSA</a>
      </div>
      <div class="card-body">
     	<p class="login-box-msg">${I18n.site_name}</p>
		<form id="loginForm" method="post">
          <div class="input-group mb-3">
            <input type="username" name="username" class="form-control" placeholder="${I18n.login_username_placeholder}">
            <div class="input-group-append">
              <div class="input-group-text">
                <span class="fas fa-envelope"></span>
              </div>
            </div>
          </div>
          <div class="input-group mb-3">
            <input type="password"  name="password" class="form-control" placeholder="${I18n.login_password_placeholder}">
            <div class="input-group-append">
              <div class="input-group-text">
                <span class="fas fa-lock"></span>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-8">
              <div class="icheck-primary">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember">
                  ${I18n.login_remember_me}
                </label>
              </div>
            </div>
            <!-- /.col -->
            <div class="col-4">
              <button type="submit" class="btn btn-primary btn-block">${I18n.login_btn}</button>
            </div>
            <!-- /.col --> 
          </div>
        </form>

        <div class="social-auth-links text-center mt-2 mb-3">
          <a href="#" class="btn btn-block btn-primary">
            <i class="fab fa-weixin mr-2"></i> 通过微信登录
          </a>
          <a href="#" class="btn btn-block btn-danger">
            <i class="fab fa-alipay mr-2"></i> 通过支付宝登录
          </a>
        </div>
        <!-- /.social-auth-links -->

        <p class="mb-1">
          <a href="forgot-password.html">忘记密码</a>
        </p>
        <p class="mb-0">
          <a href="register.html" class="text-center">注册新会员</a>
        </p>
      </div>
      <!-- /.card-body -->
    </div>
    <!-- /.card -->
  </div> 
  
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>

</body>
</html>

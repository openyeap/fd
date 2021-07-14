<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<title>${I18n.admin_name}</title>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <!-- 导航栏 -->
    <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    	<@netCommon.commonHeader />
    </nav>
    <!-- /.导航栏 -->

    <!-- 主侧边栏容器 -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
	    <@netCommon.commonLeft "help" />
    </aside>
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.job_help}</h1>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="callout callout-info">
				<h4>${I18n.admin_name_full}</h4>
				<br>
				<p>
					<a target="_blank" href="https://github.com//">Github</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<iframe src="https://ghbtns.com/github-btn.html?user=&repo=&type=star&count=true" frameborder="0" scrolling="0" width="170px" height="20px" style="margin-bottom:-5px;"></iframe> 
					<br><br>
                    <a target="_blank" href="https://www..com//">${I18n.job_help_document}</a>
                    <br><br>

				</p>
				<p></p>
            </div>
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
</body>
</html>

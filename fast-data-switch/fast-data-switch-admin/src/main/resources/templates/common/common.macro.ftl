<#macro commonStyle>
	<#-- favicon -->
	<link rel="icon" href="${request.contextPath}/static/favicon.ico" />
	<meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<!-- 离线 Google 字体: Source Sans Pro
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/google.css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    -->
	<!-- Font Awesome -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/fontawesome-free/css/all.min.css">
	<!-- Ekko Lightbox -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/ekko-lightbox/ekko-lightbox.css">
	<!-- 主题样式 -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/adminlte.min.css">
	<#-- i18n -->
	<#global I18n = I18nUtil.getMultiString()?eval />
</#macro>

<#macro commonScript>
	<!-- jQuery -->
	<script src="${request.contextPath}/static/adminlte/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="${request.contextPath}/static/adminlte/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- Ekko Lightbox -->
	<script src="${request.contextPath}/static/adminlte/plugins/ekko-lightbox/ekko-lightbox.min.js"></script>
	<!-- FDSA App -->
	<script src="${request.contextPath}/static/adminlte/dist/js/adminlte.min.js"></script>
	<!-- Filterizr-->
	<script src="${request.contextPath}/static/adminlte/plugins/filterizr/jquery.filterizr.min.js"></script>
	<!-- 用于演示 FDSA -->
    <script src="${request.contextPath}/static/adminlte/dist/js/demo.js"></script>

    <!-- 特定页面脚本 -->
	<script>
	$(function () {
		$(document).on('click', '[data-toggle="lightbox"]', function(event) {
            event.preventDefault();
            $(this).ekkoLightbox({
                alwaysShowClose: true
            });
		});

		// $('.filter-container').filterizr({gutterPixels: 3});
		$('.btn[data-filter]').on('click', function() {
		$('.btn[data-filter]').removeClass('active');
		    $(this).addClass('active');
		});
	})
	</script>
</#macro>

<#macro commonHeader>
    <!-- 左侧导航栏链接 -->
    <ul class="navbar-nav">
		<li class="nav-item">
			<a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
		</li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="/" class="nav-link">首页</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="/help" class="nav-link">帮助</a>
        </li>
      
    </ul>
 
    <!-- 右侧导航栏链接 -->
    <ul class="navbar-nav ml-auto">
        <!-- Navbar Search -->
        <li class="nav-item">
            <a class="nav-link" data-widget="navbar-search" href="#" role="button">
            <i class="fas fa-search"></i>
            </a>
            <div class="navbar-search-block">
            <form class="form-inline">
                <div class="input-group input-group-sm">
                <input class="form-control form-control-navbar" type="search" placeholder="搜索" aria-label="Search">
                <div class="input-group-append">
                    <button class="btn btn-navbar" type="submit">
                    <i class="fas fa-search"></i>
                    </button>
                    <button class="btn btn-navbar" type="button" data-widget="navbar-search">
                    <i class="fas fa-times"></i>
                    </button>
                </div>
                </div>
            </form>
            </div>
        </li>
        <#-- 消息  -->
        <li class="nav-item dropdown">
            <a class="nav-link" data-toggle="dropdown" href="#">
                <i class="far fa-comments"></i>
                <span class="caret">2</span>
            </a>
            <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
            <a href="#" class="dropdown-item">
                <!-- 消息开始 -->
                <div class="media">                
                <div class="media-body">
                    <h3 class="dropdown-item-title">
                    John Pierce
                    <span class="float-right text-sm text-muted"><i class="fas fa-star"></i></span>
                    </h3>
                    <p class="text-sm">我收到你的消息了</p>
                    <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 小时前</p>
                </div>
                </div>
                <!-- 消息结束 -->
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
                <!-- 消息开始 -->
                <div class="media">               
                <div class="media-body">
                    <h3 class="dropdown-item-title">
                    Nora Silvester
                    <span class="float-right text-sm text-warning"><i class="fas fa-star"></i></span>
                    </h3>
                    <p class="text-sm">主题在这里</p>
                    <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 小时前</p>
                </div>
                </div>
                <!-- 消息结束 -->
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item dropdown-footer">查看所有消息</a>
            </div>
        </li>
        <!-- 通知下拉菜单 -->
        <li class="nav-item dropdown">
            <a class="nav-link" data-toggle="dropdown" href="#">
            <i class="far fa-bell"></i>
            <span class="badge badge-warning navbar-badge">15</span>
            </a>
            <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
            <span class="dropdown-item dropdown-header">15 条通知</span>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
                <i class="fas fa-envelope mr-2"></i> 4 条新消息
                <span class="float-right text-muted text-sm">3 分钟前</span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
                <i class="fas fa-users mr-2"></i> 8 个好友请求
                <span class="float-right text-muted text-sm">12 小时前</span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
                <i class="fas fa-file mr-2"></i> 3 个新报告
                <span class="float-right text-muted text-sm">2 天前</span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item dropdown-footer">查看所有通知</a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-widget="fullscreen" href="#" role="button">
            <i class="fas fa-expand-arrows-alt"></i>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-widget="control-sidebar" data-slide="true" href="#" role="button">
            <i class="fas fa-th-large"></i>
            </a>
        </li>
        <#-- login user -->
        <li class="nav-item dropdown">
            <a class="nav-link" data-toggle="dropdown" href="#">               
                <span class="caret">${I18n.system_welcome} ${Request.currentUser.name}</span>
            </a>        
            <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
                <a href="#" class="dropdown-item">
                    ${I18n.change_pwd} 
                </a>
                <div class="dropdown-divider"></div>
                <a href="/logout" class="dropdown-item">
                    ${I18n.logout_btn} 
                </a>   
            </div>        
        </li>
    </ul>
    <!-- 修改密码.模态框 -->
    <div class="modal fade" id="updatePwdModal" tabindex="-1" role="dialog"  aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" >${I18n.change_pwd}</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form" >
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">${I18n.change_pwd_field_newpwd}<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input} ${I18n.change_pwd_field_newpwd}" maxlength="100" ></div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</#macro>

<#macro commonLeft pageName >
 
    <!-- 品牌 Logo -->
    <a href="/" class="brand-link">
      <img src="${request.contextPath}/static/adminlte/dist/img/AdminLTELogo.png" alt="FDSA Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
      <span class="brand-text font-weight-light">FDSA</span>
    </a> 

    <div class="sidebar">
        <!-- 侧边栏搜索 -->
        <div class="form-inline">
            <div class="input-group" data-widget="sidebar-search">
            <input class="form-control form-control-sidebar" type="search" placeholder="搜索" aria-label="Search">
            <div class="input-group-append">
                <button class="btn btn-sidebar">
                <i class="fas fa-search fa-fw"></i>
                </button>
            </div>
            </div>
        </div>
        
        <!-- 侧边栏菜单 -->
        <nav class="mt-2">
            <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                
            <li class="nav-item">
                <a href="#" class="nav-link">
                    <i class="nav-icon fas fa-cog"></i>
                    <p>
                    系统管理
                    <i class="right fas fa-angle-left"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                        <a href="${request.contextPath}/static/adminlte/pages/index.html" class="nav-link">
                            <i class="far fa-user nav-icon"></i>
                            <p>用户管理</p>
                        </a>
                    </li>
                    <li class="nav-item">
                       <a href="${request.contextPath}/static/adminlte/pages/index2.html" class="nav-link">
                           <i class="fa fa-object-group nav-icon"></i>
                           <p>配置管理</p>
                       </a>
                    </li>
                </ul>
            </li>
                
            <li class="nav-item">
                <a href="#" class="nav-link">
                    <i class="nav-icon fas fa fa-tasks"></i>
                    <p>
                    任务管理
                    <i class="right fas fa-angle-left"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/index.html" class="nav-link">
                        <i class="far fa-clock nav-icon"></i>
                        <p>任务清单</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/index2.html" class="nav-link">
                        <i class="far fa-history nav-icon"></i>
                        <p>任务日志</p>
                    </a>
                    </li>
                </ul>
            </li>

            <li class="nav-item">
                <a href="#" class="nav-link">
                    <i class="nav-icon fas fa-copy"></i>
                    <p>
                    布局选项
                    <i class="fas fa-angle-left right"></i>
                    <span class="badge badge-info right">6</span>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/top-nav.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>顶部导航</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="layout/top-nav-sidebar.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>顶部导航 + 侧边栏</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/boxed.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>盒式布局</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/fixed-sidebar.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>固定侧栏</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/fixed-sidebar-custom.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>固定侧边栏 <small>+ 自定义区域</small></p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/fixed-topnav.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>固定导航栏</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/fixed-footer.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>固定页脚</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/layout/collapsed-sidebar.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>折叠侧边栏</p>
                    </a>
                    </li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link">
                    <i class="nav-icon fas fa-chart-pie"></i>
                    <p>
                    图表
                    <i class="right fas fa-angle-left"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/charts/chartjs.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>ChartJS 插件</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/charts/flot.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>Flot 插件</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/charts/inline.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>内联图表</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/charts/uplot.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>uPlot</p>
                    </a>
                    </li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                    <i class="nav-icon fas fa-tree"></i>
                    <p>
                    UI 元素
                    <i class="fas fa-angle-left right"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/general.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>常规</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/icons.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>图标</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/buttons.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>按钮</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/sliders.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>滑块</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/modals.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>弹框 & 提醒</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/navbar.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>导航 & 选项卡</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/timeline.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>时间线</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/UI/ribbons.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>丝带</p>
                    </a>
                    </li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                    <i class="nav-icon fas fa-edit"></i>
                    <p>
                    表单
                    <i class="fas fa-angle-left right"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/forms/general.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>常规元素</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/forms/advanced.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>高级表单</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/forms/editors.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>编辑器</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/forms/validation.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>验证</p>
                    </a>
                    </li>
                </ul>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                <i class="nav-icon fas fa-table"></i>
                <p>
                表格
                <i class="fas fa-angle-left right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview">
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/tables/simple.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>简单表格</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/tables/data.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>数据表格</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/tables/jsgrid.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>jsGrid 插件</p>
                </a>
                </li>
            </ul>
            </li>
            <li class="nav-header">示例</li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/calendar.html" class="nav-link">
                <i class="nav-icon far fa-calendar-alt"></i>
                <p>
                日历
                <span class="badge badge-info right">2</span>
                </p>
            </a>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/gallery.html" class="nav-link active">
                <i class="nav-icon far fa-image"></i>
                <p>
                相册
                </p>
            </a>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/kanban.html" class="nav-link">
                <i class="nav-icon fas fa-columns"></i>
                <p>
                看板
                </p>
            </a>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                <i class="nav-icon far fa-envelope"></i>
                <p>
                邮箱
                <i class="fas fa-angle-left right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview">
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/mailbox/mailbox.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>收件箱</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/mailbox/compose.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>写信</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/mailbox/read-mail.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>查看</p>
                </a>
                </li>
            </ul>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                <i class="nav-icon fas fa-book"></i>
                <p>
                页面
                <i class="fas fa-angle-left right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview">
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/invoice.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>发票</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/profile.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>资料</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/e-commerce.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>电子商务</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/projects.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>项目</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/project-add.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>添加项目</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/project-edit.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>编辑项目</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/project-detail.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>项目详情</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/contacts.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>联系人</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/faq.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>FAQ</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/contact-us.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>联系我们</p>
                </a>
                </li>
            </ul>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                <i class="nav-icon far fa-plus-square"></i>
                <p>
                附加
                <i class="fas fa-angle-left right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview">
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>
                    登录 & 注册 v1
                    <i class="fas fa-angle-left right"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/login.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>登录 v1</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/register.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>注册 v1</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/forgot-password.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>忘记密码 v1</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/recover-password.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>重置密码 v1</p>
                    </a>
                    </li>
                </ul>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>
                    登录 & 注册 v2
                    <i class="fas fa-angle-left right"></i>
                    </p>
                </a>
                <ul class="nav nav-treeview">
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/login-v2.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>登录 v2</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/register-v2.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>注册 v2</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/forgot-password-v2.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>忘记密码 v2</p>
                    </a>
                    </li>
                    <li class="nav-item">
                    <a href="${request.contextPath}/static/adminlte/pages/examples/recover-password-v2.html" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>重置密码 v2</p>
                    </a>
                    </li>
                </ul>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/lockscreen.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>锁屏</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/legacy-user-menu.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>传统用户菜单</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/language-menu.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>多国语言</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/404.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>404 错误</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/500.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>500 错误</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/pace.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>加载状态</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/examples/blank.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>空白页</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/../starter.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>起始页</p>
                </a>
                </li>
            </ul>
            </li>
            <li class="nav-item">
            <a href="${request.contextPath}/static/adminlte/pages/#" class="nav-link">
                <i class="nav-icon fas fa-search"></i>
                <p>
                搜索
                <i class="fas fa-angle-left right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview">
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/search/simple.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>普通搜索</p>
                </a>
                </li>
                <li class="nav-item">
                <a href="${request.contextPath}/static/adminlte/pages/search/enhanced.html" class="nav-link">
                    <i class="far fa-circle nav-icon"></i>
                    <p>高级</p>
                </a>
                </li>
            </ul>
            </li> 
            
        </ul>
            
        </nav>
    </div>
 
</#macro>

<#macro commonControl >
	<!-- Control Sidebar -->
	<aside class="control-sidebar control-sidebar-dark">
		<!-- Create the tabs -->
		<ul class="nav nav-tabs nav-justified control-sidebar-tabs">
			<li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
			<li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
			<!-- Home tab content -->
			<div class="tab-pane active" id="control-sidebar-home-tab">
				<h3 class="control-sidebar-heading">近期活动</h3>
				<ul class="control-sidebar-menu">
					<li>
						<a href="javascript::;">
							<i class="menu-icon fa fa-birthday-cake bg-red"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">张三今天过生日</h4>
								<p>2015-09-10</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;"> 
							<i class="menu-icon fa fa-user bg-yellow"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">Frodo 更新了资料</h4>
								<p>更新手机号码 +1(800)555-1234</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;"> 
							<i class="menu-icon fa fa-envelope-o bg-light-blue"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">Nora 加入邮件列表</h4>
								<p>nora@example.com</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;">
						<i class="menu-icon fa fa-file-code-o bg-green"></i>
						<div class="menu-info">
							<h4 class="control-sidebar-subheading">001号定时作业调度</h4>
							<p>5秒前执行</p>
						</div>
						</a>
					</li>
				</ul>
				<!-- /.control-sidebar-menu -->
			</div>
			<!-- /.tab-pane -->

			<!-- Settings tab content -->
			<div class="tab-pane" id="control-sidebar-settings-tab">
				<form method="post">
					<h3 class="control-sidebar-heading">个人设置</h3>
					<div class="form-group">
						<label class="control-sidebar-subheading"> 左侧菜单自适应
							<input type="checkbox" class="pull-right" checked>
						</label>
						<p>左侧菜单栏样式自适应</p>
					</div>
					<!-- /.form-group -->

				</form>
			</div>
			<!-- /.tab-pane -->
		</div>
	</aside>
	<!-- /.control-sidebar -->
	<!-- Add the sidebar's background. This div must be placed immediately after the control sidebar -->
	<div class="control-sidebar-bg"></div>
</#macro>

<#macro commonFooter > 
     <footer class="main-footer">
      <div class="float-right d-none d-sm-block">
        <b>版本</b> ${I18n.admin_version}
      </div>
      <strong>Copyright &copy; 2015-${.now?string('yyyy')}&nbsp;<a href="https://github.com/fdsa-ltd">fdsa.ltd</a>.</strong> 保留所有权利。
    </footer>

</#macro>
window.__POWERED_BY_ADMA__=true;

(function () {
  Promise.all([System.import('single-spa'), System.import('vue'), System.import('vuex'), System.import('vue-router')]).then(function (modules) {
    window.singleSpa = modules[0];
    window.Vue = modules[1];
    window.Vuex = modules[2];
    window.VueRouter = modules[3];
    Vue.use(Vuex);
    const store = new Vuex.Store({
      state: {},
      mutations: {},
      modules: {
      }
    })

    // 0. 如果使用模块化机制编程，导入Vue和VueRouter，要调用 Vue.use(VueRouter)
    Vue.use(VueRouter);

    // 1. 定义 (路由) 组件。
    // 可以从其他文件 import 进来
    const about = { template: '<div>this is about page</div>' }
    // 2. 定义路由
    // 每个路由应该映射一个组件。 其中"component" 可以是
    // 通过 Vue.extend() 创建的组件构造器，
    // 或者，只是一个组件配置对象。
    // 我们晚点再讨论嵌套路由。
    const routes = [
      { path: '/about', component: about }
      ,
      { path: '/', component: { template: $('#home').html() } }
    ]

    // 3. 创建 router 实例，然后传 `routes` 配置
    // 你还可以传别的配置参数, 不过先这么简单着吧。
    const router = new VueRouter({
      mode: 'history',
      routes: routes
    })

    window.app = new Vue({
      el: '#body',
      store,
      router,
      // render: h => h(App)
    })  

    singleSpa.registerApplication(
      'app1',
      () => System.import('app1'),
      location => location.pathname.startsWith('/app1')
    );

    singleSpa.registerApplication(
      'app2',
      () => System.import('app2'),
      location => location.pathname.startsWith('/app2')
    );

    singleSpa.registerApplication(
      'app3',
      () => System.import('app3'),
      location => location.pathname.startsWith("/app3")
    );

    singleSpa.registerApplication(
      'app4',
      () => System.import('app4'),
      location => location.pathname.startsWith("/app4")
    );

    // singleSpa.registerApplication(
    //   'navbar',
    //   () => System.import('navbar'),
    //   location => true
    // );
    // singleSpa.navigateToUrl("/app1");
    // singleSpa.navigateToUrl("/app2");
    // singleSpa.navigateToUrl("/app3");
    // singleSpa.navigateToUrl("/app4");    
    // singleSpa.navigateToUrl("/")
    singleSpa.start();
  })
})()
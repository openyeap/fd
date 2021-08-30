import './set-public-path'
import Vue from 'vue';
import App from './App.vue';
import routes from './router';
import store from './store';
import Router from 'vue-router'
// import singleSpaVue from 'single-spa-vue';
// Vue.config.productionTip = false;

// const vueLifecycles = singleSpaVue({
//   Vue,
//   appOptions: {
//     el: '#app1',
//     store,
//     router,
//     render: h => h(App)
//   },
// });

// export const bootstrap = vueLifecycles.bootstrap;
// export const mount = vueLifecycles.mount;
// export const unmount = vueLifecycles.unmount;
Vue.use(VueRouter);
let vm = null;
let router = null;
function render(props) {
  if (props.name != "") {
    routes.forEach(element => {
      element.path = '/' + props.name + element.path;
    });
    router = new Router({
      mode: 'history',
      base: process.env.BASE_URL,
      routes: routes
    });
  } else {
    router = new Router({
      mode: 'hash',
      base: process.env.BASE_URL,
      routes: routes
    });
  }
  vm = new Vue({
    router,
    store,
    render: h => h(App),
  });
  vm.$mount("#"+props.name);
}

if (!window.__POWERED_BY_ADMA__) {
  alert("test")
  render({});
}

export async function bootstrap(props) {
  console.log('[vue] app bootstraped', props);
}

export async function mount(props) {
  console.log('[vue] app mount', props);
  render(props);
}

export async function unmount(props) {
  console.log('[vue] app mount', props);
  // vm.$destroy();
  // vm.$el.innerHTML = '';
  // instance = null;
  // router = null;
}

import { setPublicPath } from 'systemjs-webpack-interop'

import Vue from 'vue'
import App from './App.vue'
import routes from './router';
import store from './store';
import Router from 'vue-router'
Vue.config.productionTip = false

 


let vm = null;

function render(props) {
  var router;
  console.log(props.name)
  if (props.name) {
    routes.forEach(element => {
      element.path = '/' + props.name + element.path;
    });
    router = new Router({
      mode: 'history',
      base: process.env.BASE_URL,
      routes: routes
    });
    setPublicPath('app2', 2)
  } else {
    router = new Router({
      mode: 'hash',
      base: process.env.BASE_URL,
      routes: routes
    });
  }
  vm = new Vue({
    el: "#app",
    router,
    store,
    render: h => h(App),
  });
  if (document.querySelector("#" + props.name)) {
    vm.$mount("#" + props.name);
  } else {
    vm.$mount('#app')
  }
}

if (!window.__POWERED_BY_ADMA__) {
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
  vm.$el.innerHTML = '';
  // instance = null;
  // router = null;
}

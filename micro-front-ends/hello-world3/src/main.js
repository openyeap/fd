import { createApp } from 'vue'
import App from './App.vue'




// let vm = null;
// let router = null;
function render(props) {
    console.log(props)
    //   if (props.name != "") {
    //     // routes.forEach(element => {
    //     //   element.path = '/' + props.name + element.path;
    //     // });
    //     router = new Router({
    //       mode: 'history',
    //       base: process.env.BASE_URL,
    //     //   routes: routes
    //     });
    //   } else {
    //     router = new Router({
    //       mode: 'hash',
    //       base: process.env.BASE_URL,
    //       routes: routes
    //     });
    //   }
    
createApp(App).mount('#app')

}

if (!window.__POWERED_BY_ADMA__) {
    alert("test");
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


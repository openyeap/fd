import Router from 'vue-router'


export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '',
      name: 'home',
      component: () => import('./views/Home.vue')
    },
    {
      path: '/:name',
      name: 'home',
      component: () => import('./views/Home.vue')
    } ,
    {
      path: '/:name/:page2',
      name: 'home',
      component: () => import('./views/Home.vue')
    } 
  ]
})

import Home from './views/Home.vue'

export default [
  {
    path: '/',
    component: Home
  },
  {
    path: '/about',
    component: () => import('./views/About.vue')
  }
]
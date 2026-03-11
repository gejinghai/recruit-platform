import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue')
  },
  {
    path: '/',
    component: () => import('../layouts/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue')
      },
      {
        path: 'job',
        name: 'Job',
        component: () => import('../views/job/list.vue')
      },
      {
        path: 'job/add',
        name: 'JobAdd',
        component: () => import('../views/job/form.vue')
      },
      {
        path: 'job/edit/:id',
        name: 'JobEdit',
        component: () => import('../views/job/form.vue')
      },
      {
        path: 'ad',
        name: 'Ad',
        component: () => import('../views/ad/content.vue')
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import('../views/system/password.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else if (to.path === '/login' && userStore.token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router

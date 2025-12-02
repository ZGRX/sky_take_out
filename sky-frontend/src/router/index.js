import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'
import Layout from '../components/Layout.vue'
import DishList from '../views/DishList.vue'
import OrderList from '../views/OrderList.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dish',
    children: [
      {
        path: 'dish',
        name: 'Dish',
        component: DishList
      },
      {
        path: 'order',
        name: 'Order',
        component: OrderList
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login') {
    next('/login')
  } else if (token && to.path === '/login') {
    next('/')
  } else {
    next()
  }
})

export default router

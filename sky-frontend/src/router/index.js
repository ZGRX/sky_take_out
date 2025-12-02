import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'
import Layout from '../components/Layout.vue'
import DishList from '../views/DishList.vue'
import OrderList from '../views/OrderList.vue'
import EmployeeList from '../views/EmployeeList.vue'
import CategoryList from '../views/CategoryList.vue'
import SetmealList from '../views/SetmealList.vue'
import StatisticsDashboard from '../views/StatisticsDashboard.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/employee',
    children: [
      {
        path: 'employee',
        name: 'Employee',
        component: EmployeeList
      },
      {
        path: 'category',
        name: 'Category',
        component: CategoryList
      },
      {
        path: 'dish',
        name: 'Dish',
        component: DishList
      },
      {
        path: 'setmeal',
        name: 'Setmeal',
        component: SetmealList
      },
      {
        path: 'order',
        name: 'Order',
        component: OrderList
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: StatisticsDashboard
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

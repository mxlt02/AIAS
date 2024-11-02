import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '/',
        component: () => import('@/views/general/index'),
        name: 'inference',
        meta: { title: '通用文字识别', icon: 'el-icon-full-screen' }
      }
    ]
  },
  {
    path: '/mlsd',
    component: Layout,
    children: [
      {
        path: '/mlsd',
        component: () => import('@/views/mlsd/index'),
        name: 'inference',
        meta: { title: '文本转正', icon: 'el-icon-c-scale-to-original' }
      }
    ]
  },
  {
    path: '/table',
    component: Layout,
    name: 'table',
    meta: { title: '表格识别', icon: 'el-icon-s-grid' },
    children: [
      {
        path: 'singleTable',
        component: () => import('@/views/table/singleTable'),
        name: 'singleTable',
        meta: { title: '英文表格识别', icon: 'el-icon-s-grid' }
      },
      {
        path: 'autoTable',
        component: () => import('@/views/table/autoTable'),
        name: 'autoTable',
        meta: { title: '自动表格识别', icon: 'el-icon-s-grid' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
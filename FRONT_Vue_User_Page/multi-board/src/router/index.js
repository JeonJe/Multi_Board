import { createRouter, createWebHistory } from "vue-router";
import UserLogin from "../views/auth/UserLogin.vue";
import UserSignup from "../views/auth/UserSignup.vue";

import BoardNoticeList from "../views/boards/notice/BoardNoticeList.vue";
import BoardNoticeWrite from "../views/boards/notice/BoardNoticeWrite.vue";
import BoardNoticeView from "../views/boards/notice/BoardNoticeView.vue";

const routes = [
  {
    path: process.env.VUE_APP_USER_LOGIN_PAGE,
    name: UserLogin,
    component: UserLogin,
  },
  {
    path: process.env.VUE_APP_USER_SIGNUP_PAGE,
    name: UserSignup,
    component: UserSignup,
  },
  {
    path: process.env.VUE_APP_BOARD_NOTICE_LIST,
    name: BoardNoticeList,
    component: BoardNoticeList,
  },
  {
    path: process.env.VUE_APP_BOARD_NOTICE_WIRTE,
    name: BoardNoticeWrite,
    component: BoardNoticeWrite,
  },
  {
    path: process.env.VUE_APP_BOARD_NOTICE_VIEW,
    name: BoardNoticeView,
    component: BoardNoticeView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

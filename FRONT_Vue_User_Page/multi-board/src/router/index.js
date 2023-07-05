import { createRouter, createWebHistory } from "vue-router";
import UserLogin from "../views/auth/UserLogin.vue";
import UserSignup from "../views/auth/UserSignup.vue";

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
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

import { createRouter, createWebHistory } from "vue-router";
import UserLogin from "../views/auth/UserLogin.vue";
import UserSignup from "../views/auth/UserSignup.vue";

const routes = [
  {
    path: "/auth/login",
    name: UserLogin,
    component: UserLogin,
  },
  {
    path: "/auth/signup",
    name: UserSignup,
    component: UserSignup,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

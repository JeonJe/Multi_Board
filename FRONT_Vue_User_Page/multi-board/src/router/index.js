import { createRouter, createWebHistory } from "vue-router";
import UserLogin from "../views/auth/UserLogin.vue";

const routes = [
  {
    path: "/auth/login",
    name: UserLogin,
    component: UserLogin,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

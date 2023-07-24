import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store/user";
import BootstrapVue3 from "bootstrap-vue-3";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-3/dist/bootstrap-vue-3.css";
import "@/scss/custom.scss";
const app = createApp(App);
// app.config.globalProperties.$CryptoJS = CryptoJS
// app.use(router).mount("#app");

app.use(store);
app.use(router);
app.use(BootstrapVue3);
app.mount("#app");

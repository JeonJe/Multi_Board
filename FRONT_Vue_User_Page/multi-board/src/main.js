import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

// const app = createApp(App);
// app.config.globalProperties.$CryptoJS = CryptoJS
// app.use(router).mount("#app");

createApp(App).use(store).use(router).mount("#app");

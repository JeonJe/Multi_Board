<template>
  <div>
    <h1>로그인</h1>
    <form @submit.prevent>
      <div>
        <input
          type="text"
          id="id"
          v-model="userData.userId"
          placeholder="아이디"
        />
      </div>
      <div>
        <input
          type="password"
          id="password"
          v-model="userData.password"
          :placeholder="passwordPlaceholder"
        />
      </div>
      <button @click="login">로그인</button>
    </form>
    <button @click="signup">회원가입</button>
  </div>
</template>

<script>
import { apiLogin } from "@/service/services";
export default {
  data() {
    return {
      userData: {
        userId: "",
        password: "",
      },
    };
  },
  computed: {
    passwordPlaceholder() {
      return this.password ? "" : "비밀번호";
    },
  },
  methods: {
    login() {
      apiLogin(this.userData);
    },
    signup() {
      this.$router.push({ path: process.env.VUE_APP_USER_SIGNUP_PAGE });
    },
  },
};
</script>

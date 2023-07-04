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
      <button @click="clickLogin">로그인</button>
    </form>
    <button @click="clickSignup">회원가입</button>
    <button @click="clickCheckToken">Jwt토큰확인</button>
  </div>
</template>

<script>
import { loginUser, checkJwtToken } from "@/service/services";
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
    clickLogin() {
      loginUser(this.userData);
    },
    clickSignup() {
      this.$router.push({ path: process.env.VUE_APP_USER_SIGNUP_PAGE });
    },
    clickCheckToken() {
      checkJwtToken();
    },
  },
};
</script>

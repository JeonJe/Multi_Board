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
    <button @click="clickRemoveToken">Jwt토큰제거</button>
  </div>
</template>

<script>
import userService from "@/services/user-service";
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
    /**
     * 로그인 버튼 클릭 이벤트 핸들러 함수입니다.
     * userService를 사용하여 사용자 로그인을 처리합니다.
     */
    clickLogin() {
      userService.loginUser(this.userData);
    },
    /**
     * 회원가입 버튼 클릭 이벤트 핸들러 함수입니다.
     * 회원가입 페이지로 이동합니다.
     */
    clickSignup() {
      this.$router.push({ path: process.env.VUE_APP_USER_SIGNUP_PAGE });
    },
    /**
     * JWT 토큰 확인 버튼 클릭 이벤트 핸들러 함수입니다.
     * userService를 사용하여 JWT 토큰을 확인합니다.
     */
    clickCheckToken() {
      userService.getUserIDByJWT();
    },
    clickRemoveToken() {
      localStorage.removeItem("jwt");
    },
  },
};
</script>

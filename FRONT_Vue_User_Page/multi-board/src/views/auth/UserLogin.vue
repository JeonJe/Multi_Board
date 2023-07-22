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
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
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
    ...mapGetters(["isLoggedIn"]),
  },
  methods: {
    ...mapActions(["setLoginUser"]),
    /**
     * 로그인 버튼 클릭 이벤트 핸들러 함수
     * userService를 사용하여 사용자 로그인을 처리합니다.
     */
    async clickLogin() {
      const response = await userService.loginUser(this.userData);
      if (response) {
        console.log("response : ", response);
        await this.setLoginUser(response);
        this.$router.push({ path: process.env.VUE_APP_BOARD_FREE_LIST });
      }
    },
    /**
     * 회원가입 버튼 클릭 이벤트 핸들러 함수
     * 회원가입 페이지로 이동합니다.
     */
    clickSignup() {
      this.$router.push({ path: process.env.VUE_APP_USER_SIGNUP_PAGE });
    },
  },
};
</script>

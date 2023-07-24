<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-4">
        <h1 class="text-center">로그인</h1>
        <form @submit.prevent>
          <div class="form-outline mb-4">
            <input
              type="text"
              id="id"
              v-model="userData.userId"
              class="form-control"
              placeholder="아이디"
            />
          </div>
          <div class="form-outline mb-4">
            <input
              type="password"
              id="password"
              v-model="userData.password"
              class="form-control"
              :placeholder="passwordPlaceholder"
            />
          </div>
          <div class="d-flex justify-content-center">
            <button @click="clickLogin" class="btn btn-primary mx-2">
              로그인
            </button>
            <button @click="clickSignup" class="btn btn-secondary mx-2">
              회원가입
            </button>
          </div>
        </form>
      </div>
    </div>
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
        await this.setLoginUser(response);
        this.$router.replace({ path: process.env.VUE_APP_BOARD_FREE_LIST });
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

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-4">
        <h1 class="text-center">회원가입</h1>
        <form @submit.prevent>
          <div class="form-outline mb-4 d-flex align-items-center">
            <input
              type="text"
              id="id"
              v-model="userData.userId"
              class="form-control col-md-2"
              placeholder="아이디"
              required
            />
            <button
              @click="clickIsIdDuplicate"
              class="btn btn-primary btn-sm ml-2 col-md-2"
            >
              중복확인
            </button>
          </div>
          <div class="form-outline mb-4">
            <input
              type="password"
              id="password"
              v-model="userData.password"
              class="form-control"
              :placeholder="passwordPlaceholder"
              required
            />
          </div>
          <div class="form-outline mb-4">
            <input
              type="password"
              id="confirmPassword"
              v-model="userData.confirmPassword"
              class="form-control"
              :placeholder="confirmPasswordPlaceholder"
              required
            />
          </div>
          <div class="form-outline mb-4">
            <input
              type="text"
              id="name"
              v-model="userData.name"
              class="form-control"
              placeholder="이름"
            />
          </div>
          <div class="d-flex justify-content-center">
            <button @click="clickSignup" class="btn btn-primary mx-2">
              회원가입
            </button>
            <button @click="clickMoveToLogin" class="btn btn-danger mx-2">
              취소
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
        confirmPassword: "",
        name: "",
      },
    };
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
    passwordPlaceholder() {
      return this.password ? "" : "비밀번호";
    },
    confirmPasswordPlaceholder() {
      return this.confirmPassword ? "" : "비밀번호 확인";
    },
  },
  methods: {
    ...mapActions(["setLoginUser", "getUser"]),
    /**
     * 회원가입 버튼 클릭 이벤트 핸들러 함수
     * userService를 사용하여 사용자 회원가입을 처리합니다.
     */
    async clickSignup() {
      const response = await userService.signupUser(this.userData);
      if (response) {
        await this.setLoginUser(response);
        this.$router.push({ path: process.env.VUE_APP_BOARD_FREE_LIST });
      }
    },
    /**
     * 로그인 페이지로 이동하는 버튼 클릭 이벤트 핸들러 함수
     * 로그인 페이지로 이동합니다.
     */
    clickMoveToLogin() {
      this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
    },
    /**
     * 아이디 중복 확인 버튼 클릭 이벤트 핸들러 함수
     * userService를 사용하여 아이디 중복 여부를 확인합니다.
     */
    clickIsIdDuplicate() {
      userService.checkDuplicateId(this.userData.userId);
    },
  },
};
</script>

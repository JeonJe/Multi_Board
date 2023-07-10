<template>
  <div>
    <h1>회원가입</h1>
    <form @submit.prevent>
      <div>
        <input
          type="text"
          id="id"
          v-model="userData.userId"
          placeholder="아이디"
        />
        <button @click="clickIsIdDuplicate">중복확인</button>
      </div>
      <div>
        <input
          type="password"
          id="password"
          v-model="userData.password"
          :placeholder="passwordPlaceholder"
        />
      </div>
      <div>
        <input
          type="password"
          id="confirmPassword"
          v-model="userData.confirmPassword"
          :placeholder="confirmPasswordPlaceholder"
        />
      </div>
      <div>
        <input
          type="text"
          id="name"
          v-model="userData.name"
          placeholder="이름"
        />
      </div>
      <button @click="clickSignup">회원가입</button>
    </form>
    <button @click="clickMoveToLogin">취소</button>
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
        confirmPassword: "",
        name: "",
      },
    };
  },
  computed: {
    passwordPlaceholder() {
      return this.password ? "" : "비밀번호";
    },
    confirmPasswordPlaceholder() {
      return this.confirmPassword ? "" : "비밀번호 확인";
    },
  },
  methods: {
    /**
     * 회원가입 버튼 클릭 이벤트 핸들러 함수입니다.
     * userService를 사용하여 사용자 회원가입을 처리합니다.
     */
    clickSignup() {
      userService.signupUser(this.userData);
    },
    /**
     * 로그인 페이지로 이동하는 버튼 클릭 이벤트 핸들러 함수입니다.
     * 로그인 페이지로 이동합니다.
     */
    clickMoveToLogin() {
      this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
    },
    /**
     * 아이디 중복 확인 버튼 클릭 이벤트 핸들러 함수입니다.
     * userService를 사용하여 아이디 중복 여부를 확인합니다.
     */
    clickIsIdDuplicate() {
      userService.checkDuplicateId(this.userData.userId);
    },
  },
};
</script>

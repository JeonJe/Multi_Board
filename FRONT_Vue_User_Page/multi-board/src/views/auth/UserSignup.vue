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
    clickSignup() {
      userService.signupUser(this.userData);
    },
    clickMoveToLogin() {
      this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
    },
    clickIsIdDuplicate() {
      userService.checkDuplicateId(this.userData.userId);
    },
  },
};
</script>

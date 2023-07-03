<template>
  <div>
    <h1>회원가입</h1>
    <form>
      <div>
        <input type="text" id="id" v-model="userData.id" placeholder="아이디" />
        <button @click="isIdDuplicate">중복확인</button>
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
      <button @click="signup">회원 가입 하기</button>
    </form>
    <button @click="moveToLogin">취소</button>
  </div>
</template>

<script>
import { apiSignup } from "@/service/services";

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
    signup() {
      apiSignup(this.userData);
    },
    moveToLogin() {
      this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
    },
    isIdDuplicate(id) {
      alert(id);
    },
  },
};
</script>

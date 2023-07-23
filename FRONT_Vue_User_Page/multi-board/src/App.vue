<template>
  <nav>
    <router-link to="/boards/notice/list">공지사항</router-link>
    <router-link to="/boards/free/list">자유 게시판</router-link>
    <router-link to="/auth/login">로그인</router-link>
  </nav>
  <span v-if="isLoggedIn">{{ getUser.name }}님 로그인 중입니다.</span>
  <button v-if="isLoggedIn" @click="clickLogout">로그아웃</button>

  <router-view />
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  computed: {
    ...mapGetters(["isLoggedIn", "getUser"]),
  },
  methods: {
    ...mapActions(["clearLoginUser"]),
    /**
     * 로그아웃 버튼 클릭 이벤트 핸들러 함수
     * 사용자를 로그아웃하고 공지사항 목록 페이지로 이동
     */
    async clickLogout() {
      await this.clearLoginUser();
      this.$router.replace("/auth/login");
    },
  },
};
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

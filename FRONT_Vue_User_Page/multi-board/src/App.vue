<template>
  <div class="container">
    <nav class="row justify-content-center">
      <div class="d-flex">
        <router-link
          v-for="(link, index) in navLinks"
          :key="index"
          :to="link.to"
          class=""
          style="margin-right: 10px"
          >{{ link.text }}
        </router-link>
      </div>
    </nav>

    <div class="row justify-content-end">
      <div class="col-md-auto">
        <span v-if="isLoggedIn">
          <strong>{{ getUser.name }}</strong>
          님 로그인 중입니다.
        </span>
      </div>
    </div>
    <div class="row justify-content-end mt-2">
      <div class="col-md-auto">
        <button
          v-if="isLoggedIn"
          @click="clickLogout"
          class="btn btn-danger btn-sm"
        >
          로그아웃
        </button>
      </div>
    </div>

    <router-view />
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  data() {
    return {
      navLinks: [
        { to: "/boards/notice/list", text: "공지사항" },
        { to: "/boards/free/list", text: "자유 게시판" },
        { to: "/auth/login", text: "로그인" },
      ],
    };
  },
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

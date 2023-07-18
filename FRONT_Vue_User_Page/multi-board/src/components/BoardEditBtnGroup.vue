<template>
  <div>
    <button v-if="editPermission" @click="emitUpdateBoard">수정</button>
    <button v-if="editPermission" @click="emitDeleteBoard">삭제</button>
    <router-link :to="moveToFreeBoardList()">목록으로</router-link>
  </div>
</template>

<script>
export default {
  props: {
    editPermission: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    emitUpdateBoard() {
      this.$emit("emitUpdateBoard");
    },
    emitDeleteBoard() {
      this.$emit("emitDeleteBoard");
    },
    moveToFreeBoardList() {
      return {
        path: process.env.VUE_APP_BOARD_FREE_LIST,
        query: this.$route.query,
      };
    },
  },
};
</script>

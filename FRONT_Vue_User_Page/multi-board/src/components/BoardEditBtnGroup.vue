<template>
  <div>
    <div class="d-flex justify-content-center my-2">
      <button
        v-if="editPermission"
        @click="emitUpdateBoard"
        class="btn btn-primary mx-2"
      >
        수정
      </button>
      <button
        v-if="editPermission"
        @click="emitDeleteBoard"
        class="btn btn-danger mx-2"
      >
        삭제
      </button>
      <button @click="emitBackToList" class="btn btn-secondary mx-2">
        목록으로
      </button>
    </div>
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
    /**
     * 부모 컴포넌트로 'emitUpdateBoard' 이벤트를 전달하는 함수
     * @returns {void}
     */
    emitUpdateBoard() {
      this.$emit("emitUpdateBoard");
    },
    /**
     * 부모 컴포넌트로 'emitDeleteBoard' 이벤트를 전달하는 함수
     * @returns {void}
     */
    emitDeleteBoard() {
      this.$emit("emitDeleteBoard");
    },
    /**
     * 부모 컴포넌트로 'emitBackToList' 이벤트를 전달하는 함수
     * @returns {void}
     */
    emitBackToList() {
      this.$emit("emitBackToList");
    },
  },
};
</script>

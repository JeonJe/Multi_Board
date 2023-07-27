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
    emitUpdateBoard() {
      this.$emit("emitUpdateBoard");
    },
    emitDeleteBoard() {
      this.$emit("emitDeleteBoard");
    },
    emitBackToList() {
      this.$emit("emitBackToList");
    },
  },
};
</script>

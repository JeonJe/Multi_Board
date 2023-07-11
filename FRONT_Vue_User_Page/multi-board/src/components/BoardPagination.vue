<template>
  <div>
    <!-- 처음 페이지 링크 -->
    <a v-if="currentPage > 1" @click="clickPagination(1)">&lt;&lt;&nbsp;</a>
    <!-- 이전 페이지 링크  -->
    <a v-if="currentPage > 1" @click="clickPagination(currentPage - 1)"
      >&lt;&nbsp;</a
    >
    <template v-for="page in totalPages" :key="page">
      <!-- 현재 페이지 링크 -->
      <strong v-if="page === currentPage">{{ page }}</strong>
      <a v-else @click="clickPagination(page)">{{ page }}</a>
    </template>
    <!-- 다음 페이지 링크 -->
    <a v-if="currentPage < totalPages" @click="clickPagination(currentPage + 1)"
      >&nbsp;&gt;</a
    >
    <!-- 마지막 페이지 링크 -->
    <a v-if="currentPage < totalPages" @click="clickPagination(totalPages)"
      >&nbsp;&gt;&gt;</a
    >
  </div>
</template>

<script>
export default {
  props: {
    /**
     * 현재 페이지 번호
     */
    currentPage: {
      type: Number,
      required: true,
    },
    /**
     * 총 페이지 수
     */
    totalPages: {
      type: Number,
      required: true,
    },
  },
  methods: {
    /**
     * 페이지 링크 클릭 이벤트를 처리합니다.
     *
     * @param {number} page - 이동할 페이지 번호
     */
    clickPagination(page) {
      this.$emit("clickPagination", page);
    },
  },
};
</script>

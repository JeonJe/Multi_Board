<template>
  <div class="container">
    <h1>공지사항</h1>

    <SearchForm @emitSearchContion="getSearchCondtion" />
    <!-- 총 조회 건수 -->

    <!-- 게시글 리스트 -->

    <!-- 페이지네이션 -->
  </div>
</template>

<script>
// import { getNoticeBoardList } from "@/services/board-service";
import SearchForm from "@/components/SearchForm.vue";

export default {
  components: {
    SearchForm,
  },
  data() {
    return {
      searchCondition: {},
      // searchCondition: this.createDefaultSearchCondition(),
      boardList: [],
      categories: [],
      totalPosts: 0,
      totalPages: 0,
    };
  },
  mounted() {
    // getNoticeBoardList(this.searchCondition);
  },
  methods: {
    getSearchCondtion(searchCondition) {
      console.log(searchCondition);
    },
  },
};
</script>

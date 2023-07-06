<template>
  <div class="container">
    <h1>공지사항</h1>

    <SearchForm @emitSearchContion="updateSearchCondtion" />
    <!-- 총 조회 건수 -->

    <!-- 게시글 리스트 -->
    <table>
      <thead>
        <tr>
          <th>번호</th>
          <th>분류</th>
          <th>제목</th>
          <th>조회</th>
          <th>등록일시</th>
          <th>등록자</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
          <td>{{ index + 1 }}</td>
          <td><!-- 분류 데이터 출력 --></td>
          <td>{{ item.title }}</td>
          <td>{{ item.visitCount }}</td>
          <td>{{ getFormattedDate(item.createdAt) }}</td>
          <td>{{ item.userId }}</td>
        </tr>
      </tbody>
    </table>
    <!-- 페이지네이션 -->
  </div>
</template>

<script>
import SearchForm from "@/components/SearchForm.vue";
import boardService from "@/services/board-service";
import moment from "moment";

export default {
  components: {
    SearchForm,
  },
  data() {
    return {
      searchCondition: {},
      searchBoardList: [],
      categories: [],
      totalPosts: 0,
      totalPages: 0,
    };
  },
  mounted() {
    this.getNoticeBoardList();
  },
  methods: {
    updateSearchCondtion(searchCondition) {
      this.searchCondition = searchCondition;
      this.getNoticeBoardList();
    },
    async getNoticeBoardList() {
      try {
        const response = await boardService.getNoticeBoardList(
          this.searchCondition
        );
        this.searchBoardList = response.data;
      } catch (error) {
        console.log(error);
      }
    },
    getFormattedDate(createdAt) {
      return moment(createdAt).format("YYYY.MM.DD HH:mm");
    },
  },
};
</script>

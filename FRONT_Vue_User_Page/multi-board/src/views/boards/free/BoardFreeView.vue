<template>
  <div>
    <div v-if="boardInfo">
      <h1>공지사항</h1>
      <p>분류 : {{ boardInfo.categoryName }}</p>
      <p>제목 : {{ boardInfo.title }}</p>
      <p>생성일시 : {{ getFormattedDate(boardInfo.createdAt) }}</p>
      <p>작성자 : {{ boardInfo.userId }}</p>
      <p>조회수 : {{ boardInfo.visitCount }}</p>
      <p>내용 : {{ boardInfo.content }}</p>
      <router-link :to="moveToFreeBoardList()"> 목록으로 </router-link>
    </div>
    <div v-else>
      <p>내용을 가져오는 중입니다.</p>
    </div>
  </div>
</template>

<script>
import boardService from "@/services/board-service";
import { getFormattedDate } from "@/utils/util";

export default {
  data() {
    return {
      /**
       * 자유게시글의 상세 정보 데이터
       */
      boardInfo: null,
    };
  },
  mounted() {
    /**
     * boardId에 해당하는 자유 게시글 상세 정보를 가져옵니다.
     */
    this.getFreeBoardDetail(this.$route.params.boardId);
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수입니다.
     */
    getFormattedDate,
    /**
     * 공지사항 상세 정보를 가져오는 비동기 함수입니다.
     * @param {number} boardId - 공지사항 게시글의 ID
     */
    async getFreeBoardDetail(boardId) {
      try {
        const response = await boardService.getBoardDetail("free", boardId);
        if (response.data != "") {
          this.boardInfo = response.data;
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 공지사항 목록 페이지로 이동하는 함수입니다.
     * @returns {Object} - 공지사항 목록 페이지의 URL과 query
     */
    moveToFreeBoardList() {
      return {
        path: process.env.VUE_APP_BOARD_FREE_LIST,
        query: this.$route.query,
      };
    },
  },
};
</script>

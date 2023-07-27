<template>
  <div class="container">
    <h1>공지사항</h1>
    <br />
    <div v-if="boardInfo">
      <div class="row">
        <div class="col-md-12">
          <div class="d-flex">
            <div class="col-md-1">
              <p class="font-weight-bold text-dark">
                {{ boardInfo.categoryName }}
              </p>
            </div>
            <div class="col-md-8 text-left">
              {{ boardInfo.title }}
            </div>
            <div class="col-md-2">
              {{ getFormattedDate(boardInfo.createdAt) }}
            </div>
            <div class="col-md-1">{{ boardInfo.userId }}</div>
          </div>
        </div>
        <hr />
        <div class="d-flex justify-content-end">
          <p>조회수: {{ boardInfo.visitCount }}</p>
        </div>
      </div>
      <div
        class="mt-4 ml-2 mb-4 border p-3 text-left"
        style="overflow: auto; word-wrap: break-word"
      >
        <p>{{ boardInfo.content }}</p>
      </div>
    </div>
    <div v-else>
      <p>내용을 가져오는 중입니다.</p>
    </div>
    <BoardEditBtnGroup
      :editPermission="false"
      @emitUpdateBoard="clickEditBtn(boardId)"
      @emitDeleteBoard="clickDeleteBtn(boardId)"
      @emitBackToList="clickBackToListBtn()"
    />
  </div>
</template>

<script>
import BoardEditBtnGroup from "@/components/BoardEditBtnGroup.vue";
import boardService from "@/services/board-service";
import { getFormattedDate } from "@/utils/util";

export default {
  components: {
    BoardEditBtnGroup,
  },
  data() {
    return {
      /**
       * 공지사항 상세 정보 데이터
       */
      boardInfo: null,
    };
  },
  mounted() {
    /**
     * boardId에 해당하는 공지 게시글 상세 정보 조회
     */
    this.getNoticeBoardDetail(this.$route.params.boardId);
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수
     */
    getFormattedDate,
    /**
     * 공지사항 상세 정보를 가져오는 비동기 함수
     * @param {number} boardId - 공지사항 게시글의 ID
     */
    async getNoticeBoardDetail(boardId) {
      try {
        const response = await boardService.getBoardDetail("notice", boardId);
        if (response.data != "") {
          this.boardInfo = response.data;
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 공지사항 목록 페이지로 이동하는 함수
     * @returns {Object} - 공지사항 목록 페이지의 URL과 query
     */
    clickBackToListBtn() {
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "notice"
      );
    },
  },
};
</script>

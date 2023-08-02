<template>
  <div>
    <div class="container">
      <h1>문의게시판</h1>
      <br />
      <!-- 게시판 내용 -->
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
          <p>
            {{ boardInfo.content }}
          </p>
        </div>
        <div class="bg-light">
          <div
            v-for="reply in boardInfo.boardReplies"
            :key="reply.replyId"
            class="mb-4"
          >
            <div class="d-flex justify-content-between mb-2">
              <div class="mx-2">
                <strong> {{ reply.adminId }} </strong>
                {{ getFormattedDate(reply.createdAt) }}
              </div>
            </div>
            <div class="m-2" style="overflow: auto; word-wrap: break-word">
              <p>{{ reply.content }}</p>
            </div>
            <hr />
          </div>
        </div>
      </div>
      <div v-else>
        <p>내용을 가져올 수 없습니다.</p>
      </div>
      <BoardEditBtnGroup
        :editPermission="editPermission"
        @emitUpdateBoard="clickEditBtn(boardId)"
        @emitDeleteBoard="clickDeleteBtn(boardId)"
        @emitBackToList="clickBackToListBtn()"
      />
    </div>
  </div>
</template>

<script>
import { getFormattedDate } from "@/utils/util";
import { mapGetters } from "vuex";
import boardService from "@/services/board-service";
import BoardEditBtnGroup from "@/components/BoardEditBtnGroup.vue";

export default {
  components: {
    BoardEditBtnGroup,
  },
  data() {
    return {
      boardInfo: null,
      boardId: null,
      newComment: "",
      editPermission: false,
    };
  },
  mounted() {
    this.boardId = this.$route.params.boardId;
    this.getInquiryBoardDetail(this.boardId);
  },
  computed: {
    ...mapGetters(["isLoggedIn", "getUser"]),
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수입니다.
     */
    getFormattedDate,
    /**
     * 공지사항 상세 정보를 가져오는 비동기 함수
     * @param {number} boardId - 공지사항 게시글의 ID
     */
    async getInquiryBoardDetail(boardId) {
      try {
        const response = await boardService.getBoardDetail("inquiry", boardId);
        if (response.data != "") {
          this.boardInfo = response.data;
        }

        this.editPermission = await boardService.hasInquiryBoardEditPermission(
          boardId
        );
      } catch (error) {
        alert(error);
      }
    },
    /**
     * 게시글 수정 버튼 클릭 이벤트 핸들러 함수
     * @param {number} boardId - 수정할 게시글의 ID
     * @returns {void}
     */
    async clickEditBtn(boardId) {
      this.$router.push({
        path: `${process.env.VUE_APP_BOARD_FREE_WRITE}/${boardId}`,
        query: this.$route.query,
      });
    },
    /**
     * 게시글 삭제 버튼 클릭 이벤트 핸들러 함수
     * @param {number} boardId - 삭제할 게시글의 ID
     * @returns {void}
     */
    async clickDeleteBtn(boardId) {
      if (await boardService.deleteBoardInfo("inquiry", boardId)) {
        boardService.replaceRouterToBoardList(
          this.$router,
          this.$route,
          "inquiry"
        );
      } else {
        alert("삭제가 불가합니다. 댓글이 남아있는지 확인해주세요.");
      }
    },
    /**
     * 댓글 작성 버튼 클릭 이벤트 핸들러 함수
     * @param {string} newComment - 작성한 댓글 내용
     * @param {number} boardId - 댓글이 작성된 게시글의 ID
     * @returns {void}
     */
    async clickSumbitCommentBtn(newComment, boardId) {
      await boardService.addInquiryBoardComment(newComment, boardId);
      await this.getInquiryBoardDetail(boardId);
    },
    /**
     * 댓글 삭제 버튼 클릭 이벤트 핸들러 함수
     * @param {Object} comment - 삭제할 댓글 정보 객체
     * @param {number} boardId - 댓글이 작성된 게시글의 ID
     * @returns {void}
     */
    async clickCommentDeleteBtn(comment, boardId) {
      await boardService.deleteInquiryBoardComment(comment, boardId);
      await this.getInquiryBoardDetail(boardId);
    },
    /**
     * 공지사항 목록 페이지로 이동하는 함수
     * @returns {Object} - 공지사항 목록 페이지의 URL과 query
     */
    clickBackToListBtn() {
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "inquiry"
      );
    },
  },
};
</script>

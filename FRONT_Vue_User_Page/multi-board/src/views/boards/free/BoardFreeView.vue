<template>
  <div>
    <div v-if="boardInfo">
      <h1>공지사항</h1>
      <!-- 게시판 내용 -->
      <p>분류 : {{ boardInfo.categoryName }}</p>
      <p>제목 : {{ boardInfo.title }}</p>
      <p>생성일시 : {{ getFormattedDate(boardInfo.createdAt) }}</p>
      <p>작성자 : {{ boardInfo.userId }}</p>
      <p>조회수 : {{ boardInfo.visitCount }}</p>
      <p>내용 : {{ boardInfo.content }}</p>
      <!-- 첨부파일 -->
      <a
        v-for="(attachment, index) in boardInfo.boardAttachments"
        :key="index"
        :href="downloadAttachment(attachment.attachmentId)"
      >
        {{ attachment.originFileName }}<br />
      </a>
      <!-- 댓글 -->
      <div v-if="isLoggedIn">
        <textarea v-model="newComment" rows="4" cols="50"></textarea>
        <button @click="clickSumbitCommentBtn(newComment, boardId)">
          댓글 등록
        </button>
      </div>
      <div v-for="comment in boardInfo.boardComments" :key="comment.commentId">
        <p>작성자: {{ comment.userId }}</p>
        <p>작성시간: {{ getFormattedDate(comment.createdAt) }}</p>
        <p>{{ comment.content }}</p>
        <button
          v-if="isLoggedIn && comment.userId === getUser.userId"
          @click="clickCommentDeleteBtn(comment, boardId)"
        >
          댓글 삭제
        </button>
      </div>
    </div>
    <div v-else>
      <p>내용을 가져올 수 없습니다.</p>
    </div>
    <BoardEditBtnGroup
      :editPermission="editPermission"
      @emitUpdateBoard="clickEditBtn(boardId)"
      @emitDeleteBoard="clickDeleteBtn(boardId)"
    />
  </div>
</template>

<script>
import boardService from "@/services/board-service";
import { getFormattedDate, downloadAttachment } from "@/utils/util";
import BoardEditBtnGroup from "@/components/BoardEditBtnGroup.vue";
import { mapGetters } from "vuex";

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
    /**
     * boardId에 해당하는 자유 게시글 상세 정보를 가져옵니다.
     */
    this.boardId = this.$route.params.boardId;
    this.getFreeBoardDetail(this.boardId);
  },
  computed: {
    ...mapGetters(["isLoggedIn", "getUser"]),
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수입니다.
     */
    getFormattedDate,
    downloadAttachment,
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

        this.editPermission = await boardService.hasBoardEditPermission(
          boardId
        );
      } catch (error) {
        alert(error);
      }
    },
    async clickEditBtn(boardId) {
      this.$router.push({
        path: `${process.env.VUE_APP_BOARD_FREE_WRITE}/${boardId}`,
        query: this.$route.query,
      });
    },
    async clickDeleteBtn(boardId) {
      if (await boardService.deleteBoardInfo("free", boardId)) {
        boardService.replaceRouterToFreeBoardList(this.$router, this.$route);
      } else {
        alert("삭제가 불가합니다. 댓글이 남아있는지 확인해주세요.");
      }
    },
    async clickSumbitCommentBtn(newComment, boardId) {
      await boardService.addFreeBoardComment(newComment, boardId);
      await this.getFreeBoardDetail(boardId);
    },
    async clickCommentDeleteBtn(comment, boardId) {
      await boardService.deleteFreeBoardComment(comment, boardId);
      await this.getFreeBoardDetail(boardId);
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

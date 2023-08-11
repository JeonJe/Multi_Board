<template>
  <div>
    <div class="container">
      <h1>갤러리게시판</h1>
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
        <div v-if="boardInfo.boardImages.length > 0">
          <div class="slide-container">
            <vueper-slides
              3d
              :touchable="false"
              arrows-outside
              bullets-outside
              :slide-ratio="1"
            >
              <vueper-slide
                v-for="(slide, i) in boardImages"
                :key="i"
                :image="getImageURL(slide.fileName)"
                :title="slide.title"
              />
            </vueper-slides>
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
        <br />
        <hr />
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
import { getFormattedDate, downloadAttachment } from "@/utils/util";
import { mapGetters } from "vuex";
import boardService from "@/services/board-service";
import BoardEditBtnGroup from "@/components/BoardEditBtnGroup.vue";
import { VueperSlides, VueperSlide } from "vueperslides";
import "vueperslides/dist/vueperslides.css";

export default {
  components: {
    BoardEditBtnGroup,
    VueperSlides,
    VueperSlide,
  },
  data() {
    return {
      boardInfo: null,
      boardImages: null,
      boardId: null,
      newComment: "",
      editPermission: false,
    };
  },
  mounted() {
    this.boardId = this.$route.params.boardId;
    this.getGalleryBoardDetail(this.boardId);
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
     * 첨부파일을 다운로드하는 함수입니다.
     */
    downloadAttachment,
    /**
     * 공지사항 상세 정보를 가져오는 비동기 함수
     * @param {number} boardId - 공지사항 게시글의 ID
     */
    async getGalleryBoardDetail(boardId) {
      try {
        const response = await boardService.getBoardDetail("gallery", boardId);
        if (response.data != "") {
          this.boardInfo = response.data;
          this.boardImages = response.data.boardImages;
        }

        this.editPermission = await boardService.hasGalleryBoardEditPermission(
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
        path: `${process.env.VUE_APP_BOARD_GALLERY_WRITE}/${boardId}`,
        query: this.$route.query,
      });
    },
    /**
     * 게시글 삭제 버튼 클릭 이벤트 핸들러 함수
     * @param {number} boardId - 삭제할 게시글의 ID
     * @returns {void}
     */
    async clickDeleteBtn(boardId) {
      if (await boardService.deleteBoardInfo("gallery", boardId)) {
        boardService.replaceRouterToBoardList(
          this.$router,
          this.$route,
          "gallery"
        );
      } else {
        alert("삭제가 불가합니다.");
      }
    },
    clickBackToListBtn() {
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "gallery"
      );
    },
    getImageURL(thumbnailPath) {
      return `${process.env.VUE_APP_API_SER_URL}${process.env.VUE_APP_API_IMAGE}/${thumbnailPath}`;
    },
  },
};
</script>

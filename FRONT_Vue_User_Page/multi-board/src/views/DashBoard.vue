<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-md-6 mb-4">
        <div class="card">
          <div
            class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
          >
            공지사항
            <router-link to="/boards/notice/list" class="text-black"
              >+더보기</router-link
            >
          </div>
          <table class="table text-left">
            <thead>
              <tr>
                <th>카테고리</th>
                <th>제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in noticePosts" :key="post.id">
                <td>{{ post.categoryName }}</td>
                <td>
                  <router-link
                    :to="getNoticeBoardDetail(post.boardId)"
                    class="text-black"
                  >
                    {{ post.title }}
                    <span v-if="IsNewBoard(post.createdAt)" class="new-text"
                      >New
                    </span>
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col-md-6 mb-4">
        <div class="card">
          <div
            class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
          >
            자유게시판
            <router-link to="/boards/free/list" class="text-black"
              >+더보기</router-link
            >
          </div>
          <table class="table text-left">
            <thead>
              <tr>
                <th>카테고리</th>
                <th>제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in freePosts" :key="post.id">
                <td>{{ post.categoryName }}</td>
                <td>
                  <router-link
                    :to="getFreeBoardDetail(post.boardId)"
                    class="text-black"
                  >
                    {{ post.title }}
                    ({{ post.countBoardComment }})
                    <span v-if="IsNewBoard(post.createdAt)" class="new-text"
                      >New
                    </span>
                    <i
                      v-if="post.countBoardAttachment > 0"
                      class="fas fa-paperclip"
                    ></i>
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-4">
        <div class="card">
          <div
            class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
          >
            갤러리게시판
            <router-link to="/boards/gallery/list" class="text-black"
              >+더보기</router-link
            >
          </div>
          <table class="table text-left">
            <thead>
              <tr>
                <th>카테고리</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in galleryPosts" :key="post.id">
                <td>{{ post.categoryName }}</td>
                <td>
                  <div>
                    <img
                      :src="getFullThumbnailURL(post.thumbnailPath)"
                      alt="Thumbnail"
                      class="col-md-3 p-2"
                      style="height: 100px; object-fit: cover"
                    />
                    <router-link
                      :to="getGalleryBoardDetail(post.boardId)"
                      class="text-black"
                    >
                      {{ post.title }}

                      <span v-if="post.numOfImages - 1 > 0" class="new-text">
                        +{{ post.numOfImages - 1 }}
                      </span>
                      <span v-if="IsNewBoard(post.createdAt)" class="new-text">
                        &nbsp;New
                      </span>
                    </router-link>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col-md-6 mb-4">
        <div class="card">
          <div
            class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
          >
            문의게시판
            <router-link to="/boards/inquiry/list" class="text-black"
              >+더보기</router-link
            >
          </div>
          <table class="table text-left">
            <thead>
              <tr>
                <th>제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in inquiryPosts" :key="post.id">
                <td class="d-flex">
                  <!-- 비밀 글  -->
                  <span v-if="post.isSecret === 1">
                    <div @click="clickSecretBoard(post.boardId)">
                      <i class="fa-solid fa-lock ml-2" />
                      {{ post.title }}
                      <span v-if="post.isAnswered === 1"> (답변 완료) </span>
                      <span v-else> (미 답변) </span>
                    </div>
                  </span>
                  <!-- 공개 글 -->
                  <span v-else>
                    <router-link
                      :to="getInquiryBoardDetail(post.boardId)"
                      class="text-black"
                    >
                      <span>{{ post.title }}</span>
                      <span v-if="post.isAnswered === 1"> (답변 완료) </span>
                      <span v-else> (미 답변) </span>
                    </router-link>
                  </span>
                  <span v-if="IsNewBoard(post.createdAt)" class="new-text">
                    &nbsp;New
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
  <b-modal v-model="showModal" title="비밀번호 확인" @ok="handleOk">
    <div>
      <b-form @submit.stop.prevent="handlePasswordSubmit">
        <b-form-input
          v-model="inputPassword"
          ref="passwordInput"
          id="password"
          type="password"
          placeholder="비밀번호"
          :state="passwordState"
        ></b-form-input>
        <b-form-invalid-feedback :state="passwordState">
          <div v-if="passwordState === 'invalidLength'">
            비밀번호를 4자 이상 입력해주세요.
          </div>
          <div v-else-if="passwordState === 'invalidPassword'">
            비밀번호가 틀렸습니다.
          </div>
        </b-form-invalid-feedback>
      </b-form>
    </div>
  </b-modal>
</template>
<script>
// boardService로부터 최근 게시글 데이터를 가져온다고 가정
import boardService from "@/services/board-service"; // boardService 경로는 실제 경로에 맞게 수정하세요
import { IsNewBoard } from "@/utils/util";
export default {
  data() {
    return {
      noticePosts: [], // 공지사항 최근 게시글
      freePosts: [], // 자유게시글 최근 게시글
      galleryPosts: [], // 갤러리게시판 최근 게시글
      inquiryPosts: [], // 문의게시판 최근 게시글
      inputPassword: "",
      passwordState: null,
      selectedBoardId: null,
    };
  },
  async mounted() {
    // 서비스를 통해 각 게시판의 최근 게시글 데이터를 가져온다고 가정
    const response = await boardService.getRecentBoardList();
    if (response) {
      this.noticePosts = response.searchNoticeBoards;
      this.freePosts = response.searchFreeBoards;
      this.galleryPosts = response.searchGalleryBoards;
      this.inquiryPosts = response.searchInquiryBoards;
    }
  },
  methods: {
    IsNewBoard,
    getFullThumbnailURL(thumbnailPath) {
      return `${process.env.VUE_APP_API_SER_URL}${process.env.VUE_APP_API_IMAGE_THUMBNAIL}/${thumbnailPath}`;
    },
    getInquiryBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_INQUIRY_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
    getFreeBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_FREE_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
    getNoticeBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_NOTICE_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
    getGalleryBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_GALLERY_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
    clickSecretBoard(boardId) {
      this.showModal = true;
      this.inputPassword = "";
      this.passwordState = null;
      this.selectedBoardId = boardId;
    },
    handleOk(bvModalEvent) {
      bvModalEvent.preventDefault();
      this.handlePasswordSubmit();
    },
    async handlePasswordSubmit() {
      if (this.inputPassword.length < 4) {
        this.passwordState = "invalidLength";
        return;
      }

      const response = await boardService.checkInquiryBoardPassword(
        this.selectedBoardId,
        this.inputPassword
      );

      if (response) {
        this.$router.push(this.getBoardDetail(this.selectedBoardId));
      } else {
        this.passwordState = "invalidPassword";
      }
    },
  },
};
</script>

<style>
.card {
  border: none;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  height: 400px; /* 원하는 최대 높이로 설정 */
  overflow: auto; /* 내용이 카드를 넘어갈 경우 스크롤 생성 */
}

.card-header {
  font-weight: bold;
}

.list-group-item {
  border: none;
}
</style>

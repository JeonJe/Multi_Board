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
                <th class="align-middle">카테고리</th>
                <th class="align-middle">제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in noticePosts" :key="post.id">
                <td class="align-middle">{{ post.categoryName }}</td>
                <td class="align-middle">
                  {{ post.title }}
                  <span v-if="IsNewBoard(post.createdAt)" class="new-text"
                    >New
                  </span>
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
                <th class="align-middle">카테고리</th>
                <th class="align-middle">제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in freePosts" :key="post.id">
                <td class="align-middle">{{ post.categoryName }}</td>
                <td class="align-middle">
                  {{ post.title }}
                  <span v-if="IsNewBoard(post.createdAt)" class="new-text"
                    >New
                  </span>
                  ({{ post.countBoardComment }})
                  <i
                    v-if="post.countBoardAttachment > 0"
                    class="fas fa-paperclip"
                  ></i>
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
                <th class="align-middle">카테고리</th>
                <th class="align-middle"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in galleryPosts" :key="post.id">
                <td class="align-middle">{{ post.categoryName }}</td>
                <td class="align-middle">
                  <img
                    :src="getFullThumbnailURL(post.thumbnailPath)"
                    alt="Thumbnail"
                    class="col-md-3 p-2"
                    style="height: 100px; object-fit: cover"
                  />
                  {{ post.title }}
                  <span v-if="post.numOfImages - 1 > 0" class="new-text">
                    +{{ post.numOfImages - 1 }}
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
                <th class="align-middle">제목</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in inquiryPosts" :key="post.id">
                <td class="align-middle">
                  {{ post.title }}
                  <span v-if="post.isAnswered === 1"> (답변 완료) </span>
                  <span v-if="IsNewBoard(post.createdAt)" class="new-text">
                    &nbsp;New
                  </span>
                  <i
                    v-if="post.isSecret === 1"
                    class="fa-solid fa-lock ml-2"
                  ></i>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
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
  },
};
</script>

<style>
.card {
  border: none;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-weight: bold;
}

.list-group-item {
  border: none;
}
</style>

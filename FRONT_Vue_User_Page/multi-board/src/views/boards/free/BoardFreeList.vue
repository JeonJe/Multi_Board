<template>
  <div class="container">
    <h1>자유게시판</h1>
    <!-- 게시판 카테고리를 pros로 전달 -->
    <SearchForm
      :categories="categories"
      @emitSearchContion="updateSearchCondition"
    />

    <!-- 게시글 리스트 -->
    <div class="container">
      <div class="d-flex justify-content-end mb-2 mr-2">
        <router-link
          v-if="showRegisterButton"
          :to="clickBoardWriteBtn(this.searchCondition)"
        >
          <b-button>글 등록</b-button>
        </router-link>
      </div>
      <table class="table table-bordered table-hover">
        <thead class="table-dark text-center">
          <tr>
            <th style="width: 5%">번호</th>
            <th style="width: 5%">분류</th>
            <th style="width: 30%">제목</th>
            <th style="width: 5%">조회</th>
            <th style="width: 10%">등록일시</th>
            <th style="width: 5%">등록자</th>
          </tr>
        </thead>
        <tbody>
          <!-- 자유게시글 -->
          <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
            <td class="text-center">{{ totalPosts - index }}</td>
            <td class="text-center">{{ item.categoryName }}</td>
            <td class="text-left">
              <router-link :to="getBoardDetail(item.boardId)">
                <span class="list-title">{{ item.title }}</span>
                <span v-if="IsNewBoard(item.createdAt)" class="new-text">
                  New
                </span>
              </router-link>
              ({{ item.countBoardComment }})
              <i
                v-if="item.countBoardAttachment > 0"
                class="fas fa-paperclip"
              ></i>
            </td>
            <td class="text-center">{{ item.visitCount }}</td>
            <td class="text-center">{{ getFormattedDate(item.createdAt) }}</td>
            <td class="text-center">{{ item.userId }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 페이지네이션 -->
    <BoardPagination
      :currentPage="searchCondition.currentPage"
      :totalPages="totalPages"
      @clickPagination="updatePagination"
    />
  </div>
</template>

<script>
import { getFormattedDate, IsNewBoard } from "@/utils/util";
import { mapGetters, mapActions } from "vuex";
import SearchForm from "@/components/SearchForm.vue";
import BoardPagination from "@/components/BoardPagination.vue";
import boardService from "@/services/board-service";
import userService from "@/services/user-service";

export default {
  components: {
    SearchForm,
    BoardPagination,
  },
  data() {
    return {
      searchCondition: {},
      searchBoardList: [],
      categories: [],
      totalPosts: 0,
      totalPages: 0,
      showRegisterButton: false,
    };
  },
  async mounted() {
    await this.checkJWTAuth();
    await this.getFreeBoardCategories();
  },
  computed: {
    ...mapGetters(["isLoggedIn", "getUser"]),
  },
  methods: {
    ...mapActions(["clearLoginUser"]),
    /**
     * util의 날짜 포맷을 변환하는 함수
     */
    getFormattedDate,
    /**
     * util의 게시글이 7일 이내인지 확인하는 함수
     */
    IsNewBoard,
    /**
     * 검색 조건을 업데이트하고 자유게시글 목록을 가져오는 함수입니다.
     * @param {Object} searchCondition - 업데이트할 검색 조건 데이터
     */
    async updateSearchCondition(searchCondition) {
      this.searchCondition = searchCondition;
      await this.getFreeBoardList();
    },
    /**
     * 자유게시글 목록을 가져오는 비동기 함수입니다.
     *
     * @returns {Promise<void>} - 자유게시글 목록을 가져오는 비동기 작업의 Promise
     */
    async getFreeBoardList() {
      try {
        const response = await boardService.getBoardList(
          "free",
          this.searchCondition
        );
        if (response === "") {
          alert("표시 할 자유게시글이 없습니다.");
        } else {
          this.searchBoardList = response.data.searchFreeBoards;
          this.totalPosts = response.data.countSearchBoards;
          this.totalPages = Math.ceil(
            this.totalPosts / this.searchCondition.pageSize
          );
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 자유 게시판 카테고리를 가져오는 함수입니다.
     *
     * @returns {Promise<void>} - 카테고리를 가져오는 비동기 작업의 Promise
     */
    async getFreeBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("free");
        if (response === "") {
          alert("카테고리 목록이 없습니다.");
        } else {
          this.categories = response.data;
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 사용자 인증 상태를 확인하는 함수입니다.
     *
     * @returns {Promise<boolean>} - 사용자 인증 상태 확인 결과를 나타내는 Promise
     */
    async checkJWTAuth() {
      try {
        const hasPermission = await userService.getJWTAuthStatus();
        this.showRegisterButton = hasPermission;
        return true;
      } catch (error) {
        this.showRegisterButton = false;
        return false;
      }
    },
    /**
     * 게시글 작성 버튼 클릭 시 URL과 쿼리스트링을 반환하는 함수입니다.
     * @param {Object} searchCondition - 검색 조건 데이터
     * @returns {Object} - 게시글 작성 페이지의 URL과 쿼리스트링
     */
    clickBoardWriteBtn(searchCondition) {
      return {
        path: process.env.VUE_APP_BOARD_FREE_WRITE,
        query: searchCondition,
      };
    },

    /**
     * 페이지네이션을 업데이트하는 함수입니다.
     *
     * @param {number} page - 업데이트할 페이지 번호
     * @returns {void}
     */
    updatePagination(page) {
      this.searchCondition.currentPage = page;
      this.searchCondition.offset = (page - 1) * this.searchCondition.pageSize;
      this.getFreeBoardList();
    },
    /**
     * 게시글 상세 정보 페이지의 URL과 쿼리스트링을 반환하는 함수입니다.
     *
     * @param {number} boardId - 게시글 ID
     * @returns {Object} - 게시글 상세 정보 페이지의 URL과 쿼리스트링
     */
    getBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_FREE_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
  },
};
</script>

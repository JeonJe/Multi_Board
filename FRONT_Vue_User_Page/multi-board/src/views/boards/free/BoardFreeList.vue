<template>
  <div class="container">
    <h1>자유게시판</h1>

    <!-- 게시판 카테고리를 pros로 전달 -->
    <SearchForm
      :categories="categories"
      @emitSearchContion="updateSearchCondition"
    />

    <!-- 게시글 리스트 -->
    <div>
      <router-link
        v-if="showRegisterButton"
        :to="clickBoardWriteBtn(this.searchCondition)"
      >
        <button>글 등록</button>
      </router-link>
    </div>
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
        <!-- 자유게시글 -->
        <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
          <td>{{ index + 1 }}</td>
          <td>{{ item.categoryName }}</td>
          <router-link :to="getBoardDetail(item.boardId)">
            {{ item.title }}
            <span v-if="IsNewBoard(item.createdAt)">New</span>
          </router-link>
          <td>{{ item.visitCount }}</td>
          <td>{{ getFormattedDate(item.createdAt) }}</td>
          <td>{{ item.userId }}</td>
        </tr>
      </tbody>
    </table>
    <!-- 페이지네이션 -->
    <BoardPagination
      :currentPage="searchCondition.currentPage"
      :totalPages="totalPages"
      @clickPagination="updatePagination"
    />
  </div>
</template>

<script>
import SearchForm from "@/components/SearchForm.vue";
import BoardPagination from "@/components/BoardPagination.vue";
import boardService from "@/services/board-service";
import userService from "@/services/user-service";
import { getFormattedDate, IsNewBoard } from "@/utils/util";

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
  mounted() {
    /**
     * 게시판 카테고리를 가져옵니다.
     */
    this.getFreeBoardCategories();
    this.checkAuthentication();
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수입니다.
     */
    getFormattedDate,
    /**
     * util의 게시글이 7일 이내인지 확인하는 함수입니다.
     */
    IsNewBoard,
    /**
     * 검색 조건을 업데이트하고 자유게시글 목록을 가져오는 함수입니다.
     * @param {Object} searchCondition - 업데이트할 검색 조건 데이터
     */
    updateSearchCondition(searchCondition) {
      this.searchCondition = searchCondition;
      this.getFreeBoardList();
    },
    /**
     * 자유게시글 목록을 가져오는 비동기 함수입니다.
     */
    async getFreeBoardList() {
      try {
        const response = await boardService.getBoardList(
          "free",
          this.searchCondition
        );
        console.log(response);
        if (response === "") {
          alert("표시 할 자유게시글이 없습니다.");
        } else {
          this.searchBoardList = response.data.searchBoards;
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
     * 페이지네이션을 업데이트하는 함수입니다.
     * @param {number} page - 업데이트할 페이지 번호
     */
    updatePagination(page) {
      this.searchCondition.currentPage = page;
      this.searchCondition.offset = (page - 1) * this.searchCondition.pageSize;
      this.getFreeBoardList();
    },
    /**
     * 게시글 상세 정보 페이지의 URL과 쿼리스트링을 반환하는 함수입니다.
     * @param {number} boardId - 게시글 ID
     * @returns {Object} - 게시글 상세 정보 페이지의 URL과 쿼리스트링
     */
    getBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_FREE_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
    /**
     * 자유 게시판 카테고리를 가져옵니다.
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
    async checkAuthentication() {
      try {
        const hasPermission = await userService.getAuthenticationStatus();
        this.showRegisterButton = hasPermission;
      } catch (error) {
        this.showRegisterButton = false; // 토큰 확인 실패 시 버튼을 숨김
        console.log(error);
      }
    },
    clickBoardWriteBtn(searchCondition) {
      return {
        path: process.env.VUE_APP_BOARD_FREE_WRITE,
        query: searchCondition,
      };
    },
  },
};
</script>

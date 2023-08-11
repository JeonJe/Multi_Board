<template>
  <div class="container">
    <h1>공지사항</h1>

    <!-- 게시판 카테고리를 pros로 전달 -->
    <SearchForm
      :categories="categories"
      @emitSearchContion="updateSearchCondition"
    />

    <!-- 게시글 리스트 -->
    <div class="container">
      <table class="table table-bordered table-striped table-hover">
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
          <!-- 알림 표시된 공지 게시글 -->
          <tr v-for="item in markNoticedBoardList" :key="item.boardId">
            <td class="text-center">{{ "알림글" }}</td>
            <td class="text-center">{{ item.categoryName }}</td>
            <td class="text-left">
              <router-link :to="getBoardDetail(item.boardId)">
                <span class="list-title">{{ item.title }}</span>
                <span v-if="IsNewBoard(item.createdAt)" class="new-text">
                  New
                </span>
              </router-link>
            </td>
            <td class="text-center">{{ item.visitCount }}</td>
            <td class="text-center">{{ getFormattedDate(item.createdAt) }}</td>
            <td class="text-center">{{ item.userId }}</td>
          </tr>
          <!-- 알림 표시되지 않은 공지 게시글 -->
          <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
            <td class="text-center">
              {{ totalPosts - index - markNoticedBoardList.length }}
            </td>
            <td class="text-center">{{ item.categoryName }}</td>
            <td class="text-left">
              <router-link :to="getBoardDetail(item.boardId)">
                <span class="list-title">{{ item.title }}</span>
                <span v-if="IsNewBoard(item.createdAt)" class="new-text"
                  >New
                </span>
              </router-link>
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
import SearchForm from "@/components/SearchForm.vue";
import BoardPagination from "@/components/BoardPagination.vue";
import boardService from "@/services/board-service";
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
      markNoticedBoardList: [],
      categories: [],
      totalPosts: 0,
      totalPages: 0,
    };
  },
  async mounted() {
    /**
     * 게시판 카테고리 조회
     */
    await this.getNoticeBoardCategories();
  },
  methods: {
    /**
     * util의 날짜 포맷을 변환하는 함수
     */
    getFormattedDate,
    /**
     * util의 게시글이 7일 이내인지 확인하는 함수
     */
    IsNewBoard,
    /**
     * 검색 조건을 업데이트하고 공지사항 목록을 가져오는 함수
     * @param {Object} searchCondition - 업데이트할 검색 조건 데이터
     */
    async updateSearchCondition(searchCondition) {
      this.searchCondition = searchCondition;
      await this.getNoticeBoardList();
    },
    /**
     * 공지사항 목록을 가져오는 비동기 함수
     */
    async getNoticeBoardList() {
      try {
        const response = await boardService.getBoardList(
          "notice",
          this.searchCondition
        );
        if (response === "") {
          alert("표시 할 공지사항이 없습니다.");
        } else {
          this.searchBoardList = response.data.searchNoticeBoards;
          this.markNoticedBoardList = response.data.markNoticedBoards;
          this.countMarkNoticedBoards = response.data.countMarkNoticedBoards;
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
     * 공지 게시판 카테고리 조회
     */
    async getNoticeBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("notice");
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
     * 페이지네이션을 업데이트하는 함수
     * @param {number} page - 업데이트할 페이지 번호
     */
    updatePagination(page) {
      this.searchCondition.currentPage = page;
      this.searchCondition.offset = (page - 1) * this.searchCondition.pageSize;
      this.getNoticeBoardList();
    },
    /**
     * 게시글 상세 정보 페이지의 URL과 쿼리스트링을 반환하는 함수
     * @param {number} boardId - 게시글 ID
     * @returns {Object} - 게시글 상세 정보 페이지의 URL과 쿼리스트링
     */
    getBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_NOTICE_VIEW}/${boardId}`,
        query: this.searchCondition,
      };
    },
  },
};
</script>

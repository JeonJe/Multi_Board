<template>
  <div class="container">
    <h1>공지사항</h1>

    <SearchForm @emitSearchContion="updateSearchCondtion" />
    <!-- 총 조회 건수 -->

    <!-- 게시글 리스트 -->
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
        <tr v-for="item in markNoticedBoardList" :key="item.boardId">
          <td>{{}}</td>
          <td><!-- 분류 데이터 출력 --></td>
          <router-link :to="getBoardDetail(item.boardId)">
            {{ item.title }}
            <span v-if="IsNewBoard(item.createdAt)">New</span>
          </router-link>
          <td>{{ item.visitCount }}</td>
          <td>{{ getFormattedDate(item.createdAt) }}</td>
          <td>{{ item.userId }}</td>
        </tr>

        <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
          <td>{{ index + 1 }}</td>
          <td><!-- 분류 데이터 출력 --></td>
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
      @clickPagenation="updatePagination"
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
  mixins: [],
  data() {
    return {
      searchCondition: {},
      searchBoardList: [],
      markNoticedBoardList: [],
      markNoticedBoardSize: 0,
      categories: [],
      totalPosts: 0,
      totalPages: 0,
    };
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
     * 검색 조건을 업데이트하고 공지사항 목록을 가져오는 함수입니다.
     * @param {Object} searchCondition - 업데이트할 검색 조건 데이터
     */
    updateSearchCondtion(searchCondition) {
      this.searchCondition = searchCondition;
      this.getNoticeBoardList();
    },
    /**
     * 공지사항 목록을 가져오는 비동기 함수입니다.
     */
    async getNoticeBoardList() {
      try {
        const response = await boardService.getNoticeBoardList(
          this.searchCondition
        );
        if (response === "") {
          alert("표시 할 공지사항이 없습니다.");
        } else {
          this.searchBoardList = response.data.searchNoticeBoards;
          this.markNoticedBoardList = response.data.markNoticedBoards;
          this.countMarkNoticedBoards = response.data.countMarkNoticedBoards;
          this.totalPosts = response.data.countNoticeBoards;
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
      this.getNoticeBoardList();
    },
    /**
     * 게시글 상세 정보 페이지의 URL과 쿼리스트링을 반환하는 함수입니다.
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

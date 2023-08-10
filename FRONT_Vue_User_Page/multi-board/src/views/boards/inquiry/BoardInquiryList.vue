<template>
  <div class="container">
    <h1>문의게시판</h1>
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
          <!-- 문의게시글 -->
          <tr v-for="(item, index) in searchBoardList" :key="item.boardId">
            <td class="text-center">{{ totalPosts - index }}</td>
            <td class="text-center">{{ item.categoryName }}</td>
            <td class="text-left">
              <!-- 비밀번호가 있는 문의 게시글 -->
              <div v-if="item.isSecret === 1" class="d-flex align-items-center">
                <div class="list-title" @click="clickSecretBoard(item.boardId)">
                  <span>{{ item.title }}</span>
                  <span v-if="item.isAnswered === 1"> (답변 완료) </span>
                  <span v-if="IsNewBoard(item.createdAt)" class="new-text">
                    New
                  </span>
                </div>
                <i class="fa-solid fa-lock ml-2"></i>
              </div>
              <!-- 비밀번호가 없는 문의 게시글 -->
              <router-link v-else :to="getBoardDetail(item.boardId)">
                <span class="list-title">{{ item.title }}</span>
                <span v-if="item.isAnswered === 1" class="list-title">
                  (답변 완료)
                </span>
                <span v-if="IsNewBoard(item.createdAt)" class="new-text">
                  New
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
      showModal: false,
      inputPassword: "",
      passwordState: null,
      selectedBoardId: null,
    };
  },
  async mounted() {
    await this.checkJWTAuth();
    await this.getInquiryBoardCategories();
  },
  computed: {
    // passwordPlaceholder() {
    //   return this.inputPassword ? "" : "비밀번호";
    // },
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
     * 검색 조건을 업데이트하고 자유게시글 목록을 가져오는 함수
     * @param {Object} searchCondition - 업데이트할 검색 조건 데이터
     */
    async updateSearchCondition(searchCondition) {
      this.searchCondition = searchCondition;
      await this.getInquiryBoardList();
    },
    async getInquiryBoardList() {
      try {
        const response = await boardService.getBoardList(
          "inquiry",
          this.searchCondition
        );
        if (response === "") {
          alert("표시 할 자유게시글이 없습니다.");
        } else {
          this.searchBoardList = response.data.searchInquiryBoards;
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
     * 자유 게시판 카테고리를 가져오는 함수
     */
    async getInquiryBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("inquiry");
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
     * 사용자 인증 상태를 확인하는 함수
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
     * 게시글 작성 버튼 클릭 시 URL과 쿼리스트링을 반환하는 함수
     * @param {Object} searchCondition - 검색 조건 데이터
     * @returns {Object} - 게시글 작성 페이지의 URL과 쿼리스트링
     */
    clickBoardWriteBtn(searchCondition) {
      return {
        path: process.env.VUE_APP_BOARD_INQUIRY_WRITE,
        query: searchCondition,
      };
    },
    /**
     * 페이지네이션을 업데이트하는 함수
     * @param {number} page - 업데이트할 페이지 번호
     */
    updatePagination(page) {
      this.searchCondition.currentPage = page;
      this.searchCondition.offset = (page - 1) * this.searchCondition.pageSize;
      this.getInquiryBoardList();
    },
    /**
     * 게시글 상세 정보 페이지의 URL과 쿼리스트링을 반환하는 함수
     * @param {number} boardId - 게시글 ID
     * @returns {Object} - 게시글 상세 정보 페이지의 URL과 쿼리스트링
     */
    getBoardDetail(boardId) {
      return {
        path: `${process.env.VUE_APP_BOARD_INQUIRY_VIEW}/${boardId}`,
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

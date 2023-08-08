<template>
  <div>
    <div class="container">
      <h1>문의게시판</h1>

      <form enctype="multipart/form-data">
        <!-- 카테고리 -->
        <div class="form-group row">
          <hr />
          <label class="col-sm-1 col-form-label">분류</label>
          <div class="col-sm-4">
            <select
              v-model="boardInfo.categoryValue"
              required
              class="form-control"
            >
              <option
                v-for="(categoryOption, index) in categoryOptions"
                :key="index"
                :value="categoryOption.value"
                v-bind:selected="index === 0"
              >
                {{ categoryOption.label }}
              </option>
            </select>
          </div>
        </div>
        <hr />
        <!-- 제목 -->
        <div class="form-group row">
          <label class="col-sm-1 col-form-label">제목</label>
          <div class="col-sm-10">
            <input
              v-model="boardInfo.title"
              type="text"
              class="form-control"
              placeholder="제목"
              required
            />
          </div>
        </div>
        <hr />
        <!-- 내용 -->
        <div class="form-group row">
          <label class="col-sm-1 col-form-label">내용</label>
          <div class="col-sm-10">
            <textarea
              v-model="boardInfo.content"
              class="form-control"
              placeholder="내용"
              rows="6"
              required
            ></textarea>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-sm-1 col-form-label mt-2">비밀글</label>
          <div class="col-sm-4 d-flex align-items-center">
            <input type="checkbox" v-model="isSecretChecked" />
            <input
              type="password"
              id="password"
              v-model="boardInfo.password"
              class="form-control mx-2 mt-2"
              :placeholder="passwordPlaceholder"
              :disabled="!isSecretChecked"
            />
          </div>
        </div>
        <hr />
      </form>
      <!-- 버튼 그룹 -->
      <div class="d-flex justify-content-center my-2">
        <button
          type="button"
          @click="clickBoardUpdateSubmit(boardId)"
          v-if="isUpdate"
          class="btn btn-primary mx-2"
        >
          수정
        </button>
        <button
          type="button"
          @click="clickBoardInfoSubmit"
          v-else
          class="btn btn-primary mx-2"
        >
          등록
        </button>
        <button
          type="button"
          @click="moveToInquiryBoardList"
          class="btn btn-secondary"
        >
          목록으로
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import boardService from "@/services/board-service";
import {
  validateTitle,
  validateContent,
  validateFiles,
  downloadAttachment,
} from "@/utils/util";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      isUpdate: false,
      boardId: null,
      categories: [],
      boardInfo: {
        title: "",
        content: "",
        isSecret: "",
        password: "",
      },
      isSecretChecked: false,
    };
  },
  computed: {
    passwordPlaceholder() {
      return this.password ? "" : "비밀번호";
    },
    ...mapGetters(["isLoggedIn", "getUser"]),
    /**
     * 카테고리 select-option 목록을 생성합니다.
     */
    categoryOptions() {
      return this.categories.map((category) => ({
        value: category.categoryValue,
        label: category.categoryName,
      }));
    },
  },

  mounted() {
    const boardId = this.$route.params.boardId;
    this.initBoardInfo();

    //글 수정
    if (boardId) {
      this.isUpdate = true;
      this.boardId = boardId;
      this.getOriginInquiryBoardDetail(boardId);
    } else {
      // 글 작성
      this.isUpdate = false;
    }
  },
  methods: {
    validateTitle,
    validateContent,
    validateFiles,
    downloadAttachment,
    /**
     * 게시판 정보를 초기화
     */
    async initBoardInfo() {
      if (this.getUser && this.getUser.userId !== null) {
        this.boardInfo.userId = this.getUser.userId;
      } else {
        alert("작성자 정보를 가져오지 못했습니다.");
        boardService.replaceRouterToBoardList(
          this.$router,
          this.$route,
          "inquiry"
        );
      }
      await this.getInquiryBoardCategories();
    },
    async getOriginInquiryBoardDetail(boardId) {
      if (!(await boardService.hasInquiryBoardEditPermission(boardId))) {
        alert("수정 권한이 없습니다");
        this.$router.replace({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
        return;
      }
      const response = await boardService.getBoardDetail("inquiry", boardId);
      if (response.data != "") {
        this.boardInfo = response.data;
      }
    },
    /**
     * 자유 게시판 카테고리를 조회
     */
    async getInquiryBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("inquiry");
        if (response === "") {
          alert("카테고리 목록이 없습니다.");
        } else {
          this.categories = response.data;
          if (this.categories.length > 0) {
            this.boardInfo.categoryValue = this.categories[0].categoryValue;
          } else {
            alert("카테고리가 없습니다.");
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 게시판 정보를 서버에 저장하는 함수
     */
    async clickBoardInfoSubmit() {
      if (!(await this.validateForm())) {
        return;
      }
      this.boardInfo.isSecret = this.isSecretChecked ? 1 : 0;
      await boardService.saveBoardInfo("inquiry", this.boardInfo);
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "inquiry"
      );
    },
    /**
     * 게시글 수정 폼을 제출하는 함수
     * @param {number} boardId - 수정할 게시글의 ID
     * @returns {void}
     */
    async clickBoardUpdateSubmit(boardId) {
      if (!(await this.validateForm())) {
        return;
      }
      this.boardInfo.isSecret = this.isSecretChecked ? 1 : 0;
      await boardService.updateBoardInfo("inquiry", boardId, this.boardInfo);
      this.$router.replace({
        path: `${process.env.VUE_APP_BOARD_INQUIRY_VIEW}/${boardId}`,
        query: { ...this.$route.query },
      });
    },
    moveToInquiryBoardList() {
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "inquiry"
      );
    },
    async validateForm() {
      if (
        !this.boardInfo ||
        !this.boardInfo.title ||
        this.boardInfo.title.trim().length === 0
      ) {
        alert("제목을 입력해주세요.");
        return false;
      }
      if (!(await this.validateTitle(this.boardInfo.title))) {
        alert("제목은 1자 이상 100자 이하로 작성해주세요.");
        return false;
      }
      if (!(await this.validateContent(this.boardInfo.content))) {
        alert("내용은 1자 이상 4000자 이하로 작성해주세요.");
        return false;
      }
      if (this.isSecretChecked) {
        if (this.boardInfo.password.length < 4) {
          alert("게시글 비밀번호는 4자 이상입니다.");
          return false;
        }
      }
      return true;
    },
  },
};
</script>

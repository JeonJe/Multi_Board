<template>
  <div>
    <div class="container">
      <h1>자유게시판</h1>

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
                v-for="categoryOption in categoryOptions"
                :key="categoryOption.value"
                :value="categoryOption.value"
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
      </form>
      <hr />
      <div>
        <!-- 기존 첨부파일 리스트  -->
        <div
          v-for="(attachment, index) in boardInfo.boardAttachments"
          :key="attachment.attachmentId"
        >
          <div class="d-flex justify-content-between">
            <span>{{ attachment.originFileName }}</span>
            <div>
              <button
                type="button"
                @click="clickDeleteAttachment(index, attachment.attachmentId)"
                class="btn btn-sm btn-danger mx-2"
              >
                삭제
              </button>
              <a
                :href="downloadAttachment(attachment.attachmentId)"
                class="btn btn-sm btn-primary"
              >
                다운로드
              </a>
            </div>
          </div>
        </div>

        <!-- 새로 첨부파일 추가할 수 있는 input -->
        <div class="mt-3" v-for="(file, index) in fileInputBoxes" :key="index">
          <div class="d-flex justify-content-between align-items-center">
            <input
              type="file"
              :id="'attachment' + (index + 1)"
              :name="files"
              @change="handleFileChange($event)"
              class="form-control-file"
            />
            <button
              type="button"
              @click="clickRemoveEmptyInput(index)"
              class="btn btn-sm btn-danger mx-2"
            >
              삭제
            </button>
          </div>
        </div>

        <button
          type="button"
          @click="clickAddAttachmentForm"
          v-show="fileInputBoxes.length < 5"
          class="btn btn-secondary mt-3"
        >
          첨부파일 추가
        </button>
      </div>
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
        <router-link :to="moveToFreeBoardList()" class="btn btn-secondary"
          >목록으로</router-link
        >
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
      boardInfo: {},
      fileInputBoxes: [],
      uploadAttachments: [],
      deletedAttachmentIDs: [],
      maxAttachments: 5,
    };
  },
  computed: {
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
      this.getOriginFreeBoardDetail(boardId);
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
      this.boardInfo.userId = this.getUser.userId;
      if (this.boardInfo.userId === null) {
        alert("작성자 정보를 가져오지 못했습니다.");
        boardService.replaceRouterToFreeBoardList(this.$router, this.$route);
      }
      await this.getFreeBoardCategories();
    },
    async getOriginFreeBoardDetail(boardId) {
      if (!(await boardService.hasBoardEditPermission(boardId))) {
        alert("수정 권한이 없습니다");
        this.$router.replace({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
        return;
      }
      const response = await boardService.getBoardDetail("free", boardId);
      if (response.data != "") {
        this.boardInfo = response.data;
      }
    },
    /**
     * 자유 게시판 카테고리를 조회
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
     * 게시판 정보를 서버에 저장하는 함수
     */
    async clickBoardInfoSubmit() {
      if (!(await this.validateTitle(this.boardInfo.title))) {
        alert("제목은 100자 이하로 작성해주세요.");
        return;
      }
      if (!(await this.validateContent(this.boardInfo.content))) {
        alert("내용은 4000자 이하로 작성해주세요.");
        return;
      }
      if (!(await this.validateFiles(this.uploadAttachments))) {
        alert("파일은 2MB이하, jpg,gif,png,zip 파일만 올려주세요.");
        return;
      }

      const getNewBoardInfo = this.createFormDataToSumbit();

      await boardService.saveBoardInfo("free", getNewBoardInfo);
      boardService.replaceRouterToFreeBoardList(this.$router, this.$route);
    },
    /**
     * 게시글 수정 폼을 제출하는 함수
     * @param {number} boardId - 수정할 게시글의 ID
     * @returns {void}
     */
    async clickBoardUpdateSubmit(boardId) {
      const getNewBoardInfo = this.createFormDataToSumbit();
      await boardService.updateBoardInfo("free", boardId, getNewBoardInfo);
      this.getOriginFreeBoardDetail(boardId);
    },
    /**
     * 게시글을 수정하기 위해 제출할 FormData를 생성하는 함수
     * @returns {FormData} - 게시글 수정에 사용될 FormData 객체
     */
    createFormDataToSumbit() {
      const newBoardInfo = new FormData();

      newBoardInfo.append("categoryValue", this.boardInfo.categoryValue);
      newBoardInfo.append("userId", this.boardInfo.userId);
      newBoardInfo.append("title", this.boardInfo.title);
      newBoardInfo.append("content", this.boardInfo.content);

      this.uploadAttachments.forEach((file) => {
        newBoardInfo.append(`uploadAttachments`, file);
      });
      return newBoardInfo;
    },
    /**
     * 파일 선택 이벤트 핸들러
     *  선택한 파일을 boardInfo.uploadAttachments 배열에 추가
     * @param {Event} event - 파일 선택 이벤트 객체
     */
    handleFileChange(event) {
      const file = event.target.files[0];
      this.uploadAttachments.push(file);
    },
    /**
     * 첨부 파일 입력 양식을 추가하는 함수
     */
    clickAddAttachmentForm() {
      this.fileInputBoxes.push({});
    },
    /**
     * 빈 입력 양식을 제거하는 함수
     * @param {number} index - 제거할 입력 양식의 인덱스
     */
    clickRemoveEmptyInput(index) {
      this.fileInputBoxes.splice(index, 1);
    },
    /**
     * 첨부 파일을 삭제하는 함수
     * @param {number} index - 삭제할 첨부 파일의 인덱스
     * @param {string} attachmentId - 삭제할 첨부 파일의 ID
     */

    async clickDeleteAttachment(index, attachmentId) {
      console.log("asdf");
      this.deletedAttachmentIDs.push(attachmentId);
      this.boardInfo.boardAttachments.splice(index, 1);
    },
    /**
     * 자유 게시판 목록으로 이동하는 함수
     * @returns {Object} - 자유 게시판 목록 페이지의 URL과 쿼리스트링
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

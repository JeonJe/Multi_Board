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
            <i class="fas fa-paperclip"></i>
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
        <strong>2MB이하의 jpg, gif, png, zip 파일 업로드가 가능합니다.</strong>
        <br />
        <!-- 새로 첨부파일 추가할 수 있는 input -->
        <div
          class="mt-3"
          v-for="(file, index) in fileInputBoxes"
          :key="file.id"
        >
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
        <button
          type="button"
          @click="moveToFreeBoardList"
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
      },
      fileInputBoxes: [],
      uploadAttachments: [],
      deletedAttachmentIDs: [],
      inputFiles: [],
      maxAttachments: 5,
      nextInputId: 1,
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
     * 게시판 정보를 초기화합니다.
     * 사용자 정보 및 카테고리 목록을 설정하고, 작성자 정보가 없을 시 알림을 표시합니다.
     * @returns {void}
     */
    async initBoardInfo() {
      if (this.getUser && this.getUser.userId !== null) {
        this.boardInfo.userId = this.getUser.userId;
      } else {
        alert("작성자 정보를 가져오지 못했습니다.");
        boardService.replaceRouterToBoardList(
          this.$router,
          this.$route,
          "free"
        );
      }
      await this.getFreeBoardCategories();
    },
    /**
     * 자유 게시판 카테고리 목록을 조회합니다.
     * 서버에서 카테고리 목록을 가져오고, 목록이 비어있을 시 알림을 표시합니다.
     * @returns {void}
     */
    async getOriginFreeBoardDetail(boardId) {
      if (!(await boardService.hasFreeBoardEditPermission(boardId))) {
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
     * 게시글 작성 또는 수정 폼을 서버에 제출합니다.
     * 입력한 정보를 기반으로 게시글을 저장하거나 수정하며, 유효성 검사를 수행합니다.
     * @returns {void}
     */
    async getFreeBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("free");
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
     * 게시글 수정 폼을 제출합니다.
     * 수정한 정보를 기반으로 게시글을 업데이트하며, 유효성 검사를 수행합니다.
     * @param {number} boardId - 수정할 게시글의 ID
     * @returns {void}
     */
    async clickBoardInfoSubmit() {
      if (!(await this.validateForm())) {
        return;
      }
      const getNewBoardInfo = this.createFormDataToSumbit(this.isUpdate);

      await boardService.saveBoardInfo("free", getNewBoardInfo);
      boardService.replaceRouterToBoardList(this.$router, this.$route, "free");
    },
    /**
     * 게시글 수정을 위한 FormData를 생성합니다.
     * 작성한 게시글 정보 및 첨부 파일 등을 FormData로 구성하여 반환합니다.
     * @param {boolean} isUpdate - 수정 모드 여부 (true: 수정, false: 작성)
     * @returns {FormData} - 게시글 작성 또는 수정에 사용될 FormData 객체
     */
    async clickBoardUpdateSubmit(boardId) {
      if (!(await this.validateForm())) {
        return;
      }
      const getNewBoardInfo = this.createFormDataToSumbit(this.isUpdate);
      await boardService.updateBoardInfo("free", boardId, getNewBoardInfo);
      this.$router.replace({
        path: `${process.env.VUE_APP_BOARD_FREE_VIEW}/${boardId}`,
        query: { ...this.$route.query },
      });
    },
    /**
     * 게시글을 수정하기 위해 제출할 FormData를 생성하는 함수
     * @returns {FormData} - 게시글 수정에 사용될 FormData 객체
     */
    createFormDataToSumbit(isUpdate) {
      const newBoardInfo = new FormData();

      newBoardInfo.append("categoryValue", this.boardInfo.categoryValue);
      newBoardInfo.append("userId", this.boardInfo.userId);
      newBoardInfo.append("title", this.boardInfo.title);
      newBoardInfo.append("content", this.boardInfo.content);

      for (const file of this.inputFiles) {
        newBoardInfo.append(`uploadAttachments`, file);
      }

      if (isUpdate) {
        for (const file of this.deletedAttachmentIDs) {
          newBoardInfo.append(`deletedAttachmentIDs`, file);
        }
      }

      return newBoardInfo;
    },
    /**
     * 파일 선택 이벤트를 처리하는 함수입니다.
     * 선택한 파일을 각각의 입력 양식에 연결하고, 첨부 파일 목록을 업데이트합니다.
     * @param {Event} event - 파일 선택 이벤트 객체
     * @returns {void}
     */
    handleFileChange(event) {
      const file = event.target.files[0];
      const index = Number(event.target.id.replace("attachment", "")) - 1;
      this.inputFiles[index] = file;
      // this.uploadAttachments.push(file);
    },
    /**
     * 첨부 파일 입력 양식을 추가하는 함수입니다.
     * 새로운 첨부 파일 입력 양식을 배열에 추가하여 UI에 표시합니다.
     * @returns {void}
     */
    clickAddAttachmentForm() {
      this.fileInputBoxes.push({ id: this.nextInputId++ });
    },
    /**
     * 빈 첨부 파일 입력 양식을 제거합니다.
     * 입력 양식을 제거하면 해당 파일도 첨부 목록에서 제거됩니다.
     * @param {number} index - 제거할 입력 양식의 인덱스
     * @returns {void}
     */
    clickRemoveEmptyInput(index) {
      if (this.inputFiles[index]) {
        const removedFile = this.inputFiles.splice(index, 1)[0];
        const removedFileIndex = this.uploadAttachments.indexOf(removedFile);
        if (removedFileIndex !== -1) {
          this.uploadAttachments.splice(removedFileIndex, 1);
        }
      }
      this.fileInputBoxes.splice(index, 1);
    },
    /**
     * 첨부 파일을 삭제하는 함수입니다.
     * 선택한 첨부 파일을 삭제하고, 목록에서도 해당 첨부 파일을 제거합니다.
     * @param {number} index - 삭제할 첨부 파일의 인덱스
     * @param {string} attachmentId - 삭제할 첨부 파일의 ID
     * @returns {void}
     */
    async clickDeleteAttachment(index, attachmentId) {
      this.deletedAttachmentIDs.push(attachmentId);
      this.boardInfo.boardAttachments.splice(index, 1);
    },
    /**
     * 자유 게시판 목록 페이지로 이동합니다.
     * @returns {void}
     */
    moveToFreeBoardList() {
      boardService.replaceRouterToBoardList(this.$router, this.$route, "free");
    },
    /**
     * 폼 입력값의 유효성을 검사합니다.
     * 제목, 내용, 첨부 파일 등의 유효성을 확인하고, 문제가 있을 시 알림을 표시합니다.
     * @returns {boolean} - 유효성 검사 결과 (true: 유효, false: 유효하지 않음)
     */
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
      if (!(await this.validateFiles(this.uploadAttachments))) {
        alert("파일은 2MB이하, jpg,gif,png,zip 파일만 올려주세요.");
        return false;
      }
      return true;
    },
  },
};
</script>

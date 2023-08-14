<template>
  <div>
    <div class="container">
      <h1>갤러리게시판</h1>

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
        <strong>1MB이하의 jpg, gif, png 파일 업로드가 가능합니다.</strong>
        <br /><em>*1번째 이미지는 썸네일로 사용됩니다.</em><br />
        <!-- 기존 첨부파일 리스트  -->
        <div
          v-for="(image, index) in boardInfo.boardImages"
          :key="image.imageId"
        >
          <div class="d-flex justify-content-between">
            <span>{{ image.originFileName }}</span>
            <div>
              <button
                type="button"
                @click="clickDeleteAttachment(index, image.imageId)"
                class="btn btn-sm btn-danger mx-2"
              >
                삭제
              </button>
              <a
                :href="downloadAttachment(image.imageId)"
                class="btn btn-sm btn-primary"
              >
                다운로드
              </a>
            </div>
          </div>
        </div>
        <!-- 새로 첨부파일 추가할 수 있는 input -->
        <div
          class="mt-3 d-flex justify-content-between align-items-center"
          v-for="(file, index) in fileInputBoxes"
          :key="file.id"
        >
          <!-- 이미지 미리보기 -->
          <div v-if="file.previewUrl">
            <img
              :src="file.previewUrl"
              alt="미리보기"
              style="max-height: 50px; max-width: 50px"
            />
          </div>
          <!-- 새로운 이미지 추가 -->
          <input
            type="file"
            :id="'attachment' + (index + 1)"
            :name="files"
            @change="handleFileChange($event, index)"
            class="form-control-file"
            style="flex: 1; margin-right: 10px"
          />

          <button
            type="button"
            @click="clickRemoveEmptyInput(index)"
            class="btn btn-sm btn-danger mx-2"
          >
            삭제
          </button>
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
          @click="moveToGalleryBoardList"
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
  validateImages,
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
      maxAttachments: 20,
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
      // TODO: 갤러리 게시글 가져오기
      this.getOriginGalleryBoardDetail(boardId);
    } else {
      // 글 작성
      this.isUpdate = false;
    }
  },
  methods: {
    validateTitle,
    validateContent,
    validateImages,
    downloadAttachment,
    /**
     * 게시판 정보를 초기화하는 비동기 함수입니다.
     * 작성자 정보를 설정하고 갤러리 게시판 카테고리를 조회합니다.
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
          "gallery"
        );
      }
      await this.getGalleryBoardCategories();
    },
    /**
     * 갤러리 게시판 상세 정보를 가져오는 비동기 함수입니다.
     * 수정 권한을 확인하고 상세 정보를 가져옵니다.
     * @param {number} boardId - 게시글의 ID
     * @returns {void}
     */
    async getOriginGalleryBoardDetail(boardId) {
      if (!(await boardService.hasGalleryBoardEditPermission(boardId))) {
        alert("수정 권한이 없습니다");
        this.$router.replace({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
        return;
      }
      const response = await boardService.getBoardDetail("gallery", boardId);
      if (response.data != "") {
        this.boardInfo = response.data;
      }
    },
    /**
     * 갤러리 게시판 카테고리를 조회하는 비동기 함수입니다.
     * 카테고리 목록을 가져오고 첫 번째 카테고리를 선택합니다.
     * @returns {void}
     */
    async getGalleryBoardCategories() {
      try {
        const response = await boardService.getBoardCategories("gallery");
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
     * 게시판 정보를 서버에 저장하는 함수입니다.
     * 유효성 검사를 통과하면 새로운 게시글 정보를 서버에 전송합니다.
     * @returns {void}
     */
    async clickBoardInfoSubmit() {
      if (!(await this.validateForm())) {
        return;
      }
      const getNewBoardInfo = this.createFormDataToSumbit(this.isUpdate);

      await boardService.saveBoardInfo("gallery", getNewBoardInfo);
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "gallery"
      );
    },

    /**
     * 게시글 수정 폼을 제출하는 함수입니다.
     * 유효성 검사를 통과하면 수정된 게시글 정보를 서버에 전송합니다.
     * @param {number} boardId - 수정할 게시글의 ID
     * @returns {void}
     */
    async clickBoardUpdateSubmit(boardId) {
      if (!(await this.validateForm())) {
        return;
      }
      const getNewBoardInfo = this.createFormDataToSumbit(this.isUpdate);
      await boardService.updateBoardInfo("gallery", boardId, getNewBoardInfo);
      this.$router.replace({
        path: `${process.env.VUE_APP_BOARD_GALLERY_VIEW}/${boardId}`,
        query: { ...this.$route.query },
      });
    },
    /**
     * 게시글을 수정하기 위해 제출할 FormData를 생성하는 함수입니다.
     * @param {boolean} isUpdate - 게시글 수정 여부
     * @returns {FormData} - 게시글 수정에 사용될 FormData 객체
     */
    createFormDataToSumbit(isUpdate) {
      const newBoardInfo = new FormData();

      newBoardInfo.append("categoryValue", this.boardInfo.categoryValue);
      newBoardInfo.append("userId", this.boardInfo.userId);
      newBoardInfo.append("title", this.boardInfo.title);
      newBoardInfo.append("content", this.boardInfo.content);

      for (const file of this.inputFiles) {
        newBoardInfo.append(`uploadImages`, file);
      }

      if (isUpdate) {
        for (const file of this.deletedAttachmentIDs) {
          newBoardInfo.append(`deletedAttachmentIDs`, file);
        }
      }

      return newBoardInfo;
    },

    /**
     * 파일 선택 이벤트 핸들러입니다.
     * 선택한 파일을 boardInfo.uploadAttachments 배열에 추가하고 미리보기를 설정합니다.
     * @param {Event} event - 파일 선택 이벤트 객체
     * @returns {void}
     */
    handleFileChange(event) {
      const file = event.target.files[0];
      const index = Number(event.target.id.replace("attachment", "")) - 1;
      this.inputFiles[index] = file;
      // 이미지 미리보기
      const reader = new FileReader();
      reader.onload = (e) => {
        // 해당 첨부파일 입력창의 미리보기를 파일의 미리보기로 설정
        this.fileInputBoxes[index].previewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    /**
     * 첨부 파일 입력 양식을 추가하는 함수입니다.
     * @returns {void}
     */
    clickAddAttachmentForm() {
      this.fileInputBoxes.push({ id: this.nextInputId++ });
    },
    /**
     * 빈 입력 양식을 제거하는 함수입니다.
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
     * 첨부 파일을 삭제하는 함수
     * @param {number} index - 삭제할 첨부 파일의 인덱스
     * @param {string} attachmentId - 삭제할 첨부 파일의 ID
     */

    async clickDeleteAttachment(index, attachmentId) {
      this.deletedAttachmentIDs.push(attachmentId);
      this.boardInfo.boardImages.splice(index, 1);
    },
    /**
     * 갤러리 게시판 목록으로 이동하는 함수입니다.
     * @returns {void}
     */
    moveToGalleryBoardList() {
      boardService.replaceRouterToBoardList(
        this.$router,
        this.$route,
        "gallery"
      );
    },
    /**
     * 양식 유효성을 검사하는 비동기 함수입니다.
     * 유효성 검사를 통과하지 못하면 알림을 표시하고 false를 반환합니다.
     * @returns {boolean} - 양식 유효성 여부
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
      if (!(await this.validateImages(this.uploadAttachments))) {
        alert("이미지는 1MB이하, jpg,gif,png 파일만 올려주세요.");
        return false;
      }
      return true;
    },
  },
};
</script>

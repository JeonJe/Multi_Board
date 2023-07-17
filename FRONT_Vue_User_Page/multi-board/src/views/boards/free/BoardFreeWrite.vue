<template>
  <div>
    <div class="container">
      <div>자유게시판</div>

      <form enctype="multipart/form-data">
        <!-- 카테고리 -->
        <select v-model="boardInfo.category" class="form-control">
          <option
            v-for="categoryOption in categoryOptions"
            :key="categoryOption.value"
            :value="categoryOption.value"
          >
            {{ categoryOption.label }}
          </option>
        </select>
        <!-- 제목 -->
        <input
          v-model="boardInfo.title"
          type="text"
          placeholder="제목"
          required
        />
        <br />
        <!-- 내용 -->
        <textarea
          v-model="boardInfo.content"
          placeholder="내용"
          required
        ></textarea>
      </form>
      <!-- 기존 첨부파일 리스트  -->
      <div v-for="attachment in boardInfo.boardAttachments" :key="attachment">
        <span> {{ attachment.originFileName }}</span>
        <button
          type="button"
          @click="clickDeleteAttachment(attachment.attachmentId)"
        >
          삭제
        </button>
      </div>
      <!-- 새로 첨부파일 추가할 수 있는 input -->
      <div v-for="(file, index) in fileInputBoxes" :key="index">
        <input
          type="file"
          :id="'attachment' + (index + 1)"
          :name="files"
          @change="handleFileChange($event)"
        />
        <button type="button" @click="clickRemoveEmptyInput(index)">
          삭제
        </button>
      </div>
      <button
        type="button"
        @click="clickAddAttachmentForm"
        v-show="fileInputBoxes.length < 5"
      >
        첨부파일 추가
      </button>
      <button type="button" @click="clickBoardInfoSubmit">
        {{ isUpdate ? "수정" : "등록" }}
      </button>
      <router-link :to="moveToFreeBoardList()"> 목록으로 </router-link>
    </div>
  </div>
</template>

<script>
import userService from "@/services/user-service";
import boardService from "@/services/board-service";

export default {
  data() {
    return {
      isUpdate: false,
      boardId: null,
      categories: [],
      boardInfo: {
        category: "",
        userId: "",
        title: "",
        content: "",
        boardAttachments: [],
        uploadAttachments: [],
        deletedAttachmentIDs: [],
      },
      fileInputBoxes: [],
      maxAttachments: 5,
    };
  },
  computed: {
    /**
     * 카테고리 select-option 목록을 생성합니다.
     */
    categoryOptions() {
      const options = this.categories.map((category) => ({
        value: category.categoryValue,
        label: category.categoryName,
      }));
      return [...options];
    },
  },

  mounted() {
    const boardId = this.$route.params.boardId;
    //update
    if (boardId) {
      // TODO : 토큰 권한이 있는지 확인, 없으면 유효한 접근이 아님
      // TODO : 값 가져오기 (토큰도 검사 필요)
      this.isUpdate = true;
      this.boardId = boardId;
    } else {
      // 글쓰기
      this.isUpdate = false;
      this.initBoardInfo();
    }
  },
  methods: {
    /**
     * 파일 선택 이벤트 핸들러입니다. 선택한 파일을 boardInfo.uploadAttachments 배열에 추가합니다.
     * @param {Event} event - 파일 선택 이벤트 객체
     */
    handleFileChange(event) {
      const file = event.target.files[0];
      this.boardInfo.uploadAttachments.push(file);
    },
    /**
     * 첨부 파일 입력 양식을 추가하는 함수입니다.
     */
    clickAddAttachmentForm() {
      console.log("before", this.fileInputBoxes);
      this.fileInputBoxes.push({});
      console.log("after", this.fileInputBoxes);
    },
    /**
     * 빈 입력 양식을 제거하는 함수입니다.
     * @param {number} index - 제거할 입력 양식의 인덱스
     */
    clickRemoveEmptyInput(index) {
      this.fileInputBoxes.splice(index, 1);
    },
    /**
     * 첨부 파일을 삭제하는 함수입니다.
     * @param {number} index - 삭제할 첨부 파일의 인덱스
     * @param {string} attachmentId - 삭제할 첨부 파일의 ID
     */
    clickDeleteAttachment(index, attachmentId) {
      this.boardInfo.deletedAttachmentIDs.push(attachmentId);
      this.boardInfo.boardAttachments.splice(index, 1);
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
          this.boardInfo.category =
            this.categories.length > 0 ? this.categories[0].categoryValue : "";
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * 게시판 정보를 초기화하는 함수입니다.
     */
    async initBoardInfo() {
      await this.getFreeBoardCategories();
      this.boardInfo.userId = await userService.getUserIDByJWT();
      if (this.boardInfo.writer === null) {
        alert("작성자 정보를 가져오지 못했습니다.");

        this.$router.replace({
          path: process.env.VUE_APP_BOARD_FREE_LIST,
          query: this.$route.query,
        });
      }
    },
    /**
     * 게시판 정보를 서버에 저장하는 함수입니다.
     */
    clickBoardInfoSubmit() {
      //Multipart FormData 전송을 위해 FormData 사용
      const newBoardInfo = new FormData();

      newBoardInfo.append("categoryValue", this.boardInfo.category);
      newBoardInfo.append("userId", this.boardInfo.userId);
      newBoardInfo.append("title", this.boardInfo.title);
      newBoardInfo.append("content", this.boardInfo.content);

      this.boardInfo.uploadAttachments.forEach((file) => {
        newBoardInfo.append(`uploadAttachments`, file);
      });
      boardService.saveBoardInfo("free", newBoardInfo);
      this.moveToFreeBoardList();
    },
    /**
     * 자유 게시판 목록으로 이동하는 함수입니다.
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

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
      <input
        type="file"
        id="attachment1"
        name="files"
        class="form-control-file mb-2"
        @change="handleFileChange($event)"
      />
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
        newFiles: [],
        deletedFiles: [],
      },
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
      // write
      this.isUpdate = false;
      this.initBoardInfo();
    }
  },
  methods: {
    handleFileChange(event) {
      // 선택한 파일
      const file = event.target.files[0];

      this.boardInfo.newFiles.push(file);
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
    moveToFreeBoardList() {
      return {
        path: process.env.VUE_APP_BOARD_FREE_LIST,
        query: this.$route.query,
      };
    },
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
    clickBoardInfoSubmit() {
      //Multipart FormData 전송을 위해 FormData 사용
      const newBoardInfo = new FormData();

      newBoardInfo.append("categoryValue", this.boardInfo.category);
      newBoardInfo.append("userId", this.boardInfo.userId);
      newBoardInfo.append("title", this.boardInfo.title);
      newBoardInfo.append("content", this.boardInfo.content);

      this.boardInfo.newFiles.forEach((file) => {
        newBoardInfo.append(`newFiles`, file);
      });
      // 폼 객체 key 와 value 값을 순회.
      let entries = newBoardInfo.entries();
      for (const pair of entries) {
        console.log(pair[0] + ", " + pair[1]);
      }
      boardService.saveBoardInfo("free", newBoardInfo);
    },
  },
};
</script>

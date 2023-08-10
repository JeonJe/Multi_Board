<template>
  <b-modal id="modal-1" title="BootstrapVue">
    <p class="my-4">Hello from modal!</p>
  </b-modal>
  <b-modal :visible="true" title="modalTitle" @ok="handleOk">
    <div>
      <b-form @submit.stop.prevent="handlePasswordSubmit">
        <b-form-input
          v-model="inputPassword"
          ref="passwordInput"
          id="password"
          type="password"
          placeholder="비밀번호"
          :state="passwordState"
        >
        </b-form-input>
        <b-form-invalid-feedback :state="passwordState">
        </b-form-invalid-feedback>
      </b-form>
    </div>
  </b-modal>
</template>

<script>
import boardService from "@/services/board-service";
export default {
  props: {
    show: Boolean,
    selectedBoardId: Number, // 게시글 ID도 props로 전달 받음
  },
  data() {
    return {
      inputPassword: "",
      passwordState: null,
    };
  },
  methods: {
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
        // this.$router.push(this.getBoardDetail(this.selectedBoardId));
        this.$emit("ok"); // 모달 확인 버튼 클릭 이벤트를 상위 컴포넌트로 전달
      } else {
        this.passwordState = "invalidPassword";
      }
    },
  },
};
</script>

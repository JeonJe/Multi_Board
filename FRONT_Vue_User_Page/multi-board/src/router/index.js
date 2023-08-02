import { createRouter, createWebHistory } from "vue-router";
import UserLogin from "../views/auth/UserLogin.vue";
import UserSignup from "../views/auth/UserSignup.vue";
import BoardNoticeList from "../views/boards/notice/BoardNoticeList.vue";
import BoardNoticeView from "../views/boards/notice/BoardNoticeView.vue";
import BoardFreeList from "../views/boards/free/BoardFreeList.vue";
import BoardFreeView from "../views/boards/free/BoardFreeView.vue";
import BoardFreeWrite from "../views/boards/free/BoardFreeWrite.vue";
import BoardGalleryList from "../views/boards/gallery/BoardGalleryList.vue";
import BoardGalleryWrite from "../views/boards/gallery/BoardGalleryWrite.vue";
import BoardGalleryView from "../views/boards/gallery/BoardGalleryView.vue";
import BoardInquiryList from "../views/boards/inquiry/BoardInquiryList.vue";
import BoardInquiryView from "../views/boards/inquiry/BoardInquiryView.vue";
import BoardInquiryWrite from "../views/boards/inquiry/BoardInquiryWrite.vue";

const routes = [
  {
    path: process.env.VUE_APP_USER_LOGIN_PAGE,
    name: UserLogin,
    component: UserLogin,
  },
  {
    path: process.env.VUE_APP_USER_SIGNUP_PAGE,
    name: UserSignup,
    component: UserSignup,
  },
  {
    path: process.env.VUE_APP_BOARD_NOTICE_LIST,
    name: BoardNoticeList,
    component: BoardNoticeList,
  },
  {
    path: `${process.env.VUE_APP_BOARD_NOTICE_VIEW}/:boardId`,
    name: BoardNoticeView,
    component: BoardNoticeView,
  },
  {
    path: process.env.VUE_APP_BOARD_FREE_LIST,
    name: BoardFreeList,
    component: BoardFreeList,
  },
  {
    path: `${process.env.VUE_APP_BOARD_FREE_VIEW}/:boardId`,
    name: BoardFreeView,
    component: BoardFreeView,
  },
  {
    path: `${process.env.VUE_APP_BOARD_FREE_WRITE}/:boardId?`,
    name: BoardFreeWrite,
    component: BoardFreeWrite,
  },
  {
    path: process.env.VUE_APP_BOARD_GALLERY_LIST,
    name: BoardGalleryList,
    component: BoardGalleryList,
  },
  {
    path: `${process.env.VUE_APP_BOARD_GALLERY_WRITE}/:boardId?`,
    name: BoardGalleryWrite,
    component: BoardGalleryWrite,
  },
  {
    path: `${process.env.VUE_APP_BOARD_GALLERY_VIEW}/:boardId`,
    name: BoardGalleryView,
    component: BoardGalleryView,
  },
  {
    path: process.env.VUE_APP_BOARD_INQUIRY_LIST,
    name: BoardInquiryList,
    component: BoardInquiryList,
  },
  {
    path: `${process.env.VUE_APP_BOARD_INQUIRY_WRITE}/:boardId?`,
    name: BoardInquiryWrite,
    component: BoardInquiryWrite,
  },
  {
    path: `${process.env.VUE_APP_BOARD_INQUIRY_VIEW}/:boardId`,
    name: BoardInquiryView,
    component: BoardInquiryView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

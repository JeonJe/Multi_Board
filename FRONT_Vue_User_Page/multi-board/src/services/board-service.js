import axios from "axios";

/**
 * JSON 콘텐츠를 위한 서버 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
 */
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * 요청 전에 실행될 인터셉터
 * @param {Object} config - 요청 설정 객체
 * @returns {Object} - 처리된 요청 설정 객체
 */
api.interceptors.request.use(
  (config) => {
    console.log("[Before] Request:", config);
    return config;
  },
  (error) => {
    console.error("[Before] Request Error:", error);
    return Promise.reject(error);
  }
);

/**
 * 요청 후에 실행될 인터셉터
 * @param {Object} response - 응답 객체
 * @returns {Object} - 처리된 응답 객체
 */
api.interceptors.response.use(
  (response) => {
    console.log("[After] Response:", response);
    return response;
  },
  (error) => {
    console.error("[After] Response Error:", error);
    return Promise.reject(error);
  }
);

/**
 * 공지사항 목록을 가져오는 함수입니다.
 *
 * @param {object} searchCondition - 검색 조건
 * @returns {Promise} - 공지사항 목록을 담은 Promise 객체
 */
const getNoticeBoardList = async (searchCondtion) => {
  try {
    const response = await api.get(process.env.VUE_APP_API_BOARD_NOTICE, {
      params: searchCondtion,
    });
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

/**
 * 알림 표시된 공지사항 목록을 가져오는 함수입니다.
 *
 * @returns {Promise} - 알림 표시된 공지사항 목록을 담은 Promise 객체
 */
const getMarkNoticedBoardList = async () => {
  try {
    const response = await api.get(
      process.env.VUE_APP_API_BOARD_NOTICE_COUNT_NOTICED
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

/**
 * 공지사항 상세 정보를 가져오는 함수입니다.
 *
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 공지사항 상세 정보를 담은 Promise 객체
 */
const getNoticeBoardDetail = async (boardId) => {
  try {
    const requestURL = `${process.env.VUE_APP_API_BOARD_NOTICE}/${boardId}`;
    const response = await api.get(requestURL);
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

export default {
  getNoticeBoardList,
  getMarkNoticedBoardList,
  getNoticeBoardDetail,
};

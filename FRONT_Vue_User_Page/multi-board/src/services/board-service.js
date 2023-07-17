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
 * JSON 콘텐츠를 위한 서버 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
 */
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data",
  },
});

/**
 * 요청 전에 실행될 인터셉터
 * @param {Object} config - 요청 설정 객체
 * @returns {Object} - 처리된 요청 설정 객체
 * @throws {Error} API 요청 중 발생한 오류
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
 * @throws {Error} API 요청 중 발생한 오류
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
 * 알림 표시된 공지사항 목록을 가져오는 함수입니다.
 *
 * @returns {Promise} - 알림 표시된 공지사항 목록을 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
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
 * 게시판의 카테고리 목록을 가져오는 함수입니다.
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @returns {Promise} - 게시판의 카테고리 목록을 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const getBoardList = async (boardType, searchCondtion) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const response = await api.get(apiURL, {
      params: searchCondtion,
    });
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

/**
 * 게시판의 카테고리 목록을 가져오는 함수입니다.
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @returns {Promise} - 게시판의 카테고리 목록을 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const getBoardCategories = async (boardType) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const response = await api.get(`${apiURL}/categories`);
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

/**
 * 게시판의 상세 정보를 가져오는 함수입니다.
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 게시판의 상세 정보를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const getBoardDetail = async (boardType, boardId) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const requestURL = `${apiURL}/${boardId}`;
    const response = await api.get(requestURL);
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

const saveBoardInfo = async (boardType, newBoardInfo) => {
  try {
    const apiRUL = await getAPIUrlByBoardType(boardType);
    multipartApi.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.getItem("jwt")}`;
    const response = await multipartApi.post(apiRUL, newBoardInfo);
    console.log(response, newBoardInfo);
  } catch (err) {
    console.error(err);
  }
};
/**
 * 게시판 종류에 따라 해당하는 API URL을 가져오는 함수입니다.
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @returns {Promise} - 게시판 종류에 따른 API URL을 담은 Promise 객체
 * @throws {Error} 지원하지 않는 보드타입인 경우 발생하는 오류
 */
const getAPIUrlByBoardType = async (boardType) => {
  switch (boardType) {
    case "notice":
      return process.env.VUE_APP_API_BOARD_NOTICE;
    case "free":
      return process.env.VUE_APP_API_BOARD_FREE;
    case "gallery":
      return process.env.VUE_APP_API_BOARD_GALLARY;
    case "inquiry":
      return process.env.VUE_APP_API_BOARD_INQUIRY;
    default:
      return new Error("지원하지 않는 보드타입입니다 :  ${boardType}");
  }
};

export default {
  getMarkNoticedBoardList,
  getBoardCategories,
  getBoardDetail,
  getBoardList,
  saveBoardInfo,
};

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
 * multipart 콘텐츠를 위한 서버 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
 */
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data",
  },
});

/**
 * API 요청 시 인증 토큰을 헤더에 추가합니다.
 * 이 함수는 사용자 로그인 시 인증 토큰을 헤더에 설정하는 역할을 합니다.
 * @param {string} token - 사용자의 JWT 토큰
 */
const setAuthorizationHeader = (token) => {
  const authHeader = `Bearer ${token}`;
  api.defaults.headers.common["Authorization"] = authHeader;
  multipartApi.defaults.headers.common["Authorization"] = authHeader;
};

const jwtToken = localStorage.getItem("jwt");
if (jwtToken) {
  setAuthorizationHeader(jwtToken);
}

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
 * 알림 표시된 공지사항 목록을 가져오는 함수
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
    alert(error);
  }
};

/**
 * 게시판의 카테고리 목록을 가져오는 함수
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
    alert("리스트를 가져오지 못했습니다.");
    return false;
  }
};

/**
 * 게시판의 카테고리 목록을 가져오는 함수
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
    alert("카테고리를 가져오지 못했습니다.");
    return false;
  }
};

/**
 * 게시판의 상세 정보를 가져오는 함수
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
    alert(error);
  }
};

/**
 * saveBoardInfo 메소드는 주어진 게시판 타입과 새로운 게시글 정보를 받아와 저장하는 기능을 제공
 *
 * @param boardType     게시판 타입 (예: "free", "notice" 등)
 * @param newBoardInfo  새로운 게시글 정보
 * @return              게시글 저장 결과
 */
const saveBoardInfo = async (boardType, newBoardInfo) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const response = await multipartApi.post(apiURL, newBoardInfo);
    alert(response.data.message);
  } catch (error) {
    alert(error);
  }
};
/**
 * 게시글을 삭제하는 함수
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 게시글 삭제 결과를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const deleteBoardInfo = async (boardType, boardId) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const response = await api.delete(`${apiURL}/${boardId}`);
    alert(response.data.message);
    return true;
  } catch (error) {
    console.log(error.response.data.message);
    return false;
  }
};
/**
 * 게시글을 수정하는 함수
 *
 * @param {string} boardType - 게시판 종류 ('notice', 'free', 'gallery', 'inquiry' 등)
 * @param {number} boardId - 게시글 ID
 * @param {Object} newBoardInfo - 수정할 게시글 정보
 * @returns {Promise} - 게시글 수정 결과를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const updateBoardInfo = async (boardType, boardId, newBoardInfo) => {
  try {
    const apiURL = await getAPIUrlByBoardType(boardType);
    const response = await multipartApi.put(
      `${apiURL}/${boardId}`,
      newBoardInfo
    );
    alert(response.data.message);
    return true;
  } catch (error) {
    console.log(error.response.data.message);
    return false;
  }
};

/**
 * 게시판의 편집 권한을 확인하는 함수
 *
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 게시글 편집 권한 여부를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const hasBoardEditPermission = async (boardId) => {
  try {
    const response = await api.get(
      `${process.env.VUE_APP_API_BOARD_FREE_EDIT_PERMISSION}/${boardId}`
    );
    return response.data.data;
  } catch (error) {
    console.log(error.response.data.message);
    return false;
  }
};

/**
 * 자유 게시판에 댓글을 작성하는 함수
 *
 * @param {string} newComment - 작성할 댓글 내용
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 댓글 작성 결과를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const addFreeBoardComment = async (newComment, boardId) => {
  try {
    const response = await api.post(
      `${process.env.VUE_APP_API_BOARD_FREE}/${boardId}/comments`,
      { content: newComment }
    );
    alert(response.data.message);
  } catch (error) {
    console.log(error.response.data.message);
    alert(error.response.data.message);
  }
};
/**
 * 자유 게시판의 댓글을 삭제하는 함수
 *
 * @param {Object} comment - 삭제할 댓글 정보
 * @param {number} boardId - 게시글 ID
 * @returns {Promise} - 댓글 삭제 결과를 담은 Promise 객체
 * @throws {Error} API 요청 중 발생한 오류
 */
const deleteFreeBoardComment = async (comment, boardId) => {
  try {
    const response = await api.delete(
      `${process.env.VUE_APP_API_BOARD_FREE}/${boardId}/comments`,
      { data: comment }
    );
    alert(response.data.message);
  } catch (error) {
    console.log(error.response.data.message);
    alert(error.response.data.message);
  }
};
/**
 * 자유 게시판 목록 페이지로 라우터를 변경하는 함수
 *
 * @param {Object} router - Vue Router 객체
 * @param {Object} route - 현재 라우트 정보
 */
const replaceRouterToFreeBoardList = (router, route) => {
  router.replace({
    path: process.env.VUE_APP_BOARD_FREE_LIST,
    query: route.query,
  });
};
/**
 * 게시판 종류에 따라 해당하는 API URL을 가져오는 함수
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
  deleteBoardInfo,
  saveBoardInfo,
  updateBoardInfo,
  hasBoardEditPermission,
  addFreeBoardComment,
  deleteFreeBoardComment,
  replaceRouterToFreeBoardList,
};

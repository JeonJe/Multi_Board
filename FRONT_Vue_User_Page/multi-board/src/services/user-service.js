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
 * 사용자 회원가입을 위한 함수
 * @param {Object} userData - 사용자 데이터
 * @returns {Promise<void>}
 */
const signupUser = async (userData) => {
  try {
    const response = await api.post(
      process.env.VUE_APP_API_USER_SIGNUP,
      JSON.stringify(userData)
    );
    localStorage.setItem("jwt", response.data.data);
    alert(response.data);
    this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
  } catch (error) {
    const res = error.response.data;
    alert(res.data[0]);
  }
};

/**
 * 중복된 ID 체크를 위한 함수
 * @param {string} userId - 사용자 ID
 * @returns {Promise<void>}
 */
const checkDuplicateId = async (userId) => {
  try {
    const URL = process.env.VUE_APP_API_CHECK_DUPLICATED_ID + userId;
    await api.get(URL);
    alert("사용 가능한 ID입니다.");
  } catch (error) {
    const res = error.response.data;
    alert(res.data);
  }
};

/**
 * 사용자 로그인을 위한 함수
 * @param {Object} userData - 사용자 데이터
 * @returns {Promise<void>}
 */
const loginUser = async (userData) => {
  try {
    const response = await api.post(
      process.env.VUE_APP_API_USER_LOGIN,
      JSON.stringify(userData)
    );
    localStorage.setItem("jwt", response.data.data);
    alert("로그인이 성공하였습니다.");
    //TODO : 사용자 정보는 비밀번호를 포함하면 안되고, vuex로 상태관리가 되어야 함.
  } catch (error) {
    const res = error.response.data;
    alert(res.data);
  }
};

/**
 * JWT 토큰 확인을 위한 함수
 * @returns {Promise<void>}
 */
const checkJwtToken = async () => {
  try {
    api.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.getItem("jwt")}`;
    console.log(localStorage.getItem("jwt"));
    const response = await api.get(process.env.VUE_APP_API_CHECK_JWT_TOKEN);
    console.log(response.data);
  } catch (error) {
    const res = error.response.data;
    alert(res.data);
  }
};

export default { signupUser, loginUser, checkDuplicateId, checkJwtToken };

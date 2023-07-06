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

const getNoticeBoardList = async (searchCondtion) => {
  try {
    const response = await api.get(process.env.VUE_APP_API_BOARD_NOTICE, {
      params: searchCondtion,
    });
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};
// const signupUser = async (userData) => {
//   try {
//     const response = await api.post(
//       process.env.VUE_APP_API_USER_SIGNUP,
//       JSON.stringify(userData)
//     );
//     localStorage.setItem("jwt", response.data.data);
//     alert(response.data);
//     this.$router.push({ path: process.env.VUE_APP_USER_LOGIN_PAGE });
//   } catch (error) {
//     const res = error.response.data;
//     alert(res.data[0]);
//   }
// };

export { getNoticeBoardList };

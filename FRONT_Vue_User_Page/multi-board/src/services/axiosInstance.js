import axios from "axios";

/**
 * Axios 인스턴스 생성
 * @type {import('axios').AxiosInstance}
 */
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json", // JSON 콘텐츠 타입
  },
});

/**
 * Axios 인스턴스 생성 (multipart 콘텐츠를 위한 헤더 설정)
 * @type {import('axios').AxiosInstance}
 */
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data", // multipart 콘텐츠 타입
  },
});

/**
 * 인증 토큰을 헤더에 설정하는 함수
 * @param {string} token - 인증 토큰
 */
const setAuthorizationHeader = (token) => {
  const authHeader = `Bearer ${token}`;
  api.defaults.headers.common["Authorization"] = authHeader;
  multipartApi.defaults.headers.common["Authorization"] = authHeader;
};
/**
 * 헤더의 인증 토큰을 제거하는 함수
 */
const clearAuthorizationHeader = () => {
  api.defaults.headers.common["Authorization"] = null;
  multipartApi.defaults.headers.common["Authorization"] = null;
};

export { api, multipartApi, setAuthorizationHeader, clearAuthorizationHeader };

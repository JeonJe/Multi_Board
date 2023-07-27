import axios from "axios";

// JSON 콘텐츠와 multipart 콘텐츠를 위한 서버 URL과 헤더를 사용하여 Axios 인스턴스 생성
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json", // JSON 콘텐츠 타입
  },
});

// multipart 콘텐츠를 위한 헤더 설정
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data", // multipart 콘텐츠 타입
  },
});

const setAuthorizationHeader = (token) => {
  const authHeader = `Bearer ${token}`;
  api.defaults.headers.common["Authorization"] = authHeader;
  multipartApi.defaults.headers.common["Authorization"] = authHeader;
};

const clearAuthorizationHeader = () => {
  api.defaults.headers.common["Authorization"] = null;
  multipartApi.defaults.headers.common["Authorization"] = null;
};

// 생성한 인스턴스를 내보내기
export { api, multipartApi, setAuthorizationHeader, clearAuthorizationHeader };

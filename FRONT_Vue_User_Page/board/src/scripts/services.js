// services.js
import axios from "axios";

// JSON 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// multipart/form-data 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data",
  },
});

// CORS를 활성화하기 위한 다음 코드 추가
// API 요청에 대한 공통 헤더 설정
api.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
api.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
api.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

// multipart API 요청에 대한 공통 헤더 설정
multipartApi.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
multipartApi.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
multipartApi.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";


// 요청 전에 실행될 인터셉터
api.interceptors.request.use(
  (config) => {
    console.log("Request:", config);
    return config;
  },
  (error) => {
    console.error("Request Error:", error);
    return Promise.reject(error);
  }
);

//요청 후에 실행될 인터셉터
api.interceptors.response.use(
  (response) => {
    console.log("Response:", response);
    return response;
  },
  (error) => {
    console.error("Response Error:", error);
    return Promise.reject(error);
  }
);

const test1 = async () => {
  try {
    // process.env.URL사용 
    const response = await api.get("/test1");
    return response.data;
  } catch (error) {
    console.error("test1 Error:", error);
    throw error;
  }
};

const test2 = async () => {
  try {
    const response = await api.get("/test2");
    return response.data;
  } catch (error) {
    console.error("test2 Error:", error);
    throw error;
  }
};

export { test1, test2 };
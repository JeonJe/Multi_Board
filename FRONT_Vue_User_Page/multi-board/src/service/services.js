import axios from "axios";

// JSON 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// // multipart/form-data 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
// const multipartApi = axios.create({
//   baseURL: "http://localhost:8080",
//   headers: {
//     "Content-Type": "multipart/form-data",
//   },
// });

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

// 요청 후에 실행될 인터셉터
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

const signupUser = async (userData) => {
  try {
    const response = await api.post(
      process.env.VUE_APP_API_USER_SIGNUP,
      JSON.stringify(userData)
    );
    console.log("response: ", response.data.data);
    // localStorage.setItem("jwt",token.accessToken)
    // alert("로그인 되었습니다");
  } catch (error) {
    alert("Error: " + error.response.data);
  }
};

const checkDuplicateId = async (userId) => {
  try {
    const URL = process.env.VUE_APP_API_CHECK_DUPLICATED_ID + userId;
    await api.get(URL);
    alert("사용 가능한 ID입니다.");
  } catch (error) {
    alert("Error: " + error.response.data);
  }
};

const loginUser = async (userData) => {
  try {
    const response = await api.post(
      process.env.VUE_APP_API_USER_LOGIN,
      JSON.stringify(userData)
    );
    localStorage.setItem("jwt", response.data.data);
    alert("로그인이 성공하였습니다.");
  } catch (error) {
    alert("Error: " + error.response.data);
  }
};

export { signupUser, loginUser, checkDuplicateId };

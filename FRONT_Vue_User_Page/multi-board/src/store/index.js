import { createStore } from "vuex";

/**
 * Vuex Store를 생성
 *
 * @returns {Object} - Vuex Store 객체
 */
export default createStore({
  /**
   * 상태(State) 객체를 정의
   * @returns {Object} - 초기 상태 객체
   */
  state() {
    return {
      user: null, // 사용자 정보를 저장하는 상태 변수
    };
  },
  /**
   * Getter 함수를 정의
   */
  getters: {
    /**
     * 사용자가 로그인 중인지 여부를 반환하는 Getter 함수
     * @param {Object} state - Vuex 상태 객체
     * @returns {boolean} - 사용자 로그인 여부
     */
    isLoggedIn: (state) => !!state.user,
    /**
     * 현재 로그인한 사용자 정보를 반환하는 Getter 함수
     * @param {Object} state - Vuex 상태 객체
     * @returns {Object|null} - 현재 로그인한 사용자 정보
     */
    getUser: (state) => state.user,
  },
  mutations: {
    /**
     * 사용자 정보를 설정하는 Mutation 함수
     * @param {Object} state - Vuex 상태 객체
     * @param {Object|null} user - 설정할 사용자 정보
     */
    setUser(state, user) {
      state.user = user;
    },
    /**
     * 사용자 정보를 초기화하는 Mutation 함수
     * @param {Object} state - Vuex 상태 객체
     */
    clearUser(state) {
      state.user = null;
    },
  },
  /**
   * Action 함수를 정의합니다.
   */
  actions: {
    /**
     * 사용자 정보를 설정하는 Action 함수
     * @param {Object} context - Vuex context 객체
     * @param {Object|null} user - 설정할 사용자 정보
     */
    setLoginUser({ commit }, user) {
      commit("setUser", user);
    },
    /**
     * 사용자 정보를 초기화하는 Action 함수
     * 로그아웃 시 localStorage에서 JWT를 제거하고 사용자 정보를 초기화합니다.
     * @param {Object} context - Vuex context 객체
     */
    clearLoginUser({ commit }) {
      localStorage.removeItem("jwt");
      commit("clearUser");
    },
  },
  modules: {},
});

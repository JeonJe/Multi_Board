import { createStore } from "vuex";

export default createStore({
  state() {
    return {
      user: null,
    };
  },
  getters: {
    isLoggedIn: (state) => !!state.user,
    getUser: (state) => state.user, // Get the user information
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    },
    clearUser(state) {
      state.user = null;
    },
  },
  actions: {
    setLoginUser({ commit }, user) {
      commit("setUser", user);
    },
    clearLoginUser({ commit }) {
      localStorage.removeItem("jwt");
      commit("clearUser");
    },
  },
  modules: {
    // Define modules here if needed
  },
});

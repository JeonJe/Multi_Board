<template>
  <div>
    <h1>로그인</h1>
    <form>
      <div>
        <label for="id">ID:</label>
        <input type="text" id="id" v-model="id" />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" />
      </div>
      <button @click="login">Login</button>
    </form>
    <button @click="register">Register as a Member</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      id: "",
      password: "",
    };
  },
  methods: {
    login() {
      // Perform login logic here
      // You can access the entered ID and password as this.userId and this.password
      // Example validation:
      alert("login ");
    },
    register() {
      // Perform registration logic here
      alert("Redirect to member registration page");
    },
  },
};
</script>

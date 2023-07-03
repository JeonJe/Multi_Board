const { defineConfig } = require("@vue/cli-service");
const Dotenv = require("dotenv-webpack");

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [new Dotenv()],
  },
  // devServer: {
  //   proxy: process.env.VUE_APP_API_SER_URL,
  // },
});

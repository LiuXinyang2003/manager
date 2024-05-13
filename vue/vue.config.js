const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8028,
      host: '0.0.0.0',
      client: {
          webSocketURL: 'ws://0.0.0.0:8028/ws',
      },
      headers: {
          'Access-Control-Allow-Origin': '*',
      }
  },
  chainWebpack: config =>{
    config.plugin('html')
        .tap(args => {
          args[0].title = "考勤管理系统";
          return args;
        })
  }
})

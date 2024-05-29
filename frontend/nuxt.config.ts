// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  routeRules : {
      '/checkers/**' : {
        proxy : 'http://localhost:8080/checkers/**'
      },
      '/api/**' : {
        proxy : 'http://localhost:8080/api/**'
      }
  },
  imports : {
     dirs : [
      './scripts'
     ]
  },
  css: [
    'boxicons/css/boxicons.min.css'
  ],
})

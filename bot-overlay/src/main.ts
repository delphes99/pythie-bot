import { createApp } from "vue"
import App from "./App.vue"

createApp(App)
  .provide("backendUrl", "http://localhost:8080")
  .mount("#app")

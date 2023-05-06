import {createApp} from "vue"
import App from "./App.vue"
import "./index.css"

createApp(App)
    .provide("backendUrl", "http://localhost:8080")
    .mount("#app")

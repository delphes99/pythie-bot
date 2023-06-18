import {createApp, InjectionKey} from "vue"
import App from "./App.vue"
import "./index.css"

export const AppInjectionKeys = {
    BACKEND_URL: Symbol() as InjectionKey<string>
}

createApp(App)
    .provide(AppInjectionKeys.BACKEND_URL, "http://localhost:8080")
    .mount("#app")

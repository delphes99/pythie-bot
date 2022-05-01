import { LocalStorageItem } from "@/common/LocalStorageItem"
import { useStorage } from "@vueuse/core"
import ElementPlus from "element-plus"
import "element-plus/dist/index.css"
import { createPinia } from "pinia"
import { createApp } from "vue"
import { createI18n } from "vue-i18n"
import App from "./App.vue"

import "./common/assets/styles/index.css"
import router from "./router"

const messages = {
  en: require("./lang/en.json"),
  fr: require("./lang/fr.json"),
}

const storedLocale = useStorage(LocalStorageItem.LANGUAGE, "en").value

const i18n = createI18n({
  locale: storedLocale,
  fallbackLocale: "en",
  messages,
})

createApp(App)
  .use(router)
  .use(i18n)
  .use(ElementPlus)
  .use(createPinia())
  .provide("backendUrl", "http://localhost:8080")
  .mount("#app")

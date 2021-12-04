import { LocalStorageItem } from "@/common/LocalStorageItem";
import ElementPlus from "element-plus";
import { createApp } from "vue";
import { createI18n } from "vue-i18n";
import App from "./App.vue";

import "./common/assets/styles/index.css";
import "element-plus/dist/index.css";
import router from "./router";

const messages = {
  en: require("./lang/en.json"),
  fr: require("./lang/fr.json")
};

const storedLocale = localStorage.getItem(LocalStorageItem.LANGUAGE) ?? "en";

const i18n = createI18n({
  locale: storedLocale,
  fallbackLocale: "en",
  messages
});

createApp(App)
  .use(router)
  .use(i18n)
  .use(ElementPlus)
  .provide("backendUrl", "http://localhost:8080")
  .mount("#app");

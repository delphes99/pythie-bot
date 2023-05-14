import {NotificationService} from "@/common/components/common/notification/notification.service";
import {LocalStorageItem} from "@/common/LocalStorageItem"
import FeatureService from "@/features/feature.service";
import {InjectionKeys} from "@/injection.keys";
import MediasService from "@/media/MediasService";
import {useStorage} from "@vueuse/core"
import {createPinia} from "pinia"
import {createApp} from "vue"
import {createI18n} from "vue-i18n"
import Vue3Toasity from "vue3-toastify";

import 'vue3-toastify/dist/index.css';
import App from "./App.vue"
import "./common/assets/styles/index.css"

import en from "./lang/en.json"
import fr from "./lang/fr.json"
import router from "./router"

const messages = {
    en: en,
    fr: fr,
}

const storedLocale = useStorage(LocalStorageItem.LANGUAGE, "en").value

const i18n = createI18n({
    locale: storedLocale,
    fallbackLocale: "en",
    messages,
})

const backendUrl = "http://localhost:8080"

createApp(App)
    .use(router)
    .use(i18n)
    .use(createPinia())
    .use(Vue3Toasity, {
        autoClose: 3000,
        position: "top-right",
    })
    .provide(InjectionKeys.BACKEND_URL, backendUrl)
    .provide(InjectionKeys.MEDIA_SERVICE, new MediasService(backendUrl))
    .provide(InjectionKeys.FEATURE_SERVICE, new FeatureService(backendUrl))
    .provide(InjectionKeys.NOTIFICATION_SERVICE, new NotificationService())
    .mount("#app")

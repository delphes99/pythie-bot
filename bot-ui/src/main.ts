import {AppInjectionKeys} from "@/app.injection.keys"
import {NotificationService} from "@/common/designSystem/notification/notification.service";

import en from "@/common/lang/en.json"
import fr from "@/common/lang/fr.json"
import {LocalStorageItem} from "@/common/LocalStorageItem"
import FeatureService from "@/features/feature.service"
import MediaService from "@/media/media.service"
import {MonitoringService} from "@/monitoring/monitoring.service"
import {useStorage} from "@vueuse/core"
import {createPinia} from "pinia"
import {createApp} from "vue"
import {createI18n} from "vue-i18n"
// @ts-ignore
import JsonViewer from "vue-json-viewer"
import Vue3Toasity from "vue3-toastify"

import 'vue3-toastify/dist/index.css';
import App from "./App.vue"
import "./common/assets/styles/index.css"
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
    .use(JsonViewer)
    .provide(AppInjectionKeys.BACKEND_URL, backendUrl)
    .provide(AppInjectionKeys.MEDIA_SERVICE, new MediaService(backendUrl))
    .provide(AppInjectionKeys.FEATURE_SERVICE, new FeatureService(backendUrl))
    .provide(AppInjectionKeys.NOTIFICATION_SERVICE, new NotificationService())
    .provide(AppInjectionKeys.MONITORING_SERVICE, new MonitoringService(backendUrl))
    .mount("#app")

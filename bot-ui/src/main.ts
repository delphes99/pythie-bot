import {AppInjectionKeys} from "@/AppInjectionKeys"
import UiIcon from "@/common/designSystem/icons/UiIcon.vue";
import {NotificationService} from "@/common/designSystem/notification/NotificationService";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";

import en from "@/common/lang/en.json"
import fr from "@/common/lang/fr.json"
import {LocalStorageItem} from "@/common/utils/LocalStorage.item"
import FeatureService from "@/features/FeatureService"
import MediaService from "@/media/MediaService"
import {MonitoringService} from "@/monitoring/MonitoringService"
import {useStorage} from "@vueuse/core"
import {createPinia} from "pinia"
import {createApp} from "vue"
import {createI18n} from "vue-i18n"
// @ts-ignore
import JsonViewer from "vue-json-viewer"
import Vue3Toasity from "vue3-toastify"

import 'vue3-toastify/dist/index.css';
import App from "./App.vue"
import "./common/style/index.css"
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
    .component("json-viewer", JsonViewer)
    .component("ui-icon", UiIcon)
    .use(router)
    .use(i18n)
    .use(createPinia())
    .use(Vue3Toasity, {
        autoClose: 3000,
        position: "top-right",
    })
    .provide(AppInjectionKeys.BACKEND_URL, backendUrl)
    .provide(AppInjectionKeys.MEDIA_SERVICE, new MediaService(backendUrl))
    .provide(AppInjectionKeys.FEATURE_SERVICE, new FeatureService(backendUrl))
    .provide(AppInjectionKeys.NOTIFICATION_SERVICE, new NotificationService())
    .provide(AppInjectionKeys.MONITORING_SERVICE, new MonitoringService(backendUrl))
    .provide(AppInjectionKeys.DYNAMIC_FORM_SERVICE, new DynamicFormService(backendUrl))
    .mount("#app")

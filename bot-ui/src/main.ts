import {LocalStorageItem} from "@/common/LocalStorageItem"
import {useStorage} from "@vueuse/core"
import ElementPlus from "element-plus"
import "element-plus/dist/index.css"
import {createPinia} from "pinia"
import {createApp} from "vue"
import {createI18n} from "vue-i18n"
import App from "./App.vue"
import "./common/assets/styles/index.css"
import router from "./router"

import en from "./lang/en.json"
import fr from "./lang/fr.json"
import MediasService from "@/media/MediasService";
import FeatureService from "@/features/feature.service";
import FeatureDescriptionService from "@/features/feature-description.service";

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

export enum InjectKey {
    BACKEND_URL = "backendUrl",
    MEDIA_SERVICE = "media.service",
    FEATURE_SERVICE = "feature.service",
    FEATURE_DESCRIPTION_SERVICE = "feature-description.service",
}

const backendUrl = "http://localhost:8080"

createApp(App)
    .use(router)
    .use(i18n)
    .use(ElementPlus)
    .use(createPinia())
    .provide(InjectKey.BACKEND_URL, backendUrl)
    .provide(InjectKey.MEDIA_SERVICE, new MediasService(backendUrl))
    .provide(InjectKey.FEATURE_SERVICE, new FeatureService(backendUrl))
    .provide(InjectKey.FEATURE_DESCRIPTION_SERVICE, new FeatureDescriptionService())
    .mount("#app")

import {Options} from "@/common/designSystem/form/radio/Options";
import {LocalStorageItem} from "@/common/utils/local.storage.item"
import {lightTheme, themes} from "@/common/utils/theme/Themes"
import {useStorage} from "@vueuse/core"
import {defineStore} from "pinia"
import {computed} from "vue";
import {useI18n} from "vue-i18n";

const useApplicationThemeStore = defineStore('applicationTheme', {
    state: () => {
        return {theme: useStorage(LocalStorageItem.APPLICATION_THEME, lightTheme)}
    },
})

export function useApplicationTheme() {
    const applicationThemeStore = useApplicationThemeStore()

    const themeClass = computed(() => {
        const monochromeClass = applicationThemeStore.theme.monochrome ? " grayscale" : ""
        return `${applicationThemeStore.theme.className}-theme${monochromeClass}`
    })

    const {t} = useI18n()
    const availableThemesOptions = Options.for(
        themes,
        (theme) => t("settings.theme." + theme.name),
        (theme) => theme.name
    )

    return {
        theme: applicationThemeStore.theme,
        themeClass,
        availableThemesOptions,
        currentTheme: applicationThemeStore
    }
}
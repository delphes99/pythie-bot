import {Options} from "@/common/designSystem/form/radio/Options";
import {LocalStorageItem} from "@/common/LocalStorageItem"
import {Themes} from "@/common/utils/theme/Themes"
import {useStorage} from "@vueuse/core"
import {defineStore} from "pinia"
import {computed} from "vue";
import {useI18n} from "vue-i18n";

const useApplicationThemeStore = defineStore('applicationTheme', {
    state: () => {
        return {theme: useStorage(LocalStorageItem.APPLICATION_THEME, Themes.LIGHT)}
    },
})

export function useApplicationTheme() {
    const applicationThemeStore = useApplicationThemeStore()

    const themeClass = computed(() => {
        return `${applicationThemeStore.theme}-theme`
    })

    const {t} = useI18n()
    const availableThemesOptions = Options.for<string>(Object.values(Themes), (theme) =>
        t("settings.theme." + theme),
    )

    return {
        theme: applicationThemeStore.theme,
        themeClass,
        availableThemesOptions,
        currentTheme: applicationThemeStore
    }
}
import {Options} from "@/common/components/common/form/radio/Options";
import {Themes} from "@/common/components/common/theme/Themes"
import {LocalStorageItem} from "@/common/LocalStorageItem"
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
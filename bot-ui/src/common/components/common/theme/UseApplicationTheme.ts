import {Themes} from "@/common/components/common/theme/Themes"
import {LocalStorageItem} from "@/common/LocalStorageItem"
import {useStorage} from "@vueuse/core"
import {defineStore} from "pinia"

export const useApplicationTheme = defineStore('applicationTheme', {
    state: () => {
        return {theme: useStorage(LocalStorageItem.APPLICATION_THEME, Themes.LIGHT)}
    },
})
import {LocalStorageItem} from "@/common/utils/LocalStorage.item";
import {useStorage} from "@vueuse/core";

export function useShowThemeSwitcher() {
    return {
        showThemeSwitcher: useStorage(LocalStorageItem.SHOW_THEME_SWITCHER, false)
    }
}
import {LocalStorageItem} from "@/common/utils/local.storage.item";
import {useStorage} from "@vueuse/core";

export function useShowThemeSwitcher() {
    return {
        showThemeSwitcher: useStorage(LocalStorageItem.SHOW_THEME_SWITCHER, false)
    }
}
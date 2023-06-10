<template>
  <ui-panel title="settings.title">
    <ui-radios
        v-model="lang"
        :options="availableLocalesOptions"
        title="settings.language.label"
        name="locale"
    />
    <theme-switcher :display-label="true"/>
    <ui-toggle v-model="showThemeSwitcher" label="settings.showThemeSwitcher"/>
  </ui-panel>
</template>
<script lang="ts" setup>
import {Options} from "@/common/designSystem/form/radio/Options"
import UiRadios from "@/common/designSystem/form/radio/ui-radios.vue"
import UiToggle from "@/common/designSystem/form/toggle/ui-toggle.vue";
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import ThemeSwitcher from "@/common/style/theme-switcher.vue";
import {useShowThemeSwitcher} from "@/common/style/useShowThemeSwitcher";
import {LocalStorageItem} from "@/common/utils/local.storage.item"
import {useStorage} from "@vueuse/core"
import {watch} from "vue"
import {useI18n} from "vue-i18n"

const {t, locale, availableLocales} = useI18n()

const lang = useStorage(LocalStorageItem.LANGUAGE, availableLocales[0])

watch(lang, (newValue) => {
  locale.value = newValue
})

const availableLocalesOptions = Options.for<string>(availableLocales, (lang) =>
    t("settings.language." + lang),
)

const {showThemeSwitcher} = useShowThemeSwitcher()
</script>
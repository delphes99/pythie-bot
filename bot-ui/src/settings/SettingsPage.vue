<template>
  <UiPanel title="settings.title">
    <UiRadios
        v-model="lang"
        :options="availableLocalesOptions"
        title="settings.language.label"
        name="locale"
    />
    <ThemeSwitcher :display-label="true"/>
    <UiToggle v-model="showThemeSwitcher" label="settings.showThemeSwitcher"/>
  </UiPanel>
</template>
<script lang="ts" setup>
import {Options} from "@/common/designSystem/form/radio/Options"
import UiRadios from "@/common/designSystem/form/radio/UiRadios.vue"
import UiToggle from "@/common/designSystem/form/toggle/UiToggle.vue";
import UiPanel from "@/common/designSystem/panel/UiPanel.vue"
import ThemeSwitcher from "@/common/style/ThemeSwitcher.vue";
import {useShowThemeSwitcher} from "@/common/style/useShowThemeSwitcher";
import {LocalStorageItem} from "@/common/utils/LocalStorage.item"
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
<template>
  <ui-panel title="settings.title">
    <ui-radios
      v-model="lang"
      name="locale"
      :options="availableLocalesOptions"
      :title="t('settings.language.label')"
    />
    <ui-radios
      v-model="currentTheme.theme"
      name="theme"
      :options="availableThemesOptions"
      :title="t('settings.theme.label')"
    />
  </ui-panel>
</template>
<script setup lang="ts">
import { Options } from "@/common/components/common/form/radio/Options"
import UiRadios from "@/common/components/common/form/radio/UiRadios.vue"
import { Themes } from "@/common/components/common/theme/Themes"
import { useApplicationTheme } from "@/common/components/common/theme/UseApplicationTheme"
import { LocalStorageItem } from "@/common/LocalStorageItem"
import UiPanel from "@/ds/panel/UiPanel.vue"
import { useStorage } from "@vueuse/core"
import { watch } from "vue"
import { useI18n } from "vue-i18n"

const { t, locale, availableLocales } = useI18n()

const currentTheme = useApplicationTheme()

const lang = useStorage(LocalStorageItem.LANGUAGE, availableLocales[0])

watch(lang, (newValue) => {
  locale.value = newValue
})

const availableLocalesOptions = Options.for<string>(availableLocales, (lang) =>
  t("settings.language." + lang),
)

const availableThemesOptions = Options.for<string>(Object.values(Themes), (theme) =>
  t("settings.theme." + theme),
)
</script>
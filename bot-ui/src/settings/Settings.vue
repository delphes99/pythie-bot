<template>
  {{ $t("settings.title") }}
  <ui-radios
    v-model="lang"
    name="locale"
    :options="availableLocalesOptions"
    :title="t('settings.language.label')"
  />
  <ui-radios
    v-model="currentTheme"
    name="theme"
    :options="availableThemesOptions"
    :title="t('settings.theme.label')"
  />
</template>
<script setup lang="ts">
import { Options } from "@/common/components/common/form/radio/Options"
import UiRadios from "@/common/components/common/form/radio/UiRadios.vue"
import { LocalStorageItem } from "@/common/LocalStorageItem"
import { ref, watch } from "vue"
import { useI18n } from "vue-i18n"

const { t, locale, availableLocales } = useI18n()
const lang = ref(locale.value)
const currentTheme = ref("dark")

watch(lang, (newValue) => {
  locale.value = newValue
  localStorage.setItem(LocalStorageItem.LANGUAGE, newValue)
})

const availableLocalesOptions = Options.for<string>(availableLocales, (lang) =>
  t("settings.language." + lang),
)

const availableThemesOptions = Options.for<string>(["dark", "light"], (theme) =>
  t("settings.theme." + theme),
)
</script>
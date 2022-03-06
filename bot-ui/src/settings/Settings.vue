<template>
  {{ $t("settings.title") }}
  <label>{{ $t("settings.language.label") }}</label>
  <el-radio-group v-model="lang">
    <el-radio
      v-for="availableLocale in $i18n.availableLocales"
      :key="`locale-${availableLocale}`"
      :label="availableLocale"
    >
      {{ $t("settings.language." + availableLocale) }}
    </el-radio>
  </el-radio-group>
  <label>{{ $t("settings.theme.label") }}</label>

  <el-radio-group v-model="currentTheme">
    <el-radio
      v-for="availableTheme in themes"
      :key="`theme-${availableTheme}`"
      :label="availableTheme"
    >
      {{ $t("settings.theme." + availableTheme) }}
    </el-radio>
  </el-radio-group>
</template>
<script lang="ts">
import { LocalStorageItem } from "@/common/LocalStorageItem"
import { defineComponent, ref, watch } from "vue"
import { useI18n } from "vue-i18n"

export default defineComponent({
  name: "AppSettings",
  setup() {
    const { locale } = useI18n()
    const lang = ref(locale.value)
    const themes = ref(["dark", "light"])
    const currentTheme = ref("dark")

    watch(lang, (newValue) => {
      locale.value = newValue
      localStorage.setItem(LocalStorageItem.LANGUAGE, newValue)
    })

    return {
      lang,
      themes,
      currentTheme,
    }
  },
})
</script>

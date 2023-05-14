<template>
    <ui-panel title="settings.title">
        <ui-radios
                v-model="lang"
                :options="availableLocalesOptions"
                :title="t('settings.language.label')"
                name="locale"
        />
        <ui-radios
                v-model="currentTheme.theme"
                :options="availableThemesOptions"
                :title="t('settings.theme.label')"
                name="theme"
        />
    </ui-panel>
</template>
<script lang="ts" setup>
import {Options} from "@/common/components/common/form/radio/Options"
import UiRadios from "@/common/components/common/form/radio/UiRadios.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import {useApplicationTheme} from "@/common/components/common/theme/UseApplicationThemeStore"
import {LocalStorageItem} from "@/common/LocalStorageItem"
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

const {currentTheme, availableThemesOptions} = useApplicationTheme()
</script>
<template>
  <ui-radios
      v-model="currentTheme.theme"
      :options="availableThemesOptions"
      :title="displayLabel ? 'settings.theme.label' : undefined"
      name="theme"
      comparaisonField="name"
  />
</template>

<script setup lang="ts">
import UiRadios from "@/common/designSystem/form/radio/ui-radios.vue";
import {useApplicationTheme} from "@/common/utils/theme/UseApplicationThemeStore";

defineProps({
  displayLabel: {
    type: Boolean,
    default: false,
  },
})

const {currentTheme, availableThemesOptions} = useApplicationTheme()
</script>
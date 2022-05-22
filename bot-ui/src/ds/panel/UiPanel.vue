<template>
  <div class="border-primaryColor border-2 mb-2 p-0">
    <div class="menu-colors p-2">
      <h1 class="inline text-2xl font-medium title-font">
        {{ $t(title) }}
      </h1>
    </div>
    <div v-if="menu && menu.length">
      <div class="flex flex-row bg-secondaryColor dark:bg-secondaryColorDark">
        <div
          v-for="menuItem in menu"
          :key="menuItem.label"
        >
          <ui-button :label="menuItem.label" />
        </div>
      </div>
    </div>
    <div class="p-4">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import UiButton from "@/ds/button/UiButton.vue"
import { UiPanelMenuItem } from "@/ds/panel/UiPanelMenuItem"
import { PropType } from "vue"

defineProps({
  title: {
    type: String,
    required: true,
  },
  menu: {
    type: Object as PropType<UiPanelMenuItem[]>,
    default: null,
  },
})
</script>
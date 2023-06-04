<template>
  <div class="mb-2 p-0 bg-primaryColor shadow">
    <div class="text-primaryTextColor p-4">
      <h1 class="inline text-2xl font-medium title-font">
        {{ $t(title) }}
      </h1>
    </div>
    <div v-if="menu && menu.length">
      <div class="flex flex-row bg-secondaryColor">
        <div
            v-for="menuItem in menu"
            :key="menuItem.label"
        >
          <ui-button
              :label="menuItem.label"
              @on-click="menuItem.onClick()"
          />
        </div>
      </div>
    </div>
    <div
        v-if="$slots.default"
        class="m-4"
    >
      <slot/>
    </div>
    <slot name="withoutContainer"/>
  </div>
</template>

<script lang="ts" setup>
import UiButton from "@/common/designSystem/button/ui-button.vue"
import {UiPanelMenuItem} from "@/common/designSystem/panel/ui-panel.menu.item"
import {PropType} from "vue"

defineProps({
  title: {
    type: String,
    required: true,
  },
  class: {
    type: String,
    default: null,
  },
  menu: {
    type: Object as PropType<UiPanelMenuItem[]>,
    default: null,
  },
})
</script>
<template>
  <UiPanel :title="title">
    <slot name="header"></slot>
  </UiPanel>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
    <slot/>
  </div>
</template>

<script lang="ts" setup>
import UiPanel from "@/common/designSystem/panel/UiPanel.vue";

defineProps({
  title: {
    type: String,
    required: true,
  },
})
</script>
<template>
  <ui-panel title="Features" />
</template>

<script setup lang="ts">
import UiPanel from "@/ds/panel/UiPanel.vue"

defineProps({
  channelName: {
    type: String,
    required: true,
  },
})
</script>
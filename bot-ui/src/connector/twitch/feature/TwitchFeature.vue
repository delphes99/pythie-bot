<template>
  <UiPanel title="Features"/>
</template>

<script lang="ts" setup>
import UiPanel from "@/common/designSystem/panel/UiPanel.vue"

defineProps({
  channelName: {
    type: String,
    required: true,
  },
})
</script>
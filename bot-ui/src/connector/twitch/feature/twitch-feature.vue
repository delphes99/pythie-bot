<template>
  <ui-panel title="Features"/>
</template>

<script lang="ts" setup>
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"

defineProps({
  channelName: {
    type: String,
    required: true,
  },
})
</script>
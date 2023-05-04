<template>
    <ui-panel title="Features"/>
</template>

<script lang="ts" setup>
import UiPanel from "@/ds/panel/UiPanel.vue"

defineProps({
    channelName: {
        type: String,
        required: true,
    },
})
</script>
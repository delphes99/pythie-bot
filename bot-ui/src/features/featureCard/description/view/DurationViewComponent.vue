<template>{{ props.item.field }} : {{ props.item.value }} s</template>
<script setup lang="ts">
import { DurationFormItem } from "@/features/featureCard/description/DurationFormItem"
import { PropType } from "vue"

const props = defineProps({
  item: {
    type: Object as PropType<DurationFormItem>,
    required: true,
  },
})
</script>

<style scoped></style>
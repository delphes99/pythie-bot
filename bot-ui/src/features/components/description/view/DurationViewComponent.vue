<template>
  {{ props.item.field }} : {{ props.item.number }} s
</template>
<script setup lang="ts">
import { DurationFormItem } from "@/features/components/description/DurationFormItem.js"
import { PropType } from "vue/dist/vue.js"

const props = defineProps({
  item: {
    type: Object as PropType<DurationFormItem>,
    required: true,
  },
})
</script>

<style scoped></style>
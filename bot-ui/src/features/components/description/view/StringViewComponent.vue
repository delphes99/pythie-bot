<template>
  edit {{ props.item.field }} : {{ props.item.value }}
</template>
<script setup lang="ts">
import {StringFormItem} from "@/features/components/description/StringFormItem"
import {PropType} from "vue"

const props = defineProps({
  item: {
    type: Object as PropType<StringFormItem>,
    required: true,
  },
})
</script>
<template>
  <label :for="model.id">{{ model.field }} (s)</label>
  <input
    :id="model.id"
    v-model="model.value"
    type="text"
  >
</template>
<script setup lang="ts">
import { DurationFormItem } from "@/features/components/description/DurationFormItem"
import {computed} from "vue"
import { PropType } from "vue/dist/vue.js"

const props = defineProps({
  modelValue: {
    type: Object as PropType<DurationFormItem>,
    required: true,
  },
})
const emit = defineEmits(['update:modelValue'])

const model = computed({
  get () {
    return props.modelValue
  },

  set (value) {
    return emit('update:modelValue', value)
  },
})
</script>

<style scoped></style>
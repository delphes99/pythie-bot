<template>
  <label :for="model.id">{{ model.field }}</label>
  <input
    :id="model.id"
    v-model="model.value"
    type="text"
  >
</template>
<script setup lang="ts">
import {StringFormItem} from "@/features/components/description/StringFormItem"
import {computed, PropType, ref, watch} from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<StringFormItem>,
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
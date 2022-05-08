<template>
  <label
    v-if="label"
    :for="id"
  >{{ $t(label) }}</label>
  <select
    :id="id"
    v-model="selected"
  >
    <option
      v-for="option in options"
      :key="option"
      :value="option.option"
    >
      {{ option.display() }}
    </option>
  </select>
</template>
<script setup lang="ts">
import { UiSelectOption } from "@/ds/form/select/UiSelectOption"
import { computed, PropType } from "vue"
import { v4 as uuid } from "uuid"

const id = uuid()

const props = defineProps({
  modelValue: {
    type: Object as PropType<unknown>,
    required: true,
  },
  options: {
    type: Object as PropType<UiSelectOption<unknown>[]>,
    required: true,
  },
  label: {
    type: String,
    default: null,
  },
})

const emit = defineEmits(['update:modelValue'])

const selected = computed({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})
</script>
<template>
  <label
    v-if="label"
    :for="id"
  >{{ $t(label) }}</label>
  <input
    :id="id"
    v-model="model"
    type="text"
  >
</template>

<script setup lang="ts">
import { computed } from "vue"
import { v4 as uuid } from "uuid"

const id = uuid()

const props = defineProps({
  modelValue: {
    type: String,
    require: true,
  },
  label: {
    type: String,
    default: null,
  },
})

const emit = defineEmits(["update:modelValue"])

const model = computed({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})
</script>
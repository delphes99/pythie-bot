<template>
  <div class="radio-button-group">
    <label
      v-if="title"
    >{{ title }}</label>
    <span
      v-for="option in options.values"
      :key="option.id"
    >
      <input
        :id="option.id"
        type="radio"
        :name="name"
        :value="option.value"
        :checked="modelValue === option.value"
        class="inline"
        @change="$emit('update:modelValue', option.value)"
      >
      <label
        :for="option.id"
        class="inline pl-2"
      >
        {{ option.label() }}
      </label>
    </span>
  </div>
</template>

<script setup lang="ts">
import { Options } from "@/common/components/common/form/radio/Options"
import { PropType } from "vue"

defineProps({
  name: {
    type: String,
    required: true,
  },
  options: {
    type: Object as PropType<Options<unknown>>,
    required: true,
  },
  modelValue: {
    type: Object as PropType<unknown>,
    required: true,
  },
  title: {
    type: String,
    default: null,
    nullable: true,
  },
})

defineEmits(['update:modelValue'])
</script>
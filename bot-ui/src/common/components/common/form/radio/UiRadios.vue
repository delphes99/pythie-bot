<template>
  <div>
    <label
      v-if="title"
    >{{ title }}</label>
    <div class="flex items-center h-">
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
          class="inline h-4 w-4"
          @change="$emit('update:modelValue', option.value)"
        >
        <label
          :for="option.id"
          class="inline p-3 text-base font-medium"
        >
          {{ option.label() }}
        </label>
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Options } from "@/common/components/common/form/radio/Options"
import { PropType } from "vue"

defineProps({
  modelValue: {
    type: [Object, String] as PropType<unknown>,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  options: {
    type: Object as PropType<Options<unknown>>,
    required: true,
  },
  title: {
    type: String,
    default: null,
    nullable: true,
  },
})

defineEmits(["update:modelValue"])
</script>
<template>
  <div>
    <label
        v-if="title"
        class="block text-titleColor text-sm font-semibold mb-2"
    >{{ title }}</label>
    <div class="flex items-center h-">
      <span
          v-for="option in options.values"
          :key="option.id"
      >
        <input
            :id="option.id"
            :checked="modelValue === option.value"
            :name="name"
            :value="option.value"
            class="inline h-4 w-4"
            type="radio"
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

<script lang="ts" setup>
import {Options} from "@/common/designSystem/form/radio/Options"
import {PropType} from "vue"

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
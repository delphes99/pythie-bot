<template>
  <button
    type="button"
    class="px-5 py-2.5 font-medium rounded-lg text-sm focus:shadow-outline"
    :class="classes"
    @click="onClick"
  >
    {{ $t(label) }}
  </button>
</template>

<script setup lang="ts">
import { UiButtonType } from "@/ds/button/UiButtonType"
import { computed } from "vue"

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  type: {
    type: String as () => UiButtonType,
    default: UiButtonType.Primary,
  },
})

const emit = defineEmits(["on-click"])

const classes = computed(() => {
  return {
    "primary-button": props.type == UiButtonType.Primary,
    "secondary-button": props.type == UiButtonType.Secondary,
    "warning-button": props.type == UiButtonType.Warning,
  }
})

const onClick = () => {
  emit("on-click")
}
</script>
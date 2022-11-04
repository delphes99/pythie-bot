<template>
  <div>
    text component
  </div>
</template>

<script setup lang="ts">
import TextComponent from "@/overlay/editor/textComponent/textComponent"
import OverlayElement from "@/overlay/OverlayElement"
import { PropType } from "vue"

defineProps({
  modelValue: {
    type: Object as PropType<OverlayElement<TextComponent>>,
    default: null,
  },
})
</script>
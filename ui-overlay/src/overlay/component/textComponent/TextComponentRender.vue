<template>
  <link
    :href="`https://fonts.googleapis.com/css2?family=${component.properties.font.replaceAll(' ', '+')}&display=swap`"
    rel="stylesheet"
  >
  <div
    :style="style"
  >
    {{ component.properties.text }}
  </div>
</template>

<script setup lang="ts">
import TextComponent from "@/overlay/component/textComponent/textComponent"
import OverlayElement from "@/overlay/OverlayElement"
import { computed, PropType } from "vue"

 const props = defineProps({
  component: {
    type: Object as PropType<OverlayElement<TextComponent>>,
    default: null,
  },
})

const style = computed(() => {
  return {
    color: props.component.properties.color,
    "font-size": `${props.component.properties.fontSize}px`,
    "font-family": props.component.properties.font,
  }
})
</script>
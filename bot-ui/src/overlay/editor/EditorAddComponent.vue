<template>
  <div>
    <ui-button
      label="overlay.editor.addText"
      @on-click="$emit('addText')"
    />
  </div>
</template>

<script setup lang="ts">
import UiButton from "@/ds/button/UiButton.vue"

defineEmits(["addText"])
</script>

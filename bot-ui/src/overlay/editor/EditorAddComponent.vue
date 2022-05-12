<template>
  <div>
    <ui-button
      label="overlay.editor.addText"
      @on-click="$emit('addText')"
    />
  </div>
</template>

<script lang="ts">
import UiButton from "@/ds/button/UiButton.vue"
export default {
  name: "EditorAddComponent",
  components: { UiButton },
  emits: ["addText"],
}
</script>

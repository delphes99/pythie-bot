<template>
  <div>
    <button
      class="primary-button"
      @click="$emit('addText')"
    >
      Add text
    </button>
  </div>
</template>

<script lang="ts">
export default {
  name: "EditorAddComponent",
  emits: ["addText"],
}
</script>

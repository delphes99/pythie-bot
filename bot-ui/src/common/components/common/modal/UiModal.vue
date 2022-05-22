<template>
  <div
    v-if="isOpen"
    class="fixed top-0 left-0 w-screen h-screen bg-black bg-opacity-50 flex items-center justify-center"
    @click="close()"
  >
    <div
      id="modal"
      class="w-3/4 min-h-1/3 opacity-100 default-background rounded shadow-lg"
      @click.stop
    >
      <ui-panel :title="title">
        <slot />
      </ui-panel>
    </div>
  </div>
</template>

<script setup lang="ts">
import UiPanel from "@/ds/panel/UiPanel.vue"

defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(["update:isOpen"])

const close = () => {
  emit("update:isOpen", false)
}
</script>
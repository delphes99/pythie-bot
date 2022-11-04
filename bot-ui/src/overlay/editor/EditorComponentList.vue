<template>
  <ui-accorion-panel
    v-if="components.length !== 0"
    title="overlay.editor.items-list.title"
  >
    <ul>
      <li
        v-for="component in components"
        :key="component.id"
        class="m-2 bg-primaryColor text-primaryTextColor p-1 shadow-md"
        :class="modelValue && component.id === modelValue.id ? 'selected' : ''"
        @click="selectComponent(component)"
      >
        {{ component.representation }}
      </li>
    </ul>
  </ui-accorion-panel>
</template>
<script setup lang="ts">
import UiAccorionPanel from "@/ds/accordionPanel/UiAccorionPanel.vue"
import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import { PropType } from "vue"

const emits = defineEmits(["update:modelValue"])

defineProps({
  components: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>[]>,
    required: true,
  },
  modelValue: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    default: null,
  },
})

const selectComponent = (selected: OverlayElement<OverlayElementProperties>) => {
  emits("update:modelValue", selected)
}

</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
.selected {
  @apply bg-gray-400;
}
</style>

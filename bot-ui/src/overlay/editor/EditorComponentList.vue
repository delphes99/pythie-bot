<template>
  <div
    v-if="components.length !== 0"
    class="flex flex-col border border-black"
  >
    <h2>Items</h2>
    <ul>
      <li
        v-for="component in components"
        :key="component.id"
        :class="selection && component.id === selection.id ? 'selected' : ''"
        @click="selectComponent(component)"
      >
        {{ component.representation }}
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
import { OverlayElement } from "@/overlay/OverlayElement"
import { PropType } from "vue"

const emits = defineEmits(["update:selection"])

defineProps({
  components: {
    type: Object as PropType<OverlayElement[]>,
    required: true,
  },
  selection: {
    type: Object as PropType<OverlayElement>,
    default: null,
  },
})

const selectComponent = (selected: OverlayElement) => {
  emits("update:selection", selected)
}

</script>

<style scoped>
li {
  @apply m-2 bg-gray-200 p-1 shadow-md;
}

/*noinspection CssUnusedSymbol*/
.selected {
  @apply bg-gray-400;
}
</style>

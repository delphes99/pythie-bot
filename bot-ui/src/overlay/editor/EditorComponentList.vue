<template>
  <div
    v-if="components.length !== 0"
    class="flex flex-col bg-gray-200"
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
<script lang="ts">
import { OverlayElement } from "@/overlay/OverlayElement"
import { defineComponent, PropType } from "vue"

export default defineComponent({
  name: "EditorComponentList",
  props: {
    components: {
      type: Object as PropType<OverlayElement[]>,
      required: true,
    },
    selection: {
      type: Object as PropType<OverlayElement>,
      required: true,
    },
  },
  emits: ["update:selection"],
  setup(props, { emit }) {
    const selectComponent = (selected: OverlayElement) => {
      emit("update:selection", selected)
    }

    return {
      selectComponent,
    }
  },
})
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

<template>
  <div class="flex flex-col bg-gray-200" v-if="components.length !== 0">
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
import { OverlayElement } from "@/overlay/OverlayElement.ts";
import { defineComponent, PropType } from "vue";

export default defineComponent({
  name: "editorComponentList",
  props: {
    components: {
      type: Object as PropType<OverlayElement[]>,
      required: true
    },
    selection: {
      type: Object as PropType<OverlayElement>
    }
  },
  emits: ["update:selection"],
  setup(props, { emit }) {
    const selectComponent = (selected: OverlayElement) => {
      emit("update:selection", selected);
    };

    return {
      selectComponent
    };
  }
});
</script>

<style scoped>
li {
  @apply m-2 bg-gray-200 p-1 shadow-md;
}

.selected {
  @apply bg-gray-400;
}
</style>

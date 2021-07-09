<template>
  <fieldset
    class="flex flex-col border border-black"
    v-if="components.length !== 0"
  >
    <legend>Items</legend>
    <ul>
      <li
        v-for="component in components"
        :key="component.id"
        @click="selectComponent(component)"
      >
        {{ component.id }}
      </li>
    </ul>
  </fieldset>
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

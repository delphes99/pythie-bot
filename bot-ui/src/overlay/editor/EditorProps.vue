<template>
  <div>
    <div class="flex flex-col border border-black p-2" v-if="selection">
      <h2>Selected item</h2>
      id : {{ selection.id }}
      <div>
        <label for="text-attribute">Text : </label>
        <input
          class="input"
          type="text"
          id="text-attribute"
          v-model="selectedText"
          v-on:change="updateComponent"
        />
      </div>
      <div>
        <label for="text-attribute">X : </label>
        <input
          class="input"
          type="text"
          id="x-attribute"
          v-model="selectedLeft"
          v-on:change="updateComponent"
        />
      </div>
      <div>
        <label for="text-attribute">Y : </label>
        <input
          class="input"
          type="text"
          id="y-attribute"
          v-model="selectedTop"
          v-on:change="updateComponent"
        />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import TextComponent, { fromObject } from "@/overlay/editor/textComponent";
import { OverlayElement } from "@/overlay/OverlayElement.js";
import { defineComponent, PropType, ref, watch } from "vue";

export default defineComponent({
  name: "editorProps",
  props: {
    selection: {
      type: Object as PropType<OverlayElement>
    }
  },
  emits: ["update:selection"],
  setup(props, { emit }) {
    const components = ref<TextComponent[]>([]);
    const selectedLeft = ref();
    const selectedTop = ref();
    const selectedText = ref();

    watch(
      () => props.selection,
      (newValue, _) => {
        if (newValue instanceof TextComponent) {
          selectedText.value = newValue.text;
          selectedTop.value = newValue.top;
          selectedLeft.value = newValue.left;
        }
      }
    );

    const updateComponent = () => {
      if (props.selection instanceof TextComponent) {
        const updatedComponent = {
          ...props.selection,
          left: parseInt(selectedLeft?.value),
          top: parseInt(selectedTop?.value),
          text: selectedText.value
        };

        emit("update:selection", fromObject(updatedComponent));
      }
    };
    return {
      selectedLeft,
      selectedTop,
      selectedText,
      components,
      updateComponent
    };
  }
});
</script>

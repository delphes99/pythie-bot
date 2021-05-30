<template>
  <div>
    <button class="primary-button" v-on:click="addText">Add text</button>
  </div>
  <fieldset class="flex flex-col border border-black" v-if="selected">
    <legend>Selected item</legend>
    <div>
      <label for="text-attribute">Text : </label>
      <input
        class="input"
        type="text"
        id="text-attribute"
        v-model="selectedText"
        v-on:change="updateCompoment"
      />
    </div>
    <div>
      <label for="text-attribute">X : </label>
      <input
        class="input"
        type="text"
        id="x-attribute"
        v-model="selectedLeft"
        v-on:change="updateCompoment"
      />
    </div>
    <div>
      <label for="text-attribute">Y : </label>
      <input
        class="input"
        type="text"
        id="y-attribute"
        v-model="selectedTop"
        v-on:change="updateCompoment"
      />
    </div>
  </fieldset>
  <fieldset
    class="flex flex-col border border-black"
    v-if="components.length !== 0"
  >
    <legend>Items</legend>
    <ul>
      <li
        v-for="component in components"
        :key="component.id"
        v-on:click="selectText(component)"
      >
        {{ component.id }}
      </li>
    </ul>
  </fieldset>
</template>
<script lang="ts">
import TextComponent from "@/overlay/editor/textComponent";
import { Emitter } from "mitt";
import { defineComponent, PropType, ref } from "vue";

export default defineComponent({
  name: "editorProps",
  props: {
    bus: {
      type: Object as PropType<Emitter>,
      required: true
    }
  },
  setup(props) {
    const components = ref<TextComponent[]>([]);
    const selected = ref<TextComponent | null>(null);
    const selectedLeft = ref();
    const selectedTop = ref();
    const selectedText = ref();

    const updateSelectedText = (event: TextComponent | null) => {
      selected.value = event;
      if (event) {
        selectedLeft.value = event.left;
        selectedTop.value = event.top;
        selectedText.value = event.text;
      }
    };

    const selectText = (event: TextComponent) => {
      updateSelectedText(event);
      props.bus.emit("selectedText", event);
    };

    props.bus.on("selectedText", event => {
      updateSelectedText(event);
    });

    props.bus.on("selectionCleared", _ => {
      updateSelectedText(null);
    });

    const addText = () => {
      const component = new TextComponent(100, 100, "my text");
      components.value.push(component);
      updateSelectedText(component);
      props.bus.emit("addText", component);
    };

    const updateCompoment = () => {
      if (selected.value) {
        selected.value.left = parseInt(selectedLeft?.value);
        selected.value.top = parseInt(selectedTop?.value);
        selected.value.text = selectedText.value;
      }
      props.bus.emit("modifyText", selected.value);
    };
    return {
      addText,
      selected,
      selectedLeft,
      selectedTop,
      selectedText,
      components,
      updateCompoment,
      selectText
    };
  }
});
</script>

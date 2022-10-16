<template>
  <div v-if="selection">
    <div class="flex flex-col border border-black p-2">
      <h2>{{ $t("overlay.editor.selected-item.title") }}</h2>
      <div>{{ $t("overlay.editor.selected-item.id") }} : {{ selection.id }}</div>
      <div>{{ $t("overlay.editor.selected-item.type") }} : {{ selection.type }}</div>
    </div>
    <div class="flex flex-col border border-black p-2">
      <h2>{{ $t("overlay.editor.properties") }}</h2>
      <div>
        <ui-textfield
          v-model="selectedText"
          label="overlay.editor.text-component.text"
          @change="updateComponent"
        />
      </div>
      <div>
        <ui-textfield
          v-model="selectedLeft"
          label="overlay.editor.X"
          @change="updateComponent"
        />
      </div>
      <div>
        <ui-textfield
          v-model="selectedTop"
          label="overlay.editor.Y"
          @change="updateComponent"
        />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import TextComponent, { fromObject } from "@/overlay/editor/textComponent"
import { OverlayElement } from "@/overlay/OverlayElement"
import { defineComponent, PropType, ref, watch } from "vue"

export default defineComponent({
  name: "EditorProps",
  components: { UiTextfield },
  props: {
    selection: {
      type: Object as PropType<OverlayElement>,
      required: true,
    },
  },
  emits: ["update:selection"],
  setup(props, { emit }) {
    const components = ref<TextComponent[]>([])
    const selectedLeft = ref()
    const selectedTop = ref()
    const selectedText = ref()

    watch(
      () => props.selection,
      (newValue) => {
        if (newValue instanceof TextComponent) {
          selectedText.value = newValue.text
          selectedTop.value = newValue.top
          selectedLeft.value = newValue.left
        }
      },
    )

    const updateComponent = () => {
      if (props.selection instanceof TextComponent) {
        const updatedComponent = {
          ...props.selection,
          left: parseInt(selectedLeft?.value),
          top: parseInt(selectedTop?.value),
          text: selectedText.value,
        }

        emit("update:selection", fromObject(updatedComponent))
      }
    }
    return {
      selectedLeft,
      selectedTop,
      selectedText,
      components,
      updateComponent,
    }
  },
})
</script>
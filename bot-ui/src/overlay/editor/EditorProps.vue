<template>
  <ui-accorion-panel
    v-if="selection"
    title="overlay.editor.selected-item.title"
  >
    <div class="flex flex-col">
      <div>{{ $t("overlay.editor.selected-item.id") }} : {{ selection.id }}</div>
      <div>{{ $t("overlay.editor.selected-item.type") }} : {{ selection.type }}</div>
    </div>
  </ui-accorion-panel>
  <ui-accorion-panel
    v-if="selection"
    title="overlay.editor.properties"
  >
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
    <div>
      <ui-textfield
        v-model="selectedText"
        label="overlay.editor.text-component.text"
        @change="updateComponent"
      />
    </div>
    <div>
      <ui-textfield
        v-model="selectedFont"
        label="overlay.editor.text-component.font"
        @change="updateComponent"
      />
    </div>
    <div>
      <ui-textfield
        v-model="selectedFontSize"
        label="overlay.editor.text-component.font-size"
        @change="updateComponent"
      />
    </div>
    <div>
      <ui-color-picker
        v-model="selectedColor"
        label="overlay.editor.text-component.color"
        @update:model-value="updateComponent"
      />
    </div>
  </ui-accorion-panel>
</template>
<script setup lang="ts">
import UiAccorionPanel from "@/ds/accordionPanel/UiAccorionPanel.vue"
import UiColorPicker from "@/ds/form/colorpicker/UiColorPicker.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import TextComponent, { fromObject } from "@/overlay/editor/textComponent"
import { OverlayElement } from "@/overlay/OverlayElement"
import { PropType, ref, watch } from "vue"

const emit = defineEmits(["update:selection"])
const props = defineProps({
  selection: {
    type: Object as PropType<OverlayElement>,
    default: null,
  },
})

const selectedLeft = ref()
const selectedTop = ref()
const selectedText = ref()
const selectedColor = ref("#000000")
const selectedFont = ref()
const selectedFontSize = ref()

watch(
  () => props.selection,
  (newValue) => {
    if (newValue instanceof TextComponent) {
      selectedText.value = newValue.text
      selectedTop.value = newValue.top
      selectedLeft.value = newValue.left
      selectedColor.value = newValue.color
      selectedFont.value = newValue.font
      selectedFontSize.value = newValue.fontSize
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
      color: selectedColor.value,
      font: selectedFont.value,
      fontSize: selectedFontSize.value,
    }

    emit("update:selection", fromObject(updatedComponent))
  }
}
</script>
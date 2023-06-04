<template>
  <div>
    <ui-textfield
        v-model="selectedElement.properties.text"
        label="overlay.editor.text-component.text"
    />
  </div>
  <div>
    <ui-textfield
        v-model="selectedElement.properties.font"
        label="overlay.editor.text-component.font"
    />
  </div>
  <div>
    <ui-textfield
        v-model="selectedElement.properties.fontSize"
        label="overlay.editor.text-component.font-size"
    />
  </div>
  <div>
    <ui-color-picker
        v-model="selectedElement.properties.color"
        label="overlay.editor.text-component.color"
    />
  </div>
</template>

<script lang="ts" setup>
import UiColorPicker from "@/common/designSystem/form/colorpicker/ui-color-picker.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import {storeToRefs} from "pinia"

const store = useOverlayEditorStore()
const {selectedElement} = storeToRefs(store)
</script>
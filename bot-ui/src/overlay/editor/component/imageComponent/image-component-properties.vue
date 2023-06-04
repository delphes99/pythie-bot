<template>
  <div>
    <ui-textfield
        v-model="selectedElement.properties.url"
        label="overlay.editor.image-component.url"
    />
  </div>
  <ui-textfield
      v-model="selectedElement.properties.width"
      label="overlay.editor.image-component.width"
  />
  <ui-textfield
      v-model="selectedElement.properties.height"
      label="overlay.editor.image-component.height"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import {storeToRefs} from "pinia"

const store = useOverlayEditorStore()
const {selectedElement} = storeToRefs(store)
</script>
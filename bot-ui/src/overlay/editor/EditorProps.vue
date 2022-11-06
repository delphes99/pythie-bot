<template>
  <ui-accorion-panel
    v-if="selectedElement"
    title="overlay.editor.selected-item.title"
  >
    <div class="flex flex-col">
      <div class="truncate">
        {{ $t("overlay.editor.selected-item.id") }} : {{ selectedElement.id }}
      </div>
      <div class="truncate">
        {{ $t("overlay.editor.selected-item.type") }} : {{ selectedElement.properties.type }}
      </div>
    </div>
  </ui-accorion-panel>
  <ui-accorion-panel
    v-if="selectedElement"
    title="overlay.editor.properties"
  >
    <div>
      <ui-textfield
        v-model="selectedElement.general.left"
        label="overlay.editor.X"
      />
    </div>
    <div>
      <ui-textfield
        v-model="selectedElement.general.top"
        label="overlay.editor.Y"
      />
    </div>
    <ui-textfield
      v-model="selectedElement.general.sortOrder"
      label="overlay.editor.sortOrder"
    />
    <component
      :is="selectedElement.properties.propertiesComponent()"
    />
  </ui-accorion-panel>
</template>
<script setup lang="ts">
import UiAccorionPanel from "@/ds/accordionPanel/UiAccorionPanel.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { useOverlayEditorStore } from "@/overlay/editor/useOverlayEditorStore"
import { storeToRefs } from "pinia"

const store = useOverlayEditorStore()
const { selectedElement } = storeToRefs(store)
</script>
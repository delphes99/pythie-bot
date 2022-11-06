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
      <div>
        <ui-button
          label="overlay.editor.unselectElement"
          @on-click="unselect"
        />

        <ui-button
          label="common.delete"
          :type="UiButtonType.Warning"
          @on-click="deleteElement"
        />
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
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { useOverlayEditorStore } from "@/overlay/editor/useOverlayEditorStore"
import { storeToRefs } from "pinia"

const store = useOverlayEditorStore()
const { selectedElement } = storeToRefs(store)

function unselect() {
  store.unselect()
}

function deleteElement() {
  if (selectedElement.value) {
    store.deleteElement(selectedElement.value)
  }
}
</script>
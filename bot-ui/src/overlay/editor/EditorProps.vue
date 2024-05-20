<template>
  <UiAccorionPanel
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
        <UiButton
            label="overlay.editor.unselectElement"
            @on-click="unselect"
        />

        <UiButton
            :type="UiButtonType.Warning"
            label="common.delete"
            @on-click="deleteElement"
        />
      </div>
    </div>
  </UiAccorionPanel>
  <UiAccorionPanel
      v-if="selectedElement"
      title="overlay.editor.properties"
  >
    <div>
      <UiTextfield
          v-model="selectedElement.general.left"
          label="overlay.editor.X"
      />
    </div>
    <div>
      <UiTextfield
          v-model="selectedElement.general.top"
          label="overlay.editor.Y"
      />
    </div>
    <UiTextfield
        v-model="selectedElement.general.sortOrder"
        label="overlay.editor.sortOrder"
    />
    <component
        :is="selectedElement.properties.propertiesComponent()"
    />
  </UiAccorionPanel>
</template>
<script lang="ts" setup>
import UiAccorionPanel from "@/common/designSystem/accordionPanel/UiAccorionPanel.vue"
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiButtonType from "@/common/designSystem/button/UiButtonType"
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import {storeToRefs} from "pinia"

const store = useOverlayEditorStore()
const {selectedElement} = storeToRefs(store)

function unselect() {
  store.unselect()
}

function deleteElement() {
  if (selectedElement.value) {
    store.deleteElement(selectedElement.value)
  }
}
</script>
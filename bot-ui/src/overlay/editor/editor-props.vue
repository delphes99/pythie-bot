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
            :type="UiButtonType.Warning"
            label="common.delete"
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
<script lang="ts" setup>
import UiAccorionPanel from "@/common/designSystem/accordionPanel/ui-accorion-panel.vue"
import UiButtonType from "@/common/designSystem/button/ui-button.type"
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
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
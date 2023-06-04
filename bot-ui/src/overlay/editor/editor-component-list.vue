<template>
  <ui-accorion-panel
      v-if="components.length !== 0"
      title="overlay.editor.items-list.title"
  >
    <ul>
      <li
          v-for="component in components"
          :key="component.id"
          :class="component.id === selectedElementId ? 'selected' : ''"
          class="truncate m-2 bg-primaryColor text-primaryTextColor p-1 shadow-md"
          @click="selectComponent(component)"
      >
        [{{ component.general.sortOrder }}] {{ component.properties.representation }}
      </li>
    </ul>
  </ui-accorion-panel>
</template>
<script lang="ts" setup>
import UiAccorionPanel from "@/common/designSystem/accordionPanel/ui-accorion-panel.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import OverlayElement from "@/overlay/OverlayElement"
import {OverlayElementProperties} from "@/overlay/OverlayElementProperties"
import {storeToRefs} from "pinia"

const store = useOverlayEditorStore()
const {components, selectedElementId} = storeToRefs(store)

const selectComponent = (selected: OverlayElement<OverlayElementProperties>) => {
  store.selection(selected)
}

</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
.selected {
  @apply bg-gray-400;
}
</style>

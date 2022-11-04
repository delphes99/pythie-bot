<template>
  <div
    id="preview"
    :style="`width: ${overlay.resolution.width}px; height: ${overlay.resolution.height}px; position: relative;`"
  >
    <DraggableElement
      v-for="(element, index) in componentsVModel"
      :key="element.id"
      v-model="componentsVModel[index]"
      v-model:selection="selectionVModel"
    />
  </div>
</template>
<script setup lang="ts">
import DraggableElement from "@/overlay/editor/DraggableElement.vue"
import Overlay from "@/overlay/Overlay"
import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import { useVModel } from "@vueuse/core"
import { PropType, ref, watch } from "vue"

const emits = defineEmits(["update:selection"])

const props = defineProps({
  overlay: {
    type: Object as PropType<Overlay>,
    required: true,
  },
  components: {
    type: Array as PropType<OverlayElement<OverlayElementProperties>[]>,
    required: true,
  },
  selection: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    default: null,
  },
})

const fonts = ref<string[]>([])
fonts.value = [...new Set(props.components.map(e => e.font))]

const selectionVModel = useVModel(props, "selection", emits)
const componentsVModel = useVModel(props, "components", emits)
watch(
  () => selectionVModel.value,
  (newValue) => {
    console.log("selection changed", newValue)
  },
)

</script>

<style>
/*noinspection CssUnusedSymbol*/
.draggable.selected {
  @apply border-black;
}

/*noinspection CssUnusedSymbol*/
.draggable {
  @apply border-dashed border border-gray-300 inline-block box-border;
  user-select: none;
}

#preview {
  background-image: repeating-linear-gradient(
    to bottom,
    transparent,
    transparent 20px,
    #dddddd 1px,
    #dddddd 21px
  ),
  repeating-linear-gradient(
    to right,
    #eeeeee,
    #eeeeee 20px,
    #dddddd 1px,
    #dddddd 21px
  );
}
</style>

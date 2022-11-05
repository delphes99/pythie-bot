<template>
  <div
    :id="componentId"
    class="draggable border-dashed border-2 box-border"
    :class="classes"
    :style="style"
    @click="updateSelection()"
  >
    <component
      :is="component.properties.renderComponent()"
      :component="component"
    />
  </div>
</template>

<script setup lang="ts">
import { useOverlayEditorStore } from "@/overlay/editor/useOverlayEditorStore"
import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import interact from "interactjs"
import { storeToRefs } from "pinia"
import { computed, onMounted, onUnmounted, onUpdated, PropType, ref, watch } from "vue"

const props = defineProps({
  component: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    required: true,
  },
})

const store = useOverlayEditorStore()
const { selectedElementId } = storeToRefs(store)

const componentId = `draggable-${props.component.id}`

const x = ref(0)
const y = ref(0)

watch(
  () => props.component.general,
  (newValue) => {
    x.value = newValue.left
    y.value = newValue.top
  },
  { immediate: true, deep: true },
)

const classes = computed(() => {
  const isSelected = props.component.id === selectedElementId.value
  return {
    "border-gray-300": !isSelected,
    "border-black": isSelected,
  }
})

const style = computed(() => {
  return {
    transform: `translate(${x.value}px, ${y.value}px)`,
    position: "absolute",
    display: "inline-block",
    "user-select": "none",
  }
})

function updateSelection() {
  store.selection(props.component)
}

function updateComponent() {
  store.updateComponent(props.component.modifyGeneral(old => old.modifyCoordinate(x.value, y.value)))
}

onMounted(() => {
  interact(`#${componentId}`)
    .draggable({
      modifiers: [
        interact.modifiers.restrict({
          restriction: "parent",
          endOnly: true,
        }),
      ],
      listeners: {
        start() {
          console.log("start")
          updateSelection()
        },
        move(event) {
          console.log("move")
          x.value += event.dx
          y.value += event.dy
        },
        end() {
          console.log("end")
          updateComponent()
          updateSelection()
        },
      },
    })
})

onUnmounted(() => {
  console.log("unmounted")
})

onUpdated(() => {
  console.log("updated")
})
</script>

<style>
/*noinspection CssUnusedSymbol*/
.draggable {
  user-select: none;
}
</style>
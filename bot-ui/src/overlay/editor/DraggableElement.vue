<template>
  <div
    :id="`draggable-${modelValue.id}`"
    ref="draggable"
    class="draggable bg-red-400 border-dashed border box-border"
    :class="[selectedClass]"
    :style="style"
    @click="updateSelection()"
  >
    <component :is="modelValue.properties.renderComponent()" />
  </div>
</template>

<script setup lang="ts">
import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import interact from "interactjs"
import { computed, onMounted, onUnmounted, onUpdated, PropType, ref } from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    required: true,
  },
  selection: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    default: null,
  },
})

const emits = defineEmits(["update:modelValue", "update:selection"])

console.log(props.modelValue)
const draggable = ref()
const x = ref(props.modelValue.general.left)
const y = ref(props.modelValue.general.top)

const selectedClass = computed(() => {
  if (props.modelValue.id === props.selection?.id) {
    return "border-gray-300"
  } else {
    return "border-black"
  }
})

const style = computed(() => {
  return {
    transform: `translate(${x.value}px, ${y.value}px)`,
    position: `absolute`,
    display: `inline-block`,
  }
})

function updateSelection() {
  console.log("updateSelection")
  emits("update:selection", props.modelValue)
}

onMounted(() => {
  console.log("mounted", draggable.value.id)
  interact(`#${draggable.value.id}`)
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
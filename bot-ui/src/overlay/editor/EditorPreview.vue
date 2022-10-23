<template>
  <div
    id="preview"
    :style="`width: ${overlay.resolution.width}px; height: ${overlay.resolution.height}px; position: relative;`"
  />
</template>
<script setup lang="ts">
import TextComponent, { fromObject } from "@/overlay/editor/textComponent"
import Overlay from "@/overlay/Overlay"
import { OverlayElement } from "@/overlay/OverlayElement"
import interact from "interactjs"
import { v4 as uuidv4 } from "uuid"
import { onMounted, PropType, watch } from "vue"

const emits = defineEmits(["update:selection"])

const props = defineProps({
  overlay: {
    type: Object as PropType<Overlay>,
    required: true,
  },
  selection: {
    type: Object as PropType<OverlayElement>,
    default: null,
  },
})

const components = new Map<string, HTMLDivElement>()
const positions = new Map<string, ElementPosition>()

const loadCanvas = () => {
  function getElement(element: OverlayElement): TextComponent {
    return props.overlay.elements.find((e) => e.id === element.id) as TextComponent
  }

  function renderElements(elements: OverlayElement[]) {
    elements.forEach((element) => {
      const divElement = components.get(element.id)
      if (divElement && element instanceof TextComponent) {
        divElement.innerText = element.text
        divElement.style.color = element.color
        if (element.id == props.selection?.id) {
          divElement.classList.add("selected")
        } else {
          divElement.classList.remove("selected")
        }

        divElement.style.transform = `translate(${element.left}px, ${element.top}px)`
        const elementPosition = positions.get(element.id)
        if (elementPosition) {
          elementPosition.x = element.left
          elementPosition.y = element.top
        }
      } else {
        if (element instanceof TextComponent) {
          const divElement = document.createElement("div")
          const id = `el-${uuidv4()}`
          divElement.id = id
          divElement.classList.add("draggable")
          divElement.style.position = `absolute`
          divElement.style.display = `inline-block`
          divElement.style.transform = `translate(${element.left}px, ${element.top}px)`
          divElement.style.color = element.color
          divElement.innerHTML = element.text

          const position = { x: element.left, y: element.top }

          const updateSelection = () => {
            const updatedElement = {
              ...getElement(element),
              left: position.x ?? 0,
              top: position.y ?? 0,
            }
            emits("update:selection", fromObject(updatedElement))
          }

          divElement.onclick = () => {
            updateSelection()
          }

          interact(`#${id}`).draggable({
            modifiers: [
              interact.modifiers.restrict({
                restriction: "parent",
                endOnly: true,
              }),
            ],
            listeners: {
              start() {
                updateSelection()
              },
              move(event) {
                position.x += event.dx
                position.y += event.dy

                event.target.style.transform = `translate(${position.x}px, ${position.y}px)`
              },
              end() {
                updateSelection()
              },
            },
          })

          positions.set(element.id, position)
          components.set(element.id, divElement)
          document.getElementById("preview")?.appendChild(divElement)
        }
      }
    })
  }

  renderElements(props.overlay.elements)

  watch(
    () => props.selection,
    (newValue) => {
      if (newValue instanceof TextComponent) {
        const canvasComponent = components.get(newValue.id)
        if (canvasComponent) {
          for (const component of components.values()) {
            component.classList.remove("selected")
          }
          canvasComponent.classList.add("selected")
        } else {
          console.error("Unknown selected object")
        }
      }
    },
  )

  watch(
    () => props.overlay.elements,
    (newValue) => {
      renderElements(newValue)
    },
  )
}

onMounted(loadCanvas)

interface ElementPosition {
  x: number
  y: number
}
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

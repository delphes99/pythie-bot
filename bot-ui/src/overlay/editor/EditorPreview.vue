<template>
  <canvas id="myCanvas"></canvas>
</template>
<script lang="ts">
import TextComponent, { fromObject } from "@/overlay/editor/textComponent";
import Overlay from "@/overlay/Overlay.ts";
import { OverlayElement } from "@/overlay/OverlayElement.ts";
import { fabric } from "fabric";
import { defineComponent, onMounted, PropType, watch } from "vue";

export default defineComponent({
  name: "editorPreview",
  props: {
    overlay: {
      type: Object as PropType<Overlay>,
      required: true
    },
    selection: {
      type: Object as PropType<OverlayElement>
    }
  },
  emits: ["update:selection"],
  setup(props, { emit }) {
    const components = new Map<string, fabric.Text>();

    const loadCanvas = () => {
      const canvas = new fabric.Canvas("myCanvas");
      canvas.backgroundColor = "lightgrey";
      canvas.setWidth(props.overlay.resolution.width.toString());
      canvas.setHeight(props.overlay.resolution.height.toString());

      canvas.on("selection:cleared", _ => {
        emit("update:selection", null);
      });

      watch(
        () => props.selection,
        newValue => {
          if (newValue instanceof TextComponent) {
            const canvasComponent = components.get(newValue.id);
            if (canvasComponent) {
              canvas.setActiveObject(canvasComponent);
              canvas.requestRenderAll();
            } else {
              console.error("Unknown selected object");
            }
          }
        }
      );

      function getElement(element: OverlayElement): TextComponent {
        return props.overlay.elements.find(
          e => e.id === element.id
        ) as TextComponent;
      }

      watch(
        () => props.overlay.elements,
        newValue => {
          newValue.forEach(element => {
            const canvasComponent = components.get(element.id);
            if (canvasComponent && element instanceof TextComponent) {
              canvasComponent.top = element.top;
              canvasComponent.left = element.left;
              canvasComponent.text = element.text;
            } else {
              if (element instanceof TextComponent) {
                const rect = new fabric.Text(element.text, {
                  left: element.left,
                  top: element.top
                });

                rect.on("selected", _ => {
                  emit("update:selection", getElement(element));
                });

                rect.on("moved", e => {
                  if (e.target) {
                    const updatedElement = {
                      ...getElement(element),
                      left: e.target.left ?? 0,
                      top: e.target.top ?? 0
                    };
                    emit("update:selection", fromObject(updatedElement));
                  }
                });

                components.set(element.id, rect);
                canvas.add(rect);
              }
            }
          });

          canvas.requestRenderAll();
        }
      );
    };

    onMounted(loadCanvas);

    return {};
  }
});
</script>

<template>
  <canvas id="myCanvas"></canvas>
</template>
<script lang="ts">
import TextComponent from "@/overlay/editor/textComponent";
import Overlay from "@/overlay/Overlay.ts";
import { fabric } from "fabric";
import { Emitter } from "mitt";
import { defineComponent, onMounted, PropType } from "vue";

export default defineComponent({
  name: "editorPreview",
  props: {
    overlay: {
      type: Object as PropType<Overlay>,
      required: true
    },
    bus: {
      type: Object as PropType<Emitter>,
      required: true
    }
  },
  setup(props) {
    const components = new Map<string, fabric.Text>();

    const loadCanvas = () => {
      const canvas = new fabric.Canvas("myCanvas");
      canvas.backgroundColor = "lightgrey";
      canvas.setWidth(props.overlay.resolution.width.toString());
      canvas.setHeight(props.overlay.resolution.height.toString());

      canvas.on("selection:cleared", _ => {
        props.bus.emit("selectionCleared");
      });

      props.bus.on("modifyText", event => {
        const { id, text, left, top } = event as TextComponent;
        const canvasComponent = components.get(id);
        if (canvasComponent) {
          canvasComponent.set("text", text);
          canvasComponent.set({ left: left, top: top });
          canvas.renderAll();
        }
      });

      props.bus.on("selectedText", e => {
        if (e) {
          const component = components.get(e.id);
          if (component) {
            canvas.setActiveObject(component);
            canvas.requestRenderAll();
          }
        }
      });

      props.bus.on("addText", event => {
        const component = event as TextComponent;
        const rect = new fabric.Text(component.text, {
          left: component.left,
          top: component.top
        });

        rect.on("selected", e => {
          props.bus.emit("selectedText", component);
        });
        rect.on("moved", e => {
          if (e.target) {
            component.left = e.target.left ?? 0;
            component.top = e.target.top ?? 0;
            props.bus.emit("selectedText", component);
          }
        });

        components.set(component.id, rect);
        canvas.add(rect);
        canvas.setActiveObject(rect);
      });
    };

    onMounted(loadCanvas);

    return {};
  }
});
</script>

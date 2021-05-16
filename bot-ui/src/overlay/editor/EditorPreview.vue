<template>
  <div class="w-full h-full">
    <canvas id="myCanvas"></canvas>
  </div>
</template>
<script lang="ts">
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
    const loadCanvas = () => {
      const canvas = new fabric.Canvas("myCanvas");
      canvas.backgroundColor = "lightgrey";
      canvas.setWidth(props.overlay.resolution.width.toString());
      canvas.setHeight(props.overlay.resolution.height.toString());

      props.bus.on("addRectangle", _ => {
        const rect = new fabric.Rect({
          left: 100,
          top: 100,
          fill: "red",
          width: 20,
          height: 20
        });

        rect.on("selected", e => console.log(e));
        rect.on("moved", e => console.log(e));

        canvas.add(rect);
      });
    };

    onMounted(loadCanvas);

    return {};
  }
});
</script>

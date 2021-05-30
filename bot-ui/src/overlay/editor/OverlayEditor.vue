<template>
  <div class="flex flex-row">
    <div class="w-1/6">
      <editor-props :bus="bus"></editor-props>
    </div>
    <div class="w-5/6">
      <editor-preview :overlay="overlay" :bus="bus"></editor-preview>
    </div>
  </div>
</template>

<script lang="ts">
import EditorPreview from "@/overlay/editor/EditorPreview.vue";
import EditorProps from "@/overlay/editor/EditorProps.vue";
import OverlayRepository from "@/overlay/OverlayRepository";
import mitt from "mitt";
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: `OverlayEditor`,
  components: { EditorPreview, EditorProps },
  props: {
    overlayId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const bus = mitt();
    const overlay = ref(OverlayRepository.get(props.overlayId));

    return {
      overlay,
      bus
    };
  }
});
</script>

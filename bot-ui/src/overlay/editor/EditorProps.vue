<template>
  <div class="">
    <button v-on:click="addRectangle">Add</button>
  </div>
</template>
<script lang="ts">
import { Emitter } from "mitt";
import { defineComponent, PropType } from "vue";

export default defineComponent({
  name: "editorProps",
  props: {
    bus: {
      type: Object as PropType<Emitter>,
      required: true
    }
  },
  setup(props) {
    const addRectangle = () => props.bus.emit("addRectangle");

    return {
      addRectangle
    };
  }
});
</script>

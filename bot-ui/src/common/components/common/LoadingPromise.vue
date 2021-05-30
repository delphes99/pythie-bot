<template>
  <div v-if="isLoading">Loading</div>
  <div v-else>
    <slot></slot>
  </div>
</template>

<script lang="ts">
import { LoadPromise } from "@/common/composition/UseLoadingPromise.ts";
import { defineComponent, PropType } from "vue";

export default defineComponent({
  name: "LoadingPromise",
  props: {
    loadingPromise: {
      type: Object as PropType<LoadPromise<any>>,
      required: true
    }
  },
  setup(props) {
    return {
      isLoading: props.loadingPromise.isLoading
    };
  }
});
</script>

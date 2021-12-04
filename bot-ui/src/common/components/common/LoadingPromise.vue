<template>
  <div v-if="isLoading">Loading</div>
  <slot v-else></slot>
</template>

<script lang="ts">
import { LoadPromise } from "@/common/composition/UseLoadingPromise";
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

<template>
  <panel title="Features">
    <div class="flex flex-col p-2">
      <div v-for="feature in features" :key="feature" class="w-full mb-2 border-black border-2">{{ feature }}</div>
    </div>
  </panel>
</template>

<script>
import Panel from "@/components/common/Panel";
import {inject, ref} from "vue";

export default {
  components: {Panel},
  setup() {
    const backendUrl = inject("backendUrl")
    const features = ref([])

    async function getFeatures() {
      const response = await fetch(`${backendUrl}/features`);

      features.value = await response.json()
      console.log(features.value)
    }

    getFeatures()

    return {
      features
    }
  }
}
</script>
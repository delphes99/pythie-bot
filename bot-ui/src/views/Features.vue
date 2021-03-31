<template>
  <panel title="Features">
    <card-panel>
      <feature-card v-for="feature in features" :key="feature" :feature="feature"></feature-card>
    </card-panel>
  </panel>
</template>

<script>
import Panel from "@/components/common/Panel";
import {inject, ref} from "vue";
import FeatureCard from "@/components/feature/FeatureCard";
import CardPanel from "@/components/common/CardPanel";

export default {
  components: {CardPanel, FeatureCard, Panel},
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
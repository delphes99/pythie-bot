<template>
  <panel title="Features">
    <card-panel>
      <feature-card
        v-for="feature in features"
        :key="feature"
        :feature="feature"
      />
    </card-panel>
  </panel>
</template>

<script>
import CardPanel from "@/common/components/common/CardPanel"
import Panel from "@/common/components/common/Panel"
import { fromJson } from "@/features/configurations/Feature"
import FeatureCard from "@/features/featureCard/FeatureCard"
import { inject, ref } from "vue"

export default {
  name: "FeaturesList",
  components: { CardPanel, FeatureCard, Panel },
  setup() {
    const backendUrl = inject("backendUrl")
    const features = ref([])

    async function getFeatures() {
      const response = await fetch(`${backendUrl}/features/new`)

      features.value = await response.json().then((data) => data.map(fromJson))
    }

    getFeatures()

    return {
      features,
    }
  },
}
</script>
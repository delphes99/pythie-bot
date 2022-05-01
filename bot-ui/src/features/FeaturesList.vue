<template>
  <ui-panel :title="$t('features.title')">
    <ui-card-panel>
      <feature-card
        v-for="feature in features"
        :key="feature"
        :feature="feature"
      />

      <ui-card :title="$t('features.addFeature')">
        <select v-model="featureToAdd">
          <option
            v-for="feature in availableFeatures"
            :key="feature"
          >
            {{ $t("configuration.features." + feature) }}
          </option>
        </select>
        <button
          class="primary-button"
          @click="newFeature()"
        >
          {{ $t("common.add") }}
        </button>
      </ui-card>
    </ui-card-panel>
  </ui-panel>
</template>

<script setup lang="ts">
import UiCard from "@/common/components/common/card/UiCard.vue"
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import { fromJson } from "@/features/configurations/Feature"
import FeatureType from "@/features/configurations/FeatureType"
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import { inject, ref } from "vue"

const backendUrl = inject("backendUrl")
const features = ref([])

async function getFeatures() {
  const response = await fetch(`${backendUrl}/features/new`)

  features.value = await response.json().then((data) => data.map(fromJson))
}

getFeatures()

const availableFeatures = Object.values(FeatureType)
const featureToAdd = ref<FeatureType | null>(null)

const newFeature = () => {
  if (featureToAdd.value) {
    console.log("New feature : " + featureToAdd.value)
  }
}
</script>
<template>
  <ui-card :title="feature.id">
    <div>Type : {{ $t("configuration.features." + feature.type) }}</div>
    <ui-button label="common.edit" :router-link="`/feature/${feature.id}`" />
  </ui-card>
</template>

<script setup lang="ts">
import UiCard from "@/ds/card/UiCard.vue"
import Feature from "@/features/feature"
import UiButton from "@/ds/button/UiButton.vue";



const props = defineProps({
  feature: {
    type: Feature,
    required: true,
  },
})
</script>
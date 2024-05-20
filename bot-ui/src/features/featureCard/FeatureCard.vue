<template>
  <UiCard :title="feature.id">
    <div>Type : {{ $t("configuration.features." + feature.type) }}</div>
    <UiButton :router-link="`/feature/${feature.id}`" label="common.edit"/>
  </UiCard>
</template>

<script lang="ts" setup>
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiCard from "@/common/designSystem/card/UiCard.vue"
import {Feature} from "@/features/Feature";

defineProps({
  feature: {
    type: Feature,
    required: true,
  },
})
</script>
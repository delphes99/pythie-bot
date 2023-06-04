<template>
  <ui-card :title="feature.id">
    <div>Type : {{ $t("configuration.features." + feature.type) }}</div>
    <ui-button :router-link="`/feature/${feature.id}`" label="common.edit"/>
  </ui-card>
</template>

<script lang="ts" setup>
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiCard from "@/common/designSystem/card/UiCard.vue"
import {Feature} from "@/features/feature";

const props = defineProps({
  feature: {
    type: Feature,
    required: true,
  },
})
</script>
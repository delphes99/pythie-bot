<template>
  <ui-card :title="feature.id">
    <div>Type : {{ $t("configuration.features." + feature.type) }}</div>

  </ui-card>
</template>

<script setup lang="ts">
import UiCard from "@/ds/card/UiCard.vue"
import Feature from "@/features/configurations/Feature"
import {inject, PropType} from "vue"
import {useI18n} from "vue-i18n"

const props = defineProps({
  feature: {
    type: Object as PropType<Feature>,
    required: true,
  },
})

const backendUrl = inject("backendUrl") as string
const { t } = useI18n()

</script>
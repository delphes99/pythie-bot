<template>
    <ui-panel
            title="features.title"
    >
        <ui-card-panel>
            <feature-card
                    v-for="feature in features"
                    :key="feature"
                    :feature="feature"
            />
        </ui-card-panel>
    </ui-panel>
</template>

<script setup lang="ts">
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiPanel from "@/ds/panel/UiPanel.vue"
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import {inject, ref} from "vue"
import {useI18n} from "vue-i18n"
import Feature from "@/features/feature";

const {t} = useI18n()

const backendUrl = inject("backendUrl") as string
const features = ref<Feature[]>([])

async function getFeatures() {
    const response = await fetch(`${backendUrl}/features`)

    features.value = await response.json()
}

getFeatures()
</script>
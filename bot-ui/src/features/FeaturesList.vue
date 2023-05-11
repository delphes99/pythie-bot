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

<script lang="ts" setup>
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import {Feature} from "@/features/feature";
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import {InjectionKeys} from "@/injection.keys";
import {inject, ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()

const backendUrl = inject(InjectionKeys.BACKEND_URL) as string
const features = ref<Feature[]>([])

async function getFeatures() {
    const response = await fetch(`${backendUrl}/features`)

    features.value = await response.json()
}

getFeatures()
</script>
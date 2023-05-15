<template>
    <ui-panel
            title="features.title"
            :menu="menu"
    >
        <ui-card-panel>
            <feature-card
                    v-for="feature in features"
                    :key="feature"
                    :feature="feature"
            />
        </ui-card-panel>
    </ui-panel>
    <ui-modal
            v-model:is-open="isCreateModalOpened"
            title="Create feature">
        <ui-textfield v-model="featureId" label="feature.id"/>
        <ui-select
                v-model="selectedType"
                label="feature.type"
                :options="allTypesAsSelectOptions"
        />
        <ui-button label="common.add" @on-click="createFeature"/>
    </ui-modal>
</template>

<script lang="ts" setup>
import UiButton from "@/common/components/common/button/UiButton.vue";
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiSelect from "@/common/components/common/form/select/UiSelect.vue";
import UiTextfield from "@/common/components/common/form/textfield/UiTextfield.vue";
import UiModal from "@/common/components/common/modal/UiModal.vue";
import {useModal} from "@/common/components/common/modal/useModal";
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import {UiPanelMenuItem} from "@/common/components/common/panel/UiPanelMenuItem";
import {Feature} from "@/features/feature";
import FeatureService, {FeatureType} from "@/features/feature.service";
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import {useGetFeatureTypes} from "@/features/useGetFeatureTypes";
import {InjectionKeys} from "@/injection.keys";
import router from "@/router";
import {inject, ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()

const backendUrl = inject(InjectionKeys.BACKEND_URL) as string

const featureId = ref("")

const features = ref<Feature[]>([])

async function getFeatures() {
    const response = await fetch(`${backendUrl}/features`)

    features.value = await response.json()
}

const {allTypesAsSelectOptions, allTypes} = useGetFeatureTypes()
const selectedType = ref<FeatureType>(allTypes.value[0])

const {isOpen: isCreateModalOpened, open: openCreateModal} = useModal()
const menu = [
    UiPanelMenuItem.of("common.add", () => openCreateModal())]

async function createFeature() {
    await new FeatureService(backendUrl).createFeature(featureId.value, selectedType.value)
    await router.push({path: `/feature/${featureId.value}`})
}

getFeatures()
</script>
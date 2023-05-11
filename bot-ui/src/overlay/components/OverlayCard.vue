<template>
    <ui-card :title="overlay.title">
        {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
        <template #actions>
            <ui-button
                    :router-link="`/overlay/${overlay.id}`"
                    label="common.edit"
            />
            <ui-button
                    :type="UiButtonType.Secondary"
                    label="common.delete"
                    @on-click="$emit('deleted')"
            />
        </template>
    </ui-card>
</template>

<script lang="ts" setup>
import UiButton from "@/common/components/common/button/UiButton.vue"
import UiButtonType from "@/common/components/common/button/UiButtonType"
import UiCard from "@/common/components/common/card/UiCard.vue"
import Overlay from "@/overlay/Overlay"
import {PropType} from "vue"

defineProps({
    overlay: {
        type: Object as PropType<Overlay>,
        required: true,
    },
})

defineEmits(["deleted"])
</script>
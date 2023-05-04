<template>
    <div class="mb-2 p-0 border-primaryColor border-2">
        <div class="bg-primaryColor text-primaryTextColor p-2">
            <h1 class="inline text-2xl font-medium title-font">
                {{ $t(title) }}
            </h1>
        </div>
        <div v-if="menu && menu.length">
            <div class="flex flex-row bg-secondaryColor">
                <div
                        v-for="menuItem in menu"
                        :key="menuItem.label"
                >
                    <ui-button
                            :label="menuItem.label"
                            @on-click="menuItem.onClick()"
                    />
                </div>
            </div>
        </div>
        <div
                v-if="$slots.default"
                class="p-4"
        >
            <slot/>
        </div>
        <slot name="withoutContainer"/>
    </div>
</template>

<script lang="ts" setup>
import UiButton from "@/ds/button/UiButton.vue"
import {UiPanelMenuItem} from "@/ds/panel/UiPanelMenuItem"
import {PropType} from "vue"

defineProps({
    title: {
        type: String,
        required: true,
    },
    class: {
        type: String,
        default: null,
    },
    menu: {
        type: Object as PropType<UiPanelMenuItem[]>,
        default: null,
    },
})
</script>
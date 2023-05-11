<template>
    <div
            v-if="isOpen"
            class="fixed top-0 left-0 w-screen h-screen bg-black bg-opacity-50 flex items-center justify-center"
            @click="close()"
    >
        <div
                id="modal"
                class="max-w-screen-4/5 opacity-100 bg-backgroundColor text-backgroundTextColor rounded shadow-lg"
                @click.stop
        >
            <ui-panel
                    :title="title"
            >
                <template #withoutContainer>
                    <div class="overflow-auto max-h-screen-4/5 p-4">
                        <slot/>
                    </div>
                </template>
            </ui-panel>
        </div>
    </div>
</template>

<script lang="ts" setup>
import UiPanel from "@/common/components/common/panel/UiPanel.vue"

defineProps({
    isOpen: {
        type: Boolean,
        required: true,
    },
    title: {
        type: String,
        required: true,
    },
})

const emit = defineEmits(["update:isOpen"])

const close = () => {
    emit("update:isOpen", false)
}
</script>
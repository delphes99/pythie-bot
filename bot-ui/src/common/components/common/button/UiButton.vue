<template>
    <template v-if="routerLink">
        <router-link :to="routerLink">
            <a
                    :class="classes"
                    class="button-shape"
            >{{ $t(label) }}</a>
        </router-link>
    </template>
    <template v-else-if="link">
        <a
                :class="classes"
                :href="link"
                class="button-shape"
        >{{
            $t(label)
            }}</a>
    </template>
    <template v-else>
        <button
                :class="classes"
                class="button-shape"
                type="button"
                @click="$emit('on-click')"
        >
            {{ $t(label) }}
        </button>
    </template>
</template>

<script lang="ts" setup>
import {UiButtonType} from "@/common/components/common/button/UiButtonType"
import {computed} from "vue"

const props = defineProps({
    label: {
        type: String,
        required: true,
    },
    type: {
        type: String as () => UiButtonType,
        default: UiButtonType.Primary,
    },
    routerLink: {
        type: String,
        default: null,
    },
    link: {
        type: String,
        default: null,
    },
})

defineEmits(["on-click"])

const classes = computed(() => {
    return {
        "primary-button": props.type == UiButtonType.Primary,
        "secondary-button": props.type == UiButtonType.Secondary,
        "warning-button": props.type == UiButtonType.Warning,
    }
})
</script>

<style scoped>
.button-shape {
    @apply m-2 px-3 py-1 text-base rounded-lg focus:ring inline-block text-white border-0 rounded-md transition duration-500 select-none focus:outline-none;
}

.primary-button {
    @apply bg-primaryColor hover:bg-primaryColorHover;
}

.secondary-button {
    @apply bg-red-500 hover:bg-red-800;
}

.warning-button {
    @apply bg-red-500 hover:bg-red-800;
}
</style>
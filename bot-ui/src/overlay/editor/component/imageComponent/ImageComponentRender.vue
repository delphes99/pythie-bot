<template>
    <img
            :src="component.properties.url"
            :style="style"
    >
</template>

<script lang="ts" setup>
import ImageComponent from "@/overlay/editor/component/imageComponent/ImageComponent"
import OverlayElement from "@/overlay/OverlayElement"
import {computed, PropType} from "vue"

const props = defineProps({
    component: {
        type: Object as PropType<OverlayElement<ImageComponent>>,
        default: null,
    },
})

const style = computed(() => {
    const width = props.component.properties.width
    const height = props.component.properties.height
    return {
        width: width ? (`${width}px`) : "auto",
        height: height ? (`${height}px`) : "auto",
    }
})
</script>
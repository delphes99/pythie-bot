<template>
    <div
            :id="componentId"
            :class="classes"
            :style="style"
            class="draggable border-dashed border-2 box-border"
            @click="updateSelection()"
    >
        <component
                :is="component.properties.renderComponent()"
                :component="component"
        />
    </div>
</template>

<script lang="ts" setup>
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import OverlayElement from "@/overlay/OverlayElement"
import {OverlayElementProperties} from "@/overlay/OverlayElementProperties"
import interact from "interactjs"
import {storeToRefs} from "pinia"
import {computed, onMounted, PropType, ref, watch} from "vue"

const props = defineProps({
    component: {
        type: Object as PropType<OverlayElement<OverlayElementProperties>>,
        required: true,
    },
})

const store = useOverlayEditorStore()
const {selectedElementId} = storeToRefs(store)

const componentId = `draggable-${props.component.id}`

const x = ref(0)
const y = ref(0)

watch(
    () => props.component.general,
    (newValue) => {
        x.value = newValue.left
        y.value = newValue.top
    },
    {immediate: true, deep: true},
)

const isSelected = computed(() => props.component.id === selectedElementId.value)

const classes = computed(() => {
    return {
        "border-gray-300": !isSelected.value,
        "border-black": isSelected.value,
    }
})

const style = computed(() => {
    const Z_INDEX_MAX = "9999"
    return {
        transform: `translate(${x.value}px, ${y.value}px)`,
        position: "absolute",
        display: "inline-block",
        "user-select": "none",
        "z-index": isSelected.value ? Z_INDEX_MAX : props.component.general.sortOrder,
    }
})

function updateSelection() {
    store.selection(props.component)
}

function updateComponent() {
    store.updateComponent(props.component.modifyGeneral(old => old.modifyCoordinate(x.value, y.value)))
}

onMounted(() => {
    interact(`#${componentId}`)
        .draggable({
            modifiers: [
                interact.modifiers.restrict({
                    restriction: "parent",
                    endOnly: true,
                }),
            ],
            listeners: {
                start() {
                    updateSelection()
                },
                move(event) {
                    x.value += event.dx
                    y.value += event.dy
                },
                end() {
                    updateComponent()
                    updateSelection()
                },
            },
        })
})
</script>

<style>
/*noinspection CssUnusedSymbol*/
.draggable {
    user-select: none;
}
</style>
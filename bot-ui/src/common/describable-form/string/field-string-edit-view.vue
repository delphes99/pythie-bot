<template>
    <ui-textfield
            :id="descriptor.fieldName"
            v-model="actualValue"
            :label="descriptor.description"
    />
</template>

<script lang="ts" setup>
import {StringDescriptor} from "@/common/describable-form/string/string-descriptor";
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {computed, PropType} from "vue";

const emits = defineEmits<{
    (e: 'modifyDescriptor', descriptor: StringDescriptor): void
}>()

const props = defineProps({
    descriptor: {
        type: Object as PropType<StringDescriptor>,
        required: true,
    }
})

const actualValue = computed({
    get() {
        return props.descriptor.actualValue
    },
    set(newValue) {
        emits('modifyDescriptor', props.descriptor.withValue(newValue))
    }
})
</script>
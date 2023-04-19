<template>
    {{descriptor.description}}
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.hours"
            v-model="hourModel"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.minutes"
            v-model="minuteModel"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.seconds"
            v-model="secondModel"
    />
</template>

<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {FieldDescriptor} from "@/common/ describableForm/field-descriptor";
import {computed, PropType, ref} from "vue";
import {formatDuration, parseDuration} from "@/common/duration.utils";

const props = defineProps({
    descriptor: {
        type: Object as PropType<FieldDescriptor>,
        required: true,
    },
    currentValue: {
        type: String,
        required: true,
    },
})

const emit = defineEmits(["update:currentValue"])

const currentValue = ref(parseDuration(props.currentValue))

const hourModel = computed({
    get() {
        return currentValue.value.hours
    },

    set(value) {
        if (value) {
            emit("update:currentValue", formatDuration({...currentValue.value, hours: value}))
        } else {
            emit("update:currentValue", null)
        }
    },
})

const minuteModel = computed({
    get() {
        return currentValue.value.minutes
    },

    set(value) {
        if (value) {
            emit("update:currentValue", formatDuration({...currentValue.value, minutes: value}));
        } else {
            emit("update:currentValue", null)
        }
    },
})

const secondModel = computed({
    get() {
        return currentValue.value.seconds
    },

    set(value) {
        if (value) {
            emit("update:currentValue", formatDuration({...currentValue.value, seconds: value}));
        } else {
            emit("update:currentValue", null)
        }
    },
})
</script>
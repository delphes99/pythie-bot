<template>
    <ui-textfield
            :id="descriptor.fieldName"
            :label="descriptor.description"
            v-model="model"
    />
</template>

<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {FieldDescriptor} from "@/common/ describableForm/field-descriptor";
import {computed, PropType, ref} from "vue";

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

const currentValue = ref(props.descriptor.value)

const model = computed({
    get() {
        return props.currentValue
    },

    set(value) {
        if(value) {
            emit("update:currentValue", value)
        } else {
            emit("update:currentValue", null)
        }
    },
})
</script>
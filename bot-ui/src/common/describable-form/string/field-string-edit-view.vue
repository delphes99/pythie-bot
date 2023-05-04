<template>
    <ui-textfield
            :id="descriptor.fieldName"
            :label="descriptor.description"
            v-model="descriptor.actualValue"
    />
</template>

<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {PropType} from "vue";
import {StringDescriptor} from "@/common/describable-form/string/string-descriptor";

const props = defineProps({
    descriptor: {
        type: Object as PropType<StringDescriptor>,
        required: true,
    }
})
</script>
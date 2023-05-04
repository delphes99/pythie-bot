<template>
    <ui-textfield
            :id="descriptor.fieldName"
            v-model="descriptor.actualValue"
            :label="descriptor.description"
    />
</template>

<script lang="ts" setup>
import {StringDescriptor} from "@/common/describable-form/string/string-descriptor";
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {PropType} from "vue";

const props = defineProps({
    descriptor: {
        type: Object as PropType<StringDescriptor>,
        required: true,
    }
})
</script>
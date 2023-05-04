<template>
  {{ descriptor.description }}
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.hours"
            v-model.number="descriptor.actualValue.hours"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.minutes"
            v-model.number="descriptor.actualValue.minutes"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            label="common.duration.seconds"
            v-model.number="descriptor.actualValue.seconds"
    />
</template>

<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {PropType} from "vue";
import {DurationDescriptor} from "@/common/describable-form/duration/duration-descriptor";

const props = defineProps({
    descriptor: {
        type: Object as PropType<DurationDescriptor>,
        required: true,
    }
})
</script>
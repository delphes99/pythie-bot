<template>
  {{ descriptor.description }}
    <ui-textfield
            :id="descriptor.fieldName"
            v-model.number="descriptor.actualValue.hours"
            label="common.duration.hours"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            v-model.number="descriptor.actualValue.minutes"
            label="common.duration.minutes"
    />
    <ui-textfield
            :id="descriptor.fieldName"
            v-model.number="descriptor.actualValue.seconds"
            label="common.duration.seconds"
    />
</template>

<script lang="ts" setup>
import {DurationDescriptor} from "@/common/describable-form/duration/duration-descriptor";
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import {PropType} from "vue";

const props = defineProps({
    descriptor: {
        type: Object as PropType<DurationDescriptor>,
        required: true,
    }
})
</script>
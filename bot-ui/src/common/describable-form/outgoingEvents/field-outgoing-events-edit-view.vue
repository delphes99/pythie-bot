<template>
  {{ props.descriptor.description }}
    <br>
    <br>
  {{ props.descriptor }}
</template>

<script setup lang="ts">
import {PropType} from "vue";
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";

const props = defineProps({
    descriptor: {
        type: Object as PropType<OutgoingEventsDescriptor>,
        required: true,
    },
})

</script>
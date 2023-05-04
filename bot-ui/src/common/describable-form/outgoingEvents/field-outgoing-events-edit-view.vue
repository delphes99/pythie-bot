<template>
  {{ props.descriptor.description }}
    <br>
    <br>
    <fieldset v-for="event in descriptor.events" class="border p-2">
        <legend>{{ event.type }}</legend>
        <div v-for="descriptor in event.descriptors" :key="descriptor.fieldName">
            <component :is="descriptor.viewComponent()"
                       :descriptor="descriptor"
            />
        </div>
    </fieldset>
</template>

<script lang="ts" setup>
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";
import {PropType} from "vue";

const props = defineProps({
    descriptor: {
        type: Object as PropType<OutgoingEventsDescriptor>,
        required: true,
    },
})

</script>
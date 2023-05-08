<template>
  {{ descriptor.description }}
    <br>
    <br>
    <fieldset v-for="event in descriptor.events" :key="event.id" class="border p-2">
        <legend>{{ event.type }}</legend>
        <div v-for="descriptor in event.descriptors" :key="descriptor.fieldName">
            <component :is="descriptor.viewComponent()"
                       :descriptor="descriptor"
            />
        </div>
        <ui-button label="common.delete"
                   @click="deleteEvent(event)"
                   :type="UiButtonType.Warning"/>
    </fieldset>
</template>

<script lang="ts" setup>
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";
import UiButton from "@/ds/button/UiButton.vue";
import UiButtonType from "@/ds/button/UiButtonType";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {PropType} from "vue";

const props = defineProps({
    descriptor: {
        type: Object as PropType<OutgoingEventsDescriptor>,
        required: true,
    }
})

function deleteEvent(event: OutgoingEventDescription) {
    props.descriptor.events = props.descriptor.events.filter((e) => e.id !== event.id)
}

</script>
<template>
  {{ props.descriptor.description }}
    <br>
    <br>
  <fieldset class="border p-2" v-for="event in descriptor.events">
      <legend>{{event.type}}</legend>
      <div v-for="descriptor in event.descriptors" :key="descriptor.fieldName">
          <component :is="descriptor.viewComponent()"
                     :descriptor="descriptor"
          />
      </div>
  </fieldset>
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
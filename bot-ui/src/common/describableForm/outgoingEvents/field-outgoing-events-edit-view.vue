<template>
  <fieldset class="border p-2">
    <ui-button label="common.add"
               @click="openAddEvent"
               :type="UiButtonType.Primary"/>
    <fieldset v-for="event in props.descriptor.events" :key="event.id" class="border p-2">
      <legend>{{ event.type }}</legend>
      <div v-for="descriptor in event.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
                   @modifyDescriptor="modifyDescriptor(event, $event)"
        />
      </div>
      <ui-button label="common.delete"
                 @click="deleteEvent(event)"
                 :type="UiButtonType.Warning"/>
    </fieldset>
  </fieldset>
  <ui-modal title="common.add" v-model:is-open="isAddEventOpened">
    <ui-select v-model="selectedType" :options="allTypesAsSelectOptions" label="feature.outgoingEvents">
    </ui-select>
    <ui-button
        label="common.add"
        @click="addEvent"
        :type="UiButtonType.Primary"/>
  </ui-modal>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import {FieldDescriptor} from "@/common/describableForm/field-descriptor";
import {OutgoingEventsDescriptor} from "@/common/describableForm/outgoingEvents/outgoing-events-descriptor";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import {autowired} from "@/common/utils/injection.util";
import {OutgoingEventType} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {OutgoingEventCreateService} from "@/features/outgoingevents/outgoing-event.create.service";
import {useGetOutgoingEventTypes} from "@/features/outgoingevents/useOutgoingEventTypesList";
import {PropType, ref} from "vue";


const emits = defineEmits<{
  modifyDescriptor: [descriptor: OutgoingEventsDescriptor]
}>()

const props = defineProps({
  descriptor: {
    type: Object as PropType<OutgoingEventsDescriptor>,
    required: true,
  }
})

function deleteEvent(event: OutgoingEventDescription) {
  emits('modifyDescriptor', props.descriptor.deleteEvent(event))
}

function modifyDescriptor(event: OutgoingEventDescription, modifiedDescriptor: FieldDescriptor<any>) {
  emits('modifyDescriptor', props.descriptor.modifyEvent(event, modifiedDescriptor))
}

const {isOpen: isAddEventOpened, open: openAddEvent, close: closeAddEvent} = useModal()
const {allTypesAsSelectOptions, allTypes} = useGetOutgoingEventTypes()
const selectedType = ref<OutgoingEventType>(allTypes.value[0])

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const outgoingEventCreateService = new OutgoingEventCreateService(backendUrl)

async function addEvent() {
  const description = await outgoingEventCreateService.getNewOutgoingEventDescription(selectedType.value)
  closeAddEvent()
  emits('modifyDescriptor', props.descriptor.addEvent(description))
}
</script>
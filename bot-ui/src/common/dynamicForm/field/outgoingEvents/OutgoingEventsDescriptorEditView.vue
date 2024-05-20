<template>
  <fieldset class="border p-2">
    <UiButton label="common.add"
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
      <UiButton label="common.delete"
                @click="deleteEvent(event)"
                :type="UiButtonType.Warning"/>
    </fieldset>
  </fieldset>
  <UiModal title="common.add" v-model:is-open="isAddEventOpened">
    <UiSelect v-model="selectedType" :options="allTypesAsSelectOptions" label="feature.outgoingEvents">
    </UiSelect>
    <UiButton
        label="common.add"
        @click="addEvent"
        :type="UiButtonType.Primary"/>
  </UiModal>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/field/outgoingEvents/OutgoingEventsDescriptor";
import {autowired} from "@/common/utils/Injection.util";
import {OutgoingEventType} from "@/features/outgoingevents/OutgoingEvent";
import {OutgoingEventCreateService} from "@/features/outgoingevents/OutgoingEventCreateService";
import {OutgoingEventDescription} from "@/features/outgoingevents/OutgoingEventDescription";
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
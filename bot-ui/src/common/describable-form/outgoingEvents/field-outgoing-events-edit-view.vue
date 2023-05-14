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
import UiButton from "@/common/components/common/button/UiButton.vue";
import UiButtonType from "@/common/components/common/button/UiButtonType";
import UiSelect from "@/common/components/common/form/select/UiSelect.vue";
import UiModal from "@/common/components/common/modal/UiModal.vue";
import {useModal} from "@/common/components/common/modal/useModal";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";
import {OutgoingEventType} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {OutgoingEventCreateService} from "@/features/outgoingevents/outgoing-event.create.service";
import {useGetOutgoingEventTypes} from "@/features/outgoingevents/useOutgoingEventTypesList";
import {inject, PropType, ref} from "vue";


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

const backendUrl = inject('backendUrl') as string
const outgoingEventCreateService = new OutgoingEventCreateService(backendUrl)

async function addEvent() {
    const description = await outgoingEventCreateService.getNewOutgoingEventDescription(selectedType.value)
    closeAddEvent()
    emits('modifyDescriptor', props.descriptor.addEvent(description))
}
</script>
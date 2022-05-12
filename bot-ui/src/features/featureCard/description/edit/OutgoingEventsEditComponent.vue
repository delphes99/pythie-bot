<template>
  <div class="border">
    <label>{{ $t("feature.outgoingEvents") }}</label>
    <table class="border">
      <tr
        v-if="model.events.length === 0"
        class="border-b border-gray-200"
      >
        <td>{{ $t("feature.noOutgoingEvents") }}</td>
      </tr>
      <tr
        v-for="event in model.events"
        v-else
        :key="event.id"
        class="border-b border-gray-200"
      >
        <td>
          <label>{{ $t("configuration.outgoingEvents." + event.type) }}</label>
          <ui-textfield
            v-model="event.text"
            label="Response"
          />
          <ui-textfield
            model="event.channel"
            label="Channel"
          />
          <input
            type="button"
            class="warning-button"
            :value="$t('common.delete')"
            @click="deleteEvent(event)"
          >
        </td>
      </tr>
      <tr class="border-b border-gray-200">
        <td>
          <ui-select
            v-model="outgoingEventTypeToAdd"
            :options="availableOutgoingEventsTypeForSelect"
          />
          <ui-button
            label="common.add"
            :type="UiButtonType.Primary"
            @on-click="addEvent()"
          />
        </td>
      </tr>
    </table>
  </div>
</template>
<script setup lang="ts">
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
import UiSelect from "@/ds/form/select/UiSelect.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { OutgoingEventsFormItem } from "@/features/featureCard/description/OutgoingEventsFormItem"
import { useAddOutgoingEvent } from "@/features/featureCard/useAddOutgoingEvent"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessage"
import { computed, inject, PropType } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const backendUrl = inject("backendUrl") as string

const props = defineProps({
  modelValue: {
    type: Object as PropType<OutgoingEventsFormItem>,
    required: true,
  },
})

const emit = defineEmits(["update:modelValue"])

const model = computed<OutgoingEventsFormItem>({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})

const deleteEvent = (event: OutgoingEvent) => {
  const { id, field, events } = model.value
  model.value = new OutgoingEventsFormItem(
    id,
    field,
    events.filter((e) => e.id != event.id),
  )
}

const { outgoingEventTypeToAdd, availableOutgoingEventsTypeForSelect } =
  useAddOutgoingEvent(backendUrl, t, true)

const addEvent = () => {
  const { id, field, events } = model.value
  model.value = new OutgoingEventsFormItem(
    id,
    field,
    events.concat(new TwitchOutgoingSendMessage("", "")),
  )
}
</script>

<style scoped></style>
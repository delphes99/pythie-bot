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
          <label>{{ $t("outgoing-event." + event.type) }}</label>
          <label :for="'response-' + event.id">Response</label>
          <input
            :id="'response-' + event.id"
            v-model="event.text"
            type="text"
          >
          <label :for="'channel-' + event.id">Channel</label>
          <input
            :id="'channel-' + event.id"
            v-model="event.channel"
            type="text"
          >
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
          <input
            type="button"
            class="primary-button"
            :value="$t('common.add')"
            @click="addEvent()"
          >
        </td>
      </tr>
    </table>
  </div>
</template>
<script setup lang="ts">
import { OutgoingEventsFormItem } from "@/features/featureCard/description/OutgoingEventsFormItem"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessage"
import { computed, PropType } from "vue"

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
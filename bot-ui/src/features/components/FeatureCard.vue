<template>
  <card :title="feature.identifier">
    <template
      v-if="icon"
      #icon
    >
      <img
        :src="icon"
        width="30"
        height="20"
      >
    </template>
    <div>Type : {{ feature.type }}</div>
    <div
      v-for="description in editForm"
      :key="description.id"
    >
      <div v-if="description.type === FeatureDescriptionType.STRING">
        {{ description.field }} : {{ description.value }}
      </div>
      <div v-if="description.type === FeatureDescriptionType.DURATION">
        {{ description.field }} : {{ description.number }} s
      </div>
      <div v-if="description.type === FeatureDescriptionType.OUTGOING_EVENTS">
        <div
          v-for="event in description.outgoingEvents"
          :key="event.id"
        >
          {{ event.type }}
        </div>
      </div>
    </div>
    <template #actions>
      <button
        class="primary-button"
        @click="openSettings()"
      >
        {{ $t("common.edit") }}
      </button>
    </template>
  </card>
  <modal
    v-model:is-open="isSettingOpened"
    :title="feature.identifier"
  >
    <fieldset class="flex flex-col border border-black p-1">
      <legend>Edit</legend>
      <div
        v-for="item in editForm"
        :key="item.field"
      >
        <div v-if="item.type === FeatureDescriptionType.STRING">
          <label :for="item.id">{{ item.field }}</label>
          <input
            :id="item.id"
            v-model="item.value"
            type="text"
          >
        </div>
        <div v-if="item.type === FeatureDescriptionType.DURATION">
          <label :for="item.id">{{ item.field }} (s)</label>
          <input
            :id="item.id"
            v-model="item.number"
            type="text"
          >
        </div>
        <div v-if="item.type === FeatureDescriptionType.OUTGOING_EVENTS">
          <div
            v-for="event in item.outgoingEvents"
            :key="event.id"
          >
            <label :for="event.id">{{ item.field }} ( {{ event.id }} )</label>
            <input
              :id="event.id"
              v-model="event.text"
              type="text"
            >
          </div>
        </div>
      </div>
    </fieldset>
    <button
      class="primary-button"
      @click="saveChanges"
    >
      Save changes
    </button>
  </modal>
</template>

<script setup lang="ts">
import Card from "@/common/components/common/Card.vue"
import Modal from "@/common/components/common/CommonModal.vue"
import Feature from "@/features/configurations/Feature"
import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/TwitchOutgoingSendMessage"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, PropType, ref } from "vue"
import { useI18n } from "vue-i18n"

interface FormItem {
  id: string
  field: string
  value: string | null
  number: bigint | null
  outgoingEvents: OutgoingEvent[] | null
  type: FeatureDescriptionType
}

const props = defineProps({
  feature: {
    type: Object as PropType<Feature>,
    required: true,
  },
})

const backendUrl = inject("backendUrl") as string
const { t } = useI18n()
const isSettingOpened = ref(false)
const openSettings = () => (isSettingOpened.value = true)

const editForm = ref<FormItem[]>(
  props.feature.descriptionItems.map((desc) => {
    switch (desc.type) {
      case FeatureDescriptionType.STRING:
        return {
          id: props.feature.identifier + "_" + desc.name,
          field: desc.name,
          value: desc.currentValue as string,
          number: null,
          outgoingEvents: null,
          type: FeatureDescriptionType.STRING,
        }
      case FeatureDescriptionType.OUTGOING_EVENTS:
        return {
          id: props.feature.identifier + "_" + desc.name,
          field: desc.name,
          value: null,
          number: null,
          outgoingEvents: desc.currentValue as OutgoingEvent[],
          type: FeatureDescriptionType.OUTGOING_EVENTS,
        }
      case FeatureDescriptionType.DURATION:
        return {
          id: props.feature.identifier + "_" + desc.name,
          field: desc.name,
          value: null,
          number: desc.currentValue as bigint,
          outgoingEvents: null,
          type: FeatureDescriptionType.DURATION,
        }
    }
  }),
)

async function saveChanges() {
  if (editForm.value.some((item) => item.value?.trim() === "")) {
    ElNotification({
      title: t("common.emptyField"),
      type: "error",
    })
    return
  }

  const { identifier, type } = props.feature
  const newFeature: any = { identifier, type }

  function unknownDescriptionItemType(type: never) {
    throw new Error(`unknow description item type ${type}`)
  }

  editForm.value.forEach((item) => {
    switch (item.type) {
      case FeatureDescriptionType.STRING:
        newFeature[item.field] = item.value
        return
      case FeatureDescriptionType.OUTGOING_EVENTS:
        newFeature[item.field] = item.outgoingEvents?.map(
          (o) => new TwitchOutgoingSendMessage(o.text, newFeature.channel),
        )
        return
      case FeatureDescriptionType.DURATION:
        newFeature[item.field] = `PT${item.number?.toString()}S`
        return
      default:
        unknownDescriptionItemType(item.type)
    }
  })

  await axios.post(`${backendUrl}/feature/edit`, JSON.stringify(newFeature), {
    headers: { "Content-Type": "application/json" },
  })
}
</script>

<template>
  <card :title="feature.type">
    <div v-if="feature.channel">Channel : {{ feature.channel }}</div>
    <div v-if="feature.trigger">Command : {{ feature.trigger }}</div>
    <template v-slot:actions>
      <button class="primary-button" v-on:click="openSettings()">
        Edit
      </button>
    </template>
  </card>
  <Modal v-model:is-open="isSettingOpened" :title="feature.id">
    <div v-if="feature.channel">Channel : {{ feature.channel }}</div>
    <div v-if="feature.trigger">Command : {{ feature.trigger }}</div>
    <div class="flex justify-center">
      <label class="w-11/12">
        <textarea class="w-full" v-text="feature"></textarea>
      </label>
    </div>
  </Modal>
</template>

<script lang="ts">
import Card from "@/components/common/Card.vue";

import {defineComponent, PropType, ref} from 'vue'
import {FeatureType} from "@/twitch/feature/type/FeatureTypeEnum";
import {Feature} from "@/twitch/feature/type/Feature";
import Modal from "@/components/common/Modal.vue";

export default defineComponent({
  name: 'FeatureCard',
  components: {Card, Modal},
  props: {
    feature: {
      type: Object as PropType<Feature>,
      required: true
    },
  },
  setup(props) {
    const isSettingOpened = ref(false)
    const featureType = () => props.feature.type

    const isCommand = props.feature.type === FeatureType.TWITCH_COMMAND

    const openSettings = () => isSettingOpened.value = true

    return {
      isSettingOpened,
      featureType,
      isCommand,
      openSettings
    }
  }
})
</script>
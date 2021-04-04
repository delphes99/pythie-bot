<template>
  <card :title="feature.type">
    <template v-slot:icon v-if="icon">
      <img :src="icon" width="30" height="20" />
    </template>
    <component :is="component" :feature="feature" />
    <template v-slot:actions>
      <button
        v-if="feature.editable"
        class="primary-button"
        v-on:click="openSettings()"
      >
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
import Card from "@/common/components/common/Card.vue";

import { defineComponent, PropType, ref } from "vue";
import { FeatureType } from "@/twitch/feature/type/FeatureTypeEnum.ts";
import Feature from "@/twitch/feature/type/Feature.ts";
import Modal from "@/common/components/common/Modal.vue";
import FeatureDefaultCard from "@/features/components/FeatureDefaultCard.vue";
import FeatureTwitchCommandCard from "@/twitch/feature/component/FeatureTwitchCommandCard.vue";

function componentToCard(feature: Feature) {
  switch (feature.type) {
    case FeatureType.TWITCH_COMMAND:
      return FeatureTwitchCommandCard;
    default:
      return FeatureDefaultCard;
  }
}

export default defineComponent({
  name: "FeatureCard",
  components: {
    Card,
    Modal,
    FeatureDefaultCard,
    FeatureTwitchCommandCard
  },
  props: {
    feature: {
      type: Object as PropType<Feature>,
      required: true
    }
  },
  setup(props) {
    const isSettingOpened = ref(false);
    const featureType = () => props.feature.type;

    const component = componentToCard(props.feature);

    const openSettings = () => (isSettingOpened.value = true);
    const icon = props.feature.editable
      ? null
      : require("@/common/assets/readonly.svg");

    return {
      isSettingOpened,
      featureType,
      openSettings,
      component,
      icon
    };
  }
});
</script>

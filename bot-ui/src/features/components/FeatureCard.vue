<template>
  <card :title="feature.identifier">
    <template v-slot:icon v-if="icon">
      <img :src="icon" width="30" height="20" />
    </template>
    <div>Type : {{ feature.type }}</div>
    <div v-for="description in feature.description()" :key="description.key">
      {{ description.key }} : {{ description.value }}
    </div>
    <template v-slot:actions>
      <button class="primary-button" v-on:click="openSettings()">
        {{ $t("common.edit") }}
      </button>
    </template>
  </card>
  <Modal v-model:is-open="isSettingOpened" :title="feature.identifier">
    <fieldset class="flex flex-col border border-black p-1">
      <legend>Edit</legend>
      <div v-for="item in editForm" :key="item.field">
        <label :for="item.id">{{ item.field }}</label>
        <input type="text" :id="item.id" v-model="item.value" />
      </div>
    </fieldset>
    <button class="primary-button" @click="saveChanges">Save changes</button>
  </Modal>
</template>

<script lang="ts">
import Card from "@/common/components/common/Card.vue";
import Modal from "@/common/components/common/Modal.vue";
import Feature from "@/features/configurations/feature";
import axios from "axios";
import { ElNotification } from "element-plus";
import { defineComponent, inject, PropType, ref } from "vue";
import { useI18n } from "vue-i18n";

interface FormItem {
  id: string;
  field: string;
  value: string;
  type: FormItemType;
}

enum FormItemType {
  String,
  OutgoingEvents
}

export default defineComponent({
  name: "FeatureCard",
  components: {
    Card,
    Modal
  },
  props: {
    feature: {
      type: Object as PropType<Feature>,
      required: true
    }
  },
  setup(props) {
    const backendUrl = inject("backendUrl") as string;
    const { t } = useI18n();
    const isSettingOpened = ref(false);
    const featureType = () => props.feature.type;

    const openSettings = () => (isSettingOpened.value = true);

    const editForm = ref<FormItem[]>(
      props.feature.description().map(desc => {
        return {
          id: props.feature.identifier + "_" + desc.key,
          field: desc.key,
          value: desc.value,
          type: FormItemType.String
        };
      })
    );

    async function saveChanges() {
      if (editForm.value.some(item => item.value.trim() == "")) {
        ElNotification({
          title: t("common.emptyField"),
          type: "error"
        });
        return;
      }

      const newFeature: any = props.feature;
      editForm.value.forEach(item => {
        newFeature[item.field] = item.value;
      });

      await axios.post(
        `${backendUrl}/feature/edit`,
        JSON.stringify(newFeature),
        {
          headers: { "Content-Type": "application/json" }
        }
      );

      console.log(JSON.stringify(newFeature));
    }

    return {
      isSettingOpened,
      featureType,
      openSettings,
      editForm,
      saveChanges
    };
  }
});
</script>

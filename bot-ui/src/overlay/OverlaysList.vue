<template>
  <panel title="Features">
    <card-panel>
      <overlay-card
        v-for="overlay in overlays"
        :key="overlay"
        :overlay="overlay"
        @deleted="deleteOverlay(overlay)"
      ></overlay-card>
      <card class="align-middle">
        <div class="flex items-center justify-center h-full">
          <button class="primary-button" @click="openCreate">Add</button>
        </div>
      </card>
      <Modal v-model:is-open="isCreateModalOpened" title="Create overlay">
        <fieldset class="flex flex-col border border-black p-1">
          <legend>Overlay</legend>
          <label for="name">Name</label>
          <input type="text" id="name" v-model="addName" />
          <fieldset class="flex flex-col border border-black p-1">
            <legend>Resolution</legend>
            <label for="width">Width</label>
            <input type="text" id="width" v-model="addWidth" />
            <label for="height">Height</label>
            <input type="text" id="height" v-model="addHeight" />
          </fieldset>
        </fieldset>
        <button class="primary-button" @click="createOverlay">Add</button>
      </Modal>
    </card-panel>
  </panel>
</template>

<script lang="ts">
import Card from "@/common/components/common/Card.vue";
import CardPanel from "@/common/components/common/CardPanel.vue";
import Modal from "@/common/components/common/Modal.vue";
import Panel from "@/common/components/common/Panel.vue";
import OverlayCard from "@/overlay/components/OverlayCard.vue";
import Overlay from "@/overlay/Overlay";
import OverlayRepository from "@/overlay/OverlayRepository";
import Resolution from "@/overlay/Resolution";
import router from "@/router";
import { v4 as uuidv4 } from "uuid";
import { inject, ref } from "vue";

function useOverlayList(repository: OverlayRepository) {
  const overlays = ref<Overlay[]>([]);

  async function getOverlays() {
    overlays.value = await repository.list();
  }

  return {
    overlays,
    getOverlays
  };
}

function useCreateOverlay(repository: OverlayRepository) {
  const isCreateModalOpened = ref(false);
  const addName = ref("");
  const addWidth = ref(1920);
  const addHeight = ref(1080);

  function openCreate() {
    isCreateModalOpened.value = true;
  }

  async function createOverlay() {
    //TODO better validation
    if (!addName.value || !addWidth.value || !addHeight.value) {
      alert("missing field");
      return;
    }

    const overlay = new Overlay(
      uuidv4(),
      addName.value,
      new Resolution(addWidth.value, addHeight.value)
    );

    await repository.save(overlay);
    await router.push({ path: `/overlay/${overlay.id}` });
  }

  return {
    isCreateModalOpened,
    openCreate,
    addName,
    addWidth,
    addHeight,
    createOverlay
  };
}

export default {
  name: `OverlaysList`,
  components: { Modal, Card, OverlayCard, CardPanel, Panel },
  setup() {
    const backendUrl = inject("backendUrl") as string;
    const repository = new OverlayRepository(backendUrl);

    const { overlays, getOverlays: refresh } = useOverlayList(repository);

    refresh();

    async function deleteOverlay(overlay: Overlay) {
      await repository.deleteOverlay(overlay);
      //TODO notification card
      await refresh();
    }

    return {
      overlays,
      deleteOverlay,
      ...useCreateOverlay(repository)
    };
  }
};
</script>

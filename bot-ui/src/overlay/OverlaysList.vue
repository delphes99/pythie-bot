<template>
  <ui-panel title="Features">
    <ui-card-panel>
      <overlay-card
        v-for="overlay in overlays"
        :key="overlay"
        :overlay="overlay"
        @deleted="deleteOverlay(overlay)"
      />
      <ui-card class="align-middle">
        <div class="flex items-center justify-center h-full">
          <ui-button
            label="overlay.createOverlay"
            @on-click="openCreate"
          />
        </div>
      </ui-card>
      <ui-modal
        v-model:is-open="isCreateModalOpened"
        title="Create overlay"
      >
        <fieldset class="flex flex-col border border-black p-1">
          <legend>Overlay</legend>
          <ui-textfield
            v-model="addName"
            label="overlay.name"
          />
          <fieldset class="flex flex-col border border-black p-1">
            <legend>Resolution</legend>
            <ui-textfield
              v-model="addWidth"
              label="overlay.width"
            />
            <ui-textfield
              v-model="addHeight"
              label="overlay.height"
            />
          </fieldset>
        </fieldset>
        <ui-button
          label="common.add"
          @on-click="createOverlay"
        />
      </ui-modal>
    </ui-card-panel>
  </ui-panel>
</template>

<script lang="ts">
import UiCard from "@/ds/card/UiCard.vue"
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiModal from "@/common/components/common/modal/UiModal.vue"
import UiPanel from "@/ds/panel/UiPanel.vue"
import UiButton from "@/ds/button/UiButton.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import OverlayCard from "@/overlay/components/OverlayCard.vue"
import Overlay from "@/overlay/Overlay"
import OverlayRepository from "@/overlay/OverlayRepository"
import Resolution from "@/overlay/Resolution"
import router from "@/router"
import { v4 as uuidv4 } from "uuid"
import { inject, ref } from "vue"

function useOverlayList(repository: OverlayRepository) {
  const overlays = ref<Overlay[]>([])

  async function getOverlays() {
    overlays.value = await repository.list()
  }

  return {
    overlays,
    getOverlays,
  }
}

function useCreateOverlay(repository: OverlayRepository) {
  const isCreateModalOpened = ref(false)
  const addName = ref("")
  const addWidth = ref(1920)
  const addHeight = ref(1080)

  function openCreate() {
    isCreateModalOpened.value = true
  }

  async function createOverlay() {
    //TODO better validation
    if (!addName.value || !addWidth.value || !addHeight.value) {
      alert("missing field")
      return
    }

    const overlay = new Overlay(
      uuidv4(),
      addName.value,
      new Resolution(addWidth.value, addHeight.value),
    )

    await repository.save(overlay)
    await router.push({ path: `/overlay/${overlay.id}` })
  }

  return {
    isCreateModalOpened,
    openCreate,
    addName,
    addWidth,
    addHeight,
    createOverlay,
  }
}

export default {
  name: `OverlaysList`,
  components: { UiTextfield, UiButton, UiModal, UiCard, OverlayCard, UiCardPanel, UiPanel },
  setup() {
    const backendUrl = inject("backendUrl") as string
    const repository = new OverlayRepository(backendUrl)

    const { overlays, getOverlays: refresh } = useOverlayList(repository)

    refresh()

    async function deleteOverlay(overlay: Overlay) {
      await repository.deleteOverlay(overlay)
      //TODO notification card
      await refresh()
    }

    return {
      overlays,
      deleteOverlay,
      ...useCreateOverlay(repository),
    }
  },
}
</script>
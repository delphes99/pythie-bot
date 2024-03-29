<template>
  <ui-card-panel title="overlay.title">
    <template #header>
      <ui-button label="common.add" @on-click="openCreate()"/>
    </template>
    <overlay-card
        v-for="overlay in overlays"
        :key="overlay"
        :overlay="overlay"
        @deleted="deleteOverlay(overlay)"
    />
  </ui-card-panel>
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
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiCardPanel from "@/common/designSystem/card/ui-card-panel.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import UiModal from "@/common/designSystem/modal/ui-modal.vue"
import {useModal} from "@/common/designSystem/modal/useModal"
import {autowired} from "@/common/utils/injection.util";
import OverlayCard from "@/overlay/components/overlay-card.vue"
import Overlay from "@/overlay/Overlay"
import OverlayRepository from "@/overlay/OverlayRepository"
import Resolution from "@/overlay/Resolution"
import router from "@/router"
import {ref} from "vue"

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

const {isOpen: isCreateModalOpened, open: openCreate} = useModal()

function useCreateOverlay(repository: OverlayRepository) {
  const addName = ref("")
  const addWidth = ref(1920)
  const addHeight = ref(1080)

  async function createOverlay() {
    //TODO better validation
    if (!addName.value || !addWidth.value || !addHeight.value) {
      alert("missing field")
      return
    }

    const overlay = new Overlay(
        crypto.randomUUID(),
        addName.value,
        new Resolution(addWidth.value, addHeight.value),
    )

    await repository.save(overlay)
    await router.push({path: `/overlay/${overlay.id}`})
  }

  return {
    addName,
    addWidth,
    addHeight,
    createOverlay,
  }
}

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const repository = new OverlayRepository(backendUrl)

const {overlays, getOverlays: refresh} = useOverlayList(repository)

refresh()

async function deleteOverlay(overlay: Overlay) {
  await repository.deleteOverlay(overlay)
  //TODO notification card
  await refresh()
}

const {addName, addWidth, addHeight, createOverlay} =
    useCreateOverlay(repository)
</script>
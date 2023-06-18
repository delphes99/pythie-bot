<template>
  <div v-if="overlayNotFound">
    Overlay not found
  </div>
  <div v-else>
    <div
        v-for="element in overlay.elements"
        :key="element.id"
        class="absolute"
        :style="{
        left: element.general.left + 'px',
        top: element.general.top + 'px',
        'z-index': element.general.sortOrder,
      }"
    >
      <component
          :is="element.properties.renderComponent()"
          :component="element"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/main";
import Overlay from "@/overlay/Overlay"
import {fromJson} from "@/overlay/OverlayElement"
import {inject, ref} from "vue"

const backendUrl = inject(AppInjectionKeys.BACKEND_URL)

const overlay = ref<Overlay>()
const overlayNotFound = ref(true)

async function load() {
  let overlayId = getOverlayId()

  overlayNotFound.value = true
  if (overlayId) {
    fetchOverlay(overlayId).then(response => {
      overlay.value = response
      overlayNotFound.value = false
    })
  }
}

async function fetchOverlay(overlayId: string): Promise<Overlay> {
  const response = await fetch(`${backendUrl}/api/overlays/${overlayId}`)

  if (response.ok) {
    return response.json().then(data => {
      return new Overlay(
          data.id,
          data.name,
          data.resolution,
          data.elements
              .map((e: any) => fromJson(e))
              .filter((e: any) => e),
      )
    })
  } else {
    return Promise.reject("fetching error")
  }
}

function getOverlayId(): string | null {
  let uri = window.location.search.substring(1)
  let params = new URLSearchParams(uri)
  return params.get("id")
}

load()

</script>
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
        top: element.top + 'px',
        left: element.left + 'px',
        color: element.color,
      }"
    >
      {{ element.text }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { Overlay } from "@/overlay/Overlay"
import { inject, ref } from "vue"

const backendUrl = inject("backendUrl")

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
    return response.json()
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
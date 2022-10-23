<template>
  <link
    v-for="font in fonts"
    :key="font"
    :href="`https://fonts.googleapis.com/css2?family=${font.replaceAll(' ', '+')}&display=swap`"
    rel="stylesheet"
  >
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
        fontFamily: element.font,
        fontSize: element.fontSize + 'px',
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
const fonts = ref<string[]>([])

async function load() {
  let overlayId = getOverlayId()

  overlayNotFound.value = true
  if (overlayId) {
    fetchOverlay(overlayId).then(response => {
      overlay.value = response
      overlayNotFound.value = false
      fonts.value = [...new Set(response.elements.map(e => e.font))]
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
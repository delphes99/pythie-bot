<template>
  <div class="w-screen flex flex-row items-center justify-around menu-colors z-50">
    <div class="flex-grow">
      <h1 class="pl-2 text-2xl font-extrabold text-white">
        Pythie-bot
      </h1>
    </div>
    <div class="flex-grow-0 right-0 mx-3">
      <div
        class="flex flex-row-reverse items-center space-x-reverse space-x-3 h-16 px-5"
      >
        <connector-status
          v-for="status in statuses"
          :key="status.name"
          :connector="status.name"
          :status="status.status"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ConnectorEnum } from "@/common/components/common/connector/ConnectorEnum"
import ConnectorStatus from "@/common/components/common/connector/ConnectorStatus.vue"
import { ConnectorStatusEnum } from "@/common/components/common/connector/ConnectorStatusEnum"
import { inject, ref } from "vue"

interface Status {
  name: ConnectorEnum
  status: ConnectorStatusEnum
}

const backendUrl = inject("backendUrl")
const statuses = ref<Status[]>()

async function getStatus() {
  const response = await fetch(`${backendUrl}/connectors/status`)
  statuses.value = await response.json()
}

setInterval(() => {
  getStatus()
}, 2000)
</script>

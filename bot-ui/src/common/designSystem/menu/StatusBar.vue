<template>
  <div
      class="w-screen flex flex-row items-center justify-around bg-primaryContainerBackground text-primaryTextColor z-50 drop-shadow-lg">
    <div class="grow">
      <h1 class="pl-2 text-2xl font-extrabold text-backgroundTextColor">
        Pythie-bot
      </h1>
    </div>
    <div v-if="showThemeSwitcher">
      <theme-switcher/>
    </div>
    <div class="grow-0 right-0 mx-3">
      <div
          class="flex flex-row-reverse items-center space-x-reverse space-x-3 h-16 px-5"
      >
        <ConnectorStatus
            v-for="status in statuses"
            :key="status.name"
            :connector="status.name"
            :status="status.status"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import ThemeSwitcher from "@/common/style/ThemeSwitcher.vue";
import {useShowThemeSwitcher} from "@/common/style/useShowThemeSwitcher";
import {autowired} from "@/common/utils/Injection.util";
import ConnectorStatus from "@/connector/common/status/ConnectorStatus.vue"
import {ConnectorStatusEnum} from "@/connector/common/status/ConnectorStatusEnum"
import {ConnectorEnum} from "@/connector/ConnectorEnum"
import {ref} from "vue"

interface Status {
  name: ConnectorEnum
  status: ConnectorStatusEnum
}

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const statuses = ref<Status[]>([])

async function getStatus() {
  const response = await fetch(`${backendUrl}/connectors/status`)
  statuses.value = await response.json()
}

const {showThemeSwitcher} = useShowThemeSwitcher()
getStatus()
setInterval(() => {
  getStatus()
}, 2000)
</script>

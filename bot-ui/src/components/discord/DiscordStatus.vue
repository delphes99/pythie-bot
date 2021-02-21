<template>
  <div class="relative">
    <router-link to="/discord">
      <img src="@/assets/discord.png">
    </router-link>
    <div class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg" :class="[statusColor]" />
  </div>
</template>

<script lang="ts">
import { ref } from "vue"

enum DiscordStatus {
  unconfigured = "unconfigured",
  configured = "configured",
  error = "error",
  connected = "connected"
}
enum StatusColor {
  transparent = "border-0",
  grey = "border-8 border-grey-800",
  red = "border-8 border-red-800",
  green = "border-8 border-green-800"
}

interface Status {
  status: DiscordStatus;
}

export default {
  name: `DiscordStatus`,
  setup() {
    const status = ref<Status>({ status: DiscordStatus.unconfigured});
    const statusColor = ref<StatusColor>(StatusColor.transparent)

    async function getStatus() {
      const response = await fetch(`http://localhost:8080/status/discord`);
      status.value = await response.json();

      switch (status.value.status) {
        case DiscordStatus.unconfigured:
          statusColor.value = StatusColor.transparent
          break;
        case DiscordStatus.configured:
          statusColor.value = StatusColor.grey
          break;
        case DiscordStatus.connected:
          statusColor.value = StatusColor.green
          break;
        case DiscordStatus.error:
          statusColor.value = StatusColor.red
          break;
      }
    }

    setInterval(() => { getStatus() }, 5000)

    return {
      status,
      statusColor
    }
  }
}
</script>
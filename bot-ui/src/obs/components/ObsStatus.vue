<template>
  <div class="relative">
    <router-link to="/obs">
      <img src="@/common/assets/obs.svg" width="48" height="48" />
    </router-link>
    <div
      class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg"
      :class="[statusColor]"
    />
  </div>
</template>

<script lang="ts">
import { ref, inject } from "vue";

enum ObsStatus {
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
  status: ObsStatus;
}

export default {
  name: `ObsStatus`,

  setup() {
    const backendUrl = inject("backendUrl");
    const status = ref<Status>({ status: ObsStatus.unconfigured });
    const statusColor = ref<StatusColor>(StatusColor.transparent);

    async function getStatus() {
      const response = await fetch(`${backendUrl}/obs/status`);
      status.value = await response.json();

      switch (status.value.status) {
        case ObsStatus.unconfigured:
          statusColor.value = StatusColor.transparent;
          break;
        case ObsStatus.configured:
          statusColor.value = StatusColor.grey;
          break;
        case ObsStatus.connected:
          statusColor.value = StatusColor.green;
          break;
        case ObsStatus.error:
          statusColor.value = StatusColor.red;
          break;
      }
    }

    setInterval(() => {
      getStatus();
    }, 5000);

    return {
      status,
      statusColor
    };
  }
};
</script>

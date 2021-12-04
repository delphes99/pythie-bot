<template>
  <div
    class="w-screen flex flex-row items-center justify-around primary-color z-50"
  >
    <div class="flex-grow">
      <h1 class="pl-2 text-2xl font-extrabold text-white">Pythie-bot</h1>
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
        ></connector-status>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ConnectorEnum } from "@/common/components/common/connectorEnum.ts";
import { ConnectorStatusEnum } from "@/common/components/common/connectorStatus.ts";
import ConnectorStatus from "@/common/components/common/ConnectorStatus.vue";
import { defineComponent, inject, ref } from "vue";

interface Status {
  name: ConnectorEnum;
  status: ConnectorStatusEnum;
}

export default defineComponent({
  name: `StatusBar`,
  components: { ConnectorStatus },

  setup() {
    const backendUrl = inject("backendUrl");
    const statuses = ref<Status[]>();

    async function getStatus() {
      const response = await fetch(`${backendUrl}/connectors/status`);
      statuses.value = await response.json();
    }

    setInterval(() => {
      getStatus();
    }, 2000);

    return {
      statuses,
      ConnectorEnum
    };
  }
});
</script>

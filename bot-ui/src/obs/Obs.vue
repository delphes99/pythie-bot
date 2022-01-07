<template>
  <panel title="Status">
    <DetailedConnectorStatus :connector="connector" />
  </panel>
  <panel title="Obs configuration">
    <div class="grid grid-cols-2 gap-4 p-4">
      <label for="obs-ws-host">Obs-websocket Host</label>
      <input v-model="host" type="text" id="obs-ws-host" class="border-b-2" />
      <label for="obs-ws-host">Obs-websocket URL</label>
      <input v-model="port" type="text" id="obs-ws-port" class="border-b-2" />
      <label for="obs-password">
        Obs-websocket password (leave empty if no password)
      </label>
      <input
        v-model="password"
        type="password"
        id="obs-password"
        class="border-b-2"
      />
      <div class="flex col-span-2 justify-items-center justify-center">
        <button
          v-on:click="saveConfiguration"
          class="bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline"
        >
          Save
        </button>
      </div>
    </div>
  </panel>
</template>

<script lang="ts">
import { ConnectorEnum } from "@/common/components/common/connectorEnum";
import DetailedConnectorStatus from "@/common/components/common/DetailedConnectorStatus.vue";
import Panel from "@/common/components/common/Panel.vue";
import axios from "axios";
import { ElNotification } from "element-plus";
import { inject, ref } from "vue";
import { useI18n } from "vue-i18n";

export default {
  name: `ObsConfiguration`,
  components: { Panel, DetailedConnectorStatus },
  setup() {
    const { t } = useI18n();
    const backendUrl = inject("backendUrl");
    const host = ref("");
    const port = ref("");
    const password = ref("");

    const saveConfiguration = () => {
      const payload = {
        host: host.value,
        port: port.value,
        password: password.value
      };
      axios
        .post(`${backendUrl}/obs/configuration`, payload, {
          headers: { "Content-Type": "application/json" }
        })
        .then(() => {
          ElNotification({
            title: t("common.success"),
            type: "success"
          });
        })
        .catch(() => {
          ElNotification({
            title: t("common.error"),
            type: "error"
          });
        });
    };

    const refreshCurrentConfiguration = async () => {
      const response = await axios.get(`${backendUrl}/obs/configuration`);

      host.value = response.data.host;
      port.value = response.data.port;
    };

    refreshCurrentConfiguration();

    return {
      host,
      port,
      password,
      saveConfiguration,
      connector: ConnectorEnum.OBS
    };
  }
};
</script>

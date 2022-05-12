<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector" />
  </ui-panel>
  <ui-panel title="Obs configuration">
    <div class="grid grid-cols-2 gap-4 p-4">
      <ui-textfield
        v-model="host"
        label="obs.configuration.host"
      />
      <ui-textfield
        v-model="port"
        label="obs.configuration.port"
      />
      <label for="obs-password">
        Obs-websocket password (leave empty if no password)
      </label>
      <input
        id="obs-password"
        v-model="password"
        type="password"
        class="border-b-2"
      >
      <div class="flex col-span-2 justify-items-center justify-center">
        <button
          class="bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline"
          @click="saveConfiguration"
        >
          Save
        </button>
      </div>
    </div>
  </ui-panel>
</template>

<script setup lang="ts">
import { ConnectorEnum } from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()
const backendUrl = inject("backendUrl")
const host = ref("")
const port = ref("")
const password = ref("")

const saveConfiguration = () => {
  const payload = {
    host: host.value,
    port: port.value,
    password: password.value,
  }
  axios
    .post(`${backendUrl}/obs/configuration`, payload, {
      headers: { "Content-Type": "application/json" },
    })
    .then(() => {
      ElNotification({
        title: t("common.success"),
        type: "success",
      })
    })
    .catch(() => {
      ElNotification({
        title: t("common.error"),
        type: "error",
      })
    })
}

const refreshCurrentConfiguration = async () => {
  const response = await axios.get(`${backendUrl}/obs/configuration`)

  host.value = response.data.host
  port.value = response.data.port
}

refreshCurrentConfiguration()

const connector = ConnectorEnum.OBS
</script>
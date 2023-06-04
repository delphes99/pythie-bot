<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector"/>
  </ui-panel>
  <ui-panel title="Obs configuration">
    <ui-textfield
        v-model="host"
        label="obs.configuration.host"
    />
    <ui-textfield
        v-model="port"
        label="obs.configuration.port"
    />
    <ui-textfield
        v-model="password"
        label="obs.configuration.password"
        password="true"
    />
    <ui-button
        label="common.save"
        @on-click="saveConfiguration"
    />
  </ui-panel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import {autowired} from "@/common/utils/injection.util";
import DetailedConnectorStatus from "@/connector/common/status/detailed-connector-status.vue"
import {ConnectorEnum} from "@/connector/ConnectorEnum"
import axios from "axios"
import {ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()
const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)
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
        headers: {"Content-Type": "application/json"},
      })
      .then(() => {
        notificationService.success(t("common.success"))
      })
      .catch(() => {
        notificationService.error(t("common.error"))
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
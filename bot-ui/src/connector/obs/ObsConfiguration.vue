<template>
  <UiPanel title="Status">
    <DetailedConnectorStatus :connector="connector"/>
  </UiPanel>
  <UiPanel title="Obs configuration">
    <UiTextfield
        v-model="host"
        label="obs.configuration.host"
    />
    <UiTextfield
        v-model="port"
        label="obs.configuration.port"
    />
    <UiTextfield
        v-model="password"
        label="obs.configuration.password"
        :password="true"
    />
    <UiButton
        label="common.save"
        @on-click="saveConfiguration"
    />
  </UiPanel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue"
import UiPanel from "@/common/designSystem/panel/UiPanel.vue"
import {autowired} from "@/common/utils/Injection.util";
import DetailedConnectorStatus from "@/connector/common/status/DetailedConnectorStatus.vue"
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
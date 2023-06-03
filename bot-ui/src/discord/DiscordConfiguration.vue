<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector"/>
  </ui-panel>
  <ui-panel title="Discord configuration">
    <ui-textfield
        v-model="oAuthToken"
        label="discord.configuration.authToken"
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
import UiButton from "@/common/components/common/button/UiButton.vue"
import {ConnectorEnum} from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiTextfield from "@/common/components/common/form/textfield/UiTextfield.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import {autowired} from "@/common/utils/injection.util";
import axios from "axios"
import {ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()
const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)
const oAuthToken = ref("")

const saveConfiguration = () => {
  const payload = {oAuthToken: oAuthToken.value}
  axios
      .post(`${backendUrl}/discord/configuration`, payload, {
        headers: {"Content-Type": "application/json"},
      })
      .then(() => {
        notificationService.success(t("common.success"))
      })
      .catch(() => {
        notificationService.error(t("common.error"))
      })
}

const connector = ConnectorEnum.DISCORD
</script>
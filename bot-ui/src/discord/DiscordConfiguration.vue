<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector" />
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

<script setup lang="ts">
import { ConnectorEnum } from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiPanel from "@/ds/panel/UiPanel.vue"
import UiButton from "@/ds/button/UiButton.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()
const backendUrl = inject("backendUrl")
const oAuthToken = ref("")

const saveConfiguration = () => {
  const payload = { oAuthToken: oAuthToken.value }
  axios
    .post(`${backendUrl}/discord/configuration`, payload, {
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

const connector = ConnectorEnum.DISCORD
</script>
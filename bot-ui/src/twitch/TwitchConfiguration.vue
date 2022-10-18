<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector" />
  </ui-panel>
  <ui-panel title="Twitch bot configuration">
    <img
      class="inline-block align-middle"
      src="@/common/assets/refresh.svg"
      style="height: 20px"
      alt="Refresh configuration"
      @click="refreshCurrentConfiguration"
    >
    <h2 class="text-xl font-medium text-black">
      App credential
    </h2>
    <ui-textfield
      v-model="clientId"
      label="twitch.configuration.clientId"
    />
    <ui-textfield
      v-model="clientSecret"
      label="twitch.configuration.clientSecret"
      password="true"
    />
    <ui-button
      label="common.save"
      @on-click="saveAppCredential"
    />
    <h2 class="text-xl font-medium text-black">
      Bot identity
    </h2>
    <div v-if="botAccount">
      <div>
        Bot account : <span class="font-bold">{{ botAccount }}</span>
      </div>
      <ui-button
        label="twitch.configuration.changeBotAccount"
        :link="buildBotIdentityUrl()"
      />
    </div>
    <div v-else>
      <ui-button
        label="twitch.configuration.selectBotAccount"
        :link="buildBotIdentityUrl()"
      />
    </div>
  </ui-panel>
  <ui-panel title="twitch.channels">
    <ui-button
      label="twitch.configuration.addChannel"
      :link="buildAddChannelUrl()"
    />
    <div class="w-11/12 mx-auto">
      <div class="bg-white shadow-md rounded my-6">
        <table class="text-left w-full border-collapse">
          <thead>
            <tr>
              <th
                class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light"
              >
                Channel
              </th>
              <th
                class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light"
              >
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="channel in channels"
              :key="channel"
              class="hover:bg-grey-lighter"
            >
              <td class="py-4 px-6 border-b border-grey-light">
                <router-link :to="`/twitch/${channel}`">
                  {{ channel }}
                </router-link>
              </td>
              <td class="py-4 px-6 border-b border-grey-light">
                <ui-button
                  :type="UiButtonType.Warning"
                  label="common.delete"
                  @on-click="deleteChannel(channel)"
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </ui-panel>
</template>

<script setup lang="ts">
import { ConnectorEnum } from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import UiPanel from "@/ds/panel/UiPanel.vue"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()
const backendUrl = inject("backendUrl")

const clientId = ref("")
const clientSecret = ref("")
const botAccount = ref("")
const channels = ref([])

const refreshCurrentConfiguration = async () => {
  const response = await axios.get(`${backendUrl}/twitch/configuration`)

  clientId.value = response.data.clientId
  botAccount.value = response.data.botUsername
  channels.value = response.data.channels
}

refreshCurrentConfiguration()

const saveAppCredential = async () => {
  const payload = {
    clientId: clientId.value,
    clientSecret: clientSecret.value,
  }
  await axios.post(`${backendUrl}/twitch/configuration/appCredential`, payload, {
    headers: { "Content-Type": "application/json" },
  })

  ElNotification({
    title: t("common.success"),
    type: "success",
  })
}

const deleteChannel = async (channel: string) => {
  await axios.delete(`${backendUrl}/twitch/configuration/channel/` + channel)
  await refreshCurrentConfiguration()
}

const buildGetAuthUrl = (state: string) => () => {
  const twitchAuthUrl = "https://id.twitch.tv/oauth2/authorize"
  const params = new URLSearchParams()
  params.append("response_type", "code")
  params.append("client_id", clientId.value)
  params.append("redirect_uri", `${backendUrl}/twitch/configuration/userCredential`)
  params.append(
    "scope",
    "user:read:email bits:read channel:read:hype_train channel:read:subscriptions chat:read chat:edit whispers:edit channel:moderate channel:read:redemptions channel:manage:redemptions channel:manage:polls channel:manage:predictions",
  )
  params.append("state", state)

  return twitchAuthUrl + "?" + params.toString()
}

const buildBotIdentityUrl = buildGetAuthUrl("botConfiguration")
const buildAddChannelUrl = buildGetAuthUrl("addChannel")
const connector = ConnectorEnum.TWITCH
</script>
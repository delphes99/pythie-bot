<template>
  <ui-panel title="Status">
    <DetailedConnectorStatus :connector="connector"/>
  </ui-panel>
  <ui-panel title="Twitch bot configuration">
    <ui-icon
        name="refresh"
        alt="Refresh configuration"
        class="inline-block align-middle w-4 h-4 fill-backgroundTextColor"
        @click="refreshCurrentConfiguration"
    />
    <h2 class="text-xl font-medium">
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
    <h2 class="text-xl font-medium">
      Bot identity
    </h2>
    <div v-if="botAccount">
      <div>
        Bot account : <span class="font-bold">{{ botAccount }}</span>
      </div>
      <ui-button
          :link="buildBotIdentityUrl()"
          label="twitch.configuration.changeBotAccount"
      />
    </div>
    <div v-else>
      <ui-button
          :link="buildBotIdentityUrl()"
          label="twitch.configuration.selectBotAccount"
      />
    </div>
  </ui-panel>
  <ui-panel title="twitch.channels" :menu="[UiPanelMenuItem.of('twitch.configuration.addChannel', addChannel)]">
    <ui-table
        :data="channels"
    >
      <ui-table-column header-name="twitch.channel" v-slot="{item:channel}">
        <router-link :to="`/twitch/${channel}`">
          {{ channel }}
        </router-link>
      </ui-table-column>
      <ui-table-column header-name="common.actions" v-slot="{item}">
        <ui-button
            :type="UiButtonType.Warning"
            label="common.delete"
            @on-click="deleteChannel(item)"
        />
      </ui-table-column>
    </ui-table>
  </ui-panel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButtonType from "@/common/designSystem/button/ui-button.type"
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import UiIcon from "@/common/designSystem/icons/ui-icon.vue";
import {UiPanelMenuItem} from "@/common/designSystem/panel/ui-panel.menu.item";
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import UiTableColumn from "@/common/designSystem/table/ui-table-column.vue";
import UiTable from "@/common/designSystem/table/ui-table.vue";
import {autowired} from "@/common/utils/injection.util";
import DetailedConnectorStatus from "@/connector/common/status/detailed-connector-status.vue"
import {ConnectorEnum} from "@/connector/ConnectorEnum"
import axios from "axios"
import {ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()
const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)

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
    headers: {"Content-Type": "application/json"},
  })

  notificationService.success(t("common.success"))
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
      "user:read:email bits:read channel:read:hype_train channel:read:subscriptions chat:read chat:edit whispers:edit channel:moderate channel:read:redemptions channel:manage:redemptions channel:manage:polls channel:manage:predictions channel:manage:vips channel:manage:moderators moderator:manage:shoutouts moderator:read:followers",
  )
  params.append("state", state)

  return twitchAuthUrl + "?" + params.toString()
}

const buildBotIdentityUrl = buildGetAuthUrl("botConfiguration")

function addChannel() {
  window.open(buildGetAuthUrl("addChannel")())
}

const connector = ConnectorEnum.TWITCH
</script>
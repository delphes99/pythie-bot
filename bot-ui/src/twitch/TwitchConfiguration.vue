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
    <label for="clientSecret">Client Secret</label>
    <input
      id="clientSecret"
      v-model="clientSecret"
      type="password"
      class="border-b-2"
    >
    <div>
      <button
        class="primary-button focus:shadow-outline"
        @click="saveAppCredential"
      >
        Save
      </button>
    </div>
    <h2 class="text-xl font-medium text-black">
      Bot identity
    </h2>
    <div v-if="botAccount">
      <div>
        Bot account : <span class="font-bold">{{ botAccount }}</span>
      </div>
      <a
        :href="buildBotIdentityUrl()"
        class="primary-button focus:shadow-outline"
      >
        Change bot account
      </a>
    </div>
    <div v-else>
      <a
        :href="buildBotIdentityUrl()"
        class="primary-button focus:shadow-outline"
      >
        Connect bot account
      </a>
    </div>
  </ui-panel>
  <ui-panel title="Channels">
    <div>
      <a
        :href="buildAddChannelUrl()"
        class="primary-button focus:shadow-outline"
      >
        Add channel
      </a>
    </div>
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
                <button
                  class="secondary-button focus:shadow-outline"
                  @click="deleteChannel(channel)"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </ui-panel>
</template>

<script lang="ts">
import { ConnectorEnum } from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

export default {
  name: `TwitchConfiguration`,
  components: { UiTextfield, DetailedConnectorStatus, UiPanel },
  setup() {
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

    return {
      clientId,
      clientSecret,
      botAccount,
      channels,
      saveAppCredential,
      deleteChannel,
      buildBotIdentityUrl,
      buildAddChannelUrl,
      refreshCurrentConfiguration,
      connector: ConnectorEnum.TWITCH,
    }
  },
}
</script>
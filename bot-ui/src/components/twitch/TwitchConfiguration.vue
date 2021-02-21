<template>
  <panel title="Twitch bot configuration">
    <img class="inline-block align-middle" src="@/assets/refresh.svg" style="height: 20px"
         v-on:click="refreshCurrentConfiguration"/>
    <h2 class="text-xl font-medium text-black">App credential</h2>
    <div class="px-2 grid grid-cols-2 space-y-1">
      <label for="clientId">Client Id</label>
      <input v-model="clientId" type="text" id="clientId" class="border-b-2">
      <label for="clientSecret">Client Secret</label>
      <input v-model="clientSecret" type="password" id="clientSecret" class="border-b-2">
    </div>
    <div>
      <button
          v-on:click="saveAppCredential"
          class="primary-button focus:shadow-outline"
      >
        Save
      </button>
    </div>
    <h2 class="text-xl font-medium text-black">Bot identity</h2>
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
  </panel>
  <panel title="Channels">
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
            <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light">
              Channel
            </th>
            <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light">
              Actions
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="channel in channels" :key="channel" class="hover:bg-grey-lighter">
            <td class="py-4 px-6 border-b border-grey-light">
              <router-link :to="`/twitch/${channel}`">
                {{ channel }}
              </router-link>
            </td>
            <td class="py-4 px-6 border-b border-grey-light">
              <button
                  v-on:click="deleteChannel(channel)"
                  class="secondary-button focus:shadow-outline"
              >
                Delete
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </panel>
</template>

<script lang="ts">
import {ref} from 'vue'
import axios from "axios";
import Panel from "@/components/common/Panel.vue";

export default {
  name: `TwitchConfiguration`,
  components: {Panel},
  setup() {
    //TODO inject back url
    const backUrl = 'http://localhost:8080'

    const clientId = ref("")
    const clientSecret = ref("")
    const botAccount = ref("")
    const channels = ref([])

    const refreshCurrentConfiguration = async () => {
      const response = await axios.get(backUrl + '/twitch/configuration')

      clientId.value = response.data.clientId
      botAccount.value = response.data.botUsername
      channels.value = response.data.channels
    }

    refreshCurrentConfiguration()

    const saveAppCredential = async () => {
      const payload = {
        clientId: clientId.value,
        clientSecret: clientSecret.value
      }
      const response = await axios.post(backUrl + '/twitch/configuration/appCredential', payload, {
        headers: {'Content-Type': 'application/json'}
      })

      //TODO better modal
      alert("OK")
    }

    const deleteChannel = async (channel: string) => {
      await axios.delete(backUrl + '/twitch/configuration/channel/' + channel)
      await refreshCurrentConfiguration()
    }

    const buildGetAuthUrl = (state: string) => () => {
      const twitchAuthUrl = "https://id.twitch.tv/oauth2/authorize"
      const params = new URLSearchParams()
      params.append('response_type', 'code',)
      params.append('client_id', clientId.value)
      params.append('redirect_uri', backUrl + '/twitch/configuration/userCredential',)
      params.append('scope', 'user:read:email bits:read channel:read:hype_train channel:read:subscriptions chat:read chat:edit whispers:edit channel:moderate channel:read:redemptions channel:manage:redemptions',)
      params.append('state', state)

      return twitchAuthUrl + '?' + params.toString()
    }

    const buildBotIdentityUrl = buildGetAuthUrl('botConfiguration')
    const buildAddChannelUrl = buildGetAuthUrl('addChannel')

    return {
      clientId,
      clientSecret,
      botAccount,
      channels,
      saveAppCredential,
      deleteChannel,
      buildBotIdentityUrl,
      buildAddChannelUrl,
      refreshCurrentConfiguration
    }
  }
}
</script>
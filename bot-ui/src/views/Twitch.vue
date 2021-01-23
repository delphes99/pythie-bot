<template>
  <div class="m-3 w-full">
    <div class="border-b border-2">
      <h1 class="text-xl font-medium text-black title-font primary-color">Twitch bot configuration</h1>
      <h2>App credential</h2>
      <div class="px-2 grid grid-cols-2 auto-cols-min">
        <label for="clientId">Client Id</label>
        <input v-model="clientId" type="text" id="clientId" class="border-b-2">
        <label for="clientSecret">Client Secret</label>
        <input v-model="clientSecret" type="password" id="clientSecret" class="border-b-2">
        <div>
          <button v-on:click="saveAppCredential"
                  class="col-span-2 bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline">
            Save
          </button>
        </div>
      </div>
      <h2>Bot identity</h2>
      <div>
        <div v-if="botAccount">
          Bot account : {{botAccount}}
          <a :href="buildBotIdentityUrl()">Change bot account</a>
        </div>
        <div v-else>
          <a :href="buildBotIdentityUrl()">Connect bot account</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {ref} from 'vue'
import axios from "axios";

export default {
  name: `TwitchConfiguration`,
  setup() {
    const clientId = ref("")
    const clientSecret = ref("")
    const botAccount = ref("")

    const refreshCurrentConfiguration = async () => {
      const response = await axios.get('http://localhost:8080/twitch/configuration')

      clientId.value = response.data.clientId
      botAccount.value = response.data.botUsername
    }

    refreshCurrentConfiguration()

    const saveAppCredential = async () => {
      const payload = {
        clientId: clientId.value,
        clientSecret: clientSecret.value
      }
      //TODO inject back url
      const response = await axios.post('http://localhost:8080/twitch/configuration/appCredential', payload, {
        headers: {'Content-Type': 'application/json'}
      })

      //TODO better modal
      alert("OK")
    }

    const buildBotIdentityUrl = () => {
      const twitchAuthUrl = "https://id.twitch.tv/oauth2/authorize"
      const params = new URLSearchParams()
      params.append("response_type", "code",)
      params.append("client_id", clientId.value)
      params.append("redirect_uri", "http://localhost:8080/twitch/configuration/userCredential",)
      params.append("scope", "user:read:email bits:read channel:read:hype_train channel:read:subscriptions chat:read channel:moderate channel:read:redemptions channel:manage:redemptions",)
      params.append("state", "botConfiguration")

      return twitchAuthUrl + "?" + params.toString()
    }

    return {
      clientId,
      clientSecret,
      botAccount,
      saveAppCredential,
      buildBotIdentityUrl
    }
  }
}
</script>
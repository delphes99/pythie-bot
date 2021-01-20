<template>
  <div class="m-3 w-full">
    <div class="border-b border-2">
      <h1 class="text-xl font-medium text-black title-font primary-color">Discord configuration</h1>
      <div class="px-2 space-x-2">
        <label for="discordOauth">Oauth bot token</label>
        <input v-model="oAuthToken" type="password" id="discordOauth" class="border-b-2">
        <button v-on:click="saveOauthToken"
                class="bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline">
          Save
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import axios from "axios";

export default {
  name: `DiscordConfiguration`,
  setup() {
    const oAuthToken = ref("")

    const saveOauthToken = () => {
      const payload = { oAuthToken: oAuthToken.value }
      //TODO inject back url
      axios.post('http://localhost:8080/discord/configuration', payload, {
        headers: {'Content-Type': 'application/json'}
      })
          .then(function (response) {
            //TODO better modal
            alert("OK")
          })
          .catch(function (error) {
            //TODO better modal
            alert("KO")
          });
    }

    return {
      oAuthToken,
      saveOauthToken
    }
  }
}
</script>
<template>
  <div class="m-3 w-full">
    <panel title="Discord configuration">
      <label for="discordOauth">Oauth bot token</label>
      <input v-model="oAuthToken" type="password" id="discordOauth" class="border-b-2">
      <button v-on:click="saveOauthToken"
              class="bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline">
        Save
      </button>
    </panel>
  </div>
</template>

<script lang="ts">
import {inject, ref} from 'vue'
import axios from "axios";
import Panel from "@/components/common/Panel.vue";

export default {
  name: `DiscordConfiguration`,
  components: {Panel},
  setup() {
    const backendUrl = inject('backendUrl')
    const oAuthToken = ref("")

    const saveOauthToken = () => {
      const payload = { oAuthToken: oAuthToken.value }
      axios.post(`${backendUrl}/discord/configuration`, payload, {
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
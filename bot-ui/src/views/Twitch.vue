<template>
  <div class="m-3 w-full">
    <div class="border-b border-2">
      <h1 class="text-xl font-medium text-black title-font primary-color">Twitch bot configuration</h1>
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
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import axios from "axios";

export default {
  name: `TwitchConfiguration`,
  setup() {
    const clientId = ref("")
    const clientSecret = ref("")

    const saveAppCredential = () => {
      const payload = {
        clientId: clientId.value,
        clientSecret: clientSecret.value
      }
      //TODO inject back url
      axios.post('http://localhost:8080/twitch/configuration/appCredential', payload, {
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
      clientId,
      clientSecret,
      saveAppCredential
    }
  }
}
</script>
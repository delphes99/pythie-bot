<template>
  <panel title="Obs configuration">
    <div class="grid grid-cols-2 gap-4 p-4">
      <label for="obs-ws-url">Obs-websocket URL</label>
      <input v-model="url" type="text" id="obs-ws-url" class="border-b-2" />
      <label for="obs-password">
        Obs-websocket password (leave empty if no password)
      </label>
      <input
        v-model="password"
        type="password"
        id="obs-password"
        class="border-b-2"
      />
      <div class="flex col-span-2 justify-items-center justify-center">
        <button
          v-on:click="saveConfiguration"
          class="bg-indigo-500 text-white rounded-md px-3 py-1 m-2 transition duration-500 ease select-none hover:bg-indigo-600 focus:outline-none focus:shadow-outline"
        >
          Save
        </button>
      </div>
    </div>
  </panel>
</template>

<script lang="ts">
import { inject, ref } from "vue";
import axios from "axios";
import Panel from "@/common/components/common/Panel.vue";

export default {
  name: `ObsConfiguration`,
  components: { Panel },
  setup() {
    const backendUrl = inject("backendUrl");
    const url = ref("");
    const password = ref("");

    const saveConfiguration = () => {
      const payload = { url: url.value, password: password.value };
      axios
        .post(`${backendUrl}/obs/configuration`, payload, {
          headers: { "Content-Type": "application/json" }
        })
        .then(function(response) {
          //TODO better modal
          alert("OK");
        })
        .catch(function(error) {
          //TODO better modal
          alert("KO");
        });
    };

    return {
      url,
      password,
      saveConfiguration
    };
  }
};
</script>

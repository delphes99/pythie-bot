<template>
  <div class="flex flex-col space-y-2">
    <label for="send-channel-receiver">Listened channel</label>
    <input
      type="text"
      id="send-channel-receiver"
      v-model="featurecopy.channel"
      class="outgoing-event-text"
    />
    <label for="send-message-trigger">Trigger on</label>
    <input
      type="text"
      id="send-message-trigger"
      v-model="featurecopy.trigger"
      class="outgoing-event-text"
    />
    <div
      v-for="event in featurecopy.responses"
      :key="event.type"
      class="p-1 border border-black"
    >
      <h2>Send message</h2>
      <div class="ml-10 flex flex-col">
        <label for="send-message-text">Message</label>
        <input
          type="text"
          id="send-message-text"
          v-model="event.text"
          class="outgoing-event-text"
        />
        <label for="send-message-channel">Channel</label>
        <input
          type="text"
          id="send-message-channel"
          v-model="event.channel"
          class="outgoing-event-text"
        />
      </div>
    </div>
  </div>

  <button v-on:click="save" class="primary-button">Save</button>
</template>
<script type="ts">
import TwitchCommand from "@/twitch/feature/type/TwitchCommand.ts"
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: 'feature-command-edit',
  props: {
    feature: TwitchCommand
  },
  setup(props) {

    const featurecopy = ref({...props.feature})
    const save = () => {
      console.log("save")
      console.log(featurecopy.value)
    }

    return {
      featurecopy,
      save
    }
  }
})
</script>

<style scoped>
.outgoing-event-text {
  @apply ml-5 bg-white py-2 px-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 focus:outline-none focus:ring-2;
}
</style>

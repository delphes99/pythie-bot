<template>
  <card :title="overlay.title">
    {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
    <template v-slot:actions>
      <router-link :to="`/overlay/${overlay.id}`">
        <a class="primary-button focus:shadow-outline">
          Edit
        </a>
      </router-link>
      <button class="secondary-button" @click="$emit('deleted')">
        Delete
      </button>
    </template>
  </card>
</template>

<script lang="ts">
import Card from "@/common/components/common/Card.vue";
import Overlay from "@/overlay/Overlay.ts";
import { defineComponent, PropType } from "vue";

export default defineComponent({
  name: "OverlayCard",
  components: {
    Card
  },
  emits: ["deleted"],
  props: {
    overlay: {
      type: Object as PropType<Overlay>,
      required: true
    }
  }
});
</script>

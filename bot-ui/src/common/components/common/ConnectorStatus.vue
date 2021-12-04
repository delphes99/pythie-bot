<template>
  <div class="relative">
    <router-link :to="link">
      <img :src="image" width="48" height="48" :alt="connector" />
    </router-link>
    <div
      class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg"
      :class="[statusColor]"
    />
  </div>
</template>

<script lang="ts">
import discordImage from "@/common/assets/discord.png";
import obsImage from "@/common/assets/obs.svg";
import twitchImage from "@/common/assets/twitch.svg";
import { ConnectorEnum } from "@/common/components/common/connectorEnum.ts";
import { ConnectorStatusEnum } from "@/common/components/common/connectorStatus.ts";
import { computed, defineComponent } from "vue";

enum StatusColor {
  transparent = "border-0",
  grey = "border-8 border-grey-800",
  red = "border-8 border-red-800",
  green = "border-8 border-green-800",
  orange = "border-8 border-orange-800",
  yellow = "border-8 border-yellow-800"
}

function toImage(connector: ConnectorEnum) {
  switch (connector) {
    case ConnectorEnum.TWITCH:
      return twitchImage;
    case ConnectorEnum.DISCORD:
      return discordImage;
    case ConnectorEnum.OBS:
      return obsImage;
    default:
      throw new Error("Unknown connector");
  }
}

function toStatusColor(status: ConnectorStatusEnum): StatusColor {
  switch (status) {
    case ConnectorStatusEnum.unconfigured:
      return StatusColor.transparent;
    case ConnectorStatusEnum.configured:
      return StatusColor.grey;
    case ConnectorStatusEnum.connected:
      return StatusColor.green;
    case ConnectorStatusEnum.inError:
      return StatusColor.red;
    case ConnectorStatusEnum.connecting:
      return StatusColor.yellow;
    case ConnectorStatusEnum.disconnecting:
      return StatusColor.orange;
    default:
      return StatusColor.transparent;
  }
}

function toLink(connector: ConnectorEnum): string {
  switch (connector) {
    case ConnectorEnum.TWITCH:
      return "/twitch";
    case ConnectorEnum.DISCORD:
      return "/discord";
    case ConnectorEnum.OBS:
      return "/obs";
    default:
      throw new Error("Unknown connector");
  }
}

export default defineComponent({
  name: `ConnectorStatus`,
  props: {
    connector: {
      type: String as () => ConnectorEnum,
      required: true
    },
    status: {
      type: String as () => ConnectorStatusEnum,
      required: true
    }
  },
  setup(props) {
    const statusColor = computed(() => toStatusColor(props.status));
    const image = computed(() => toImage(props.connector));
    const link = computed(() => toLink(props.connector));

    return {
      statusColor,
      image,
      link
    };
  }
});
</script>

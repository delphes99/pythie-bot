<template>
  <el-dropdown trigger="contextmenu">
    <div class="relative">
      <router-link :to="link">
        <img
          :src="image"
          width="48"
          height="48"
          :alt="connector"
        >
      </router-link>
      <div
        class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg"
        :class="[statusColor]"
      />
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="action in actions"
          :key="action.label"
          @click="doAction(action)"
        >
          {{ action.label }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script lang="ts">
import discordImage from "@/common/assets/discord.png"
import obsImage from "@/common/assets/obs.svg"
import twitchImage from "@/common/assets/twitch.svg"
import { ConnectorEnum } from "@/common/components/common/connectorEnum"
import { ConnectorStatusEnum } from "@/common/components/common/connectorStatus"
import axios from "axios"
import { ElNotification } from "element-plus"
import { computed, defineComponent, inject } from "vue"
import { useI18n } from "vue-i18n"

enum StatusColor {
  transparent = "border-0",
  grey = "border-8 border-grey-800",
  red = "border-8 border-red-800",
  green = "border-8 border-green-800",
  orange = "border-8 border-orange-800",
  yellow = "border-8 border-yellow-800",
}

interface DropDownAction {
  label: string
  url: string
}

function toActions(
  connector: ConnectorEnum,
  status: ConnectorStatusEnum,
  backendUrl: string,
  t: (key: string) => string,
): DropDownAction[] {
  const CONNECT_ACTION: DropDownAction = {
    label: t("common.connect"),
    url: `${backendUrl}/connectors/${connector}/connect`,
  }
  const DISCONNECT_ACTION: DropDownAction = {
    label: t("common.disconnect"),
    url: `${backendUrl}/connectors/${connector}/disconnect`,
  }

  switch (status) {
    case ConnectorStatusEnum.configured:
    case ConnectorStatusEnum.inError:
      return [CONNECT_ACTION]
    case ConnectorStatusEnum.connected:
      return [DISCONNECT_ACTION]
    case ConnectorStatusEnum.unconfigured:
    case ConnectorStatusEnum.connecting:
    case ConnectorStatusEnum.disconnecting:
    default:
      return []
  }
}

function toImage(connector: ConnectorEnum) {
  switch (connector) {
    case ConnectorEnum.TWITCH:
      return twitchImage
    case ConnectorEnum.DISCORD:
      return discordImage
    case ConnectorEnum.OBS:
      return obsImage
    default:
      throw new Error("Unknown connector")
  }
}

function toStatusColor(status: ConnectorStatusEnum): StatusColor {
  switch (status) {
    case ConnectorStatusEnum.unconfigured:
      return StatusColor.transparent
    case ConnectorStatusEnum.configured:
      return StatusColor.grey
    case ConnectorStatusEnum.connected:
      return StatusColor.green
    case ConnectorStatusEnum.inError:
      return StatusColor.red
    case ConnectorStatusEnum.connecting:
      return StatusColor.yellow
    case ConnectorStatusEnum.disconnecting:
      return StatusColor.yellow
    case ConnectorStatusEnum.mixed:
      return StatusColor.orange
    default:
      return StatusColor.transparent
  }
}

function toLink(connector: ConnectorEnum): string {
  switch (connector) {
    case ConnectorEnum.TWITCH:
      return "/twitch"
    case ConnectorEnum.DISCORD:
      return "/discord"
    case ConnectorEnum.OBS:
      return "/obs"
    default:
      throw new Error("Unknown connector")
  }
}

export default defineComponent({
  name: `ConnectorStatus`,
  props: {
    connector: {
      type: String as () => ConnectorEnum,
      required: true,
    },
    status: {
      type: String as () => ConnectorStatusEnum,
      required: true,
    },
  },
  setup(props) {
    const { t } = useI18n()
    const backendUrl = inject("backendUrl") as string
    const statusColor = computed(() => toStatusColor(props.status))
    const image = computed(() => toImage(props.connector))
    const link = computed(() => toLink(props.connector))
    const actions = computed(() =>
      toActions(props.connector, props.status, backendUrl, t),
    )

    const doAction = (action: DropDownAction) => {
      axios
        .post(action.url, null, {
          headers: { "Content-Type": "application/json" },
        })
        .then(() => {
          ElNotification({
            title: t("common.success"),
            type: "success",
          })
        })
        .catch(() => {
          ElNotification({
            title: t("common.error"),
            type: "error",
          })
        })
    }

    return {
      statusColor,
      image,
      link,
      actions,
      doAction,
    }
  },
})
</script>

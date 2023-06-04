<template>
  <Menu as="div" class="relative inline-block text-left">
    <MenuButton>
      <div>
        <img
            :alt="connector"
            :src="image"
            height="48"
            width="48"
        >
        <div
            :class="[statusColor]"
            class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg"
        />
      </div>
    </MenuButton>
    <MenuItems
        class="absolute right-0 mt-2 w-56 origin-top-right divide-y-4 divide-primaryColor bg-primaryColor shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
      <MenuItem v-slot="{ active }"
                v-for="action in actions"
                :key="action.label">
        <router-link :class="[
                      active ? 'bg-primaryColorHover text-primaryTextColor' : 'bg-backgroundColor text-backgroundTextColor',
                      'group flex w-full items-center px-2 py-2 text-sm',
                    ]"
                     :to="link">
          {{ $t("common.configuration") }}
        </router-link>
      </MenuItem>
      <MenuItem v-slot="{ active }"
                v-for="action in actions"
                :key="action.label">
        <a :class="[
                      active ? 'bg-primaryColorHover text-primaryTextColor' : 'bg-backgroundColor text-backgroundTextColor',
                      'group flex w-full items-center px-2 py-2 text-sm',
                    ]"
           @click="doAction(action)">
          {{ action.label }}
        </a>
      </MenuItem>
    </MenuItems>
  </Menu>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import discordImage from "@/common/assets/discord.png"
import obsImage from "@/common/assets/obs.svg"
import twitchImage from "@/common/assets/twitch.svg"
import {autowired} from "@/common/utils/injection.util";
import {StatusColor} from "@/connector/common/status/connector.status.color"
import {ConnectorStatusEnum} from "@/connector/common/status/connector.status.enum"
import {ConnectorEnum} from "@/connector/ConnectorEnum"
import {Menu, MenuButton, MenuItem, MenuItems} from '@headlessui/vue'
import axios from "axios"
import {computed} from "vue"
import {useI18n} from "vue-i18n"

const props = defineProps({
  connector: {
    type: String as () => ConnectorEnum,
    required: true,
  },
  status: {
    type: String as () => ConnectorStatusEnum,
    required: true,
  },
})

const {t} = useI18n()

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)

const statusColor = computed(() => StatusColor.of(props.status))
const image = computed(() => toImage(props.connector))
const link = computed(() => toLink(props.connector))
const actions = computed(() =>
    toActions(props.connector, props.status, backendUrl, t),
)

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

const doAction = (action: DropDownAction) => {
  axios
      .post(action.url, null, {
        headers: {"Content-Type": "application/json"},
      })
      .then(() => {
        notificationService.success(t("common.success"))
      })
      .catch(() => {
        notificationService.success(t("common.error"))
      })
}

interface DropDownAction {
  label: string
  url: string
}
</script>
<template>
    <el-dropdown trigger="contextmenu">
        <div class="relative">
            <router-link :to="link">
                <img
                        :alt="connector"
                        :src="image"
                        height="48"
                        width="48"
                >
            </router-link>
            <div
                    :class="[statusColor]"
                    class="absolute -bottom-1 -right-1 z-2 rounded-full shadow-lg"
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

<script lang="ts" setup>
import discordImage from "@/common/assets/discord.png"
import obsImage from "@/common/assets/obs.svg"
import twitchImage from "@/common/assets/twitch.svg"
import {ConnectorEnum} from "@/common/components/common/connector/ConnectorEnum"
import {ConnectorStatusEnum} from "@/common/components/common/connector/ConnectorStatusEnum"
import {DropDownAction} from "@/common/components/common/connector/DropDownAction"
import {StatusColor} from "@/common/components/common/connector/StatusColor"
import axios from "axios"
import {ElNotification} from "element-plus"
import {computed, inject} from "vue"
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
const backendUrl = inject("backendUrl") as string
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
</script>
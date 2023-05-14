<template>
    <ui-panel title="Status">
        <DetailedConnectorStatus :connector="connector"/>
    </ui-panel>
    <ui-panel title="Discord configuration">
        <ui-textfield
                v-model="oAuthToken"
                label="discord.configuration.authToken"
                password="true"
        />
        <ui-button
                label="common.save"
                @on-click="saveConfiguration"
        />
    </ui-panel>
</template>

<script lang="ts" setup>
import UiButton from "@/common/components/common/button/UiButton.vue"
import {ConnectorEnum} from "@/common/components/common/connector/ConnectorEnum"
import DetailedConnectorStatus from "@/common/components/common/connector/DetailedConnectorStatus.vue"
import UiTextfield from "@/common/components/common/form/textfield/UiTextfield.vue"
import {NotificationService} from "@/common/components/common/notification/notification.service";
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import {InjectionKeys} from "@/injection.keys";
import axios from "axios"
import {inject, ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()
const backendUrl = inject(InjectionKeys.BACKEND_URL) as string
const notificationService = inject(InjectionKeys.NOTIFICATION_SERVICE) as NotificationService
const oAuthToken = ref("")

const saveConfiguration = () => {
    const payload = {oAuthToken: oAuthToken.value}
    axios
        .post(`${backendUrl}/discord/configuration`, payload, {
            headers: {"Content-Type": "application/json"},
        })
        .then(() => {
            notificationService.success(t("common.success"))
        })
        .catch(() => {
            notificationService.error(t("common.error"))
        })
}

const connector = ConnectorEnum.DISCORD
</script>
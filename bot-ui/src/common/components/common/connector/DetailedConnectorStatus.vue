<template>
    <ui-table
            :data="statusesTableData"
            :empty-message="$t('common.noData')"
    />
</template>

<script lang="ts" setup>
import {ConnectorEnum} from "@/common/components/common/connector/connectorEnum"
import {ConnectorStatusEnum} from "@/common/components/common/connector/connectorStatusEnum"
import {ColumnDefinition} from "@/common/components/common/table/ColumnDefinition"
import {TableData} from "@/common/components/common/table/TableData"
import UiTable from "@/common/components/common/table/UiTable.vue"
import {InjectionKeys} from "@/injection.keys";
import {inject, ref} from "vue"
import {useI18n} from "vue-i18n"

interface Status {
    name: ConnectorEnum
    status: ConnectorStatusEnum
    subStatus: Status[]
}

interface SubStatus {
    name: ConnectorEnum
    status: string
}

const props = defineProps({
    connector: {
        type: String as () => ConnectorEnum,
        required: true,
    },
})

const {t} = useI18n()
const backendUrl = inject(InjectionKeys.BACKEND_URL) as string

const statusesTableData = ref<TableData<SubStatus> | null>(null)

async function getStatus() {
    const response = await fetch(`${backendUrl}/connectors/status`)
    const allStatus: Status[] = await response.json()
    const connectorStatus = allStatus.filter((s) => s.name === props.connector)[0]

    statusesTableData.value = new TableData(
        [
            {
                name: props.connector,
                status: t("connector.status." + connectorStatus.status),
            },
            ...connectorStatus.subStatus.map((sub) => ({
                name: sub.name,
                status: t("connector.status." + sub.status),
            })),
        ],
        [
            new ColumnDefinition<SubStatus>(
                t("connector.connectionName"),
                (data: SubStatus) => data.name,
            ),
            new ColumnDefinition<SubStatus>(
                t("connector.connectionStatus"),
                (data: SubStatus) => data.status,
            ),
        ],
    )
}

getStatus()

setInterval(() => {
    getStatus()
}, 2000)
</script>
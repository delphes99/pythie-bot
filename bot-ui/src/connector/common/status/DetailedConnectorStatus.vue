<template>
  <UiTable
      :data="statusesTableData"
  >
    <UiTableColumn header-name="connector.connectionName" property-name="name"/>
    <UiTableColumn header-name="connector.connectionStatus" property-name="status"/>
  </UiTable>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiTable from "@/common/designSystem/table/UiTable.vue"
import UiTableColumn from "@/common/designSystem/table/UiTableColumn.vue";
import {autowired} from "@/common/utils/Injection.util";
import {ConnectorStatusEnum} from "@/connector/common/status/ConnectorStatusEnum"
import {ConnectorEnum} from "@/connector/ConnectorEnum"
import {ref} from "vue"
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
const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)

const statusesTableData = ref<SubStatus[]>()

async function getStatus() {
  const response = await fetch(`${backendUrl}/connectors/status`)
  const allStatus: Status[] = await response.json()
  const connectorStatus = allStatus.filter((s) => s.name === props.connector)[0]

  statusesTableData.value = [
    {
      name: props.connector,
      status: t("connector.status." + connectorStatus.status),
    },
    ...connectorStatus.subStatus.map((sub) => ({
      name: sub.name,
      status: t("connector.status." + sub.status),
    })),
  ]
}

await getStatus()

setInterval(() => {
  getStatus()
}, 2000)
</script>
<template>
  <ui-table
      :data="statusesTableData"
  >
    <ui-table-column header-name="connector.connectionName" property-name="name"/>
    <ui-table-column header-name="connector.connectionStatus" property-name="status"/>
  </ui-table>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import {ConnectorEnum} from "@/common/components/common/connector/connectorEnum"
import {ConnectorStatusEnum} from "@/common/components/common/connector/connectorStatusEnum"
import UiTableColumn from "@/common/components/common/table/ui-table-column.vue";
import UiTable from "@/common/components/common/table/ui-table.vue"
import {autowired} from "@/common/utils/injection.util";
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
<template>
  <ui-panel :title="$t('medias.add')">
    <form>
      <label for="file">{{ $t("medias.file") }}</label>
      <input
        id="file"
        ref=""
        type="file"
        name="file"
        @change="selectFile"
      >
      <label for="filename">{{ $t("medias.filename") }}</label>
      <input
        id="filename"
        v-model="filename"
        type="text"
        name="filename"
      >
      <br>
      <input
        type="button"
        :value="$t('common.validate')"
        @click="upload"
      >
    </form>
  </ui-panel>
  <ui-panel :title="$t('medias.list')">
    <ui-table
      :data="fileListData"
      :empty-message="$t('medias.noFiles')"
    />
  </ui-panel>
</template>

<script setup lang="ts">
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import { ColumnDefinition } from "@/common/components/common/table/ColumnDefinition"
import { TableData } from "@/common/components/common/table/TableData"
import UiTable from "@/common/components/common/table/UiTable.vue"
import { Media } from "@/media/Media"
import MediasService from "@/media/MediasService"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const backendUrl = inject("backendUrl") as string

const mediaService = new MediasService(backendUrl)

const filename = ref<string>("")

const selectedFile = ref()
const selectFile = (event: any) => {
  let file = event.target.files[0]
  selectedFile.value = file

  filename.value = file.name
}

const upload = () => {
  mediaService.upload(filename.value, selectedFile.value)
}

const fileListData = ref<TableData<Media> | null>(null)

mediaService.list().then((medias) => {
  fileListData.value = new TableData(medias, [
    new ColumnDefinition<Media>(t("medias.filename"), (data: Media) => data.fileName),
  ])
})
</script>
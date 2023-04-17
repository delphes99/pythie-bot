<template>
  <ui-panel title="medias.add">
    <form>
      <label
        class="block text-titleColor text-sm font-semibold mb-2"
        for="file"
      >
        {{ $t("medias.file") }}
      </label>
      <input
        id="file"
        ref=""
        type="file"
        name="file"
        @change="selectFile"
      >
      <ui-textfield
        id="filename"
        v-model="filename"
        label="medias.filename"
      />
      <br>
      <ui-button
        label="common.validate"
        @on-click="upload"
      />
    </form>
  </ui-panel>
  <ui-panel title="medias.list">
    <ui-table
      :data="fileListData"
      :empty-message="$t('medias.noFiles')"
    />
  </ui-panel>
</template>

<script setup lang="ts">
import UiPanel from "@/ds/panel/UiPanel.vue"
import { ColumnDefinition } from "@/common/components/common/table/ColumnDefinition"
import { TableData } from "@/common/components/common/table/TableData"
import UiTable from "@/common/components/common/table/UiTable.vue"
import UiButton from "@/ds/button/UiButton.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { Media } from "@/media/Media"
import MediasService from "@/media/MediasService"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const mediaService = inject("media.service") as MediasService

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
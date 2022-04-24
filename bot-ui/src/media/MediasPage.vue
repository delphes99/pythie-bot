<template>
  <panel :title="$t('medias.add')">
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
  </panel>
  <panel :title="$t('medias.list')">
    <table>
      <tr>
        <th>{{ $t("medias.filename") }}</th>
      </tr>
      <tr
        v-for="file in files"
        :key="file.fileName"
      >
        <td>
          {{ file.fileName }}
        </td>
      </tr>
    </table>
  </panel>
</template>

<script setup lang="ts">
import Panel from "@/common/components/common/Panel.vue"
import { Media } from "@/media/Media"
import MediasService from "@/media/MediasService"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const backendUrl = inject("backendUrl") as string

const mediaService = new MediasService(backendUrl)

const filename = ref<string>("")

const files = ref<Media[]>([])
const selectedFile = ref()
const selectFile = (event: any) => {
  let file = event.target.files[0]
  selectedFile.value = file

  filename.value = file.name
}

const upload = () => {
  mediaService.upload(filename.value, selectedFile.value)
}

mediaService.list().then((medias) => (files.value = medias))
</script>
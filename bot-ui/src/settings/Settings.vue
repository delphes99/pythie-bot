<template>
  {{ $t("settings.title") }}
  <label for="lang">{{ $t("settings.language") }}</label>
  <select id="lang" v-model="lang">
    <option
      v-for="locale in $i18n.availableLocales"
      :key="`locale-${locale}`"
      :value="locale"
      >{{ locale }}
    </option>
  </select>
</template>
<script lang="ts">
import { LocalStorageItem } from "@/common/LocalStorageItem.ts";
import { defineComponent, ref, watch } from "vue";
import { useI18n } from "vue-i18n";

export default defineComponent({
  name: "Settings",
  setup() {
    const { locale } = useI18n();
    const lang = ref(locale.value);

    watch(lang, newValue => {
      locale.value = newValue;
      localStorage.setItem(LocalStorageItem.LANGUAGE, newValue);
    });

    return {
      lang
    };
  }
});
</script>

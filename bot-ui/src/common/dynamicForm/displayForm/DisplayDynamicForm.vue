<template>
  <fieldset class="border p-2">
    <legend>{{ form.type }}</legend>
    <div v-for="field in form.fields" :key="field.id">
      <component :is="field.viewComponent()"
                 :field="field"
                 v-model="field.actualValue"
      />
    </div>
    <UiButton label="common.save" @click="saveDescription"/>
  </fieldset>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import {autowired} from "@/common/utils/Injection.util";
import {ref} from "vue";

const props = defineProps({
  name: {
    type: String,
    required: true,
  },
})

const dynamicFormService = autowired(AppInjectionKeys.DYNAMIC_FORM_SERVICE)
const form = ref(await dynamicFormService.getForm(props.name))

function saveDescription() {
  console.log(form.value.buildJson())
}

</script>
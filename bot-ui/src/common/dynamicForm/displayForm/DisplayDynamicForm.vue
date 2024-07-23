<template>
  <fieldset class="border p-2">
    <legend>{{ form.type }}</legend>
    <div v-for="field in form.fields" :key="field.id">
      <component :is="field.viewComponent()"
                 :field="field"
                 v-model="field.actualValue"
      />
    </div>
    <UiButton v-if="withSaveButton" label="common.save" @click="saveDescription"/>
  </fieldset>
</template>

<script setup lang="ts">
import UiButton from "@/common/designSystem/button/UiButton.vue";
import {DynamicForm} from "@/common/dynamicForm/DynamicForm";

const props = defineProps({
  form: {
    type: DynamicForm,
    required: true,
  },
  withSaveButton: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(["saveForm"])

function saveDescription() {
  emit("saveForm")
}

</script>
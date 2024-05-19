<template>
  <fieldset class="border p-2">
    <legend>{{ description.type }}</legend>
    <div v-for="entry in viewComponentRefByDescriptor.entries()" :key="entry[0].fieldName">
      <component :is="entry[0].viewComponent()"
                 :descriptor="entry[0]"
                 :ref="entry[1]"
      />
    </div>
    <ui-button label="common.save" @click="saveDescription"/>
  </fieldset>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/ui-button.vue";
import {FieldDescriptor} from "@/common/dynamicForm/field/field-descriptor";
import {autowired} from "@/common/utils/injection.util";
import {Ref, ref} from "vue";

const props = defineProps({
  name: {
    type: String,
    required: true,
  },
})

const dynamicFormService = autowired(AppInjectionKeys.DYNAMIC_FORM_SERVICE)
const description = await dynamicFormService.getForm(props.name)

const descriptors = description.fields
const viewComponentRefByDescriptor: Map<FieldDescriptor<any>, Ref> = new Map();

descriptors.forEach(descriptor => {
  viewComponentRefByDescriptor.set(descriptor, ref(null))
})

function saveDescription() {
  for (let entry of viewComponentRefByDescriptor.entries()) {
    console.log("actual : ", entry[1].value[0]?.buildValue())
  }
}

</script>
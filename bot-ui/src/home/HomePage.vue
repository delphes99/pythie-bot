<template>
  <div class="home">
    Pythie
  </div>
  <DisplayDynamicForm name="twitch-send-message-form"/>
  <FormListByTag tag="feature"/>
</template>
<script setup lang="ts">
import DisplayDynamicForm from "@/common/dynamicForm/displayForm/DisplayDynamicForm.vue";
import FormListByTag from "@/common/dynamicForm/displayForm/FormListByTag.vue";
</script>
<template>
  <component :is="router.comp" :args="router.args" />
</template>

<script>
import router from "./route";
import AppMain from "@/apps/main";

export default {
  components: { AppMain },
  data() {
    return {
      router,
    };
  },
};
</script>
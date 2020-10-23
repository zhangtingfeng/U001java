<template>
  <span>{{text}}</span>
</template>

<script>
export default {
  data() {
    return { text: this.value ? new Date(this.value).format("HH:mm:ss") : "" };
  },
  props: ["value"]
};
</script>

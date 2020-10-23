<template>
  <date
    type="datetime"
    class="datetime"
    :name="name"
    :validate="validate"
    :readonly="readonly"
    v-model="data"
  />
</template>

<script>
import ui from "@c/ui";

export default {
  components: {
    Date: ui.Date
  },
  data() {
    return { data: this.value };
  },
  props: {
    value: { default: "" },
    name: "",
    readonly: { default: false },
    validate: ""
  },
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  }
};
</script>

<style scoped>
.datetime {
  min-width: 200px;
}
</style>

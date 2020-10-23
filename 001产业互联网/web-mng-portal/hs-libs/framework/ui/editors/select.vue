<template>
  <select2
    class="form-control"
    :id="name"
    :name="name"
    :options="options"
    :readonly="readonly"
    v-model="data"
    v-validate="validate"
    :data-vv-name="name"
  ></select2>
</template>

<script>
import { Select2 } from "@c/ui";
import dict from "@f/framework/dict";

export default {
  components: {
    Select2
  },
  data() {
    return { data: this.value, options: [] };
  },
  props: {
    args: { default: "" },
    value: { default: "" },
    name: { default: "" },
    validate: { default: "" },
    readonly: { default: false }
  },
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  },
  mounted() {
    dict.get(this.args).then(data => {
      this.options = data;
    });
  }
};
</script>

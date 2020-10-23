<template>
  <select2
    class="form-control"
    multi="true"
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
    return { data: this.parseValue(this.value), options: [] };
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
  methods: {
    parseValue() {
      return "string" === typeof this.value
        ? this.value.split(",")
        : this.value;
    }
  },
  mounted() {
    dict.get(this.args).then(data => {
      this.options = data;
    });
  }
};
</script>

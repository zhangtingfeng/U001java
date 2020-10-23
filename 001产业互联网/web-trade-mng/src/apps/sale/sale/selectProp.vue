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
import { comps,editors } from "@f/framework";

export default {
  components: {
    Select2: comps.Select2
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
      this.options = [];
      if(this.args){
        this.options=JSON.parse(this.args);
      }
  }
};
</script>

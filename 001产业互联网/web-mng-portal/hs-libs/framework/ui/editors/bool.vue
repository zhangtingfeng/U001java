<template>
  <div class="checkbox">
    <checkbox
      :name="name"
      :label="args"
      v-model="data"
      v-validate="validate"
      :data-vv-name="name"
    />
  </div>
</template>

<script>
import checkbox from "@c/ui/Checkbox";

export default {
  components: {
    checkbox,
  },
  data() {
    let data = { data: 1 == this.value || "1" == this.value, label: "" };
    if (this.name) {
      let meta = this.$parent.data && this.$parent.data.meta;
      if (meta) data.label = `${meta.name}.field.${this.name}`;
    }
    return data;
  },
  props: ["value", "name", "validate", "args"],
  watch: {
    data(val) {
      this.$emit("input", val ? 1 : 0);
    },
  },
};
</script>
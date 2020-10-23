<template>
    <textarea :name="name" v-model="data" :readonly="readonly" class="form-control" :data-vv-name="name"></textarea>
</template>

<script>
export default {
  data() {
    return { data: this.value };
  },
  props: {
    value: { default: ""},
    name: { default: ""},
    readonly: { default: false }
  },
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  }
};
</script>

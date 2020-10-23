<template>
	<input class="form-control" type="password" :name="name" v-model="data"  :data-vv-name="name"/>
</template>

<script>
export default {
  data() {
    return { data: this.value };
  },
  props: ["value", "name"],
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  }
};
</script>

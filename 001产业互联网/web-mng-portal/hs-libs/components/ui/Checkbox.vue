<template>
  <a href="#" :class="clz" @click.prevent.stop="change">
    <input type="checkbox" v-model="checked" :name="name">
    <div class="view"></div>
    {{label}}
  </a>
</template>

<script>
export default {
  data() {
    return {
      checked: this.value
    };
  },
  computed: {
    clz() {
      return `ui-checkbox ${this.label ? "has-label" : ""} ${
        this.disabled ? "disabled" : ""
      } ${this.readonly ? "readonly" : ""} ${
        this.checked ? "checked" : "unchecked"
      }`;
    }
  },
  props: {
    value: { default: false },
    name: "",
    label: "",
    disabled: false,
    readonly: false
  },
  methods: {
    change() {
      if (this.disabled || this.readonly) return;
      this.checked = !this.checked;
    }
  },
  watch: {
    checked(val) {
      this.$emit("input", val);
    },
    value(val) {
      this.checked = val;
    }
  }
};
</script>

<style scoped>
input {
  display: none;
}
div.view {
  width: 1.3em;
  height: 1.3em;
  display: inline-block;
  vertical-align: middle;
  background-repeat: no-repeat;
  background-size: 100%;
}
.has-label div.view {
  margin-right: 0.3em;
}
.unchecked div.view {
  background-image: url(../../assets/imgs/checkbox-1.png);
}
.unchecked div.view:hover {
  background-image: url(../../assets/imgs/checkbox-2.png);
}
.checked div.view {
  background-image: url(../../assets/imgs/checkbox-3.png);
}
.unchecked.readonly div.view,
.unchecked.disabled div.view {
  background-image: url(../../assets/imgs/checkbox-4.png);
}
checkbox.checked.readonly div.view,
.checked.disabled div.view {
  background-image: url(../../assets/imgs/checkbox-5.png);
}
</style>

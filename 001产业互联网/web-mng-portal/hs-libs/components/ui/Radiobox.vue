<template>
  <a :class="clz" @click.prevent.stop="change" href="#">
    <input type="radio" :checked="checked" :name="name" :value="value">
    <div class="view"></div>
    {{text}}
  </a>
</template>

<script>
export default {
  data() {
    return {
      checked: this.m == this.value,
      text: this.label ? this.label : this.value
    };
  },
  computed: {
    clz() {
      return `ui-radio ${this.disabled ? "disabled" : ""} ${
        this.readonly ? "readonly" : ""
      } ${this.checked ? "checked" : "unchecked"}`;
    }
  },
  model: {
    prop: "m",
    event: "change"
  },
  props: {
    m: "",
    name: "",
    value: "",
    label: "",
    disabled: false,
    readonly: false
  },
  methods: {
    change() {
      if (this.disabled || this.readonly) return;
      this.checked = true;
    }
  },
  watch: {
    checked(val) {
      if (val) this.$emit("change", this.value);
    },
    m(val) {
      this.checked = this.value == val;
    }
  }
};
</script>

<style scoped>
a {
  font-weight: normal;
}
input {
  display: none;
}
div.view {
  position: relative;
  margin-right: 0.5em;
  top: 0.4em;
  width: 1.5em;
  height: 1.5em;
  display: inline-block;
  background-repeat: no-repeat;
  background-size: 100%;
}
.unchecked div.view {
  background-image: url(../../assets/imgs/radio-1.png);
}
.unchecked div.view:hover {
  background-image: url(../../assets/imgs/radio-2.png);
}
.unchecked.readonly div.view,
.unchecked.disabled div.view {
  background-image: url(../../assets/imgs/radio-4.png);
}
.checked div.view {
  background-image: url(../../assets/imgs/radio-3.png);
}
.checked.readonly div.view,
.checked.disabled div.view {
  background-image: url(../../assets/imgs/radio-5.png);
}
</style>

<template>
  <span>{{text}}</span>
</template>

<script>
import dict from "../../dict";

export default {
  data() {
    return { text: "" };
  },
  props: ["value"],
  methods: {
    update(){
      dict.getObjs("districts").then(data => {
        this.load(data);
      });
    },
    load(datas) {
      let d = this.value ? this.value.substr(4, 2) : "00";
      let c = this.value ? this.value.substr(2, 2) : "00";
      let p = this.value ? this.value.substr(0, 2) : "00";

      let pn = "00" == p ? "" : datas[`${p}0000`] ? datas[`${p}0000`].text : "";
      let cn = "00" == c ? "" : datas[`${p}${c}00`] ? datas[`${p}${c}00`].text : "";
      let dn = "00" == d ? "" : datas[this.value] ? datas[this.value].text : "";

      this.text = pn + cn + dn;
    }
  },
  mounted() {
    this.update();
  },
  watch:{
    value(){
      this.update();
    }
  }
};
</script>

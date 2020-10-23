<template>
  <span>{{text[text.length &lt; 2 ? 0 : localeIdx]}}</span>
</template>

<script>
import dict from "@f/framework/dict";
export default {
  data() {
    return { datas: [] };
  },
  props: ["value", "name", "args"],
  computed: {
    localeIdx() {
      return this.$i18n.locale == "en" ? 1 : 0;
    },
    text() {
      let text = "";
      if (this.datas) {
        if (this.value != undefined) {
          for (let d of this.datas) {
            if (d.id == this.value) {
              text = (d.text || d.name).split("|");
              break;
            }
          }
          if (!text) text = [`[${this.value}]`];
        }
      } else {
        text = `[${this.name}.{this.value}]`;
      }

      return text;
    }
  },
  mounted() {
    dict
      .get(this.args)
      .then(data => {
        this.datas = data;
      })
      .catch(() => {
        this.datas = undefined;
      });
  }
};
</script>

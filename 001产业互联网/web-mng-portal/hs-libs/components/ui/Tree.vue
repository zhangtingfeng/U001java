<template>
  <div></div>
</template>

<script>
import "jstree";
import "jstree/dist/themes/default/style.css";

export default {
  data() {
    return { checked: this.value || [] };
  },
  props: ["value", "data"],
  methods: {
    init() {
      let $t = $(this.$el)
        .jstree({
          plugins: ["checkbox"],
          core: {
            expand_selected_onload: true,
            data: this.data
          },
          checkbox: {
            three_state: false,
            tie_selection: true,
            cascade: "undetermined"
          }
        })
        .on("changed.jstree", () => {
          this.checked = $t.jstree("get_checked");
          this.$emit("input", this.checked);
        })
        .on("ready.jstree", () => {
          $t.jstree("check_node", this.checked || []);
          $t.jstree("open_all");
        });
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style>
</style>

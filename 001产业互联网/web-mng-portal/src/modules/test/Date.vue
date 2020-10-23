<template>
  <div class="input-group date" ref="$dp">
    <input
      type="text"
      :name="name"
      class="form-control"
      @focus="showPopup"
      v-validate="validate"
      :data-vv-name="name"
    />
    <span class="input-group-addon">
      <span class="fa fa-calendar"></span>
    </span>
  </div>
</template>

<script>
import locale from "@c/locale";
import "moment";
import "eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css";
import "eonasdan-bootstrap-datetimepicker";

const TYPES = {
  date: "YYYY-MM-DD",
  time: "HH:mm:ss",
  datetime: "YYYY-MM-DD HH:mm:ss"
};

export default {
  data() {
    return {};
  },
  props: {
    value: { default: "" },
    type: { default: "datetime" },
    name: "",
    minDate: { default: undefined },
    maxDate: { default: undefined },
    readonly: { default: false },
    validate: ""
  },
  methods: {
    init() {
      let $dp = $(this.$refs.$dp)
        .datetimepicker({
          format: TYPES[this.type],
          showTodayButton: true,
          showClear: true,
          defaultDate: "2020-07-08 23:59:59",
          sideBySide: true,
          toolbarPlacement: "bottom",
          date: this.value,
          minDate: this.minDate,
          maxDate: this.maxDate
        })
        .on("dp.change", e => {
          this.$emit("input", e.date ? e.date.format(TYPES[this.type]) : "");
        });
      if (this.readonly) $dp.datetimepicker("disable");
      this.updateLocale();
      if (!this.value) {
        $dp.datetimepicker("clear");
      }
    },
    updateLocale() {
      $(this.$refs.$dp).datetimepicker("options", {
        locale: locale.get() == locale.DEFAULT ? "zh-CN" : "en"
      });
    },
    showPopup() {
      $(this.$refs.$dp).datetimepicker("show");
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    "$i18n.locale"(val) {
      this.updateLocale();
    }
  }
};
</script>

<style scoped>
input {
  cursor: pointer;
}
</style>

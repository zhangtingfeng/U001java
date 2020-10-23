<template>
  <a href="#" @click.prevent="chg()">
    <i class="fa fa-globe"></i>
    <span>{{$t('name')}}</span>
  </a>
</template>

<script>
import locale from "@c/locale";
export default {
  methods: {
    updateTitle() {
      $("title").html(this.$t("app.name"));
    },
    chg() {
      var l = "en" == locale.get() ? "cn" : "en";
      locale.set(l);
      this.updateTitle();
    }
  },
  mounted() {
    this.updateTitle();
  }
};
</script>

<style>

</style>
<i18n-yaml>
en:
  name: "中文"
cn:
  name: "English"
</i18n-yaml>

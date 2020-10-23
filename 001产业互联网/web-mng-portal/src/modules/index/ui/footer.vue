<template>
  <footer class="main-footer">
    <!-- <div class="pull-right hidden-xs">
      <b>Version</b> 1.0.0
    </div>-->
    <strong v-html="$t('foot-message')" />
  </footer>
</template>

<script>
export default {};
</script>
<style lang="less" scoped>
.main-footer {
  text-align: right;
}
</style>
<i18n-yaml>
en:
  foot-message: "&copy; 2020"
cn:
  foot-message: "&copy; 2020"
</i18n-yaml>

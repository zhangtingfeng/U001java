<template>
  <section class="content">
    <div class="error-page">
      <h2 class="headline text-red">500</h2>

      <div class="error-content">
        <h3>
          <i class="fa fa-warning text-red"></i>
          {{$t("error.msg")}}
        </h3>

        <p>{{$t('msg1')}}</p>
        <p>
          <a href="#" class="btn btn-link" @click.prevent="showDetail =! showDetail">{{$t('detail')}}</a>
          <a href="index.html" class="btn btn-link">{{$t('msg2')}}</a>
        </p>
        <p v-if="showDetail">
          <pre>{{error}}</pre>
        </p>
        <p></p>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  props: ["error"],
  data() {
    return {
      showDetail: false
    };
  }
};
</script>

<style scoped>
.content {
  margin: 50px;
}
</style>
<i18n-yaml>
en:
  msg1: "Oops! Something went wrong. Please reload the page, and contact with system admin."
  detail: "Detail"
  msg2: "Reload"
cn:
  msg1: "很不幸! 系统发生了错误。请重新加载页面,  联系系统管理员。"
  detail: "错误详情"
  msg2: "返回首页"
</i18n-yaml>
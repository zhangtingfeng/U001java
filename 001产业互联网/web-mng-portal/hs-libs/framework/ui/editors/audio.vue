<template>
  <div class="form-group">
    <button
      class="btn btn-primary"
      v-on:click.prevent="translate(value)"
      v-if="value"
    >{{$t('translate')}}</button>
    <audio class="audio-style" controls :name="name" v-if="value">
      <source :src="value" type="audio/wav" />
      <source :src="value" type="audio/mpeg" />
      <source :src="value" type="audio/ogg" />
      <embed :src="value" />
      {{$t('tips')}}
    </audio>
    <span v-if="!value">{{$t("novoice")}}</span>
  </div>
</template>

<script>
import progress from "@c/progress";
import msg from "@c/msg";
import ajax from "@c/ajax";

export default {
  data() {
    return { data: this.value };
  },
  props: ["value", "name"],
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  },
  methods: {
    translate: function(value, event) {
      let form = this.$parent.data.form;
      if (null == form.voicetext || undefined == form.voicetext) {
        progress.show();
        let promise = ajax.post({
          url: `api/incident/translate.json`,
          data: { voiceurl: value, id: form.id }
        });
        promise
          .then(data => {
            form.voicetext = data.data;
            // 触发数据变更
            let temp = form.customername;
            form.customername = "-";
            form.customername = temp;
          })
          .catch(rs => {
            msg.info("servererror", "error");
          })
          .always(() => {
            progress.hide();
          });
      }
    }
  }
};
</script>
<style>
.audio-style {
  vertical-align: middle;
}
</style>
<i18n-yaml>
en:
  translate: "translate"
  tips: "Your browser does not support this audio play."
  novoice: "No voice"
cn:
  translate: "翻译"
  tips: "当前浏览器不支持在线播放"
  novoice: "没有录音文件"
</i18n-yaml>